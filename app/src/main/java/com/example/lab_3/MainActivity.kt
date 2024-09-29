package com.example.lab_3

import Logic.Car
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val checkBoxNewCar = findViewById<CheckBox>(R.id.isNewCar)
        var cars = mutableListOf<Car>()
        var cars_names = mutableListOf<String>()
        var carsMap : MutableMap <String, Int> = mutableMapOf()
        val myTxt = findViewById<EditText>(R.id.myEdit)
        val myList = findViewById<ListView>(R.id.userlist)
        val addButton = findViewById<Button>(R.id.addBtn)



        var arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, cars_names
        )

        myList.adapter = arrayAdapter

        addButton.setOnClickListener {
            val carName = myTxt.text.toString()
            if (carName.isEmpty()) {
                toastShow("Строка пуста")
            } else if (carsMap.containsKey(carName)) {
                toastShow("Имя уже занято")
            } else {
                var mode = false
                var carType = "Автомобиль"
                if (checkBoxNewCar.isChecked)
                {
                    mode = true
                    carType = "Электрокар"
                }
                val newCar = Car(carName,mode,1)
                cars.add(newCar)
                cars_names.add(carName)
                carsMap.put(carName, newCar.hashCode())
                myTxt.text.clear()
                arrayAdapter.notifyDataSetChanged()
                toastShow("$carType $carName успешно добален")
            }

        }

    }

    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }



}

