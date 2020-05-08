package com.example.arenamsk.ui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.arenamsk.R
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.utils.ActionEvent
import kotlinx.android.synthetic.main.activity_web.*
import org.greenrobot.eventbus.EventBus

/** Класс с WebView для открытия страницы оплаты */
class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        //Получаем ссылку из интента
        val link = intent.getStringExtra("url")

        //Открываем ссылку во встроенном webView
        with(webview) {
            settings.javaScriptEnabled = true
            webViewClient = SimpleWebViewClient()
            loadUrl(link)
        }
    }

    override fun onStop() {
        if (AuthUtils.isUserAuthorized()) EventBus.getDefault().post(ActionEvent.PaymentFinished())

        super.onStop()
    }

    /** Веб клиент для открытия ссылок в webView приложения, а не в каком-то другом браузере */
    private class SimpleWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            webView: WebView,
            url: String
        ): Boolean {
            return false
        }
    }
}
