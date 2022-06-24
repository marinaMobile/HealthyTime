package com.AgainstGravity.RecRoo.bp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.AgainstGravity.RecRoo.R
import com.AgainstGravity.RecRoo.wp.TestActivity
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.*

class Movement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movement)
        val textView: TextView = findViewById(R.id.txtMover)
        runBlocking {

            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                getAsync(applicationContext)
            }
            job.join()
            val jsoup: String? = Hawk.get(Constant.aResult, "")
            Log.d("cora", "cora $jsoup")

            textView.text = jsoup

            if (jsoup == "9iKl") {
                Intent(applicationContext, TestActivity::class.java).also { startActivity(it) }

            } else {
                Intent(applicationContext, Web::class.java).also { startActivity(it) }
            }
            finish()
        }
    }

    private suspend fun getAsync(context: Context) {
        val asyncKey = Soup(context)
        val asyncResult = asyncKey.getDocSecretKey()
        Hawk.put(Constant.aResult, asyncResult)
    }
}
