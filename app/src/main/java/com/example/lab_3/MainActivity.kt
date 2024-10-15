package com.example.lab_3

import Logic.Car
import android.annotation.SuppressLint
import android.content.Intent
import android.os.BaseBundle
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
        val btnListView = findViewById<Button>(R.id.btn_listView)
        val checkBoxNewCar = findViewById<CheckBox>(R.id.isNewCar)
        val carNumber = findViewById<EditText>(R.id.carNumber)
        val driverName = findViewById<EditText>(R.id.driverName)

        var cars = mutableListOf<Car>()
        var carNames = mutableSetOf<String>()

//        var carsMap : MutableMap <String, Int> = mutableMapOf()

        val myTxt = findViewById<EditText>(R.id.myEdit)
//        val myList = findViewById<ListView>(R.id.userlist)
        val addButton = findViewById<Button>(R.id.addBtn)



        var arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, arrayOf(carNames)
        )

//        myList.adapter = arrayAdapter

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
                cars.add(newCar)
                carNames.add("Название:" + carName + "\nПробег:" + carNumber.text.toString() + "\nИмя водителя:" + driverName.text.toString())
                carNames.add(carName)
                myTxt.text.clear()
                arrayAdapter.notifyDataSetChanged()
                toastShow("$carType $carName успешно добален")
            }

        }
        btnListView.setOnClickListener {
            val switchActivityIntent = Intent(
                this,
                listViewActivity::class.java,

            )
            val bundle = Bundle().apply {
                putParcelableArrayList("cars", ArrayList(cars))
            }
            switchActivityIntent.putStringArrayListExtra("car_names", ArrayList(carNames));
            switchActivityIntent.putExtras(bundle)
            startActivity(switchActivityIntent)

        }
    }

    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }



}


