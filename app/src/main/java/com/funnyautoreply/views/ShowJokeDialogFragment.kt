package com.funnyautoreply.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.funnyautoreply.R
import com.funnyautoreply.data.Message
import com.funnyautoreply.databinding.DialogShowJokeBinding

class ShowJokeDialogFragment(private val item: Message?) : AppCompatDialogFragment() {
    private lateinit var binding: DialogShowJokeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogShowJokeBinding.inflate(LayoutInflater.from(context))
        binding.tvCategory.text=item?.category
        binding.tvPhoneNumber.text=item?.phoneNumber
        binding.tvDate.text=Message.toFormattedDate(item?.date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.sentJoke)
            .setMessage(item?.joke)
            .setView(binding.root)
            .setPositiveButton(R.string.ok, null)
            .create()
    }
}