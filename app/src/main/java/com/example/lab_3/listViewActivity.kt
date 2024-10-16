package com.example.lab_3

import Logic.Car
import Logic.carHolder
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class listViewActivity : AppCompatActivity() {
//    private var carData: carHolder = application as carHolder
    private var cars : List<Car> = emptyList()
    private var carDescription  = mutableListOf<String>()
    private var carNames : Set<String> = emptySet()
//    private var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf(carDescription))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val carData = application as carHolder
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carDescription)
        carData.getSharedData().observe(this) { data ->
            val tempCars = carData.getCars() ?: emptyList()
            cars = tempCars
            val tempDescription = carData.getCarDescriptions()
            carDescription.addAll(tempDescription)
            val tempCarNames = carData.getCarNames()
            carNames = tempCarNames.toSet()
            arrayAdapter.notifyDataSetChanged()
        }

        val listOfCars = findViewById<ListView>(R.id.listViewCars)

        listOfCars.adapter = arrayAdapter
    }

    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

}