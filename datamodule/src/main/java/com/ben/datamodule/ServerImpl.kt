package com.ben.datamodule

import com.ben.data.Server
import com.ben.model.Message
import io.reactivex.subjects.PublishSubject
import kotlin.random.Random

class ServerImpl : Server {

    val publisher: PublishSubject<Message> = PublishSubject.create()

    override fun connect(): PublishSubject<Message> {
        return publisher
    }

    override fun sendMessage(msg: Message) {

        publisher.onNext(msg)

        if (Random.nextBoolean()) //Random answers for test
            publisher.onNext(Message("Мне грустно и одиноко путник, расскажи мне историю…", Message.Type.INCOMING_TEXT))
    }
}