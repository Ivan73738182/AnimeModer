package com.ivan.animemoder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class AdminActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var adapter: VideoAdapter
    private val pendingList = mutableListOf<Video>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val rvPending = findViewById<RecyclerView>(R.id.rvPending)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        rvPending.layoutManager = LinearLayoutManager(this)

        adapter = VideoAdapter(pendingList,
            onApprove = { video -> approveVideo(video) },
            onReject = { video -> rejectVideo(video) }
        )
        rvPending.adapter = adapter

        // Выход
        btnLogout.setOnClickListener {
            val session = SessionManager(this)
            session.logout()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loadPending()
    }

    private fun loadPending() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url("http://node.waifly.com:27211/admin/pending").build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val jsonObj = JSONObject(json!!)
                    val videosArray = jsonObj.getJSONArray("videos")
                    val type = object : TypeToken<List<Video>>() {}.type
                    val newVideos: List<Video> = Gson().fromJson(videosArray.toString(), type)

                    runOnUiThread {
                        pendingList.clear()
                        pendingList.addAll(newVideos)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(this@AdminActivity, "Ошибка загрузки", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun approveVideo(video: Video) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url("http://node.waifly.com:27211/admin/approve/${video.id}").post(okhttp3.RequestBody.create(null, ByteArray(0))).build()
                client.newCall(request).execute()
                runOnUiThread { 
                    Toast.makeText(this@AdminActivity, "Одобрено!", Toast.LENGTH_SHORT).show()
                    loadPending() 
                }
            } catch (e: Exception) {}
        }
    }

    private fun rejectVideo(video: Video) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url("http://node.waifly.com:27211/admin/reject/${video.id}").post(okhttp3.RequestBody.create(null, ByteArray(0))).build()
                client.newCall(request).execute()
                runOnUiThread { 
                    Toast.makeText(this@AdminActivity, "Отклонено", Toast.LENGTH_SHORT).show()
                    loadPending() 
                }
            } catch (e: Exception) {}
        }
    }
}
