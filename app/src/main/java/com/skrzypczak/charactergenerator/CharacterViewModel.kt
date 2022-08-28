package com.skrzypczak.charactergenerator

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skrzypczak.charactergenerator.database.CardAttribution
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.ui.PageListener
import java.lang.Integer.min
import java.util.*
import kotlin.random.Random.Default.nextInt

class CharacterViewModel(private val controller: CardsActivityController) : ViewModel() {

    companion object {
        private const val ATTR_DEFAULT_VALUE = 2
        private val INDICES = arrayOf(0, 1, 2, 3, 4)
    }

    private val _characterName = MutableLiveData<String>().apply { postValue("") }
    val characterName: LiveData<String> = _characterName

    private val _race = MutableLiveData<String>().apply { postValue("") }
    val race: LiveData<String> = _race

    private val _attrStrength = MutableLiveData<Int>().apply { postValue(3) }
    val attrStrength: LiveData<Int> = _attrStrength

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

    private val _history = MutableLiveData<String>().apply { postValue("") }
    val history: LiveData<String> = _history

    private val _role = MutableLiveData<String>().apply { postValue("") }
    val role: LiveData<String> = _role

    private val _suggestItems = MutableLiveData<String>().apply { postValue("") }
    val suggestItems: LiveData<String> = _suggestItems

    private val _characterImage = MutableLiveData<Drawable>()
    val characterImage: LiveData<Drawable> = _characterImage

    private val _cardState = MutableLiveData(CardState.DISABLED)
    val cardState: LiveData<CardState> = _cardState

    private var obversePageListener: PageListener? = null
    private var reversePageListener: PageListener? = null

    fun setCharacterName(name: String) {
        _characterName.value = name
    }

    fun setRace(race: String) {
        _race.value = race
    }

    fun setStrength(value: Int) {
        _attrStrength.value = value
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

    fun setHistory(text: String) {
        _history.value = text
    }

    fun setSuggestRole(role: String) {
        _role.value = role
    }

    fun setSuggestItems(text: String) {
        _suggestItems.value = text
    }

    fun setCharacterImage(image: Drawable) {
        _characterImage.value = image
    }

    fun chooseImage() {
        controller.chooseImage()
    }

    fun randomizeNumberStats() {
        val array = Array(5) { 0 }

        for (i in 0 until 5) {
            array[i] = nextInt(min(4, 5 - array.sum()))
        }

        INDICES.shuffle()

        _attrStrength.value = ATTR_DEFAULT_VALUE + array[INDICES[0]]
        _attrWisdom.value = ATTR_DEFAULT_VALUE + array[INDICES[1]]
        _attrAgility.value = ATTR_DEFAULT_VALUE + array[INDICES[2]]
        _attrSpirit.value = ATTR_DEFAULT_VALUE + array[INDICES[3]]
        _attrWit.value = ATTR_DEFAULT_VALUE + array[INDICES[4]]

        _inspirationLimit.value = nextInt(3, 6 + 1)

        _damageLimit.value = nextInt(3, 7 + 1)
        _fearLimit.value = 10 - (_damageLimit.value ?: 0)
    }

    fun saveCard() {
        controller.insertCard(
            CardModel(
                Date(),
                characterName.value ?: "",
                race.value ?: "",
                Uri.EMPTY,
                CardAttribution(
                    attrStrength.value ?: ATTR_DEFAULT_VALUE,
                    attrWisdom.value ?: ATTR_DEFAULT_VALUE,
                    attrAgility.value ?: ATTR_DEFAULT_VALUE,
                    attrSpirit.value ?: ATTR_DEFAULT_VALUE,
                    attrWit.value ?: ATTR_DEFAULT_VALUE,
                    inspirationLimit.value ?: 4,
                    damageLimit.value ?: 5,
                    fearLimit.value ?: 5
                ),
                passiveSkill.value ?: "",
                history.value ?: "",
                suggestItems.value ?: ""
            )
        )//todo save image and uri
    }

    fun loadCard(cardModel: CardModel) {
        _characterName.value = cardModel.name
        _race.value = cardModel.race
        _attrStrength.value = cardModel.attribution.strength
        _attrWisdom.value = cardModel.attribution.wisdom
        _attrAgility.value = cardModel.attribution.agility
        _attrSpirit.value = cardModel.attribution.spirit
        _attrWit.value = cardModel.attribution.wit
        _inspirationLimit.value = cardModel.attribution.inspirationLimit
        _fearLimit.value = cardModel.attribution.fearLimit
        _damageLimit.value = cardModel.attribution.damageLimit
        _passiveSkill.value = cardModel.passiveSkill
        _history.value = cardModel.history
        _suggestItems.value = cardModel.suggestStartSet
//        _characterImage.value = controller.readImage(cardModel.imageUri)
    }

    fun generateCard() {
        controller.generateCard(
            obversePageListener?.getScreenShot(),
            reversePageListener?.getScreenShot()
        )
    }

    fun initObverseListener(listener: PageListener) {
        obversePageListener = listener
        changeState(CardState.OBVERSE_ENABLED)
    }

    fun initReverseListener(listener: PageListener) {
        reversePageListener = listener
        changeState(CardState.REVERSE_ENABLED)
    }

    private fun changeState(cardState: CardState) {
        val currentState = this.cardState.value
        _cardState.value = when {
            currentState == CardState.DISABLED -> cardState
            currentState == CardState.OBVERSE_ENABLED && cardState == CardState.REVERSE_ENABLED -> CardState.ENABLED
            currentState == CardState.REVERSE_ENABLED && cardState == CardState.OBVERSE_ENABLED -> CardState.ENABLED
            currentState == CardState.ENABLED -> CardState.ENABLED
            else -> CardState.DISABLED
        }
    }
}