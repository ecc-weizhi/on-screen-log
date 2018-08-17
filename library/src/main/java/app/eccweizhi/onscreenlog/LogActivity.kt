package app.eccweizhi.onscreenlog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar


class LogActivity : AppCompatActivity() {
    private lateinit var logRecyclerView: RecyclerView
    private var adapterObserver: RecyclerView.AdapterDataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        logRecyclerView = findViewById(R.id.logRecyclerView)
        val toolBar: Toolbar = findViewById(R.id.activityToolbar)
        setSupportActionBar(toolBar)

        setupRecyclerView()

    }

    override fun onDestroy() {
        adapterObserver?.let { logRecyclerView.adapter.unregisterAdapterDataObserver(it) }
        adapterObserver = null
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        logRecyclerView.adapter = OnScreenLog.INSTANCE?.adapter

        val llm = LinearLayoutManager(this)
        logRecyclerView.layoutManager = llm
        adapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                logRecyclerView.smoothScrollToPosition(positionStart + itemCount)
            }
        }
        adapterObserver?.let { logRecyclerView.adapter.registerAdapterDataObserver(it) }
    }
}