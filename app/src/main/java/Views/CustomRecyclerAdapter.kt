package Views

import Interfaces.OnItemClickListner
import Logic.Car
import Logic.carHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.window.application
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3.R

class CustomRecyclerAdapter(private val listener: OnItemClickListner): RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {


    var filters: BooleanArray = booleanArrayOf(false,true,true)
        set(value) {
            field = value.copyOf()
        }

    var showData: List<Car> = emptyList()

    var data: List<Car> = emptyList()
        set(newValue) {
            showData = filter(newValue)
            field = newValue
            notifyDataSetChanged()
        }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewCar)
        val textV: TextView = itemView.findViewById(R.id.textViewCar)
        val updateButton : Button = itemView.findViewById(R.id.buttonUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.updateButton.setOnClickListener {
            val carPos = data.indexOf(showData[position])
            listener.onItemCLik(data[carPos].id)
        }

        holder.imageView.setOnClickListener{
            val carPos = data.indexOf(showData[position])
            listener.onContextMenu(holder.imageView, data[carPos].id)
        }


        holder.textV.text = showData[position].getCarDescription()
        if (showData[position].carType)
        {
            holder.imageView.setImageResource(R.drawable.tesla)
        }
        else {
            holder.imageView.setImageResource(R.drawable.car)
        }
    }

    fun filter(list: List<Car>) : List<Car>
    {

        val showElectricCars = filters[1]
        val showNonElectricCars = filters[2]
        val shouldSort = filters[0]

        var filteredList = list
        if (!showElectricCars && showNonElectricCars) {
            filteredList = filteredList.filter { !it.carType }
        } else if (showElectricCars && !showNonElectricCars) {
            filteredList = filteredList.filter { it.carType }
        }


        if (shouldSort) {
            filteredList = filteredList.sortedByDescending { it.carMiliage }
        }

        return filteredList
    }

    fun filter()
    {

        val showElectricCars = filters[1]
        val showNonElectricCars = filters[2]
        val shouldSort = filters[0]

        var filteredList = data

        if (!showElectricCars && showNonElectricCars) {
            filteredList = filteredList.filter { !it.carType }
        } else if (showElectricCars && !showNonElectricCars) {
            filteredList = filteredList.filter { it.carType }
        }


        if (shouldSort) {
            filteredList = filteredList.sortedByDescending { it.carMiliage }
        }

        showData = filteredList
        notifyDataSetChanged()
    }



}
