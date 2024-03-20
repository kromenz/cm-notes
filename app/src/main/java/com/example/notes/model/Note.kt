package com.example.notes.model

import android.os.Parcelable
import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val note: String,
    val noteTitle: String,
    val date: Date
): Parcelable
