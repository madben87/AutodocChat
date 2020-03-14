package com.ben.autodocchat.view.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ben.autodocchat.core.BaseViewModel
import com.ben.data.Server
import com.ben.model.Message
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(context: Application, private val server: Server) :
    BaseViewModel(context) {

    private val liveData = MutableLiveData<Message>()

    init {
        bind()
    }

    private fun bind() {
        connectToServer()
    }

    fun getData() : MutableLiveData<Message> {
        return liveData
    }

    fun sendMessage(msg: Message) {
        server.sendMessage(msg)
    }

    private fun connectToServer() {
        server.connect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    liveData.value = it
                }
            ).addTo(disposables)
    }
}
