package com.example.lab_3

import Logic.Car
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
//        val text_Name = findViewById<EditText>(R.id.car_name)
//        val txt = findViewById<TextView>(R.id.textView)
//        val back = findViewById<Button>(R.id.button)
//
//
//        var car: Car? = intent.getParcelableExtra("car")
//        var car2 = car
//        if (car != null)
//        {
//            text_Name.setText(car.driverName)
//            if (car != null) {
//                car.driverName = "asd"
//            }
//            if (car != null) {
//                txt.setText(car.driverName)
//            }
//        }
//
//        back.setOnClickListener {
//            val resultIntent = Intent()
//            resultIntent.putExtra("updatedItem", car)
//            setResult(RESULT_OK, resultIntent)
//            finish()
//        }
//
//    }
//
//    companion object {
//        val RESULT_OK: Int
//            get() = 1
//    }
    }
}