package ru.interview.revoluttest


import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cur.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

const val TAG = "MYLOGGER"

class CurActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun log(msg: String) = Log.d(TAG, msg)
    }

    private lateinit var adapter: CursAdapter
    private val handler = Handler()
    private var activeRequestJob: Job? = null
    private val requestRunnable: Runnable = RequestRunnable()

    inner class RequestRunnable : Runnable {
        override fun run() {
            activeRequestJob = GlobalScope.launch(Dispatchers.Main) {
                try {
                    if (adapter.itemCount == 0 || adapter.curInput != null) {
                        val rCurs: RCurs = Api.api.curs(adapter.baseRate.name).await()
                        adapter.updateRatesWithMap(parseCursMap(rCurs.rates))
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@CurActivity, "net error ${e.message}", Toast.LENGTH_SHORT).show()
                }

                handler.postDelayed(this@RequestRunnable, 1000L)
            }

            log("run, activeRequestJob = $activeRequestJob")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur)
        adapter = CursAdapter {}.also {
            rvCurs.adapter = it
        }
    }

    override fun onPause() {
        cancelRequest()
        super.onPause()
    }


    override fun onResume() {
        super.onResume()
        handler.post(requestRunnable)
    }

    private fun cancelRequest(): Unit {
        handler.removeCallbacks(requestRunnable)

        log("activeRequestJob = $activeRequestJob")

        if (activeRequestJob?.isActive == true && activeRequestJob?.isCancelled == false) {
            activeRequestJob?.cancel()
        }
    }

    private fun parseCursMap(json: JsonObject): Map<String, Rate> {
        return json.entrySet()
            .map { e -> Rate(e.key, e.value.asDouble) }
            .associateBy { r -> r.name }
    }


}