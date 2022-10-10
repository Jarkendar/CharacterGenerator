package com.skrzypczak.charactergenerator

import android.content.Context
import android.util.Log
import com.skrzypczak.charactergenerator.utils.capitalizeWords
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.io.IOException
import kotlin.random.Random

class CharacterNameGenerator(context: Context, private val coroutineScope: CoroutineScope) {

    private val markow3Dictionary: Map<String, Map<String, Double>>

    init {
        markow3Dictionary = try {
            val json = context.assets.open("markow_dict_level_3.json").bufferedReader().use { it.readText() }

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Map<String, Map<String, Double>>> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Map::class.java, String::class.java, Double::class.javaObjectType ))
            jsonAdapter.fromJson(json) as Map<String, Map<String, Double>>
        } catch (e: IOException) {
            Log.e("ERROR", "CharacterNameGenerator init: $e", e)
            emptyMap()
        }
    }

    private fun getDictionary(markowLevel: Int) = markow3Dictionary

    fun generateNameUseMarkow(minLength: Int = 3, maxLength: Int, markowLevel: Int = 3, blockWhenEnd: (name: String) -> Unit) {
        coroutineScope.async {
            val dictionary = getDictionary(markowLevel)

            var startWord = dictionary.toList()[Random.nextInt(dictionary.size)].first

            while (startWord[0] in "-'\" " || startWord[1] in "-'\" ") {
                startWord = dictionary.toList()[Random.nextInt(dictionary.size)].first
            }

            var suffix = startWord.substring(startWord.length - markowLevel)

            var name = startWord

            val length = Random.nextInt(minLength, maxLength)
            for (i in startWord.length until length) {
                val nextChar = getNextLetter(dictionary, suffix)
                name += nextChar
                suffix += nextChar
                suffix = name.substring(name.length - markowLevel)
            }
            blockWhenEnd(name.trim().lowercase().capitalizeWords())
        }
    }

    private fun getNextLetter(dictionary: Map<String, Map<String, Double>>, suffix: String): String {
        return weightedRandom(dictionary[suffix] ?: dictionary.toList()[Random.nextInt(dictionary.size)].second)
    }

    private fun weightedRandom(row: Map<String, Double>): String {
        val sum = row.values.sum()
        val randomValue = Random.nextDouble(sum)

        var temp = .0

        for ((key, value) in row.entries) {
            temp += value
            if (randomValue <= temp) {
                return key
            }
        }
        return row.toList()[Random.nextInt(row.size)].first
    }
}