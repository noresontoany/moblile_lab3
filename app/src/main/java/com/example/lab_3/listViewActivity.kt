package com.example.lab_3

import Interfaces.OnItemClickListner
import Logic.carHolder
import Views.CustomRecyclerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class listViewActivity : AppCompatActivity(), OnItemClickListner {
    private lateinit var adapter: CustomRecyclerAdapter
    private var selectedPosition = -1
    private var lastRedacted = -1
    companion object {
        const val IDM_DELETE = 101
        const val IDM = 102
    }
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

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = CustomRecyclerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager =  GridLayoutManager(this, 2)
        val carData = application as carHolder
        val menu = bottomNavigationView.menu
        registerForContextMenu(bottomNavigationView);

        val prevItem = menu.findItem(R.id.navigation_view)
        prevItem.isVisible = false

        carData.getSharedData().observe(this) { data ->
            val tempDescription = carData.getCarDescriptions()
            adapter.data = data.toList()
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


    override fun onItemCLik(pos: Int) {
        val switchActivityIntent = Intent(
                this,
                carDescriptionActivity::class.java,
                )
            lastRedacted = pos
            switchActivityIntent.putExtra("id", pos)
            startActivity(switchActivityIntent)

    }

    override fun onContextMenu(view: ImageView, pos: Int) {
        registerForContextMenu(view)
        selectedPosition = pos
        Toast.makeText(this, selectedPosition.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Удалить")


    }
    override fun onContextItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            IDM_DELETE -> {
                if (selectedPosition != -1) {
                    val carData = application as carHolder
                    carData.deleteCar(selectedPosition) // удаляем элемент
                    Toast.makeText(this, "Элемент удален", Toast.LENGTH_SHORT).show()
                }

                false
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onMenuItemCLick(men: MenuItem) {
        Toast.makeText(this, "ЕЩЕ РАЗ", Toast.LENGTH_SHORT).show()
    }

}