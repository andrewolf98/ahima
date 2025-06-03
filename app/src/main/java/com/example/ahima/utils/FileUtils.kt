package com.example.ahima.utils

import android.content.Context
import java.io.File

object FileUtils {

    fun readFile(context: Context, fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            null
        }
    }

    fun writeFile(context: Context, fileName: String, content: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun deleteFile(context: Context, fileName: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
}
