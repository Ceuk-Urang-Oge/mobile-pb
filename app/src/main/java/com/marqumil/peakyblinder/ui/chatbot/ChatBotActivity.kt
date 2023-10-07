package com.marqumil.peakyblinder.ui.chatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivityChatBotBinding

class ChatBotActivity : AppCompatActivity() {

    // Create a list to store the chat messages

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var viewModel: ChatBotViewModel
    private lateinit var adapter: ChatAdapter
    private lateinit var recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.progressBar.visibility = android.view.View.GONE

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
        }

        viewModel = ViewModelProvider(this).get(ChatBotViewModel::class.java)


        binding.sendButton.setOnClickListener {
            // Get the user's message
            val message = binding.messageEditText.text.toString()
            Log.d("ChatBotActivity", "onCreate: $message")

            // Add the message to the chat
            viewModel.addMessage(ChatMessage(message, true))

            Log.d("ChatBotActivity", "onCreate: ${viewModel.getCurrentUserMessages()[0].text}")
            Log.d("ChatBotActivity", "onCreate: ${viewModel.getChatbotMessages()}")

            // Clear the message edit text
            binding.messageEditText.setText("")

            Log.d("ChatBotActivity", "onCreate: ${viewModel.getCurrentUserMessages().size}")
            // show my message
            adapter = ChatAdapter(viewModel.getCurrentUserMessages())
            recycler_view = binding.messageRecyclerView
            recycler_view.adapter = adapter

            val manager = LinearLayoutManager(this)
            manager.stackFromEnd = true

            // make recycler view scroll bottom right away
            recycler_view.scrollToPosition(viewModel.getCurrentUserMessages().size - 1)
            recycler_view.layoutManager = manager
            recycler_view.setHasFixedSize(true)


        }



    }

    fun startChatbot(viewModel: ChatBotViewModel) {
        // Add the first chat message
        viewModel.addMessage(ChatMessage("Hi there!", false))

        // Get the chatbot's reply
        val reply = viewModel.getChatbotMessages()[0].text

        // Add the chatbot's reply
        viewModel.addMessage(ChatMessage(reply, false))

        Log.d("ChatBotActivity", "startChatbot: $reply")

    }

}