package com.example.workshopapp.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log.e
import com.example.workshopapp.Constants
import com.example.workshopapp.models.attended
import com.example.workshopapp.models.user
import com.example.workshopapp.models.workshop

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WorkShopDatabase"
        private const val TABLE_USERS = "UserTable"
        private const val TABLE_WORKSHOPS = "WorkshopTable"
        private const val TABLE_USER_WORKSHOPS = "UserWorkshopTable"

        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_WORKSHOP_NAME = "workshopName"
        private const val KEY_DATE = "date"
        private const val KEY_WORKSHOP_DESC = "workshopDesc"
        private const val KEY_FEES = "fees"
        private const val KEY_WORKSHOP_ID = "workshopID"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USERS + " ( "
                + KEY_EMAIL + " TEXT PRIMARY KEY, "
                + KEY_USERNAME + " TEXT, "
                + KEY_PASSWORD + " TEXT );")

        val CREATE_WORKSHOP_TABLE = ("CREATE TABLE " + TABLE_WORKSHOPS + " ( "
                + KEY_WORKSHOP_ID + " INT PRIMARY KEY, "
                + KEY_WORKSHOP_NAME + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_WORKSHOP_DESC + " TEXT, "
                + KEY_FEES + " INT );")

        val CREATE_USER_WORKSHOP_TABLE = ("CREATE TABLE " + TABLE_USER_WORKSHOPS + " ( "
                + KEY_EMAIL + " TEXT, "
                + KEY_WORKSHOP_ID + " INT);")

        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_WORKSHOP_TABLE)
        db?.execSQL(CREATE_USER_WORKSHOP_TABLE)

        val allWorkshop = Constants.getWorkShops()
        for(workshop in allWorkshop){
            val contentValues = ContentValues()
            contentValues.put(KEY_WORKSHOP_ID, workshop.workshopId)
            contentValues.put(KEY_WORKSHOP_NAME, workshop.workshopName)
            contentValues.put(KEY_DATE, workshop.date)
            contentValues.put(KEY_WORKSHOP_DESC, workshop.workshopDesc)
            contentValues.put(KEY_FEES, workshop.fees)

            val result =db?.insert(TABLE_WORKSHOPS, null, contentValues)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS '$TABLE_USERS'");
        db?.execSQL("DROP TABLE IF EXISTS '$TABLE_WORKSHOPS'");
        db?.execSQL("DROP TABLE IF EXISTS '$TABLE_USER_WORKSHOPS'");
        onCreate(db);
    }

    fun addUser(user : user) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_EMAIL, user.email)
        contentValues.put(KEY_USERNAME, user.username)
        contentValues.put(KEY_PASSWORD, user.password)

        val result =db.insert(TABLE_USERS, null, contentValues)
        db.close()

        return result
    }

    fun addWorkshopUser(attended: attended) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_EMAIL, attended.email)
        contentValues.put(KEY_WORKSHOP_ID, attended.workshopId)

        val result =db.insert(TABLE_USER_WORKSHOPS, null, contentValues)
        db.close()

        return result
    }

    @SuppressLint("Range", "LongLogTag")
    fun loginCheck(email : String) : user? {
        val db = this.readableDatabase
        var user : user ?= null
        try {
            val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_EMAIL" + "=?",
                arrayOf(email)
            )
            if (cursor.moveToFirst()){
                user = user(
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                )
                e("User" , user.toString())
            }
            cursor.close()
        }catch (e : SQLiteException){
            e("Error occurred in extracting user" , e.toString())
            return user
        }
        return user
    }

    @SuppressLint("Range", "LongLogTag")
    fun getWkList() : ArrayList<workshop> {
        val query = "SELECT * FROM $TABLE_WORKSHOPS;"
        val db = this.readableDatabase
        val wkList : ArrayList<workshop> = ArrayList()
        try {
            val cursor : Cursor = db.rawQuery(query,null)
            if (cursor.moveToFirst()){
                do{
                    val wk = workshop(cursor.getInt(cursor.getColumnIndex(KEY_WORKSHOP_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_WORKSHOP_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_WORKSHOP_DESC)),
                    cursor.getInt(cursor.getColumnIndex(KEY_FEES)),
                    )
                    wkList.add(wk)
                }while (cursor.moveToNext())
            }
        }catch (e: SQLiteException){
            e("Error occurred in extracting workshop list" , e.toString())
            return wkList
        }

        return wkList
    }

    @SuppressLint("Range", "LongLogTag")
    fun getWkListPfUser(email: String) : ArrayList<Int> {
        val db = this.readableDatabase
        val wkList : ArrayList<Int> = ArrayList()
        try {
            val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USER_WORKSHOPS WHERE $KEY_EMAIL" + "=?",
                arrayOf(email)
            )
            if (cursor.moveToFirst()){
                do{
                    wkList.add(cursor.getInt(cursor.getColumnIndex(KEY_WORKSHOP_ID)))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e: SQLiteException){
            e("Error occurred in extracting Userworkshop list" , e.toString())
            return wkList
        }
        e("UserDashboard list" , wkList.toString())
        return wkList
    }

}