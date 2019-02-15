package com.bearhat.stayfocused

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class TimerScreenPresenter: android.arch.lifecycle.ViewModel(),ViewModel{

    private val DEFAULT_ALARM_COUNT = 10
    private val DEFAULT_ALARM_DURATION = 550L
    private val UPPER_COUNT_LIMIT = 25
    private val UPPER_DURATION_LIMIT = 2.0F

    private var timerScreenView:  View? = null
    private var timerUpdateData : MutableLiveData<TimerUpdate> = MutableLiveData()
    private var timerRunning = false
    private var timerJob:Job? = null
    private var timerTime = 0
    private var timerInterval = 0
    private var vibrateEnabled = true
    private var repeatEnabled = true
    private var soundEnabled = true
    private var vibrateDuration = DEFAULT_ALARM_DURATION
    private var vibrateCount = DEFAULT_ALARM_COUNT
    private var soundDuration = DEFAULT_ALARM_DURATION
    private var soundCount = DEFAULT_ALARM_COUNT



    override fun takeView(view: View) {
        timerScreenView = view
        timerScreenView?.setTimerUpdateObservable(timerUpdateData)
        timerScreenView?.populateSoundSettings(soundCount,soundDuration.toFloat()/1000F)
        timerScreenView?.populateVibateSettings(vibrateCount,vibrateDuration.toFloat()/1000F)
    }

    override fun dropView() {
        timerScreenView = null
    }

    override fun actionButtonPressed(){
        if( timerTime > 0) {
            if ((timerJob == null || !timerJob!!.isActive)) {
                timerRunning = true
                timerScreenView?.timerStatusChanged(true, timerInterval)
                timerJob = GlobalScope.launch {
                    while (timerRunning) {
                        delay(1000)
                        if(timerTime<=0){
                            timerTime = 0
                            break
                        }else {
                            withContext(Dispatchers.Main) {
                                timerTime--
                                sendUpdateTime(timerTime)
                            }
                        }
                    }
                    //Timer finished, trigger alarm
                    triggerAlarm()
                }
            } else {
                stopTimer()
            }
        }else{
            stopTimer()
        }
    }

    override fun timeEntered( minutes: Int, seconds: Int) {
        timerTime = seconds+minutes*60
        if(!timerRunning) {
            timerInterval = timerTime
        }
    }

    override fun enableVibateSwitched() {
        vibrateEnabled = !vibrateEnabled
        timerScreenView?.setVibrateSwitch(vibrateEnabled)
    }

    override fun enableRepeatSwitched() {
        repeatEnabled = !repeatEnabled
        timerScreenView?.setRepeatSwitch(repeatEnabled)
    }

    override fun enableSoundSwitched() {
        soundEnabled = !soundEnabled
        timerScreenView?.setSoundSwitch(soundEnabled)
    }

    override fun vibateDurationChanged(duration: Float) {
        if(duration > UPPER_DURATION_LIMIT){
            timerScreenView?.populateVibateSettings(vibrateCount,UPPER_DURATION_LIMIT)
        }else {
            vibrateDuration = (duration * 1000).toLong()
        }
    }

    override fun vibateNumPulseChanged(num: Int) {
        if(num > UPPER_COUNT_LIMIT) {
            timerScreenView?.populateVibateSettings(UPPER_COUNT_LIMIT,vibrateDuration.toFloat()/1000F)
        }else{
            vibrateCount = num
        }
    }

    override fun soundDurationChanged(duration: Float) {
        if(duration > UPPER_DURATION_LIMIT){
            timerScreenView?.populateSoundSettings(soundCount,UPPER_DURATION_LIMIT)
        }else {
            soundDuration = (duration * 1000).toLong()
        }
    }

    override fun soundNumBeepChanged(num: Int) {
        if(num > UPPER_COUNT_LIMIT) {
            timerScreenView?.populateSoundSettings(UPPER_COUNT_LIMIT,soundDuration.toFloat()/1000F)
        }else{
            soundCount = num
        }
    }

    private fun sendUpdateTime(timerToSend:Int){
        val minutes = timerToSend/60
        val seconds = timerToSend%60
        timerUpdateData.value = TimerUpdate(minutes,seconds)
    }

    private fun restartTimerLoop(){
            timerTime = timerInterval
        if(repeatEnabled) {
            actionButtonPressed()
        }else{
            stopTimer()
        }
    }

    private fun stopTimer(){
        timerRunning = false
        timerScreenView?.timerStatusChanged(false,timerInterval)
        timerJob?.cancel()
        timerJob= null
    }

    private fun triggerAlarm(){
        timerScreenView?.triggerAlarm(vibrateDuration,vibrateCount,soundDuration,soundCount)
        GlobalScope.launch(Dispatchers.Main) {
            sendUpdateTime(timerInterval) // reset the UI to the original value
            restartTimerLoop()
        }
    }
}