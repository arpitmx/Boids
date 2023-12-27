package com.bitpolarity.boids

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import android.widget.Toast
import androidx.annotation.StringRes

import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
File : ExtensionsUtil.kt -> com.ncs.versa.Utility
Description : This file contains extension functions for different datatypes for easting out the development process 

Author : Alok Ranjan (VC uname : apple)
Link : https://github.com/arpitmx
From : Bitpolarity x Noshbae (@Project : Versa Android)

Creation : 3:16 pm on 25/05/23

Todo >
Tasks CLEAN CODE : 
Tasks BUG FIXES : 
Tasks FEATURE MUST HAVE : 
Tasks FUTURE ADDITION : 


*/


object ExtensionsUtil {

    //Logging extensions




    // Visibililty Extensions

    fun View.gone() = run { visibility = View.GONE }
    fun View.visible() = run { visibility = View.VISIBLE }
    fun View.invisible() = run { visibility = View.INVISIBLE }

    infix fun View.visibleIf(condition: Boolean) =
        run { visibility = if (condition) View.VISIBLE else View.GONE }

    infix fun View.goneIf(condition: Boolean) =
        run { visibility = if (condition) View.GONE else View.VISIBLE }

    infix fun View.invisibleIf(condition: Boolean) =
        run { visibility = if (condition) View.INVISIBLE else View.VISIBLE }


    fun View.progressGone(context: Context, duration: Long = 1500L) = run {
        animFadeOut(context, duration)
        visibility = View.GONE

    }

    fun View.progressVisible(context: Context, duration: Long = 1500L) = run {
        visibility = View.VISIBLE
        animFadein(context, duration)
    }


    fun View.progressGoneSlide(context: Context, duration: Long = 1500L) = run {
        animSlideUp(context, duration)
        visibility = View.GONE

    }

    fun View.progressVisibleSlide(context: Context, duration: Long = 1500L) = run {
        visibility = View.VISIBLE
        animSlideDown(context, duration)
    }

    // Toasts



    fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun Activity.toast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    //Snackbar

    fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).setAnimationMode(ANIMATION_MODE_SLIDE).show()
    }

    fun View.snackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }


    fun Activity.hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }



    fun EditText.showKeyboardB() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        requestFocus()
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }


    // Convert px to dp
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    //Convert dp to px
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


    val String.isDigitOnly: Boolean
        get() = matches(Regex("^\\d*\$"))

    val String.isAlphabeticOnly: Boolean
        get() = matches(Regex("^[a-zA-Z]*\$"))

    val String.isAlphanumericOnly: Boolean
        get() = matches(Regex("^[a-zA-Z\\d]*\$"))


    //Null check
    val Any?.isNull get() = this == null

    fun Any?.ifNull(block: () -> Unit) = run {
        if (this == null) {
            block()
        }
    }

    /**
     * Set Drawable to the left of EditText
     * @param icon - Drawable to set
     */
    fun EditText.setDrawable(icon: Drawable) {
        this.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
    }


    /**
     * Function to run a delayed function
     * @param millis - Time to delay
     * @param function - Function to execute
     */
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    /**
     * Show multiple views
     */
    fun showViews(vararg views: View) {
        views.forEach { view -> view.visible() }
    }


    /**
     * Hide multiple views
     */
    fun hideViews(vararg views: View) {
        views.forEach { view -> view.gone() }
    }


    //Date formatting
    fun String.toDate(format: String = "yyyy-MM-dd HH:mm:ss"): Date? {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.parse(this)
    }

    fun Date.toStringFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }


    //Network check


    //Permission
    fun Context.isPermissionGranted(permission: String) = run {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }



    //Animation
    fun View.animSlideUp(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_in_bottom)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }

    fun View.animSlideDown(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation =
            AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_out_bottom)
                .apply {
                    duration = animDuration
                }
        this.startAnimation(animation)
    }

    fun View.animSlideLeft(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }

    fun View.animSlideRight(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }

    fun View.animFadein(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }


    fun View.rotate180(context: Context, animDuration: Long = 500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate180)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }

    fun View.set180(context: Context, animDuration: Long = 500L) {
        clearAnimation()
        val currentRotation = tag as? Float ?: 0f
        val targetRotation = if (currentRotation == 0f) 180f else 0f
        val rotationProperty =
            PropertyValuesHolder.ofFloat(View.ROTATION, currentRotation, targetRotation)
        val animator = ObjectAnimator.ofPropertyValuesHolder(this, rotationProperty)
            .apply {
                duration = animDuration
            }
        tag = targetRotation

        animator.start()
    }


    fun View.rotateInfinity(context: Context) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotateinfi)
        this.startAnimation(animation)
    }



    fun View.blink(context: Context) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.blink)
        this.startAnimation(animation)
    }

    fun View.rotateInfinityReverse(context: Context) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotateinfirev)
        this.startAnimation(animation)
    }

    fun View.animFadeOut(context: Context, animDuration: Long = 1500L) = run {
        this.clearAnimation()
        val animation =
            AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_out)
                .apply {
                    duration = animDuration
                }
        this.startAnimation(animation)

    }

    fun View.bounce(context: Context, animDuration: Long = 500L) = run {
        this.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
            .apply {
                duration = animDuration
            }
        this.startAnimation(animation)
    }

    private const val SHORT_HAPTIC_FEEDBACK_DURATION = 5L


    fun View.setOnClickThrottleBounceListener(throttleTime: Long = 600L, onClick: () -> Unit) {

        this.setOnClickListener(object : View.OnClickListener {

            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                context?.let {
                    v.bounce(context)
                    if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) return
                    else onClick()
                    lastClickTime = SystemClock.elapsedRealtime()
                }

            }
        })
    }



    fun View.setOnClickSingleTimeBounceListener(onClick: () -> Unit) {

        this.setOnClickListener(object : View.OnClickListener {
            private var clicked: Boolean = false
            override fun onClick(v: View) {
                //context.performHapticFeedback()
                v.bounce(context)
                if (clicked) return
                else onClick()
                clicked = true
            }
        })
    }

    inline fun View.setOnClickFadeInListener(crossinline onClick: () -> Unit) {
        setOnClickListener {
            // context.performHapticFeedback()
            it.animFadein(context, 100)
            onClick()
        }
    }


    fun View.setSingleClickListener(throttleTime: Long = 600L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }



}