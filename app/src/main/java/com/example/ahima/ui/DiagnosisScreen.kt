package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ahima.data.gad7Questions
import com.example.ahima.data.phq9Questions
import com.example.ahima.storage.LocalStorage

@Composable
fun DiagnosisScreen(navController: NavController) {
    var currentTest by remember { mutableStateOf("PHQ-9") }
    var answers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }

    val questions = if (currentTest == "PHQ-9") phq9Questions else gad7Questions

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Диагностический тест: $currentTest", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        questions.forEachIndexed { index, question ->
            Text("${index + 1}. $question")
            Row(Modifier.fillMaxWidth()) {
                (0..3).forEach { score ->
                    Button(
                        onClick = { answers[index] = score },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("$score")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row {
            Button(onClick = {
                val total = answers.values.sum()
                if (currentTest == "PHQ-9") {
                    LocalStorage.savePHQ9(total)
                    currentTest = "GAD-7"
                    answers.clear()
                } else {
                    LocalStorage.saveGAD7(total)
                    val profile = LocalStorage.getProfile()
                    println("Профиль: PHQ-9=${profile.phq9Score}, GAD-7=${profile.gad7Score}")
                }
            }) {
                Text(if (currentTest == "PHQ-9") "Далее к GAD-7" else "Завершить")
            }
        }
    }
}
