package com.thatwaz.shoppuppet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconRef: Int,
    val colorResId: Int,
    val initials: String? = null
//    val colorRef: Int? = null
)

//@Entity(tableName = "shops")
//data class Shop(
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
//    val name: String,
//    val iconRef: String,
//    val colorRef: Int
//)

