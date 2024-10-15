package Logic

import android.os.Parcel
import android.os.Parcelable

@Parcelize
data class Car(val name: String?, var newCar: Boolean, var carMiliage: Int, var driverName: String?): Parcelable
{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeByte(if (newCar) 1 else 0)
        parcel.writeInt(carMiliage)
        parcel.writeString(driverName)
    }

    override fun describeContents(): Int {
        return 0
    }





    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }


}

annotation class Parcelize
