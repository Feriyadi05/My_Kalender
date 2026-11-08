package com.example.mykalender

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mykalender.ui.theme.MyKalenderTheme

class MainActivity : ComponentActivity() {

    private lateinit var prefs: PrefsManager

    // State di memori; nilai aslinya diambil dari SharedPreferences
    private var modeGelap by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PrefsManager(this)
        muatPengaturan()
        enableEdgeToEdge()
        setContent {
            val izinLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= 33) {
                    izinLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            MyKalenderTheme(darkTheme = modeGelap) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarScreen(
                        prefs = prefs,
                        modeGelap = modeGelap,
                        onModeGelapChange = { aktif ->
                            modeGelap = aktif        // ubah tampilan sekarang
                            prefs.modeGelap = aktif  // simpan dengan apply()
                        }
                    )
                }
            }
        }
    }

    // Dipanggil setiap layar muncul lagi: baca ulang pengaturan terbaru
    override fun onResume() {
        super.onResume()
        muatPengaturan()
    }

    private fun muatPengaturan() {
        modeGelap = prefs.modeGelap
    }
}