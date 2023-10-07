package com.marqumil.peakyblinder.ui.chatbot

import androidx.lifecycle.ViewModel


class ChatBotViewModel : ViewModel() {

    val chatMessages = mutableListOf<ChatMessage>()

    // Get the current user's messages
    fun getCurrentUserMessages() = chatMessages.filter { it.isMe }

    // Get the chatbot's messages
    fun getChatbotMessages() = chatMessages.filter { !it.isMe }

    // Add a new message to the chat
    fun addMessage(message: ChatMessage) {
        chatMessages.add(message)
    }


}