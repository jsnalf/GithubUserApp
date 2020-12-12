package com.vanta.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vanta.githubuserapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "github_user_app"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.UserColumns.USERNAME} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.NAME} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.LOCATION} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.REPOSITORY} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.COMPANY} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.FOLLOWERS} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.FOLLOWING} TEXT NOT NULL," +
                "${DatabaseContract.UserColumns.AVATAR_URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}