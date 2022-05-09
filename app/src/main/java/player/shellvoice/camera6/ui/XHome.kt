package player.shellvoice.camera6.ui

import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.tools.getConfig

class XHome : XPage(R.layout.activity_main) {
    override fun letsGo() {
        getConfig({},{})
    }
}