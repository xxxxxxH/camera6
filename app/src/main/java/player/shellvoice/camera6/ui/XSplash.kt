package player.shellvoice.camera6.ui

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.item_stickers.root
import kotlinx.android.synthetic.main.xsplash_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.event.XEvent
import player.shellvoice.camera6.tools.configBean
import player.shellvoice.camera6.tools.getConfig
import player.shellvoice.camera6.tools.getPermissions
import player.shellvoice.camera6.tools.login

class XSplash : XPage(R.layout.xsplash_layout) {
    private var isShowOpen = false
    override fun letsGo() {
        getPermissions {
            if (it) {
                getConfig({
                    if (login) {
                        displayOpen()
                        return@getConfig
                    }
                    if (configBean.needLogin()) {
                        loginBtn.visibility = View.VISIBLE
                        return@getConfig
                    }
                    displayOpen()
                }, {
                    displayOpen()
                })
            }
        }
        EventBus.getDefault().register(this)
        loginBtn.setOnClickListener {

            startActivity(Intent(this, XLogin::class.java))
        }
    }

    private fun displayOpen() {
        if (displayOpenAdReal(root, true)) {
            isShowOpen = true
            return
        }
        startActivity(Intent(this, XHome::class.java))
        finish()
    }

    override fun insertAdDismiss() {
        super.insertAdDismiss()
        if (configBean.displayOpenAdWithInsertAd()) {
            if (isShowOpen) {
                isShowOpen = !isShowOpen
                startActivity(Intent(this, XHome::class.java))
                finish()
            }
        }
    }

    override fun splashAdDismiss() {
        super.splashAdDismiss()
        if (isShowOpen) {
            isShowOpen = !isShowOpen
            startActivity(Intent(this, XHome::class.java))
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: XEvent) {
        if (e.getMessage()[0] == "end") {
            finish()
        }
    }
}