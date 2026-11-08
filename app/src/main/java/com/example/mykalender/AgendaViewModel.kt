package com.example.mykalender

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykalender.data.Agenda
import com.example.mykalender.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AgendaViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).agendaDao()

    val daftarAgenda = dao.semua()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun tambah(agenda: Agenda) {
        viewModelScope.launch {
            val id = dao.tambah(agenda)
            if (agenda.ingatkan) {
                ReminderScheduler.jadwalkan(
                    getApplication<Application>(),
                    agenda.copy(id = id)
                )
            }
        }
    }

    fun hapus(agenda: Agenda) {
        viewModelScope.launch {
            ReminderScheduler.batalkan(getApplication<Application>(), agenda)
            dao.hapus(agenda)
        }
    }
}