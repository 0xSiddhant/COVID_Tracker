package com.example.covidtrackerindia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdaptor: StateAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(
            R.layout.item_header,
            list,
            false
        ))
        fetchResult()
    }

    private fun fetchResult() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }

            if (response.isSuccessful) {

                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    /// First Item of data contains MetaData of the list
                    bindCombinedDate(data.statewise[0])
                    bindStatwiseData(data.statewise.subList(1, data.statewise.size))
                }
            }
        }
    }

    private fun bindStatwiseData(subList: List<StatewiseItem>) {
        stateAdaptor = StateAdaptor(subList)
        list.adapter = stateAdaptor
    }

    private fun bindCombinedDate(data : StatewiseItem) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        lastUpdateLbl.text = "Last Updated \n${getTimeAgo(simpleDateFormat.parse(data.lastupdatedtime))}"

        confirmedTV.text = data.confirmed
        recoveredTV.text = data.recovered
        deathTV.text = data.deaths
        activeTV.text = data.active
    }

    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy,  hh:mm a").format(past).toString()
            }
        }

    }
}