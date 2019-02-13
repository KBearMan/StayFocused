package com.bearhat.stayfocused

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class TimerScreenPresenter: android.arch.lifecycle.ViewModel(),ViewModel{

    private var timerScreenView:  View? = null
    private var timerUpdateData : MutableLiveData<TimerUpdate> = MutableLiveData()
    private var timerRunning = false
    private var timerJob:Job? = null
    private var timerTime = 0
    private var timerInterval = 0

    override fun takeView(view: View) {
        timerScreenView = view
        timerScreenView?.setTimerUpdateObservable(timerUpdateData)
    }

    override fun dropView() {
        timerScreenView = null
    }

    override fun actionButtonPressed(){
        if( timerTime > 0) {
            if ((timerJob == null || !timerJob!!.isActive)) {
                timerScreenView?.changeButtonText("Stop")
                timerJob = GlobalScope.launch {
                    timerRunning = true
                    while (timerRunning) {
                        delay(1000)
                        if(timerTime<=0){
                            timerTime = 0
                            break
                        }else {
                            withContext(Dispatchers.Main) {
                                timerTime--
                                sendUpdateTime()
                            }
                        }
                    }
                    //Timer finished, trigger alarm and restart loop
                    timerScreenView?.triggerAlarm()
                    restartTimerLoop()
                }
            } else {
                timerScreenView?.changeButtonText("Start")
                timerJob!!.cancel()
            }
        }
    }

    override fun timeEntered( minutes: Int, seconds: Int) {
        timerTime = seconds+minutes*60
        timerInterval = timerTime
    }

    private fun sendUpdateTime(){
        val hours = timerTime/3600
        val minutes = (timerTime - (hours*3600))/60
        val seconds = timerTime%60
        timerUpdateData.value = TimerUpdate(minutes,seconds)
    }

    private fun restartTimerLoop(){
        timerTime = timerInterval
        timerJob = null
        actionButtonPressed()
    }

}