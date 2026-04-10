package com.example.magazyn

import java.sql.Connection
import java.sql.DriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseConnector {
    private const val IP = "188.68.236.147"
    private const val DB_NAME = "magazyn_db"
    private const val USER = "postgres"
    private const val PASS = "password"
    private const val URL = "jdbc:postgresql://$IP:5432/$DB_NAME"

    suspend fun getConnection(): Connection? = withContext(Dispatchers.IO) {
        return@withContext try {
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(URL, USER, PASS)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}