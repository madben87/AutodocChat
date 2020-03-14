package com.ben.model

class Message(val content: String,
              val type: Type) {

    enum class Type(val value: Int) {
        INCOMING_TEXT(301),
        INCOMING_IMAGE(302),
        OUTCOMING_TEXT(303),
        OUTCOMING_IMAGE(304);
    }
}