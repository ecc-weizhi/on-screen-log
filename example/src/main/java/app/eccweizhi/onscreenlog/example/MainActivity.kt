package app.eccweizhi.onscreenlog.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(),
        View.OnClickListener {

    private var subscription: Disposable? = null
    private val randomLogGenerator = RandomLogGenerator(MyApplication.INSTANCE.onScreenLog)
    private var adapterObserver: RecyclerView.AdapterDataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLogButton.setOnClickListener(this)
        stopLogButton.setOnClickListener(this)

        // Just need to set RecyclerView adapter to on-screen-log adapter.
        onScreenLogRecyclerView.adapter = MyApplication.INSTANCE.onScreenLog.adapter

        setupRecyclerView()
    }

    override fun onDestroy() {
        adapterObserver?.let { onScreenLogRecyclerView.adapter.unregisterAdapterDataObserver(it) }
        adapterObserver = null
        subscription?.dispose()
        subscription = null
        super.onDestroy()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.startLogButton -> {
                subscription ?: run {
                    subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .subscribe { randomLogGenerator.logRandomMessage() }
                }
            }
            R.id.stopLogButton -> {
                subscription?.dispose()
                subscription = null
            }
        }
    }

    /**
     * Fanciful stuff to autoscroll RecyclerView. This is not necessary to use on-screen-log.
     */
    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(this)
        onScreenLogRecyclerView.layoutManager = llm
        adapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onScreenLogRecyclerView.smoothScrollToPosition(positionStart + itemCount)
            }
        }
        adapterObserver?.let { onScreenLogRecyclerView.adapter.registerAdapterDataObserver(it) }
    }
}
