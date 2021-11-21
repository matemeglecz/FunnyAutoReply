package com.funnyautoreply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funnyautoreply.R
import com.funnyautoreply.data.Message
import com.funnyautoreply.databinding.ItemMessageBinding

class MessageAdapter(private val listener: MessageSelectedListener) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val items = mutableListOf<Message>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MessageViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = items[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = items.size

    interface MessageSelectedListener {
        fun onItemSelected(item: Message?)
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemMessageBinding.bind(itemView)
        var item: Message? = null

        init {
            binding.root.setOnClickListener { listener.onItemSelected(item) }
        }

        fun bind(newMessage: Message?) {
            item=newMessage
            binding.tvPhoneNumber.text = item?.phoneNumber
            binding.tvDate.text =  Message.toFormattedDate(item?.date)
        }
    }

    /*fun addItem(item: Message) {
        items.add(0, item)
        notifyItemInserted(0)
    }*/

    fun update(shoppingItems: List<Message>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }
}