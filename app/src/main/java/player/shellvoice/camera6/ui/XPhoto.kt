package player.shellvoice.camera6.ui

import android.app.ActivityOptions
import android.content.Intent
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.xphoto_layout.*
import net.idik.lib.slimadapter.SlimAdapter
import net.idik.lib.slimadapter.SlimInjector
import net.idik.lib.slimadapter.viewinjector.IViewInjector
import player.shellvoice.camera6.R
import player.shellvoice.camera6.basic.XPage
import player.shellvoice.camera6.tools.getPhoto

class XPhoto : XPage(R.layout.xphoto_layout) {

    private val type by lazy {
        intent.getIntExtra("type",-1)
    }

    override fun letsGo() {
        getPhoto {
            if (it.size > 0) {
                recycler.layoutManager = GridLayoutManager(this, 3)
                SlimAdapter.create().register(R.layout.xitem_photo, object : SlimInjector<String> {
                    override fun onInject(
                        data: String,
                        injector: IViewInjector<out IViewInjector<*>>
                    ) {
                        val imageView = injector.findViewById<ImageView>(R.id.xItemImage)
                        Glide.with(this@XPhoto).load(data).into(imageView)
                        imageView.setOnClickListener { v ->
                            val intent = Intent(this@XPhoto, XEditor::class.java)
                            intent.putExtra("url", data)
                            intent.putExtra("type", type)
                            startActivity(
                                intent, ActivityOptions.makeSceneTransitionAnimation(
                                    this@XPhoto, v, "a"
                                ).toBundle()
                            )
                        }
                    }
                }).attachTo(recycler).updateData(it)
            }
        }
    }
}