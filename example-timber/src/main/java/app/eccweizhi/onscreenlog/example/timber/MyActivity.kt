package app.eccweizhi.onscreenlog.example.timber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity(),
        View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonV.setOnClickListener(this)
        buttonD.setOnClickListener(this)
        buttonI.setOnClickListener(this)
        buttonW.setOnClickListener(this)
        buttonE.setOnClickListener(this)
        buttonWtf.setOnClickListener(this)

        val llm = LinearLayoutManager(this)
        onScreenLogRecyclerView.layoutManager = llm
        onScreenLogRecyclerView.adapter = MyApplication.INSTANCE.onScreenLog.adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonV -> {
                Timber.v("This is a verbose message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonD -> {
                Timber.d("This is a debug message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonI -> {
                Timber.i("This is a info message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonW -> {
                Timber.w("This is a warn message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonE -> {
                Timber.e("This is a error message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonWtf -> {
                Timber.wtf("This is a wtf message %s",
                        UUID.randomUUID().toString())
            }
        }
    }
}