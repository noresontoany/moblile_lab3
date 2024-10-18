package com.example.lab_3

import Logic.Car
import Logic.carHolder
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class listViewActivity : AppCompatActivity() {
    private var carDescription  = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation2)


        val carData = application as carHolder
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carDescription)
        val menu = bottomNavigationView.menu
        val prevItem = menu.findItem(R.id.navigation_view)
        prevItem.isVisible = false

        carData.getSharedData().observe(this) { data ->
            val tempDescription = carData.getCarDescriptions()
            arrayAdapter.clear()
            carDescription.addAll(tempDescription)
            arrayAdapter.notifyDataSetChanged()
        }
        val listOfCars = findViewById<ListView>(R.id.listViewCars)
        listOfCars.adapter = arrayAdapter

        listOfCars.setOnItemClickListener { parent, view, position, id ->
            val switchActivityIntent = Intent(
                this,
                carDescriptionActivity::class.java,

                )

            switchActivityIntent.putExtra("id", id.toInt())
            startActivity(switchActivityIntent)

        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_previous -> {
                    onBackPressed()
                    true
                }
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


}