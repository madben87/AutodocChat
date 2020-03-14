package com.ben.data

import com.ben.model.Message
import io.reactivex.subjects.PublishSubject

interface Server {

    fun connect(): PublishSubject<Message>

    fun sendMessage(msg: Message)
}