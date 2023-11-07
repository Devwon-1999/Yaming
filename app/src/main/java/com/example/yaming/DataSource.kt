package com.example.yaming

import android.content.ContentValues
import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.provider.ContactsContract.Data

class WeightDataSource(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    fun addSource(userinfo: UserInfoDB): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_ID, userinfo.id)
        values.put(DatabaseHelper.COLUMN_WEIGHT, userinfo.weight)
        values.put(DatabaseHelper.COLUMN_HEIGHT, userinfo.height)
        val insertId = db.insert(DatabaseHelper.TABLE_USER_INFO, null, values)
        db.close()
        return insertId
    }

    fun getAllSource(): List<UserInfoDB> {
        val UserInfoDataList = mutableListOf<UserInfoDB>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USER_INFO,
            arrayOf(DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_HEIGHT, DatabaseHelper.COLUMN_WEIGHT),
            null,
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val UserInfoDB = UserInfoDB()
                UserInfoDB.id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
                UserInfoDB.height = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT))
                UserInfoDB.weight = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT))
                UserInfoDataList.add(UserInfoDB)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return UserInfoDataList
    }
}