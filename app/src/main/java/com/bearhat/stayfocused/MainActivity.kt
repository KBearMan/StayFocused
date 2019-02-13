package com.bearhat.stayfocused

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var timerRunning = false

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
        timerRunning = false
    }

    private fun startTimer(){
        if(checkForValidTimer()){
            //TODO actually start the timer
            timerRunning = true

        }else{
            showInvalidInputDialog()
        }
    }

    private fun checkForValidTimer():Boolean{
        //TODO check that the timer is not all 0's and is valid positive numbers
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
}
