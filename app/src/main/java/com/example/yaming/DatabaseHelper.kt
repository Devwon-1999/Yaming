package com.example.yaming

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "User.db"
        private const val DB_VERSION = 1

        const val TABLE_USER_INFO = "userinfo"
        const val COLUMN_ID = "id"
        const val COLUMN_HEIGHT = "height"
        const val COLUMN_WEIGHT = "weight"

        private const val DATABASE_CREATE = "create table $TABLE_USER_INFO (" +
                "$COLUMN_ID integer primary key autoincrement, " +
                "$COLUMN_HEIGHT integer not null, " +
                "$COLUMN_WEIGHT integer not null);"


    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        onCreate(db)
    }
}