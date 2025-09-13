package com.example.callblocker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppContent() }
    }
}

@Composable
fun AppContent() {
    val prefsKey = "block_prefs"
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = context.getSharedPreferences(prefsKey, android.content.Context.MODE_PRIVATE)

    var blocks by remember { mutableStateOf(prefs.getStringSet("blocks", setOf("+99878"))!!.toMutableSet()) }
    var newEntry by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Блокируемые префиксы / номера:", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        for (b in blocks) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(b)
                Button(onClick = {
                    val m = blocks.toMutableSet()
                    m.remove(b)
                    blocks = m
                    prefs.edit().putStringSet("blocks", blocks).apply()
                }) { Text("Удалить") }
            }
            Spacer(Modifier.height(6.dp))
        }

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = newEntry, onValueChange = { newEntry = it }, label = { Text("Добавить префикс или номер (+99878)") })
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = {
                if (newEntry.isNotBlank()) {
                    val m = blocks.toMutableSet()
                    m.add(newEntry.trim())
                    blocks = m
                    prefs.edit().putStringSet("blocks", blocks).apply()
                    newEntry = ""
                }
            }) { Text("Добавить") }

            Spacer(Modifier.width(8.dp))

            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
            }) { Text("Настройки систем. диспетчера вызовов") }
        }

        Spacer(Modifier.height(20.dp))
        Divider()
        Spacer(Modifier.height(10.dp))
        Text("Запись звонков:", style = MaterialTheme.typography.h6)
        Text("Встроенная запись (если есть на вашем Samsung) — рекомендованный путь. Сторонняя двусторонняя запись нестабильна и может нарушать политики.")
    }
}
