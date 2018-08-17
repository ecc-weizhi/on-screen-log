package app.eccweizhi.onscreenlog

import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


/**
 * A [RecyclerView.Adapter] that provides logging message to be displayed.
 */
class LogAdapter internal constructor(private val subject: BehaviorSubject<List<Message>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messageList: List<Message> = listOf()
    private var disposable: Disposable? = null
    private var attachedRecyclerViewCount = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerViewCount++

        if (disposable == null) {
            disposable = subject.sample(SAMPLE_DURATION_MS, TimeUnit.MILLISECONDS, true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { newList: List<Message> ->
                        val diffUtilCb = MessageDiffUtilCallback(messageList, newList)
                        val result = DiffUtil.calculateDiff(diffUtilCb, false)
                        messageList = newList
                        result.dispatchUpdatesTo(this)
                    }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        attachedRecyclerViewCount--
        if (attachedRecyclerViewCount <= 0) {
            disposable?.dispose()
            disposable = null
        }

        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_default, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        val message = messageList[position]
        (holder as ViewHolder).bindView(message)
    }

    private companion object {
        private const val SAMPLE_DURATION_MS = 400L
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context = itemView.context
        private val tagTextView: TextView = itemView.findViewById(R.id.tagTextView)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bindView(message: Message) {
            tagTextView.text = message.tag
            messageTextView.text = message.content

            tagTextView.setTextColor(getFontColor(message.priority))
            messageTextView.setTextColor(getFontColor(message.priority))
        }

        @ColorInt
        private fun getFontColor(priority: Int): Int {
            val colorRes = when (priority) {
                Log.VERBOSE -> R.color.text_color_verbose
                Log.DEBUG -> R.color.text_color_debug
                Log.INFO -> R.color.text_color_info
                Log.WARN -> R.color.text_color_warn
                Log.ERROR -> R.color.text_color_error
                Log.ASSERT -> R.color.text_color_wtf
                else -> throw IllegalStateException("Unknown priority")
            }

            return ContextCompat.getColor(context,
                    colorRes)
        }
    }

    private class MessageDiffUtilCallback(
            private val oldList: List<Message>,
            private val newList: List<Message>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int,
                                     newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int,
                                        newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}