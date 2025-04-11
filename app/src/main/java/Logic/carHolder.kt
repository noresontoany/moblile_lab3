@file:Suppress("DEPRECATION")

package Logic

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import data.CarContract
import data.FeedReaderDbHelper
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import kotlin.reflect.full.declaredMemberProperties
import kotlinx.coroutines.*
class carHolder : Application(), LifecycleObserver {
    val cars: MutableLiveData<List<Car>> = MutableLiveData()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    val filters_names = arrayOf(
        "Sort by Descending",
        "Show Only Electric Cars",
        "Show Only Non-Electric Cars"
    )
    var filters = booleanArrayOf(false, false, false)
    private val FILTER_FILE_NAME = "filters.xls"
//    private val CARS_DATA_FILE_NAME = "cars.xls"
    private val CARS_SHARED_FILE_NAME = "carsAppInfo"

    override fun onCreate() {
        context = applicationContext
        loadFilters()
        loadCarsFromDatabase()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        super.onCreate()
    }

    override fun onTerminate() {
        saveAllData()
        super.onTerminate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        saveAllData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroyed() {
        saveAllData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppForegrounded() {
        saveAllData()
    }

    fun saveAllData() {
        saveFilters()
    }

    fun saveFilters() {
        try {
            openFileOutput(FILTER_FILE_NAME, Context.MODE_PRIVATE)?.use { fileOutputStream ->
                HSSFWorkbook().use { wb ->
                    val sheet = wb.createSheet("Filters")
                    val row = sheet.createRow(0)
                    for ((i, el) in filters.withIndex()) {
                        val cell = row.createCell(i)
                        cell.setCellValue(el.toString())
                    }
                    wb.write(fileOutputStream)
                }
            } ?: throw Exception("Не удалось создать файл")
        } catch (e: Exception) {
            throw e
        }
    }

    fun saveFile() {
        val resolver = this.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, CARS_SHARED_FILE_NAME) // Имя файла
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS
            )
            put(
                MediaStore.MediaColumns.MIME_TYPE,
                "application/vnd.ms-excel"
            )
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                val workbook: Workbook = HSSFWorkbook()
                workbook.use { wb ->
                    val sheet = wb.createSheet("Cars")
                    val arr = cars.value
                    val row = sheet.createRow(0)
                    for ((i, el) in Car::class.declaredMemberProperties.withIndex()) {
                        row.createCell(i).setCellValue(el.name)
                    }
                    for (i in 0..<arr!!.size) {
                        val row = sheet.createRow(i + 1)
                        for ((j, el) in Car::class.declaredMemberProperties.withIndex()) {
                            println(el)
                            val value = el.get(arr[i])
                            val cell = row.createCell(j)
                            cell.setCellValue(value.toString())
                        }
                    }

                }
                workbook.write(outputStream)
            }
        } ?: throw Exception("Не удалось создать файл")
    }
    fun loadFilters() {
        val arr = emptyList<Boolean>().toMutableList()
        val file = File(this.filesDir, FILTER_FILE_NAME)

        if (!file.exists()) return

        file.inputStream().use { inputStream ->
            HSSFWorkbook(inputStream).use { wb ->
                val sheet = wb.getSheetAt(0)
                val row = sheet.getRow(0)
                for (el in row) {
                    arr.add(el.toString().toBoolean())
                }

            }
        }
        filters = arr.toBooleanArray()
    }

    fun setSharedData(arr: List<Car>) {
        this.cars.value = arr
    }

    fun getSharedData(): MutableLiveData<List<Car>> {
        return cars
    }

    fun getCarNames(): Set<String> {
        val temp = mutableSetOf<String>()
        for (el in cars.value!!) {
            el.name?.let { temp.add(it) }
        }
        return temp
    }

    private fun loadCarsFromDatabase() {
        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase
        val newCarsList = getAllCars(db)
        cars.postValue(newCarsList)
    }
    private fun getAllCars(db: SQLiteDatabase): List<Car> {
        val carList = mutableListOf<Car>()

        val query = """
        SELECT 
            cars.${CarContract.CarEntry._ID} AS car_id,
            cars.${CarContract.CarEntry.COLUMN_CAR_NAME} AS car_name,
            cars.${CarContract.CarEntry.COLUMN_CAR_TYPE} AS car_type,
            cars.${CarContract.CarEntry.COLUMN_CAR_MILIAGE} AS car_mileage,
            persons.${CarContract.PersonEntry.COLUMN_DRIVE_NAME} AS driver_name
        FROM ${CarContract.CarEntry.TABLE_NAME} cars
        LEFT JOIN ${CarContract.PersonEntry.TABLE_NAME} persons
        ON cars.${CarContract.CarEntry.COLUMN_PERSON_ID} = persons.${CarContract.PersonEntry._ID}
    """

        val cursor = db.rawQuery(query, null)

        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(c.getColumnIndexOrThrow("car_id"))
                val name = c.getString(c.getColumnIndexOrThrow("car_name"))
                val carType = c.getInt(c.getColumnIndexOrThrow("car_type")) == 1
                val carMileage = c.getInt(c.getColumnIndexOrThrow("car_mileage"))
                val driverName = c.getString(c.getColumnIndexOrThrow("driver_name"))

                carList.add(Car(name, carType, carMileage, driverName, id))
            }
        }

        return carList
    }


    fun addCar(name: String?, carType: Boolean, carMileage: Int, driverName: String?) {

        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.writableDatabase


        val driverId = getOrAddDriverId(db, driverName)

        val values = ContentValues().apply {
            put(CarContract.CarEntry.COLUMN_CAR_NAME, name)
            put(CarContract.CarEntry.COLUMN_CAR_TYPE, if (carType) 1 else 0)
            put(CarContract.CarEntry.COLUMN_CAR_MILIAGE, carMileage)
            put(CarContract.CarEntry.COLUMN_PERSON_ID, driverId)
        }

        db.insert(CarContract.CarEntry.TABLE_NAME, null, values)
        loadCarsFromDatabase()
    }

    private fun getOrAddDriverId(db: SQLiteDatabase, driverName: String?): Long {
        if (driverName.isNullOrEmpty()) return -1

        val cursor = db.query(
            CarContract.PersonEntry.TABLE_NAME,
            arrayOf(CarContract.PersonEntry._ID),
            "${CarContract.PersonEntry.COLUMN_DRIVE_NAME} = ?",
            arrayOf(driverName),
            null, null, null
        )

        val driverId = if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndexOrThrow(CarContract.PersonEntry._ID))
        } else {

            val values = ContentValues().apply {
                put(CarContract.PersonEntry.COLUMN_DRIVE_NAME, driverName)
            }
            db.insert(CarContract.PersonEntry.TABLE_NAME, null, values)
        }

        cursor.close()
        return driverId
    }

    fun updateCar(id: Long, name: String?, carType: Boolean, carMileage: Int, driverName: String?) {
        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.writableDatabase

        val driverId = getOrAddDriverId(db, driverName)

        val values = ContentValues().apply {
            put(CarContract.CarEntry.COLUMN_CAR_NAME, name)
            put(CarContract.CarEntry.COLUMN_CAR_TYPE, if (carType) 1 else 0)
            put(CarContract.CarEntry.COLUMN_CAR_MILIAGE, carMileage)
            put(CarContract.CarEntry.COLUMN_PERSON_ID, driverId)
        }

        val selection = "${CarContract.CarEntry._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        db.update(CarContract.CarEntry.TABLE_NAME, values, selection, selectionArgs)
        loadCarsFromDatabase()
    }


    fun deleteCar(id: Long) : String{
        try {


            val dbHelper = FeedReaderDbHelper(this)
            val db = dbHelper.writableDatabase

            val selection = "${CarContract.CarEntry._ID} = ?"
            val selectionArgs = arrayOf(id.toString())

            db.delete(CarContract.CarEntry.TABLE_NAME, selection, selectionArgs)
            loadCarsFromDatabase()
        }
        catch (e: Exception) {
            return "Ошибка при удалении"
        }
        return "Элемент удален"
    }
    fun getCar(id: Long): Car? {
        return cars.value?.find { it.id == id }
    }
}
