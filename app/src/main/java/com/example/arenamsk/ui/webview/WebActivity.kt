package com.example.arenamsk.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.arenamsk.R
import kotlinx.android.synthetic.main.activity_web.*

/** Класс с WebView для открытия страницы оплаты */
class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        //Получаем ссылку на оплату из интента
        val link = intent.getStringExtra("paymentUrl")

        //Открываем ссылку во встроенном webView
        with(webview) {
            settings.javaScriptEnabled = true
            webViewClient = SimpleWebViewClient()
            loadUrl(link)
        }
    }

    /** Переопределяем кнопку назад, чтобы навигация была внутри сайта, а не приложения */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack()) {
            this.webview.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
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
