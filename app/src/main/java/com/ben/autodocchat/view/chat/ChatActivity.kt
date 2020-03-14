package com.ben.autodocchat.view.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ben.autodocchat.R
import com.ben.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.android.ext.android.inject

class ChatActivity : AppCompatActivity() {

    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 101
    private val IMAGE_REQUEST_CODE = 102

    private val viewModel: ChatViewModel by inject()

    private val adapter = MessageAdapter()
    private val adapterObserver = AdapterObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()

        viewModel.apply {
            getData().observe(this@ChatActivity, Observer {
                adapter.addItem(it)
            })
        }

        chatList.adapter = adapter

        sendMsg.setOnClickListener {
            if (!msgInput.text.isNullOrBlank()) {
                viewModel.sendMessage(Message(msgInput.text.toString(), Message.Type.OUTCOMING_TEXT))
                msgInput.text = null
            }
        }

        attachFile.setOnClickListener {
            chooseFile()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.chat_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onStart() {
        super.onStart()
        adapter.registerAdapterDataObserver(adapterObserver)
    }

    override fun onStop() {
        super.onStop()
        adapter.unregisterAdapterDataObserver(adapterObserver)
    }

    private fun chooseFile() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED)
            makeRequest()
        else
            openStorage()

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_REQUEST_CODE)
    }

    private fun sendImage(path: String) {
        viewModel.sendMessage(Message(path, Message.Type.OUTCOMING_IMAGE))
    }

    private fun openStorage() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openStorage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data.toString()
            sendImage(selectedImage)
        }
    }

    private inner class AdapterObserver: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            chatList.scrollToPosition(adapter.itemCount - 1)
        }
    }
}
