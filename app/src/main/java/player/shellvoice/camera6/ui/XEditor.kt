package player.shellvoice.camera6.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lcw.library.stickerview.Sticker
import kotlinx.android.synthetic.main.xeditor_bottom.*
import kotlinx.android.synthetic.main.xeditor_content.*
import net.idik.lib.slimadapter.SlimAdapter
import net.idik.lib.slimadapter.SlimInjector
import net.idik.lib.slimadapter.viewinjector.IViewInjector
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.bean.ResourceBean
import player.shellvoice.camera6.tools.getDialog
import player.shellvoice.camera6.tools.getResourceFile
import player.shellvoice.camera6.tools.getSlimmingData

class XEditor : XPage(R.layout.xeditor_layout) {

    private val url by lazy {
        intent.getStringExtra("url")
    }

    private val type by lazy {
        intent.getIntExtra("type", -1)
    }

    private var dialogSave: AlertDialog? = null

    private var dialogLoading: AlertDialog? = null

    @SuppressLint("SetTextI18n")
    override fun letsGo() {
        Glide.with(this).load(url).into(editorBg)
        cancel.setOnClickListener { finishAfterTransition() }
        save.setOnClickListener {
            dialogSave = getDialog(1,false){
                dialogSave?.dismiss()
                dialogLoading = getDialog(2){

                }
                dialogLoading?.show()
            }
            dialogSave?.show()
        }
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
                                        imageView.setOnClickListener {
                                            val b = BitmapFactory.decodeResource(this@XEditor.resources, data.id)
                                            sticker.addSticker(Sticker(this@XEditor, b))
                                        }
                                    }

                                }).attachTo(recycler).updateData(it)
                        }
                    }
                }
                1 -> {
                    titleTv.text = "Slimming"
                    getSlimmingData {
                        if (it.size > 0) {
                            recycler.layoutManager = GridLayoutManager(this, 5)
                            SlimAdapter.create().register(R.layout.item_slim, object : SlimInjector<ResourceBean> {
                                override fun onInject(
                                    data: ResourceBean,
                                    injector: IViewInjector<out IViewInjector<*>>
                                ) {
                                    val imageView = injector.findViewById<ImageView>(R.id.itemSlim)
                                    imageView.setBackgroundResource(data.id)
                                }
                            }).attachTo(recycler).updateData(it)
                        }
                    }
                }
                2 -> {
                    titleTv.text = "Cartoon"
                    getResourceFile(R.mipmap::class.java, "mipmap", "cartoon") {
                        if (it.size > 0) {
                            recycler.layoutManager = GridLayoutManager(this, 4)
                            SlimAdapter.create().register(R.layout.item_stickers,
                                object : SlimInjector<ResourceBean> {
                                    override fun onInject(
                                        data: ResourceBean,
                                        injector: IViewInjector<out IViewInjector<*>>
                                    ) {
                                        val imageView =
                                            injector.findViewById<ImageView>(R.id.itemSticker)
                                        Glide.with(this@XEditor).load(data.id).into(imageView)
                                        imageView.setOnClickListener {

                                        }
                                    }

                                }).attachTo(recycler).updateData(it)
                        }
                    }
                }
                3 -> {
                    titleTv.text = "Age Alter"
                    getResourceFile(R.mipmap::class.java, "mipmap", "icon_age") {
                        if (it.size > 0) {
                            recycler.layoutManager = GridLayoutManager(this, 4)
                            SlimAdapter.create().register(R.layout.item_stickers,
                                object : SlimInjector<ResourceBean> {
                                    override fun onInject(
                                        data: ResourceBean,
                                        injector: IViewInjector<out IViewInjector<*>>
                                    ) {
                                        val imageView =
                                            injector.findViewById<ImageView>(R.id.itemSticker)
                                        Glide.with(this@XEditor).load(data.id).into(imageView)
                                        imageView.setOnClickListener {

                                        }
                                    }

                                }).attachTo(recycler).updateData(it)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}