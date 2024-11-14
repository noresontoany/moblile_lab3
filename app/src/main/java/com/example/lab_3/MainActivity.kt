package com.example.lab_3

import Logic.Car
import Logic.carHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val carNameTextInput = findViewById<EditText>(R.id.carNameAdd)
        val driverNameTextInput = findViewById<EditText>(R.id.driverNameAdd)
        val carNumberIntInput = findViewById<EditText>(R.id.carMiliageAdd)
        val checkBoxNewCar = findViewById<CheckBox>(R.id.carTypeAdd)

        var carNames : Set<String> = emptySet()
        val addButton = findViewById<Button>(R.id.addCArBtn)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val buttonSaveFile = findViewById<Button>(R.id.buttonSaveFile)
        loadInputs()
        val menu = bottomNavigationView.menu
        val addItem = menu.findItem(R.id.navigation_add)
        addItem.isVisible = false

        val carData: carHolder = application as carHolder
        carData.getSharedData().observe(this) { data ->
            val tempCarNames = carData.getCarNames()
            carNames = tempCarNames
        }
        buttonSaveFile.setOnClickListener {
            carData.saveFile()
        }
        addButton.setOnClickListener {
            val carName = carNameTextInput.text.toString()
            if (carName.isEmpty() || carNumberIntInput.text.toString().isEmpty()||driverNameTextInput.text.toString().isEmpty()) {
                toastShow("Строка пуста")
            } else if (carName in carNames) {
                toastShow("Имя уже занято")
            } else {
                var mode = false
                var carType = "Автомобиль"
                if (checkBoxNewCar.isChecked)
                {
                    mode = true
                    carType = "Электрокар"
                }
                val newCar = Car(carName,mode, carNumberIntInput.text.toString().toInt(),driverNameTextInput.text.toString())
                carData.addCars(newCar)
                toastShow("$carType $carName успешно добавлен")
            }

        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_view -> {
                    val intent = Intent(this, listViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_add -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onPause() {
        saveInputs()
        super.onPause()
    }

    override fun onDestroy() {
        saveInputs()
        super.onDestroy()
    }

    override fun onStop() {
        saveInputs()
        super.onStop()
    }

    override fun finish() {
        saveInputs()
        super.finish()
    }

    override fun onResume() {
        loadInputs()
        super.onResume()
    }

    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }


    private fun saveInputs()
    {
        val carNameTextInput = findViewById<EditText>(R.id.carNameAdd)
        val driverNameTextInput = findViewById<EditText>(R.id.driverNameAdd)
        val carNumberIntInput = findViewById<EditText>(R.id.carMiliageAdd)
        val checkBoxNewCar = findViewById<CheckBox>(R.id.carTypeAdd)

        try {
            val file = File(filesDir, "mainActivityCache.dat")

            val fos = FileOutputStream(file, false)

            fos.write((carNameTextInput.text.toString() + "\n").toByteArray())
            fos.write((checkBoxNewCar.isChecked.toString() + "\n").toByteArray())
            fos.write((carNumberIntInput.text.toString() + "\n").toByteArray())
            fos.write((driverNameTextInput.text.toString() + "\n").toByteArray())

            fos.close()

            toastShow("Данные сохранены в файл")
        } catch (e: IOException) {
            e.printStackTrace()
            toastShow("Ошибка при сохранении данных")
        }

    }

    private  fun loadInputs()
    {
        val carNameTextInput = findViewById<EditText>(R.id.carNameAdd)
        val driverNameTextInput = findViewById<EditText>(R.id.driverNameAdd)
        val carNumberIntInput = findViewById<EditText>(R.id.carMiliageAdd)
        val checkBoxNewCar = findViewById<CheckBox>(R.id.carTypeAdd)

        try {
            val file = File(filesDir, "mainActivityCache.dat")
            if (!file.exists()) {
                toastShow("Файл не существует")
                return
            }

            val fos = FileInputStream(file)
            val buf = fos.readBytes()

            val splitString = String(buf).split("\n")
            val dataList = ArrayList<String>(splitString)

            try {
              carNameTextInput.setText(dataList[0])
              checkBoxNewCar.isChecked = dataList[1].toBoolean()
              carNumberIntInput.setText(dataList[2])
              driverNameTextInput.setText(dataList[3])

            } catch (e: IndexOutOfBoundsException) {
                toastShow("Ошибка при сохранении данных")
            }

            fos.close()

            toastShow("Данные сохранены в файл")
        } catch (e: IOException) {
            e.printStackTrace()
            toastShow("Ошибка при сохранении данных")
        }
    }





}


