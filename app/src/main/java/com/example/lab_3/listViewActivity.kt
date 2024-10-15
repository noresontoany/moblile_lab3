package com.example.lab_3

import Logic.Car
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class listViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        val listOfCars = findViewById<ListView>(R.id.listViewCars)

        val carNames = intent.getStringArrayListExtra("car_names")

//        val carNames = arrayOf("qwe","qwe","qwe","qwe","qwe","qwe","qwe","qwe","qwe","qwe","qwe","qwelats","qwe","qwe","qwe","qwe","qwelats","qwe","qwe","qwe","qwe","qwelats","qwe","qwe","qwe","qwe","qwelats")
        var cars =  ArrayList(intent.extras?.getParcelableArrayList<Car>("cars"))

        var arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, listOf("Пусто")
        )
        if (carNames != null)
        {
            arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, carNames
            )
        }

        listOfCars.adapter = arrayAdapter

        listOfCars.setOnItemClickListener { parent, view, position, id ->
            toastShow(position.toString())
            val switchActivityIntent = Intent(
                this,
                carDescriptionActivity::class.java,
                )
            val car = cars?.get(id.toInt())
            switchActivityIntent.putExtra("car", car)
            val ll = startActivityForResult(switchActivityIntent, REQUEST_CODE_EDIT)
            arrayAdapter.notifyDataSetChanged()
        }

    }




    private fun toastShow(message: String)
    {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object{
        const val REQUEST_CODE_EDIT = 0
    }


}