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
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class listViewActivity : AppCompatActivity(), OnItemClickListner {
    private lateinit var adapter: CustomRecyclerAdapter
    private var selectedItemId:Long = (-1).toLong()
    private var lastRedactedId:Long = (-1).toLong()
    companion object {
        const val IDM_DELETE = 101
        const val IDM_MMM = 102
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

        val filterB = findViewById<FloatingActionButton>(R.id.filterButton)
        filterB.setOnClickListener {
            showFilterSortDialog()
        }

        carData.getSharedData().observe(this) { data ->
//            val tempDescription = carData.getCarDescriptions()
            adapter.data = data
        }
        adapter.filters = carData.filters
        adapter.filter()
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


    override fun onItemCLik(id: Long) {
        val switchActivityIntent = Intent(
            this,
            carDescriptionActivity::class.java,
        )
        lastRedactedId = id
        switchActivityIntent.putExtra("id", lastRedactedId)
        startActivity(switchActivityIntent)

    }

    override fun onContextMenu(imageView: ImageView, id: Long) {
        registerForContextMenu(imageView)
        selectedItemId = id
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
                if (selectedItemId.toInt() != -1) {
                    val carData = application as carHolder
                    carData.deleteCar(selectedItemId)
                    Toast.makeText(this, "Элемент удален", Toast.LENGTH_SHORT).show()
                }
                false
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onMenuItemCLick(item: MenuItem) {
        Toast.makeText(this, "ЕЩЕ РАЗ", Toast.LENGTH_SHORT).show()
    }





    private fun showFilterSortDialog() {
        val carData = application as carHolder
        val items = carData.filters_names
        val checkedItems = carData.filters
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter and Sort Options")
            .setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
                // Обновляем состояние выбранного элемента
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Apply") { dialog, _ ->
                carData.filters = checkedItems
                adapter.filters = checkedItems
                adapter.filter()
                applyFiltersAndSort(checkedItems)
            }
            .setNegativeButton("Cancel") { dialog, _ ->

                dialog.dismiss() // Закрыть диалог при отмене
            }
            .show()
    }
    private fun applyFiltersAndSort(checkedItems: BooleanArray) {
        if (checkedItems[0]) sortCarsByDescending() // Sort by Descending
        if (checkedItems[1]) showOnlyElectricCars() // Show Only Electric Cars
        if (checkedItems[2]) showOnlyNonElectricCars() // Show Only Non-Electric Cars
    }

    private fun sortCarsByDescending() {
        Toast.makeText(this, "Sorted by Descending Mileage", Toast.LENGTH_SHORT).show()
    }

    private fun showOnlyElectricCars() {

        Toast.makeText(this, "Showing Only Electric Cars", Toast.LENGTH_SHORT).show()
    }

    private fun showOnlyNonElectricCars() {
        Toast.makeText(this, "Showing Only Non-Electric Cars", Toast.LENGTH_SHORT).show()
    }


}