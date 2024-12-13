package data

import android.provider.BaseColumns
import org.apache.poi.ss.formula.functions.Column

object CarContract {
    object PersonEntry : BaseColumns{
        const val TABLE_NAME = "driver"
        const val _ID = BaseColumns._ID;
        const val COLUMN_DRIVE_NAME = "name"
    }

    object CarEntry : BaseColumns{
        const val TABLE_NAME = "cars"
        const val _ID = BaseColumns._ID;
        const val COLUMN_CAR_NAME = "name"
        const val COLUMN_CAR_TYPE = "car_type"
        const val COLUMN_CAR_MILIAGE = "car_miliage"
        const val COLUMN_PERSON_ID = "person_id"
    }
}