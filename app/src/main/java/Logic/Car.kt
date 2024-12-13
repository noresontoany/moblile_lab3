package Logic
data class Car(
    var name: String?,
    var carType: Boolean,
    var carMiliage: Int,
    var driverName: String?,
    val id: Long
)
{
    fun getCarDescription(): String
    {
        var s = this.name + " - ";
        if (this.carType)
            s += "электрокар"
        else
            s += "автомобиль"

        s+="\nПробег" + this.carMiliage + "\nИмя водителя:" + this.driverName
        return s
    }

}