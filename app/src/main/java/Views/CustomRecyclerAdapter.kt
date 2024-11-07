package Views

import Interfaces.OnItemClickListner
import Logic.Car
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3.R

class CustomRecyclerAdapter(private val listener: OnItemClickListner): RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    var data: List<Car> = emptyList()
        set(newValue) {
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
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.updateButton.setOnClickListener {
            listener.onItemCLik(position)
        }

        holder.imageView.setOnClickListener{
            listener.onContextMenu(holder.imageView, position)
        }


        holder.textV.text = data[position].getCarDescription()
        if (data[position].carType)
        {
            holder.imageView.setImageResource(R.drawable.tesla)
        }
        else {
            holder.imageView.setImageResource(R.drawable.car)
        }


    }



}
