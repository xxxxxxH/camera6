package player.shellvoice.camera6.basic

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.IATSplashEyeAd
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import player.shellvoice.camera6.R
import player.shellvoice.camera6.tools.*

abstract class XPage(layoutId: Int) : AppCompatActivity(layoutId) {
    private var isBackground = false
    private var openAd: ATSplashAd? = null
    private var insertAd: MaxInterstitialAd? = null
    private var openId = ""
    private var insertId = ""
    private var nativeId = ""
    private var bannerId = ""
    protected var isMain = false
    private val openAdListener = object : XAdListener.openAdLisenter {
        override fun onAdLoaded() {
            "open onAdLoaded $openAd".xLogs()
        }

        override fun onNoAdError(p0: AdError?) {
            "open $p0".xLogs()
                        getOpenAd()
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: IATSplashEyeAd?) {
            splashAdDismiss()
            getOpenAd()
        }

    }
    private val insertAdListener = object : XAdListener.inertAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            "insert onAdLoaded $insertAd".xLogs()
        }

        override fun onAdHidden(ad: MaxAd?) {
            lastTime = System.currentTimeMillis()
            getInsertAd()
            insertAdDismiss()
        }


        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            getInsertAd()
        }

        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
            getInsertAd()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openId = getString(R.string.top_on_open_ad_id)
        insertId = getString(R.string.lovin_insert_ad_id)
        nativeId = getString(R.string.lovin_native_ad_id)
        openAd = XApp.instance!!.openAd(openId, openAdListener)
        openAd!!.loadAd()
        openAd.xLogs()

        insertAd = XApp.instance!!.insertAd(insertId, this)
        insertAd!!.setListener(insertAdListener)
        insertAd!!.loadAd()
        insertAd.xLogs()
        letsGo()
        appendBanner()
    }

    private fun getOpenAd() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            openAd?.onDestory()
            openAd = XApp.instance!!.openAd(openId, openAdListener)
            openAd?.loadAd()
        }
    }

    fun displayOpenAdReal(v: ViewGroup, isForce: Boolean = false): Boolean {
        if (configBean.displayOpenAdWithInsertAd()) {
            return displayInsertAdReal(isMust = isForce)
        } else {
            return displayOpenAd(v)
        }
    }

    private fun displayOpenAd(v: ViewGroup, isForce: Boolean = false): Boolean {
        openAd?.let {
            if (it.isAdReady) {
                it.show(this, v)
                return true
            }
        }
        return false
    }

    private fun getInsertAd() {
        lifecycleScope.launch(Dispatchers.IO) {
            insertAd?.destroy()
            delay(3500)
            insertAd = XApp.instance!!.insertAd(insertId, this@XPage)
            insertAd!!.setListener(insertAdListener)
            insertAd!!.loadAd()
        }
    }

    fun displayInsertAdReal(
        percent: Boolean = false,
        isMust: Boolean = false,
        tag: String = "",
        isMain: Boolean = false
    ): Boolean {
        if (isMust) {
            this.isMain = isMain
            return displayInsertAd()
        } else {
            if (configBean.isCanDisplayInsertAd()) {
                if ((percent && configBean.isCanDisplayByPercent()) || (!percent)) {
                    if (System.currentTimeMillis() - lastTime > configBean.insertAdInterval() * 1000) {
                        var result = false
                        if (list.getOrNull(index) == true) {
                            result = displayInsertAd(tag)
                        }
                        index++
                        if (index >= list.size) {
                            index = 0
                        }
                        return result
                    }
                }
            }
            return false
        }
    }

    private fun displayInsertAd(tag: String = ""): Boolean {
        insertAd?.let {
            if (it.isReady) {
                it.showAd(tag)
                return true
            }
        }
        return false
    }

    fun displayNativeAd(display:(MaxNativeAdView?)->Unit){
        val ad = XApp.instance!!.nativeAd(nativeId)
        ad.loadAd()
        ad.setNativeAdListener(object :MaxNativeAdListener(){
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                display(p0)
            }

            override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                super.onNativeAdLoadFailed(p0, p1)
                p0.xLogs()
                p1.xLogs()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        isBackground = isInBackground()
    }


    override fun onResume() {
        super.onResume()
        if (isBackground) {
            isBackground = false
            val content = findViewById<ViewGroup>(android.R.id.content)
            (content.getTag(R.id.open_ad_view_id) as? FrameLayout)?.let {
                displayOpenAdReal(it)
            } ?: kotlin.run {
                FrameLayout(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    content.addView(this)
                    content.setTag(R.id.open_ad_view_id, this)
                    displayOpenAdReal(this)
                }
            }
        }
    }

    abstract fun letsGo()

    open fun insertAdDismiss() {}

    open fun splashAdDismiss() {}
}