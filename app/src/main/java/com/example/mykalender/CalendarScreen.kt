package com.example.mykalender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykalender.data.Agenda
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val localeId = Locale("id", "ID")
private val formatJam = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun CalendarScreen(
    prefs: PrefsManager,
    modeGelap: Boolean,
    onModeGelapChange: (Boolean) -> Unit,
    vm: AgendaViewModel = viewModel()
) {
    val namaHari = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

    var month by remember { mutableStateOf(YearMonth.now()) }
    var selected by remember { mutableStateOf(LocalDate.now()) }
    var tampilDialog by remember { mutableStateOf(false) }

    val daftarAgenda by vm.daftarAgenda.collectAsState()

    val agendaHariIni = daftarAgenda
        .filter { it.tanggal == selected }
        .sortedBy { it.jam }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { tampilDialog = true }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mode gelap", style = MaterialTheme.typography.titleMedium)
                Switch(checked = modeGelap, onCheckedChange = onModeGelapChange)
            }

            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { month = month.minusMonths(1) }) { Text("<") }
                Text(
                    text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy", localeId)),
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = { month = month.plusMonths(1) }) { Text(">") }
            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {
                namaHari.forEach { nama ->
                    Text(
                        text = nama,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = if (nama == "Min") Color.Red else Color.Unspecified
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val offset = month.atDay(1).dayOfWeek.value - 1
            val jumlahHari = month.lengthOfMonth()
            val jumlahBaris = (offset + jumlahHari + 6) / 7

            for (baris in 0 until jumlahBaris) {
                Row(Modifier.fillMaxWidth()) {
                    for (kolom in 0..6) {
                        val angka = baris * 7 + kolom - offset + 1

                        if (angka in 1..jumlahHari) {
                            val tanggal = month.atDay(angka)
                            val isToday = tanggal == LocalDate.now()
                            val isSelected = tanggal == selected
                            val punyaAgenda = daftarAgenda.any { it.tanggal == tanggal }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> MaterialTheme.colorScheme.primaryContainer
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable { selected = tanggal },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$angka",
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                            kolom == 6 -> Color.Red
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    if (punyaAgenda) {
                                        Box(
                                            Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                    else MaterialTheme.colorScheme.tertiary
                                                )
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(Modifier.weight(1f).height(48.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Text(
                text = selected.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", localeId)),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            if (agendaHariIni.isEmpty()) {
                Text("Belum ada agenda. Tekan + untuk menambah.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(agendaHariIni, key = { it.id }) { agenda ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(agenda.judul, fontWeight = FontWeight.Bold)
                                    Text(
                                        agenda.jam.format(formatJam) +
                                                if (agenda.ingatkan) "  •  Pengingat aktif" else "",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    if (agenda.catatan.isNotBlank()) {
                                        Text(agenda.catatan, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                                TextButton(onClick = { vm.hapus(agenda) }) {
                                    Text("Hapus", color = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (tampilDialog) {
        TambahAgendaDialog(
            tanggal = selected,
            jamAwal = prefs.jamDefault,
            ingatkanAwal = prefs.ingatkanDefault,
            onDismiss = { tampilDialog = false },
            onSimpan = { judul, catatan, jam, ingatkan ->
                prefs.jamDefault = jam
                prefs.ingatkanDefault = ingatkan
                vm.tambah(
                    Agenda(
                        judul = judul,
                        catatan = catatan,
                        jam = jam,
                        tanggal = selected,
                        ingatkan = ingatkan
                    )
                )
                tampilDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahAgendaDialog(
    tanggal: LocalDate,
    jamAwal: LocalTime,
    ingatkanAwal: Boolean,
    onDismiss: () -> Unit,
    onSimpan: (String, String, LocalTime, Boolean) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var ingatkan by remember { mutableStateOf(ingatkanAwal) }
    var tampilJam by remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState(
        initialHour = jamAwal.hour,
        initialMinute = jamAwal.minute,
        is24Hour = true
    )
    val jam = LocalTime.of(timeState.hour, timeState.minute)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agenda baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(tanggal.format(DateTimeFormatter.ofPattern("d MMMM yyyy", localeId)))
                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Judul") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan (opsional)") }
                )
                OutlinedButton(onClick = { tampilJam = true }) {
                    Text("Jam: ${jam.format(formatJam)}")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = ingatkan, onCheckedChange = { ingatkan = it })
                    Spacer(Modifier.width(8.dp))
                    Text("Ingatkan saya")
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = judul.isNotBlank(),
                onClick = { onSimpan(judul.trim(), catatan.trim(), jam, ingatkan) }
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )

    if (tampilJam) {
        AlertDialog(
            onDismissRequest = { tampilJam = false },
            confirmButton = {
                TextButton(onClick = { tampilJam = false }) { Text("OK") }
            },
            text = { TimePicker(state = timeState) }
        )
    }
}