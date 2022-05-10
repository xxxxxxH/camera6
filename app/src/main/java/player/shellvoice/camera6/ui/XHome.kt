package player.shellvoice.camera6.ui

import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.tools.getConfig
import player.shellvoice.camera6.tools.upload

class XHome : XPage(R.layout.activity_main) {
    override fun letsGo() {
        upload()
    }
}