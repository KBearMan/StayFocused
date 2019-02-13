package com.bearhat.stayfocused

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import android.os.VibrationEffect
import android.os.Build
import android.os.Vibrator
import android.media.ToneGenerator
import android.media.AudioManager





class TimerScreenActivity : AppCompatActivity(),View{

    lateinit var timerScreenPresenter:TimerScreenPresenter

    // Create the observer which updates the UI.
    val timerUpdateObservable = Observer<TimerUpdate> { update ->
        update?.let { setTimerText(update) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timerScreenPresenter = ViewModelProviders.of(this).get(TimerScreenPresenter::class.java)
        timerScreenPresenter.takeView(this)
        actionButton.setOnClickListener { timerScreenPresenter.actionButtonPressed() }
        minutesText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length > 0) {
                    if(secondsText.text.toString().length > 0 ) {
                        timerScreenPresenter.timeEntered(s.toString().toInt(),secondsText.text.toString().toInt())
                    }else{
                        timerScreenPresenter.timeEntered(s.toString().toInt(),0)
                    }
                }else{
                    if(secondsText.text.toString().length > 0 ) {
                        timerScreenPresenter.timeEntered(0,secondsText.text.toString().toInt())
                    }else{
                        timerScreenPresenter.timeEntered(0, 0)
                    }
                }
            }
        })

        secondsText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length > 0) {
                    if(minutesText.text.toString().length > 0 ) {
                        timerScreenPresenter.timeEntered(minutesText.text.toString().toInt(), s.toString().toInt())
                    }else{
                        timerScreenPresenter.timeEntered(0, s.toString().toInt())
                    }
                }else{
                    if(minutesText.text.toString().length > 0 ) {
                        timerScreenPresenter.timeEntered(minutesText.text.toString().toInt(), 0)
                    }else{
                        timerScreenPresenter.timeEntered(0, 0)
                    }
                }
            }
        })
    }

    override fun setStartingTimer(startTime: TimerUpdate) {
        setTimerText(startTime)
    }


    override fun resetTimer() {
        minutesText.setText("00")
        secondsText.setText("00")
    }

    override fun setTimerUpdateObservable(data: LiveData<TimerUpdate>) {
        data.observe(this,timerUpdateObservable)
    }

    override fun timerStatusChanged(started:Boolean) {
        if(started){
            actionButton.text = "STOP"
            minutesText.clearFocus()
            secondsText.clearFocus()
            minutesText.isClickable = false
            secondsText.isClickable = false
        }else{
            actionButton.text = "START"
            minutesText.isClickable = true
            secondsText.isClickable = true
        }
    }

    override fun triggerAlarm() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationDuration = 250L // in milliseconds

        for(i in 1..10) {
            val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 50)

            // Vibrate for 3 sets of 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationDuration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(vibrationDuration)
            }

            Thread.sleep(100)
        }
    }

    private fun setTimerText(update:TimerUpdate){
        minutesText.setText(String.format("%02d", update.minute))
        secondsText.setText(String.format("%02d", update.second))
    }

}
