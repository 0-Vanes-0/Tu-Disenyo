package com.example.jetsnack.ui.home.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object DesignStorage {
    private const val DESIGNS_DIR = "saved_designs"

    fun saveDesign(context: Context, bitmap: Bitmap, clothesType: String): String? {
        val dir = File(context.filesDir, DESIGNS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = "design_${clothesType}_${UUID.randomUUID()}.png"
        val file = File(dir, fileName)
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSavedDesigns(context: Context): List<String> {
        val dir = File(context.filesDir, DESIGNS_DIR)
        if (!dir.exists()) return emptyList()
        return dir.listFiles { file -> file.extension == "png" }
            ?.sortedByDescending { it.lastModified() }
            ?.map { it.absolutePath } ?: emptyList()
    }

    fun loadBitmap(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }

    fun getClothesTypeFromPath(path: String): String {
        val file = File(path)
        val name = file.name
        if (name.startsWith("design_") && name.endsWith(".png")) {
            val parts = name.split("_")
            if (parts.size >= 3) {
                return parts[1]
            }
        }
        return "Custom Print"
    }
}
