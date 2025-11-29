package es.lasalle.videoconferencia.s04.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson<List<String>>(it, listType)
        }
    }

    private fun isValidJson(jsonString: String): Boolean {
        return try {
            gson.fromJson(jsonString, Object::class.java)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun safeToStringList(value: String?): List<String>? {
        return value?.let {
            try {
                val listType = object : TypeToken<List<String>>() {}.type
                gson.fromJson<List<String>>(it, listType) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}