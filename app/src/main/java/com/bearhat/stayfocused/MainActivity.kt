package com.bearhat.stayfocused

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var timerRunning = false
    var mainLooper : Handler? = null


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
        //TODO actually stop the timer

        setTimerTextClickable(true)
        timerRunning = false
    }

    private fun startTimer(){
        if(checkForValidTimer()){
            //TODO actually start the timer
            mainLooper = Handler()

            setTimerTextClickable(false)
            timerRunning = true

        }else{
            showInvalidInputDialog()
        }
    }

    private fun checkForValidTimer():Boolean{
        //TODO check that the timer is not all 0's and is valid positive numbers
        try{

        }catch(e:Exception){

        }
        return true
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
