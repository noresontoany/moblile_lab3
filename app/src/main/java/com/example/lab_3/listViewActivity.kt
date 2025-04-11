package com.example.lab_3

import Interfaces.OnItemClickListner
import Logic.carHolder
import PriceDialog
import Views.CustomRecyclerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import data.models.Brand
import data.models.Model
import data.models.Price
import data.repository.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.Executors


class listViewActivity : AppCompatActivity(), OnItemClickListner {
    private lateinit var adapter: CustomRecyclerAdapter
    private var selectedItemId:Long = (-1).toLong()
    private var lastRedactedId:Long = (-1).toLong()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    companion object {
        const val IDM_DELETE = 101
        const val IDM_PRICE = 102
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

    private var filterThread: Thread? = null

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
        menu?.add(Menu.NONE, IDM_PRICE, Menu.NONE, "Узнать цену")
    }
    override  fun onContextItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            IDM_DELETE -> {
                if (selectedItemId.toInt() != -1) {
                    val carData = application as carHolder
                    val msg = carData.deleteCar(selectedItemId)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                false
            }
            IDM_PRICE -> {
                if (selectedItemId.toInt() != -1) {
                    getPrice()
                }
                false
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onMenuItemCLick(item: MenuItem) {
        Toast.makeText(this, "ЕЩЕ РАЗ", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFilterSortDialog() {
        val carData = application as carHolder
        val items = carData.filters_names
        val checkedItems = carData.filters.copyOf()

        AlertDialog.Builder(this)
            .setTitle("Filter and Sort Options")
            .setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Apply") { _, _ ->
                carData.filters = checkedItems
                adapter.filters = checkedItems


                filterThread?.interrupt()

                filterThread = Thread {
                    try {
                        val filteredList = adapter.filterInBackground()

                        if (!Thread.interrupted()) {
                            runOnUiThread {
                                if (!isDestroyed) {
                                    adapter.showData = filteredList
                                    adapter.notifyDataSetChanged()
                                    showFilterToasts(checkedItems)
                                }
                            }
                        }
                    } catch (e: InterruptedException) {
                        Log.d("Thread1", "Фильтрация отменена")
                    }
                }.apply {
                    start()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showFilterToasts(checkedItems: BooleanArray) {
        if (checkedItems[0]) Toast.makeText(this, "Sorted by mileage", Toast.LENGTH_SHORT).show()
        if (checkedItems[1]) Toast.makeText(this, "Electric only", Toast.LENGTH_SHORT).show()
        if (checkedItems[2]) Toast.makeText(this, "Non-electric only", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        filterThread?.interrupt()
    }

    override fun onPause() {
        super.onPause()
        filterThread?.interrupt()
    }

    fun getPrice() {
        val dialog = PriceDialog()
        dialog.show(supportFragmentManager, "priceDialog")
    }

}