@file:Suppress("DEPRECATION")

package Logic

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Xml
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.lab_3.listViewActivity
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
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

    fun saveAllData()
    {
        saveData()
        saveFilters()
    }

    fun saveFilters()
    {
        val workbook : Workbook = HSSFWorkbook()
        val sheet = workbook.createSheet("Filters")
        val row = sheet.createRow(0)
        for ((i, el) in filters.withIndex())
        {
            val cell = row.createCell(i)
            cell.setCellValue(el.toString())
        }
        val fileOutputStream = openFileOutput(FILTER_FILE_NAME, Context.MODE_PRIVATE)
        workbook.write(fileOutputStream)
        fileOutputStream.close()
        workbook.close()
    }
    fun saveFile() {
        val resolver = this.contentResolver
        // Задаем параметры для файла
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, CARS_SHARED_FILE_NAME) // Имя файла
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS
            ) // Путь в Shared Storage
            put(
                MediaStore.MediaColumns.MIME_TYPE,
                "application/vnd.ms-excel"
            )
        }

        // Создаем URI для файла
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        // Записываем данные в файл
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                val workbook: Workbook = HSSFWorkbook() // Создаем рабочую книгу для .xls файла
                val sheet = workbook.createSheet("Cars") // Создаем лист с именем "Пример"
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


                workbook.write(outputStream)
                outputStream.close()
                workbook.close()
            }
        } ?: throw Exception("Не удалось создать файл")
    }

    fun loadFilters()
    {
        val arr = emptyList<Boolean>().toMutableList()
        val file = File(this.filesDir, FILTER_FILE_NAME)
        if (!file.exists())
        {
            return
        }
        file.inputStream().use {inputStream ->
            val workbook = HSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val row = sheet.getRow(0)
            for (el in row)
            {
                arr.add(el.toString().toBoolean())
            }

            workbook.close()
        }
        filters = arr.toBooleanArray()

    }

    fun saveData() {
        val workbook: Workbook = HSSFWorkbook() // Создаем рабочую книгу для .xls файла
        val sheet = workbook.createSheet("Cars") // Создаем лист с именем "Пример"
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


        val fileOutputStream = openFileOutput(CARS_DATA_FILE_NAME, Context.MODE_PRIVATE)
        workbook.write(fileOutputStream)
        fileOutputStream.close()
        workbook.close()
    }

    fun loadData(): List<Car> {


        val arr = mutableListOf<Car>()
        val file = File(this.filesDir, CARS_DATA_FILE_NAME)

        if (!file.exists()) {
            return emptyList()
        }

        file.inputStream().use { inputStream ->
            val workbook = HSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0) // Читаем первый лист

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
            workbook.close()
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
        val toast = Toast.makeText(applicationContext, foundCar.toString(), Toast.LENGTH_SHORT)
        toast.show()

        val currentCars = (cars.value ?: emptyList()).toMutableList()
        val index = currentCars.indexOf(foundCar)
        currentCars[index] = car // Обновляем существующую машину

        cars.value = currentCars.toList()
    }

    fun deleteCar(id: String) {
        val currentCars = (cars.value ?: emptyList()).toMutableList()
        val foundCar: Car? = currentCars.find { it.id == id }
        foundCar?.let { currentCars.remove(it) }
        cars.value = currentCars.toList()
    }

}
