package com.androiddevs.mvvmnewsapp.ui.fragment.article

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R

//lateinit var shared : SharedPreferences


class onFinishOptionDialogFragment : DialogFragment(){



    interface onFinishOptionDialogLister {
        fun onDialogTweetClick(dialog:DialogFragment)
        fun onDialogMemoClick(dialog:DialogFragment)
        fun onDialogbackClick(dialog:DialogFragment)

    }

    public fun setListener(lister: onFinishOptionDialogLister){
        mListener = lister
    }

    private var mListener:onFinishOptionDialogLister? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.title)
                .setMessage(R.string.message)
                    .setPositiveButton(R.string.tweet) {dialog, _ ->
                        Toast.makeText(activity, "Tweetします", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack(R.id.breakingNewsFragment,false)
//                        val latestScore = shared.getInt("DataStore",1)
                        mListener?.onDialogTweetClick(this)
                        //Tweet画面に強制的に遷移＋ArticleのURLを表示させる
                        //それと同時にBreakingNewsのやつの数字を一個上げる。
                        //遷移した画面から戻ってきたときは、特にないもない。
                        }
                    .setNegativeButton(R.string.memo) {dialog, id ->
                        Toast.makeText(activity, "メモをするアプリを選んでください。", Toast.LENGTH_SHORT).show()

                        mListener?.onDialogMemoClick(this)
                        //シェア画面
                        //それと同時にBreakingNewsのやつの数字を一個上げる。
                        //遷移した画面から戻ってきたときは、特にないもない。
                    }
                    .setNeutralButton(R.string.quit) { dialog, id ->
                    // Neutralボタンがタップされたときに実行される処理
                        Toast.makeText(activity, "「無回答」がタップされた", Toast.LENGTH_SHORT).show()
                        //popBackStackとはなんぞや
                        mListener?.onDialogbackClick(this)
                     }
            //キャンセル可能にする
            isCancelable = true
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}







