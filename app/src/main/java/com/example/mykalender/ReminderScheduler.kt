package com.example.mykalender

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.mykalender.data.Agenda
import java.time.LocalDateTime
import java.time.ZoneId

object ReminderScheduler {

    private fun pendingIntent(context: Context, agenda: Agenda): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("judul", agenda.judul)
            putExtra("catatan", agenda.catatan)
            putExtra("id", agenda.id.toInt())
        }
        return PendingIntent.getBroadcast(
            context,
            agenda.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun jadwalkan(context: Context, agenda: Agenda) {
        val waktu = LocalDateTime.of(agenda.tanggal, agenda.jam)
        val millis = waktu.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (millis <= System.currentTimeMillis()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            millis,
            pendingIntent(context, agenda)
        )
    }

    fun batalkan(context: Context, agenda: Agenda) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context, agenda))
    }
}