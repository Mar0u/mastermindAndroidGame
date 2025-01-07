package com.example.mastermindAndroid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="user", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val description: String = "",
    @ColumnInfo(name = "imageData", typeAffinity = ColumnInfo.BLOB)
    val imageData : ByteArray? = null,
    var games: Int = 0,
    var wins: Int = 0,
    var points: Int = 0
)