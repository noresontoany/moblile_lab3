package com.example.lab_3

import Logic.Car
import Logic.carHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView


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
        val checkBoxNewCar = findViewById<CheckBox>(R.id.carTypeAdd)
        val carNumber = findViewById<EditText>(R.id.carMiliageAdd)
        val driverName = findViewById<EditText>(R.id.driverNameAdd)
        val myTxt = findViewById<EditText>(R.id.carNameAdd)
        val addButton = findViewById<Button>(R.id.addCArBtn)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        val menu = bottomNavigationView.menu
        val addItem = menu.findItem(R.id.navigation_add)
        addItem.isVisible = false

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



    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }





}


