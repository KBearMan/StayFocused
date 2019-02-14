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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        configureAlarmUI()
        configureAlarmSettingsUI()
    }

    fun configureAlarmUI(){
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

    fun configureAlarmSettingsUI(){
        enableLoopingSwitch.setOnClickListener { timerScreenPresenter.enableRepeatSwitched() }
        enableSoundSwitch.setOnClickListener { timerScreenPresenter.enableSoundSwitched() }
        enableVibrateSwitch.setOnClickListener { timerScreenPresenter.enableVibateSwitched() }
        vibateNumberText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val numToReturn = if(s.toString().isNotEmpty()) {
                    s.toString().toInt()
                }else{
                    0
                }
                timerScreenPresenter.vibateNumPulseChanged(numToReturn)
            }
        })

        vibrateDurationText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val numToReturn = if(s.toString().isNotEmpty()) {
                    s.toString().toFloat()
                }else{
                    0F
                }
                timerScreenPresenter.vibateDurationChanged(numToReturn)
            }
        })

        soundNumberText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val numToReturn = if(s.toString().isNotEmpty()) {
                    s.toString().toInt()
                }else{
                    0
                }
                timerScreenPresenter.soundNumBeepChanged(numToReturn)
            }
        })

        soundDurationText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val numToReturn = if(s.toString().isNotEmpty()) {
                    s.toString().toFloat()
                }else{
                    0F
                }
                timerScreenPresenter.soundDurationChanged(numToReturn)
            }
        })

    }

    override fun resetTimer() {
        minutesText.setText("")
        secondsText.setText("")
    }

    override fun setTimerUpdateObservable(data: LiveData<TimerUpdate>) {
        data.observe(this,timerUpdateObservable)
    }

    override fun timerStatusChanged(started: Boolean, timerInterval: Int) {
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
            if(timerInterval > 0 ){
                minutesText.setText("${timerInterval/60}")
                secondsText.setText("${timerInterval%60}")
            }
        }
    }

    override fun triggerAlarm(vibeDuration:Long, vibeCount:Int,beepDuration:Long,beepCount:Int) {

        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        GlobalScope.launch {
            for(i in 0..beepCount){
                val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 250)
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,beepDuration.toInt())
                delay(150)
            }
        }

        GlobalScope.launch {

            for(i in 0..vibeCount) {
                // Vibrate for 3 sets of 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(vibeDuration, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    //deprecated in API 26
                    v.vibrate(vibeDuration)
                }
                delay(150)
            }
        }
    }

    override fun setRepeatSwitch(isEnabled: Boolean) {
        enableLoopingSwitch.isChecked = isEnabled
    }

    override fun setSoundSwitch(isEnabled: Boolean) {
        enableSoundSwitch.isChecked = isEnabled
        if(isEnabled){
            soundSettingsArea.setBackgroundResource(R.drawable.bg_active_settings_border)
        }else{
            soundSettingsArea.setBackgroundResource(R.drawable.bg_inactive_settings_border)
        }
    }

    override fun setVibrateSwitch(isEnabled: Boolean) {
        enableVibrateSwitch.isChecked = isEnabled
        if(isEnabled){
            vibrateDurationText.isFocusableInTouchMode = true
            vibateNumberText.isFocusable = true
            vibateSettingsArea.setBackgroundResource(R.drawable.bg_active_settings_border)
        }else{
            vibrateDurationText.isFocusableInTouchMode = true
            vibateNumberText.isFocusable = true
            vibateSettingsArea.setBackgroundResource(R.drawable.bg_inactive_settings_border)
        }
    }

    override fun populateVibateSettings(numPulses: Int, duration: Float) {
        vibateNumberText.setText(numPulses.toString())
        vibrateDurationText.setText(duration.toString())
    }

    override fun populateSoundSettings(numPulses: Int, duration: Float) {
        soundNumberText.setText(numPulses.toString())
        soundDurationText.setText(duration.toString())
    }

    private fun setTimerText(update:TimerUpdate){
        minutesText.setText(String.format("%02d", update.minute))
        secondsText.setText(String.format("%02d", update.second))
    }

}
