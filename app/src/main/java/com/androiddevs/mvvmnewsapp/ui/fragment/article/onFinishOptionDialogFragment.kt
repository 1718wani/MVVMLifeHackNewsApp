package com.androiddevs.mvvmnewsapp.ui.fragment.article

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.R

class onFinishOptionDialogFragment : DialogFragment() {

    private lateinit var listener: onFinishOptionDialogLister

    public interface onFinishOptionDialogLister {
        public fun onDialogTweetClick(dialog:DialogFragment,which:Int)
        public fun onDialogMemoClick(dialog:DialogFragment,which:Int)
        public fun onDialogQuitClick(dialog:DialogFragment,which:Int)
    }

    var mLister:onFinishOptionDialogLister? = null
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
            builder.setTitle(R.string.title)
                .setMessage(R.string.message)
                    .setPositiveButton(R.string.tweet) {dialog, id ->
                        Toast.makeText(activity, "Tweetします", Toast.LENGTH_SHORT).show()
//                        mListener?.onDialogTweetClick(activity)
                        }
                    .setNegativeButton(R.string.memo) {dialog, id ->
                        Toast.makeText(activity, "メモするよ", Toast.LENGTH_SHORT).show()
                         }
                    .setNeutralButton(R.string.quit) { dialog, id ->
                    // Neutralボタンがタップされたときに実行される処理
                        Toast.makeText(activity, "「無回答」がタップされた", Toast.LENGTH_SHORT).show()
                        //popBackStackとはなんぞや
                        findNavController().popBackStack(R.id.breakingNewsFragment,false)
                     }

            //キャンセル可能にする
            isCancelable = true
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}