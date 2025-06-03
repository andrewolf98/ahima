package com.example.ahima.report

import android.content.Context
import android.util.Log
import com.example.ahima.model.UserProfile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ReportGenerator {

    fun generateReport(context: Context, profile: UserProfile): File? {
        return try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val reportFile = File(context.filesDir, "ahima_report_$date.txt")
            val content = buildString {
                append("AHIMA Отчёт\n")
                append("Дата: $date\n")
                append("PHQ-9: ${profile.phq9Score}\n")
                append("GAD-7: ${profile.gad7Score}\n")
                append("\nРекомендации:\n")

                if (profile.phq9Score >= 15) {
                    append("- Возможные признаки депрессии. Рекомендуется консультация специалиста.\n")
                }
                if (profile.gad7Score >= 15) {
                    append("- Высокий уровень тревоги. Рассмотрите дыхательные практики.\n")
                }
                if (profile.phq9Score < 10 && profile.gad7Score < 10) {
                    append("- Профиль в пределах нормы. Поддерживайте рутину.\n")
                }
            }
            reportFile.writeText(content)
            reportFile
        } catch (e: Exception) {
            Log.e("ReportGenerator", "Ошибка генерации отчёта", e)
            null
        }
    }
}
