package com.androiddevs.mvvmnewsapp.ui.fragment.article

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

val TAG = "data"

class ArticleFragment : Fragment(R.layout.fragment_article){

    private lateinit var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener

    lateinit var viewModel : NewsViewModel
    //これ何者かよくわからんが、とにかくデータ引っ張ってくるのに役に立つような感じか
    //発動条件がなにかがいまいちわからない。CodeArgslabのサンプルにもあった
    val args: ArticleFragmentArgs by navArgs()
    //↑これに関しては、ナビゲーションで移動用に存在しているみたい


    //MVVM構造だと、基本的に現状あるボタンそれぞれに処理を書くけど、それ以上のデータ処理をかかないからonViewCreated{}内
    // にすべてのコードあつまりがちやな、逆にそれ以外あるのか？
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        //ここでデータをSeleazablにしておいているからアクセスできているという認識でいいか
        val article = args.article
        val webMyView = webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        webMyView.viewTreeObserver.addOnScrollChangedListener{
            //一番最初に!をつけないといけないよ
            if(!webMyView.canScrollVertically(1)){
                Log.d("TAG","ページが下までいきました")
                //Pickerを表示するメソッドを記載。
                val dialog = onFinishOptionDialogFragment()
                dialog.show(childFragmentManager,"TAG")
            }
        }



        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"お気に入りに保存されました",Snackbar.LENGTH_SHORT).show()
        }


    }

}