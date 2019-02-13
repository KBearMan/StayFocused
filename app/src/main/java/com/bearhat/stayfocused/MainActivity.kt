package com.bearhat.stayfocused

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : AppCompatActivity() {

    var timerRunning = false
    var mainLooper : Handler? = null
    var timerStartTime: Date? = null


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
            mainLooper = Handler()
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE + minutesText.text.toString().toInt()))
            cal.set(Calendar.SECOND,cal.get(Calendar.SECOND + secondsText.text.toString().toInt()))
            timerStartTime = cal.time
            runTimer()
            setTimerTextClickable(false)
            timerRunning = true

        }else{
            showInvalidInputDialog()
        }
    }

    private fun runTimer(){
        mainLooper?.post{
            //TODO check if timer is out, else update
            if(Date().after(timerStartTime)){
                //Timer done

            }else{
                // Timer not done
                updateTimerUI()
                Thread.sleep(1000) // 1 second
                runTimer()
            }

        }
    }

    private fun checkForValidTimer():Boolean{
        //TODO check that the timer is not all 0's and is valid positive numbers
        try{

        }catch(e:Exception){

        }
        return true
    }

    private fun updateTimerUI(){

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
