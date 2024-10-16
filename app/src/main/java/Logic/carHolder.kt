package Logic

import android.app.Application
import androidx.lifecycle.MutableLiveData

class carHolder : Application() {
    private val cars : MutableLiveData<List<Car>> = MutableLiveData()
    fun getSharedData(): MutableLiveData<List<Car>>
    {
        return cars
    }
    fun setSharedData(arr : List<Car>)
    {
        this.cars.value = arr
    }

    fun getCars() : List<Car>?
    {
        return cars.value
    }
    fun getCarDescriptions(): List<String>
    {
            val temp =  mutableListOf<String>()
        for (el in cars.value!!)
        {
            var s = el.name + " - ";
            if (el.isElectro)
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

    fun updateCar(id: Int, car: Car)
    {
        val currentCars = cars.value ?: emptyList()
//        currentCars[id]  = car
    }
}