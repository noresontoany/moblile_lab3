package Logic
import java.util.UUID
data class Car(
    var name: String?,
    var carType: Boolean,
    var carMiliage: Int,
    var driverName: String?
)
{
    val  id: String = UUID.randomUUID().toString()
//    val  id: String = "UUID.randomUUID().toString()"
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