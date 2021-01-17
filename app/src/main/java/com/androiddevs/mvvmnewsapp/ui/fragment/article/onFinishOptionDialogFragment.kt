package com.androiddevs.mvvmnewsapp.ui.fragment.article

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.androiddevs.mvvmnewsapp.R

class onFinishOptionDialogFragment : DialogFragment() {

    public interface onFinishOptionDialogLister {
        public fun onDialogPositiveClick(dialog:DialogFragment)
        public fun onDialogNegativeClick(dialog:DialogFragment)
    }

//    var mLister:onFinishOptionDialogLister? = null
//
//    override fun onAttach(context: Context?) {
//        super.onAttach()
//        try {
//            mLister = context as onFinishOptionDialogLister
//        } catch (e: ClassCastException) {
//            throw ClassCastException("${context.toString()} must implement NoticeDialogListener")
//        }
//    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_on_finish_option)
                    .setItems(R.array.arrayOnFinishOption,
                        DialogInterface.OnClickListener {dialog, which ->
                            val opitons = resources.getStringArray(R.array.arrayOnFinishOption)
                            println(opitons[which])
                        })
            //これの意味はわかっていない。
            isCancelable = true
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}