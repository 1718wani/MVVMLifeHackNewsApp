package com.androiddevs.mvvmnewsapp.ui.fragment.article

//import android.content.Context
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.packagecheck.Companion.isApplicationInstalled
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*
import java.lang.Double.parseDouble
import kotlin.math.floor


val TAG = "data"

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel : NewsViewModel
    //これ何者かよくわからんが、とにかくデータ引っ張ってくるのに役に立つような感じか
    //発動条件がなにかがいまいちわからない。CodeArgslabのサンプルにもあった
    val args: ArticleFragmentArgs by navArgs()
    //↑これに関しては、

    //MVVM構造だと、基本的に現状あるボタンそれぞれに処理を書くけど、それ以上のデータ処理をかかないからonViewCreated{}内
    // にすべてのコードあつまりがちやな、逆にそれ以外あるのか？
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val dataStore = activity?.getSharedPreferences("DataStore",Context.MODE_PRIVATE)
        val originalData : String? = dataStore?.getString("input","1")
        val postData: SharedPreferences.Editor? = dataStore?.edit()
        //ここでデータをSeleazablにしておいているからアクセスできているという認識でいいか
        val article = args.article
        val url = article.url
        val webMyView = webView.apply {
            webViewClient = WebViewClient()
            loadUrl(url)
            settings.javaScriptEnabled = true
            settings.displayZoomControls = true
        }
        webMyView.viewTreeObserver.addOnScrollChangedListener{
            //一番最初に!をつけないといけないよ
            if(!webMyView.canScrollVertically(1)){
                Log.d("TAG","ページが下までいきました")
                //Pickerを表示するメソッドを記載。
//                val fragmentTransaction = childFragmentManager.beginTransaction()
                val dialog = onFinishOptionDialogFragment()
                dialog.setListener(object :
                    onFinishOptionDialogFragment.onFinishOptionDialogLister {
                    override fun onDialogTweetClick(dialog: DialogFragment) {
                        dialog.dismissAllowingStateLoss()

                        if (isApplicationInstalled(this@ArticleFragment.requireContext(),"com.twitter.android") ) {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.setPackage("com.twitter.android")
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT, "$url" )
                            startActivity(intent)
                        }else{
                            Toast.makeText(context, "Twitterがインストールされていません。", Toast.LENGTH_LONG).show()
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android&hl=ja&gl=US"))
                        }
                        val intOriginalData = originalData?.let { parseDouble(it) }
                        val multifiedData = intOriginalData?.times(1.01)
                        val flooredData = multifiedData?.times(100.0)?.let { floor(it) }
                            ?.div(100.0)

                        val stringmultifiedData = flooredData.toString()
                        postData?.putString("input", stringmultifiedData)
                        postData?.apply()

                    }

                    override fun onDialogMemoClick(dialog: DialogFragment) {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "$url")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)

                        val intOriginalData = originalData?.let { parseDouble(it) }
                        val multifiedData = intOriginalData?.times(1.01)
                        val flooredData = multifiedData?.times(100.0)?.let { floor(it) }
                            ?.div(100.0)

                        val stringmultifiedData = flooredData.toString()
                        postData?.putString("input", stringmultifiedData)
                        postData?.apply()

                        dialog.dismissAllowingStateLoss()

                    }

                    override fun onDialogbackClick(dialog: DialogFragment) {
                        dialog.dismissAllowingStateLoss()
                        findNavController().popBackStack(R.id.breakingNewsFragment,false)

                    }

                })
                dialog.show(childFragmentManager,TAG)

//                fragmentTransaction.commitAllowingStateLoss()
            }
        }

        fab.setOnClickListener{
            //ここにif文の挿入
            if (viewModel.notYetArticle(url)){
                viewModel.saveArticle(article)
                Snackbar.make(view,"お気に入りに保存されました",Snackbar.LENGTH_SHORT).show()

            }else{
                Snackbar.make(view,"すでにこの記事はお気に入りにあります。",Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}


