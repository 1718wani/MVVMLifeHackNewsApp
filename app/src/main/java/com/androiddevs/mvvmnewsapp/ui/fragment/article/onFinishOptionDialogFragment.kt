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

class onFinishOptionDialogFragment : DialogFragment(){



//    interface onFinishOptionDialogLister {
//        fun onDialogTweetClick(dialog:DialogFragment)
//        fun onDialogMemoClick(dialog:DialogFragment)
//    }
//
//    private var mListener:onFinishOptionDialogLister? = null
//
//
//
////    ContextをNullableにするとoverrideできない
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
////        mListener = context as onFinishOptionDialogLister
//        try {
//            mListener = context as onFinishOptionDialogLister
//        } catch (e: ClassCastException) {
//            throw ClassCastException("${context.toString()} must implement onFinishOptionDialogListener")
//        }
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.title)
                .setMessage(R.string.message)
                    .setPositiveButton(R.string.tweet) {dialog, _ ->
                        Toast.makeText(activity, "Tweetします", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack(R.id.breakingNewsFragment,false)
//                        mListener?.onDialogTweetClick(this)
                        //Tweet画面に強制的に遷移＋ArticleのURLを表示させる
                        //それと同時にBreakingNewsのやつの数字を一個上げる。
                        //遷移した画面から戻ってきたときは、特にないもない。
                        }
                    .setNegativeButton(R.string.memo) {dialog, id ->
                        Toast.makeText(activity, "ほかの方法でシェアする", Toast.LENGTH_SHORT).show()
//                        mListener?.onDialogMemoClick(this)
                        //シェア画面
                        //それと同時にBreakingNewsのやつの数字を一個上げる。
                        //遷移した画面から戻ってきたときは、特にないもない。
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "This is my text to sen.")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
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
//val sendintentのところ
//この下の実装じゃだめでした。  Parcelable encountered IOException writing serializable object (name = com.androiddevs.mvvmnewsapp.models.Article)
//                            action = Intent.ACTION_SEND
//                            val data = getSerializableExtra("これはテストです")
//                            putExtra(Intent.EXTRA_TEXT, data)
//                            type = "text/plain"
