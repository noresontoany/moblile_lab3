package data

import Logic.Car
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SQL_CREATE_CAR_TABLE =
        """
    CREATE TABLE ${CarContract.CarEntry.TABLE_NAME} (
        ${CarContract.CarEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${CarContract.CarEntry.COLUMN_CAR_NAME} TEXT NOT NULL,
        ${CarContract.CarEntry.COLUMN_CAR_TYPE} INTEGER NOT NULL, 
        ${CarContract.CarEntry.COLUMN_CAR_MILIAGE} INTEGER NOT NULL,
        ${CarContract.CarEntry.COLUMN_PERSON_ID} INTEGER NOT NULL,
        FOREIGN KEY (${CarContract.CarEntry.COLUMN_PERSON_ID}) REFERENCES ${CarContract.PersonEntry.TABLE_NAME} (${CarContract.PersonEntry._ID})
        ON DELETE CASCADE ON UPDATE CASCADE
    );
    """

    private val SQL_CREATE_PERSON_TABLE =
        """
    CREATE TABLE ${CarContract.PersonEntry.TABLE_NAME} (
        ${CarContract.PersonEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${CarContract.PersonEntry.COLUMN_DRIVE_NAME} TEXT NOT NULL
    );
    """

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_PERSON_TABLE)
        db.execSQL(SQL_CREATE_CAR_TABLE)
    }
    fun getDatabasePath(context:Context): String {
        return context.getDatabasePath(DATABASE_NAME).absolutePath
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "FeedReader2.db"
    }



}
