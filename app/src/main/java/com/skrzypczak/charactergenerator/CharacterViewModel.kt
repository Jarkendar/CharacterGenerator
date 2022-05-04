package com.skrzypczak.charactergenerator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharacterViewModel: ViewModel() {

    private val _characterName = MutableLiveData<String>().apply { postValue("") }
    val characterName: LiveData<String> = _characterName

    private val _race = MutableLiveData<String>().apply { postValue("") }
    val race: LiveData<String> = _race

    private val _attrStrong = MutableLiveData<Int>().apply { postValue(3) }
    val attrStrong: LiveData<Int> = _attrStrong

    private val _attrWisdom = MutableLiveData<Int>().apply { postValue(3) }
    val attrWisdom: LiveData<Int> = _attrWisdom

    private val _attrAgility = MutableLiveData<Int>().apply { postValue(3) }
    val attrAgility: LiveData<Int> = _attrAgility

    private val _attrSpirit = MutableLiveData<Int>().apply { postValue(3) }
    val attrSpirit: LiveData<Int> = _attrSpirit

    private val _attrWit = MutableLiveData<Int>().apply { postValue(2) }
    val attrWit: LiveData<Int> = _attrWit

    private val _inspirationLimit = MutableLiveData<Int>().apply { postValue(4) }
    val inspirationLimit: LiveData<Int> = _inspirationLimit

    private val _damageLimit = MutableLiveData<Int>().apply { postValue(5) }
    val damageLimit: LiveData<Int> = _damageLimit

    private val _fearLimit = MutableLiveData<Int>().apply { postValue(5) }
    val fearLimit: LiveData<Int> = _fearLimit

    private val _passiveSkill = MutableLiveData<String>().apply { postValue("") }
    val passiveSkill: LiveData<String> = _passiveSkill

    fun setCharacterName(name: String) {
        _characterName.value = name
    }

    fun setRace(race: String) {
        _race.value = race
    }

    fun setStrong(value: Int) {
        _attrStrong.value = value
    }

    fun setWisdom(value: Int) {
        _attrWisdom.value = value
    }

    fun setAgility(value: Int) {
        _attrAgility.value = value
    }

    fun setSpirit(value: Int) {
        _attrSpirit.value = value
    }

    fun setWit(value: Int) {
        _attrWit.value = value
    }

    fun setInspirationLimit(value: Int) {
        _inspirationLimit.value = value
    }

    fun setDamageLimit(value: Int) {
        _damageLimit.value = value
    }

    fun setFearLimit(value: Int) {
        _fearLimit.value = value
    }

    fun setPassiveSkill(text: String) {
        _passiveSkill.value = text
    }
}