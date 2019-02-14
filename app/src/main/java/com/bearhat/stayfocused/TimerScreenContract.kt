package com.bearhat.stayfocused

import android.arch.lifecycle.LiveData

interface View {
    fun resetTimer()
    fun setTimerUpdateObservable(data:LiveData<TimerUpdate>)
    fun setStartingTimer(startTime:TimerUpdate)
    fun timerStatusChanged(started: Boolean, timerInterval: Int)
    fun setRepeatSwitch(isEnabled:Boolean)
    fun setSoundSwitch(isEnabled:Boolean)
    fun setVibrateSwitch(isEnabled:Boolean)
    fun populateVibateSettings(numPulses:Int,duration:Float)
    fun populateSoundSettings(numPulses:Int,duration:Float)
    fun triggerAlarm()
}

interface ViewModel: ViewModelBase<View> {
    fun actionButtonPressed()
    fun timeEntered(minutes:Int,seconds:Int)
    fun enableVibateSwitched()
    fun enableRepeatSwitched()
    fun enableSoundSwitched()
    fun vibateDurationChanged(duration:Float)
    fun vibateNumPulseChanged(num:Int)
    fun soundDurationChanged(duration:Float)
    fun soundNumBeepChanged(num:Int)
}
