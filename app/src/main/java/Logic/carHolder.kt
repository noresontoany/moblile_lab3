@file:Suppress("DEPRECATION")

package Logic

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import kotlin.reflect.full.declaredMemberProperties

class carHolder : Application(), LifecycleObserver {
    val cars: MutableLiveData<List<Car>> = MutableLiveData()
    val filters_names = arrayOf(
        "Sort by Descending",
        "Show Only Electric Cars",
        "Show Only Non-Electric Cars"
    )
    var filters = booleanArrayOf(false, false, false)
    private val FILTER_FILE_NAME = "filters.xls"
    private val CARS_DATA_FILE_NAME = "cars.xls"
    private val CARS_SHARED_FILE_NAME = "carsAppInfo"

    override fun onCreate() {
        cars.value = loadData()
        loadFilters()
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
        saveData()
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
    fun saveData() {
        try {
            openFileOutput(CARS_DATA_FILE_NAME, Context.MODE_PRIVATE).use { fileOutputStream ->
                HSSFWorkbook().use { wb ->
                    val sheet = wb.createSheet("Cars") // Создаем лист с именем "Пример"
                    val arr = cars.value
                    val row = sheet.createRow(0)

                    for ((i, el) in Car::class.declaredMemberProperties.withIndex()) {
                        row.createCell(i).setCellValue(el.name)
                    }

                    for (i in 0..<arr!!.size) {
                        val row = sheet.createRow(i + 1) // Создаем первую строку
                        for ((j, el) in Car::class.declaredMemberProperties.withIndex()) {
                            println(el)
                            val value = el.get(arr[i])
                            val cell = row.createCell(j)
                            cell.setCellValue(value.toString())
                        }
                        println("=======================\n")
                    }
                    wb.write(fileOutputStream)
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
    fun loadData(): List<Car> {


        val arr = mutableListOf<Car>()
        val file = File(this.filesDir, CARS_DATA_FILE_NAME)

        if (!file.exists()) {
            return emptyList()
        }

        try {
            file.inputStream().use { inputStream ->
                HSSFWorkbook(inputStream).use { wb ->

                    val sheet = wb.getSheetAt(0) // Читаем первый лист

                    val props = mutableListOf<String>()

                    for ((i, row) in sheet.withIndex()) {
                        when (i) {
                            0 -> {
                                for (cell in row) {
                                    props.add(cell.toString())
                                }
                            }

                            else -> {
                                val values = mutableListOf<String>()
                                for (cell in row) {
                                    values.add(cell.toString())
                                }
                                arr.add(
                                    Car(
                                        values[4],
                                        values[1].toBoolean(),
                                        values[0].toInt(),
                                        values[2]
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            throw e
        }
        return arr
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

    fun addCars(car: Car) {
        val currentCars = cars.value ?: emptyList()
        cars.value = currentCars + car
    }

    fun getCar(id: String): Car {
        return cars.value!!.find { it.id == id }!!
    }

    fun updateCar(id: String, car: Car) {
        val foundCar: Car = cars.value!!.find { it.id == id }!!
//        val toast = Toast.makeText(applicationContext, foundCar.toString(), Toast.LENGTH_SHORT)
//        toast.show()
////
        val currentCars = cars.value!!.toMutableList()
        val index = cars.value!!.indexOf(foundCar)
        currentCars[index].carMiliage = car.carMiliage
        currentCars[index].driverName = car.driverName
        currentCars[index].name = car.name
        currentCars[index].carType = car.carType
        cars.value = currentCars.toList()
    }

    fun deleteCar(id: String) {
        val currentCars = (cars.value ?: emptyList()).toMutableList()
        val foundCar: Car? = currentCars.find { it.id == id }
        foundCar?.let { currentCars.remove(it) }
        cars.value = currentCars.toList()
    }

}
