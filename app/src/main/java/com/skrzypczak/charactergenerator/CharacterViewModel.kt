package com.skrzypczak.charactergenerator

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skrzypczak.charactergenerator.database.CardAttribution
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.ui.PageListener
import java.lang.Integer.min
import java.util.*
import kotlin.random.Random.Default.nextInt

class CharacterViewModel(private val controller: CardsActivityController, private val characterNameGenerator: CharacterNameGenerator) : ViewModel() {

    companion object {
        private const val ATTR_DEFAULT_VALUE = 2
        private val INDICES = arrayOf(0, 1, 2, 3, 4)
    }

    val characterName = MutableLiveData<String>().apply { postValue("") }

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

    val passiveSkill = MutableLiveData<String>().apply { postValue("") }

    val history = MutableLiveData<String>().apply { postValue("") }

    val suggestItems = MutableLiveData<String>().apply { postValue("") }

    private val _characterImage = MutableLiveData<Drawable>()
    val characterImage: LiveData<Drawable> = _characterImage

    private val _cardState = MutableLiveData(CardState.DISABLED)
    val cardState: LiveData<CardState> = _cardState

    private var obversePageListener: PageListener? = null
    private var reversePageListener: PageListener? = null

    fun setCharacterName(name: String) {
        characterName.postValue(name)
    }

    fun setRace(race: String) {
        _race.value = race
    }

    fun setCharacterImage(image: Drawable) {
        _characterImage.value = image
    }

    fun chooseImage() {
        controller.chooseImage()
    }

    fun randomizeName() {
        characterNameGenerator.generateNameUseMarkow(3, 16) {
            setCharacterName(it)
        }
    }

    fun randomizeNumberStats() {
        randomizeName()
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
            ),
            characterImage.value
        )
    }

    fun loadCard(cardModel: CardModel) {
        characterName.value = cardModel.name
        _race.value = cardModel.race
        _attrStrength.value = cardModel.attribution.strength
        _attrWisdom.value = cardModel.attribution.wisdom
        _attrAgility.value = cardModel.attribution.agility
        _attrSpirit.value = cardModel.attribution.spirit
        _attrWit.value = cardModel.attribution.wit
        _inspirationLimit.value = cardModel.attribution.inspirationLimit
        _fearLimit.value = cardModel.attribution.fearLimit
        _damageLimit.value = cardModel.attribution.damageLimit
        passiveSkill.value = cardModel.passiveSkill
        history.value = cardModel.history
        suggestItems.value = cardModel.suggestStartSet
        _characterImage.value = controller.readImage(cardModel.imageUri)
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