package com.example.mykalender.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "agenda")
data class Agenda(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val judul: String,
    val catatan: String = "",
    val jam: LocalTime,
    val tanggal: LocalDate,
    val ingatkan: Boolean = false
)