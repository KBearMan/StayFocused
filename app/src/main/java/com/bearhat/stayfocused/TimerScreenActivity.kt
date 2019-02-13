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
import android.content.Context.VIBRATOR_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
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
                timerScreenPresenter.timeEntered(s.toString().toInt(),secondsText.text.toString().toInt())
            }
        })

        secondsText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                timerScreenPresenter.timeEntered(minutesText.text.toString().toInt(),s.toString().toInt())
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

    override fun changeButtonText(s: String) {
        actionButton.text = s
    }

    override fun triggerAlarm() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationDuration = 250L // in milliseconds

        for(i in 0..3) {
            val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 50)

            // Vibrate for 3 sets of 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationDuration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(vibrationDuration)
            }
        }
    }

    private fun setTimerText(update:TimerUpdate){
        minutesText.setText(String.format("%02d", update.minute))
        secondsText.setText(String.format("%02d", update.second))
    }

}
