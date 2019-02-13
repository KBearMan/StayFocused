package com.bearhat.stayfocused

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var timerRunning = false
    var mainLooper : Handler? = null
    var timerEndTime: Date? = null
    val timerRunnable = Runnable {
        if(Date().after(timerEndTime)){
            //Timer done
            stopTimer()
        }else{
            // Timer not done
            updateTimerUI()
            Thread.sleep(1000) // 1 second
            runTimer()
        }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionButton.setOnClickListener {
            startStopTimer()
        }
    }

    private fun startStopTimer(){
        if(isTimerRunning()){
            stopTimer()
        }else{
            startTimer()
        }
    }

    private fun stopTimer(){
        mainLooper?.removeCallbacksAndMessages(timerRunnable)
        actionButton.text = "START"
        setTimerTextClickable(true)
        timerRunning = false
    }

    private fun startTimer(){
        if(checkForValidTimer()){
            actionButton.text = "STOP"
            mainLooper = Handler()
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE + minutesText.text.toString().toInt()))
            cal.set(Calendar.SECOND,cal.get(Calendar.SECOND + secondsText.text.toString().toInt()))
            timerEndTime = cal.time
            runTimer()
            setTimerTextClickable(false)
            timerRunning = true

        }else{
            showInvalidInputDialog()
        }
    }

    private fun runTimer(){
        mainLooper?.removeCallbacksAndMessages(timerRunnable)
        mainLooper?.post(timerRunnable)
    }

    private fun checkForValidTimer():Boolean{
        //TODO check that the timer is not all 0's and is valid positive numbers
        try{

        }catch(e:Exception){

        }
        return true
    }

    private fun updateTimerUI(){
        //TODO not sure why this isn't working as intended. Seconds is close but minutes is completely wrong.

        val currentTime = Date()
        val remainingTimeInMillis = timerEndTime?.time?.minus(currentTime.time)

        val minutes = remainingTimeInMillis?.div(1000*60)?.toInt()
        minutesText.setText("$minutes")

        val seconds = remainingTimeInMillis?.rem(60000)?.div(1000)?.toInt()
        secondsText.setText("$seconds")
    }

    private fun showInvalidInputDialog(){
        val builder = android.app.AlertDialog.Builder(this)
        val dialog = builder.setTitle("Error")
                .setMessage("Invalid inputs, please put in a proper time.")
                .setNeutralButton("OK", { _,_ -> })
                .create()
                .show()
    }

    private fun isTimerRunning():Boolean{
        return timerRunning
    }

    private fun setTimerTextClickable(clickable:Boolean){
        minutesText.isClickable = clickable
        secondsText.isClickable = clickable
    }
}
