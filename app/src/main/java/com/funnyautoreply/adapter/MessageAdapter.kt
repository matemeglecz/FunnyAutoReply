package com.funnyautoreply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funnyautoreply.data.Message
import com.funnyautoreply.databinding.ItemMessageBinding
import java.text.DateFormat

class MessageAdapter() :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val items = mutableListOf<Message>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewHolder(
        ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = items[position]

        val sdf = DateFormat.getDateTimeInstance()
        var strdate = ""
        val date=Message.toCalendar(message.date)

        if (date != null) {
            strdate = sdf.format(date.time)
        }

        holder.binding.tvPhoneNumber.text = message.phoneNumber
        holder.binding.tvDate.text = strdate
    }

    override fun getItemCount(): Int = items.size

    interface MessageClickListener {
        fun onItemChanged(item: Message)
    }

    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: Message) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<Message>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }
}