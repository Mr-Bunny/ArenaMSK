package com.arenabooking.arenamsk.ui.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.utils.ActionEvent
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
            settings.userAgentString = "Android"
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.builtInZoomControls = true
            settings.setAppCacheEnabled(true)
            settings.setAppCacheMaxSize(10 * 1024 * 1024)
            settings.setAppCachePath("")
            settings.databaseEnabled = true
            settings.domStorageEnabled = true
            settings.setGeolocationEnabled(true)
            settings.saveFormData = false
            settings.savePassword = false
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            // Flash settings
            settings.pluginState = WebSettings.PluginState.ON

            // Geo location settings
            settings.setGeolocationEnabled(true)
            settings.setGeolocationDatabasePath("/data/data/selendroid")
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
        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }

        // Для старых устройств
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
