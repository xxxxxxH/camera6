package player.shellvoice.camera6.tools

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XApp
import player.shellvoice.camera6.http.OnNetworkRequest
import player.shellvoice.camera6.http.HttpTools

fun AppCompatActivity.getConfig(onSuccess:()->Unit,onFailure:()->Unit){
    HttpTools.with(this)
        .fromUrl("https://seasheel.xyz/config")
        .ofTypeGet()!!
        .connect(object :OnNetworkRequest{
            override fun onStart() {

            }

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


fun AppCompatActivity.appendBanner(){
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

fun Any?.xLogs(){
    Log.e("xxxxxxH", "$this")
}