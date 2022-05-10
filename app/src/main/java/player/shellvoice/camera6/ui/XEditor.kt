package player.shellvoice.camera6.ui

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.xeditor_bottom.*
import kotlinx.android.synthetic.main.xeditor_content.*
import net.idik.lib.slimadapter.SlimAdapter
import net.idik.lib.slimadapter.SlimInjector
import net.idik.lib.slimadapter.viewinjector.IViewInjector
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.bean.ResourceBean
import player.shellvoice.camera6.tools.getResourceFile

class XEditor : XPage(R.layout.xeditor_layout) {

    private val url by lazy {
        intent.getStringExtra("url")
    }

    private val type by lazy {
        intent.getIntExtra("type", -1)
    }

    @SuppressLint("SetTextI18n")
    override fun letsGo() {
        Glide.with(this).load(url).into(editorBg)
        if (type != -1) {
            when (type) {
                0 -> {
                    titleTv.text = "Sticker"
                    getResourceFile(R.mipmap::class.java, "mipmap", "sticker") {
                        if (it.size > 0) {
                            recycler.layoutManager = GridLayoutManager(this, 3)
                            SlimAdapter.create().register(R.layout.item_stickers,
                                object : SlimInjector<ResourceBean> {
                                    override fun onInject(
                                        data: ResourceBean,
                                        injector: IViewInjector<out IViewInjector<*>>
                                    ) {
                                        val imageView =
                                            injector.findViewById<ImageView>(R.id.itemSticker)
                                        Glide.with(this@XEditor).load(data.id).into(imageView)
                                    }

                                }).attachTo(recycler).updateData(it)
                        }
                    }
                }
                1 -> {
                    titleTv.text = "Slimming"
                }
                2 -> {
                    titleTv.text = "Cartoon"
                }
                3 -> {
                    titleTv.text = "Age Alter"
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}