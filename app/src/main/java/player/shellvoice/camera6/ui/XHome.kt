package player.shellvoice.camera6.ui

import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.tools.getDialog
import player.shellvoice.camera6.tools.getPermissions


class XHome : XPage(R.layout.activity_main) {
    private var dialogExit: AlertDialog? = null
    override fun letsGo() {
        getPermissions {
            if (it) {

                displayNativeAd { max ->
                    max?.let { ad ->
                        homeAd.removeAllViews()
                        homeAd.addView(ad)
                    }
                }

                sticker.setOnClickListener {
                    displayInsertAdReal()
                    route2PhotoPage(0)
                }
                slimming.setOnClickListener {
                    displayInsertAdReal()
                    route2PhotoPage(1)
                }
                cartoon.setOnClickListener {
                    displayInsertAdReal()
                    route2PhotoPage(2)
                }
                ageAlter.setOnClickListener {
                    displayInsertAdReal()
                    route2PhotoPage(3)
                }
                camera.setOnClickListener {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(intent)
                }
            }
        }

    }

    private fun route2PhotoPage(type: Int) {
        startActivity(Intent(this, XPhoto::class.java).apply {
            putExtra("type", type)
        })
    }

    override fun onBackPressed() {
        dialogExit = getDialog(0, false) {
//            val xh = displayInsertAdReal(isMust = true, isMain = true)
//            if (!xh) {
                finish()
//            }
        }
        dialogExit?.show()
    }

    override fun insertAdDismiss() {
        super.insertAdDismiss()
//        if (isMain) {
//            finish()
//        }
    }
}