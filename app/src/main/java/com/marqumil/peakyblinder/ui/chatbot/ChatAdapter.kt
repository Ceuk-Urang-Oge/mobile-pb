package com.marqumil.peakyblinder.ui.chatbot

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.remote.response.ArtikelData
import com.marqumil.peakyblinder.remote.response.ArtikelResponse
import com.marqumil.peakyblinder.ui.article.DetailArticleActivity

class ChatAdapter(private val chatMessages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatMessages[position]

        holder.messageTextView.text = chatMessage.text
        Log.d("ChatAdapter", chatMessage.text)

        if (chatMessage.isMe) {
            holder.messageTextView.setBackgroundResource(R.drawable.rounded_bg_button_sec)
            holder.messageTextView.setTextColor(holder.itemView.resources.getColor(R.color.white))
            Log.d("ChatAdapter", "isMe")
        } else {
            holder.messageTextView.setBackgroundResource(R.drawable.rounded_bg_button)
            holder.messageTextView.setTextColor(holder.itemView.resources.getColor(R.color.black))
            Log.d("ChatAdapter", "isNotMe")
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.tvMessenger)

    }
}