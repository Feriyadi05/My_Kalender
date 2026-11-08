package com.example.mykalender

import android.content.Context
import java.time.LocalTime

// Satu-satunya pintu ke SharedPreferences.
class PrefsManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(NAMA_FILE, Context.MODE_PRIVATE)

    // Mode gelap: true = tema gelap
    var modeGelap: Boolean
        get() = prefs.getBoolean(KEY_MODE_GELAP, false)
        set(nilai) {
            prefs.edit().putBoolean(KEY_MODE_GELAP, nilai).apply()
        }

    // Pilihan terakhir switch "Ingatkan saya"
    var ingatkanDefault: Boolean
        get() = prefs.getBoolean(KEY_INGATKAN, true)
        set(nilai) {
            prefs.edit().putBoolean(KEY_INGATKAN, nilai).apply()
        }

    // Jam terakhir yang dipilih
    var jamDefault: LocalTime
        get() = LocalTime.of(prefs.getInt(KEY_JAM, 8), prefs.getInt(KEY_MENIT, 0))
        set(nilai) {
            prefs.edit()
                .putInt(KEY_JAM, nilai.hour)
                .putInt(KEY_MENIT, nilai.minute)
                .apply()
        }

    companion object {
        private const val NAMA_FILE = "pengaturan_kalender"
        private const val KEY_MODE_GELAP = "mode_gelap"
        private const val KEY_INGATKAN = "ingatkan_default"
        private const val KEY_JAM = "jam_default"
        private const val KEY_MENIT = "menit_default"
    }
}