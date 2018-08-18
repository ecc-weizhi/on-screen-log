package app.eccweizhi.onscreenlog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar


class LogActivity : AppCompatActivity() {
    private lateinit var logRecyclerView: RecyclerView
    private var adapterObserver: RecyclerView.AdapterDataObserver? = null
    private var shouldAutoScroll = true

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
                if (shouldAutoScroll) {
                    logRecyclerView.smoothScrollToPosition(positionStart + itemCount)
                }
            }
        }
        adapterObserver?.let { logRecyclerView.adapter.registerAdapterDataObserver(it) }
        logRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lastVisiblePosition = llm.findLastVisibleItemPosition()
                val itemCount = recyclerView.adapter.itemCount

                shouldAutoScroll = if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisiblePosition >= (itemCount - 1)
                } else {
                    false
                }
            }
        })
    }
}