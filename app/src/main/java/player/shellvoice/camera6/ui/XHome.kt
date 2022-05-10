package player.shellvoice.camera6.ui

import android.content.Intent
import com.michael.easydialog.EasyDialog
import kotlinx.android.synthetic.main.activity_main.*
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.tools.getDialog
import player.shellvoice.camera6.tools.getPermissions

class XHome : XPage(R.layout.activity_main) {
    var dialogExit: EasyDialog? = null
    override fun letsGo() {
        getPermissions {
            if (it) {
                sticker.setOnClickListener { route2PhotoPage(0) }
                slimming.setOnClickListener { route2PhotoPage(1) }
                cartoon.setOnClickListener { route2PhotoPage(2) }
                ageAlter.setOnClickListener { route2PhotoPage(3) }
            }
        }

    }

    private fun route2PhotoPage(type: Int) {
        startActivity(Intent(this, XPhoto::class.java).apply {
            putExtra("type", type)
        })
    }

    override fun onBackPressed() {
        dialogExit = getDialog(0) {
            finish()
        }
        dialogExit?.show()
    }
}