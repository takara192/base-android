package com.example.colorphone.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.StrictMode
import android.os.SystemClock
import android.text.InputType
import android.text.format.Formatter
import android.transition.Slide
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.google.android.material.internal.ViewUtils.showKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.TimeZone

fun Context.haveNetworkConnection(): Boolean {
    var haveConnectedWifi = false
    var haveConnectedMobile = false
    return try {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName
                    .equals("MOBILE", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        haveConnectedWifi || haveConnectedMobile
    } catch (e: java.lang.Exception) {
        System.err.println(e.toString())
        false
    }
}

fun AppCompatImageView.loadUrl(url: Any, px: Int) {
    this.load(url) {
        crossfade(true)
        transformations(
            RoundedCornersTransformation(px.toFloat())
        )
    }
}
fun View.safetyRequestFocus() {
    if (isAttachedToWindow) {
        requestFocus()
        showKeyboard(this)
    }
}

fun Activity.getHeightDevice(): Int {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Activity.getWidthDevice(): Int {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun View.changeBackgroundColor(newColor: Int) {
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun ImageView.setTintColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
}

fun TextView.changeTextColor(newColor: Int) {
    setTextColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun View.animRotation() {
    val anim = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.interpolator = LinearInterpolator()
    anim.duration = 4000
    anim.isFillEnabled = true
    anim.repeatCount = Animation.INFINITE
    anim.fillAfter = true
    startAnimation(anim)
}

fun View.animRotation2() {
    val anim = RotateAnimation(
        360f, 0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.interpolator = LinearInterpolator()
    anim.duration = 4000
    anim.isFillEnabled = true
    anim.repeatCount = Animation.INFINITE
    anim.fillAfter = true
    startAnimation(anim)
}

fun View.animRotation3() {
    val anim = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.interpolator = LinearInterpolator()
    anim.duration = 1500
    anim.isFillEnabled = true
    anim.repeatCount = Animation.INFINITE
    anim.fillAfter = true
    startAnimation(anim)
}

fun View.isShow() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.show() {
    this.context

    CoroutineScope(Dispatchers.Main).launch {
        visibility = View.VISIBLE
    }
}

fun View.gone() {
    CoroutineScope(Dispatchers.Main).launch {
        visibility = View.GONE
    }
}

fun View.inv() {
    CoroutineScope(Dispatchers.Main).launch {
        visibility = View.INVISIBLE
    }
}

fun View.setPreventDoubleClick(debounceTime: Long = 500, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.changeBackgroundDrawableColor(color: Int) {
    val bg = this.background as? GradientDrawable

    bg?.mutate()

    bg?.setColor(color)
}

fun ImageView.changeSVGColor(color: Int) {
    val drawable = this.drawable

    val wrapper = DrawableCompat.wrap(drawable).mutate()

    DrawableCompat.setTint(wrapper, color)

    this.setImageDrawable(wrapper)
}

fun View.setPreventDoubleClickScaleView(debounceTime: Long = 500, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }

            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }

            return true
        }
    })
}

fun Fragment.displayToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(@StringRes msg: Int) {
    Toast.makeText(context, getString(msg), Toast.LENGTH_SHORT).show()
}

fun Fragment.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.sendEmailMore(
    addresses: Array<String>,
    subject: String? = null,
    body: String? = ""
) {

    disableExposure()

    val title = " [Note] Please tell us your bug or suggestions. We will hear everything!"

    val intent = Intent(Intent.ACTION_SENDTO)
    // intent.type = "message/rfc822"
    intent.data = Uri.parse("mailto:") // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, addresses)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: title)

//    intent.putExtra(
//        Intent.EXTRA_TEXT, body + "\n\n\n" +
//                "DEVICE INFORMATION (Device information is useful for application improvement and development)"
//                + "\n\n" + getDeviceInfo()
//    )
    //intent.selector = emailSelectorIntent

    try {
        startActivity(Intent.createChooser(intent, ""))
    } catch (e: Exception) {
        Toast.makeText(this, "you need install gmail", Toast.LENGTH_SHORT).show()
    }
}

private fun getDeviceInfo(): String {
    val densityText = when (Resources.getSystem().displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> "HDPI"
    }

    var megAvailable = 0L

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
            megAvailable = bytesAvailable / (1024 * 1024)
        }
    }


    return "Manufacturer ${Build.MANUFACTURER}, Model ${Build.MODEL}," +
            " ${Locale.getDefault()}, " +
            "osVer ${Build.VERSION.RELEASE}, Screen ${Resources.getSystem().displayMetrics.widthPixels}x${Resources.getSystem().displayMetrics.heightPixels}, " +
            "$densityText, Free space ${megAvailable}MB, TimeZone ${
                TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
            }"
}

private fun disableExposure() {
    if (Build.VERSION.SDK_INT >= 24) {
        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun Context.getStringResourceByName(aString: String): String {
    val packageName: String = packageName
    val resId: Int = resources.getIdentifier(aString, "string", packageName)
    return getString(resId)
}

fun Context.openBrowser(url: String) {
    var url = url
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        url = "http://$url"
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        startActivity(browserIntent)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
}

fun LinearLayout.setWidth(width: Int) {
    val params = LinearLayout.LayoutParams(
        width,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    this.apply {
        layoutParams = params
        requestFocus()
    }
}

fun ConstraintLayout.setWidth(width: Int) {
    val params = LinearLayout.LayoutParams(
        width,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    this.apply {
        layoutParams = params
        requestFocus()
    }
}

fun Context.getIp(): String {
    val wifiMgr = this.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val wifiInfo = wifiMgr!!.connectionInfo
    val ip = wifiInfo.ipAddress
    return Formatter.formatIpAddress(ip)
}

fun EditText.setOnNextAction(onNext: () -> Unit) {
    setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)

    setOnKeyListener { v, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            onNext()
            return@setOnKeyListener true
        } else return@setOnKeyListener false
    }

    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            onNext()
            return@setOnEditorActionListener true
        } else return@setOnEditorActionListener false
    }
}

fun TextView.setDrawableLeft(id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun Window.setSoftInputResize() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Context.getDisplayWidth(): Int? {
    val displayMetrics = resources?.displayMetrics
    return displayMetrics?.widthPixels
}

fun Context.getDisplayHeight(): Int {
    val displayMetrics = resources.displayMetrics
    return displayMetrics.heightPixels
}

val Float.px: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Int.px: Int get() = ((this * Resources.getSystem().displayMetrics.density).toInt())

fun View.visibilityRight(visibility: Boolean) {
    setVisibility(Gravity.END, visibility)
}

fun View.visibilityLeft(visibility: Boolean) {
    setVisibility(Gravity.START, visibility)
}

fun View.setVisibility(slide: Int, visibility: Boolean, durationTime: Long = 300) {
    val transition = Slide(slide)
    transition.apply {
        duration = durationTime
        addTarget(this@setVisibility)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.isVisible = visibility
}