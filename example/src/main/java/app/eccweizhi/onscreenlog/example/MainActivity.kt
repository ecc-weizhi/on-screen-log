package app.eccweizhi.onscreenlog.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
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
                MyApplication.INSTANCE.onScreenLog.v(javaClass.simpleName,
                        "This is a verbose message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonD -> {
                MyApplication.INSTANCE.onScreenLog.d(javaClass.simpleName,
                        "This is a debug message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonI -> {
                MyApplication.INSTANCE.onScreenLog.i(javaClass.simpleName,
                        "This is a info message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonW -> {
                MyApplication.INSTANCE.onScreenLog.w(javaClass.simpleName,
                        "This is a warn message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonE -> {
                MyApplication.INSTANCE.onScreenLog.e(javaClass.simpleName,
                        "This is a error message %s",
                        UUID.randomUUID().toString())
            }
            R.id.buttonWtf -> {
                MyApplication.INSTANCE.onScreenLog.wtf(javaClass.simpleName,
                        "This is a wtf message %s",
                        UUID.randomUUID().toString())
            }
        }
    }
}
