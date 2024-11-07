package Logic

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

class carHolder : Application() {
    private val cars : MutableLiveData<List<Car>> = MutableLiveData()
    fun setSharedData(arr : List<Car>)
    {
        this.cars.value = arr
    }
    fun getSharedData(): MutableLiveData<List<Car>>
    {
        return cars
    }
    fun getCarDescriptions(): List<String>
    {
        val temp =  mutableListOf<String>()
        for (el in cars.value!!)
        {
            var s = el.name + " - ";
            if (el.carType)
                s += "электрокар"
            else
                s += "автомобиль"
            s+="\nПробег" + el.carMiliage + "\nИмя водителя:" + el.driverName
            temp.add(s)
        }
        return temp
    }

    fun getCarNames():Set<String>
    {
        val temp = mutableSetOf<String>()
        for (el in cars.value!!)
        {
            if (el.name != null) {
                temp.add(el.name)
            }
        }
        return temp
    }
    fun addCars(car: Car)
    {
        val currentCars = cars.value ?: emptyList()
        cars.value = currentCars + car // Создаём новый список
    }
    fun getCar(id: Int) : Car
    {
        return cars.value!![id]
    }
    fun updateCar(id: Int, car: Car)
    {
        var currentCars = (cars.value ?: emptyList()).toMutableList()
        currentCars[id] = car
        cars.value = currentCars.toList()
    }
    fun deleteCar(id: Int)
    {
        var currentCars = (cars.value ?: emptyList()).toMutableList()
        currentCars.removeAt(id)
        cars.value = currentCars.toList()
        if (id != 0) {
            Toast.makeText(this, "Пытались удалить" + id.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}