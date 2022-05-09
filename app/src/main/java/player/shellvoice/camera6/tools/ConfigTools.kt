package player.shellvoice.camera6.tools

import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.internal.ActivityLifecycleTracker
import player.shellvoice.camera6.bean.ConfigBean
import player.shellvoice.camera6.bean.UpdateBean

fun formatResult1(s: String): String? {
    return try {
        StringBuffer(s).replace(1, 2, "").toString()
    } catch (e: Exception) {
        e.fillInStackTrace()
        null
    }
}

fun formatResult2(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun formatResult3(s: String): ConfigBean? {
    return gson.fromJson(s, ConfigBean::class.java)
}

fun AppCompatActivity.formatResult4(pojo: ConfigBean): String? {
    configBean = pojo
    if (configBean.insertAdInvokeTime() != invokeTime || configBean.insertAdRealTime() != realTime) {
        invokeTime = configBean.insertAdInvokeTime()
        realTime = configBean.insertAdRealTime()
        index = 0
        lastTime = 0
        list = mutableListOf<Boolean>().apply {
            if (invokeTime >= realTime) {
                (0 until invokeTime).forEach { _ ->
                    add(false)
                }
                (0 until realTime).forEach { index ->
                    set(index, true)
                }
            }
        }
    }
    if (configBean.faceBookId().isNotBlank()) {
        initFaceBook()
    }
    return pojo.info
}

fun formatResult5(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun formatResult6(s: String): UpdateBean? {
    return gson.fromJson(s, UpdateBean::class.java)
}

fun formatResult7(pojo: UpdateBean) {
    updateBean = pojo
}

fun AppCompatActivity.initFaceBook() {
    FacebookSdk.apply {
        setApplicationId(configBean.faceBookId())
        sdkInitialize(this@initFaceBook)
        ActivityLifecycleTracker.apply {
            onActivityCreated(this@initFaceBook)
            onActivityResumed(this@initFaceBook)
        }
    }
}