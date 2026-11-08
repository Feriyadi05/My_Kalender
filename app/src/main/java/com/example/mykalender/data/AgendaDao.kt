package com.example.mykalender.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDao {
    @Query("SELECT * FROM agenda ORDER BY tanggal, jam")
    fun semua(): Flow<List<Agenda>>

    @Insert
    suspend fun tambah(agenda: Agenda): Long

    @Delete
    suspend fun hapus(agenda: Agenda)
}