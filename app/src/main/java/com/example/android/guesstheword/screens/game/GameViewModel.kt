package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.service.autofill.Transformation
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    companion object{
        private const val DONE=0L
        private const val ONE_SECOND=1000L
        private const val COUNTDOWN_TIME= 10000L
    }

    private val timer:CountDownTimer
    // The current word
    //var word = MutableLiveData<String>()
    private var _word: MutableLiveData<String> = MutableLiveData("")
    val word: LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    val currentTimeString=Transformations.map(currentTime){
        time->DateUtils.formatElapsedTime(time)
    }
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        _eventGameFinish.value = false
        _score.value = 0
        resetList()
        nextWord()
        timer= object :CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value=(millisUntilFinished/ ONE_SECOND)
            }
            override fun onFinish() {
                _currentTime.value= DONE
                _eventGameFinish.value=true
            }
        }
        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen", "hospital", "basketball", "cat", "change", "snail", "soup", "calendar",
            "sad", "desk", "guitar", "home", "railway", "zebra", "jelly", "car",
            "crow", "trade", "bag", "roll", "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //_eventGameFinish.value=true
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}