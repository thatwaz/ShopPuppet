package com.thatwaz.shoppuppet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconResName: String,
    val colorResName: String,
    val initials: String? = null,
    val isPriority: Boolean = false
)



