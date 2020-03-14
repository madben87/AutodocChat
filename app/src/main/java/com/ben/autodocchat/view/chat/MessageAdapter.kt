package com.ben.autodocchat.view.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ben.autodocchat.R
import com.ben.autodocchat.extensions.dpToPx
import com.ben.model.Message
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_incoming_image_message.view.*
import kotlinx.android.synthetic.main.item_incoming_text_message.view.*
import kotlinx.android.synthetic.main.item_outcoming_image_message.view.*
import kotlinx.android.synthetic.main.item_outcoming_text_message.view.*

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.BaseHolder>() {

    private val dataList = ArrayList<Message>()

    fun addItem(item: Message) {
        dataList.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {

        when(viewType) {
            Message.Type.INCOMING_TEXT.value -> {
                return IncomingTextHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_incoming_text_message,
                    parent,
                    false
                ))
            }
            Message.Type.INCOMING_IMAGE.value -> {
                return IncomingImageHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_incoming_image_message,
                    parent,
                    false
                ))
            }
            Message.Type.OUTCOMING_TEXT.value -> {
                return OutcomingTextHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_outcoming_text_message,
                    parent,
                    false
                ))
            }
            Message.Type.OUTCOMING_IMAGE.value -> {
                return OutcomingImageHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_outcoming_image_message,
                    parent,
                    false
                ))
            }
            else -> {
                return IncomingTextHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_incoming_text_message,
                    parent,
                    false
                ))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type.value
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(dataList[position])
    }

    abstract class BaseHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        abstract fun bind(item: Message)
    }

    class IncomingTextHolder(override val containerView: View) :
        BaseHolder(containerView) {

        override fun bind(item: Message) {
            containerView.inMessage.text = item.content
        }
    }

    class IncomingImageHolder(override val containerView: View) :
        BaseHolder(containerView) {

        override fun bind(item: Message) {
            Glide.with(containerView.context)
                .asBitmap().dontAnimate().load(item.content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(containerView.resources.getDrawable(R.drawable.autodoc))
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8)))
                .into(containerView.inImage)
        }
    }

    class OutcomingTextHolder(override val containerView: View) :
        BaseHolder(containerView) {

        override fun bind(item: Message) {
            containerView.outMessage.text = item.content
        }
    }

    class OutcomingImageHolder(override val containerView: View) :
        BaseHolder(containerView) {

        override fun bind(item: Message) {
            Glide.with(containerView.context)
                .asBitmap().dontAnimate().load(item.content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(containerView.resources.getDrawable(R.drawable.autodoc))
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8)))
                .into(containerView.outImage)
        }
    }
}