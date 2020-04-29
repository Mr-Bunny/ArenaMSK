package com.example.arenamsk.ui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.arenamsk.R
import com.example.arenamsk.utils.ActionEvent
import kotlinx.android.synthetic.main.activity_web.*
import org.greenrobot.eventbus.EventBus

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(ActionEvent.PaymentFinished())
            setResult(Activity.RESULT_OK)
            finish()
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
