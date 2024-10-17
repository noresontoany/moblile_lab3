package com.example.lab_3

import Logic.Car
import Logic.carHolder
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class carDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_description)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idCar = intent.getIntExtra("id", -1)
        if (idCar == -1) {
            Toast.makeText(this, "ahahahaha", Toast.LENGTH_SHORT).show()
            finish()
        }
        val text_Name = findViewById<EditText>(R.id.carNameEdit)
        val text_DriverName = findViewById<EditText>(R.id.driverNameEdit)
        val text_CarMiliage = findViewById<EditText>(R.id.carMiliageEdit)
        val check_carType = findViewById<CheckBox>(R.id.carTypeEdit)
        val back = findViewById<Button>(R.id.btnBackToCarListView)
        val edit = findViewById<Button>(R.id.btnCarEdit)
        val carData = application as carHolder
        var car = carData.getCar(idCar)
        carData.getSharedData().observe(this) { data ->
            car = carData.getCar(idCar)
            text_Name.hint = car.name
            text_DriverName.hint = car.driverName
            text_CarMiliage.hint = car.carMiliage.toString()
            check_carType.isChecked = car.carType
        }


        edit.setOnClickListener {
            val newCar = createCar(text_Name, text_DriverName, text_CarMiliage, check_carType, car)
            carData.updateCar(idCar,newCar)
            text_Name.text.clear()
            text_DriverName.text.clear()
            text_CarMiliage.text.clear()
        }



        back.setOnClickListener {
            finish()
        }

    }

    private fun createCar(name: EditText, driverName: EditText, carMiliage: EditText, carType:CheckBox, car:Car):Car
    {
//        val newCar = Car(name.text.toString()?:car.name, carType.isChecked.toString().toBoolean(),
//            carMiliage.text.toString().toInt()?:carMiliage.hint.toString().toInt(),  driverName.text.toString()?:driverName.hint.toString())

        var carName = name.text.toString()
        if(carName.isEmpty())
            carName = car.name.toString()

        var carDriverName = driverName.text.toString()
        if (carDriverName.isEmpty())
            carDriverName = car.driverName.toString()

        var cType = carType.isChecked

        var carCarMiliage = carMiliage.text.toString()
        if (carCarMiliage.isEmpty())
            carCarMiliage = car.carMiliage.toString()

        val newCar = Car(carName, cType, carCarMiliage.toInt(), carDriverName)
        return newCar
    }
}