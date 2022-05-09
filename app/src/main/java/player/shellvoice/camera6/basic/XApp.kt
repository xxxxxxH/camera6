package player.shellvoice.camera6.basic

import android.app.Activity
import android.app.Application
import android.content.Context
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdLoader

class XApp:Application() {
    companion object {
        var instance: XApp? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        XAd.initialize(this)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        XAd.getInstance().initializationSdk()
    }

    fun insertAd(id: String, ac: Activity): MaxInterstitialAd {
        return MaxInterstitialAd(id, XAd.getInstance().lovinSdk, ac)
    }

    fun nativeAd(id: String): MaxNativeAdLoader {
        return MaxNativeAdLoader(id, XAd.getInstance().lovinSdk, this)
    }

    fun bannerAd(id: String): MaxAdView {
        return MaxAdView(id, XAd.getInstance().lovinSdk, this)
    }

    fun openAd(id: String, listener: ATSplashAdListener?): ATSplashAd {
        return ATSplashAd(this, id, listener)
    }
}