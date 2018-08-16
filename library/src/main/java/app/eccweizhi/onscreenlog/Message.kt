package app.eccweizhi.onscreenlog


internal data class Message(val id: Long,
                            val priority: Int,
                            val tag: String,
                            val content: String)