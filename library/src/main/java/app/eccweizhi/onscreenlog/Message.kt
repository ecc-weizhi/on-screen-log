package app.eccweizhi.onscreenlog


data class Message(val id: Long,
                   val priority: Int,
                   val tag: String,
                   val content: String)