package com.fitform.ai.ui.resources

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.InputStream

/**
 * Loads program names and descriptions from JSON assets file.
 * Uses Gson library for JSON parsing.
 */
class ProgramDataLoader(private val context: Context) {
    private val json: JsonObject by lazy { loadJsonFromAssets() }
    private val gson = Gson()

    private fun loadJsonFromAssets(): JsonObject {
        val fileName = "strings/programs.json"
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            gson.fromJson(jsonString, JsonObject::class.java)
        } catch (e: Exception) {
            JsonObject() // Empty object if file not found
        }
    }

    fun getProgramName(programId: ProgramId, language: String): String {
        val programKey = programId.id
        val programsObj = json.getAsJsonObject("programs")
        val programObj = programsObj?.getAsJsonObject(programKey)
        val nameObj = programObj?.getAsJsonObject("name")
        return when (language) {
            "ru" -> nameObj?.get("ru")?.asString ?: nameObj?.get("en")?.asString ?: programKey
            else -> nameObj?.get("en")?.asString ?: programKey
        }
    }

    fun getProgramDescription(programId: ProgramId, language: String): String {
        val programKey = programId.id
        val programsObj = json.getAsJsonObject("programs")
        val programObj = programsObj?.getAsJsonObject(programKey)
        val descriptionObj = programObj?.getAsJsonObject("description")
        return when (language) {
            "ru" -> descriptionObj?.get("ru")?.asString ?: descriptionObj?.get("en")?.asString ?: ""
            else -> descriptionObj?.get("en")?.asString ?: ""
        }
    }
}
