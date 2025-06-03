package com.example.ahima.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ahima.model.UserProfile
import java.io.File

@Composable
fun ReportScreen() {
    val context = LocalContext.current
    var status by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Создание отчёта", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val reportFile = File(context.cacheDir, "report.txt")
            reportFile.writeText("Отчёт пользователя\nPHQ-9: 10\nGAD-7: 7")

            val uri = Uri.fromFile(reportFile)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/plain"
            }

            context.startActivity(Intent.createChooser(shareIntent, "Отправить отчёт"))
            status = "Отчёт сгенерирован и готов к отправке"
        }) {
            Text("Сгенерировать отчёт")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(status)
    }
}
