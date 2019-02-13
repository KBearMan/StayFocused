package com.bearhat.stayfocused

import android.arch.lifecycle.LiveData

interface View {
    fun resetTimer()
    fun setTimerUpdateObservable(data:LiveData<TimerUpdate>)
    fun setStartingTimer(startTime:TimerUpdate)
    fun timerStatusChanged(started: Boolean, timerInterval: Int)
    fun triggerAlarm()
}

interface ViewModel: ViewModelBase<View> {
    fun actionButtonPressed()
    fun timeEntered(minutes:Int,seconds:Int)
}
