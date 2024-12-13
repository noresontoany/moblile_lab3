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
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val idCar = intent.getLongExtra("id", -1)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation3)
        val text_Name = findViewById<EditText>(R.id.carNameEdit)
        val text_DriverName = findViewById<EditText>(R.id.driverNameEdit)
        val text_CarMiliage = findViewById<EditText>(R.id.carMiliageEdit)
        val check_carType = findViewById<CheckBox>(R.id.carTypeEdit)
        val edit = findViewById<Button>(R.id.btnCarEdit)
        val carData = application as carHolder
        var car = carData.getCar(idCar!!)

        carData.getSharedData().observe(this) { data ->
            car = carData.getCar(idCar)
            text_Name.hint = car!!.name
            text_DriverName.hint = car!!.driverName
            text_CarMiliage.hint = car!!.carMiliage.toString()
            check_carType.isChecked = car!!.carType
        }


        edit.setOnClickListener {

            val setOfNames = carData.getCarNames()

            if (text_Name.text.toString() in setOfNames)
            {
                Toast.makeText(this, "Имя занято !!", Toast.LENGTH_SHORT).show()
            }
            else {
                var carName = text_Name.text.toString()
                if(carName.isEmpty())
                    carName = car!!.name.toString()

                var carDriverName = text_DriverName.text.toString()
                if (carDriverName.isEmpty())
                    carDriverName = car!!.driverName.toString()

                var cType = check_carType.isChecked

                var carCarMiliage = text_CarMiliage.text.toString()
                if (carCarMiliage.isEmpty())
                    carCarMiliage = car!!.carMiliage.toString()

                carData.updateCar(idCar, carName, cType, carCarMiliage.toInt(), carDriverName)
                text_Name.text.clear()
                text_DriverName.text.clear()
                text_CarMiliage.text.clear()

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
    override fun onRestart() {

        super.onRestart()

    }
}