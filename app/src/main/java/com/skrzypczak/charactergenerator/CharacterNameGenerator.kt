package com.skrzypczak.charactergenerator

import android.content.Context
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import java.io.IOException

class CharacterNameGenerator(context: Context, private val coroutineScope: CoroutineScope) {

    private val dictionary: Map<String, Map<String, Double>>

    init {
        dictionary = try {
            val json = context.assets.open("markow_dict_level_3.json").bufferedReader().use { it.readText() }

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Map<String, Map<String, Double>>> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Map::class.java, String::class.java, Double::class.javaObjectType ))
            jsonAdapter.fromJson(json) as Map<String, Map<String, Double>>
        } catch (e: IOException) {
            Log.e("ERROR", "CharacterNameGenerator init: $e", e)
            emptyMap()
        }
    }
}