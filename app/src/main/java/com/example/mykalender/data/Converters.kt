package com.example.mykalender.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

// Room hanya paham tipe dasar (teks, angka). Kelas ini mengajari Room
// cara menyimpan LocalDate dan LocalTime sebagai teks.
class Converters {
    @TypeConverter fun dariTanggal(nilai: LocalDate): String = nilai.toString()
    @TypeConverter fun keTanggal(teks: String): LocalDate = LocalDate.parse(teks)
    @TypeConverter fun dariJam(nilai: LocalTime): String = nilai.toString()
    @TypeConverter fun keJam(teks: String): LocalTime = LocalTime.parse(teks)
}