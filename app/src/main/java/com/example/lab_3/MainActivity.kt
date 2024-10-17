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


class MainActivity : AppCompatActivity() {

    private var carNames : Set<String> = emptySet()
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

        val btnListView = findViewById<Button>(R.id.btnCarListView)
        val checkBoxNewCar = findViewById<CheckBox>(R.id.carTypeAdd)
        val carNumber = findViewById<EditText>(R.id.carMiliageAdd)
        val driverName = findViewById<EditText>(R.id.driverNameAdd)
        val myTxt = findViewById<EditText>(R.id.carNameAdd)
        val addButton = findViewById<Button>(R.id.addCArBtn)


        val carData: carHolder = application as carHolder
        carData.getSharedData().observe(this) { data ->
            val tempCarNames = carData.getCarNames()
            carNames = tempCarNames
        }

        addButton.setOnClickListener {
            val carName = myTxt.text.toString()
            if (carName.isEmpty() || carNumber.text.toString().isEmpty()||driverName.text.toString().isEmpty()) {
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
                val newCar = Car(carName,mode, carNumber.text.toString().toInt(),driverName.text.toString())
                carData.addCars(newCar)
                toastShow("$carType $carName успешно добален")
            }

        }
        btnListView.setOnClickListener {
            val switchActivityIntent = Intent(
                this,
                listViewActivity::class.java,

            )
            startActivity(switchActivityIntent)

        }
    }

    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }



}


