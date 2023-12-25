package com.bitpolarity.boids.BoidView

import android.util.DisplayMetrics
import android.util.Log
import com.bitpolarity.boids.BoidView.Helper.Vector2D
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Boid(var posX: Float, var posY: Float, var velocityX: Float, var velocityY: Float) {


    val displayMetrics = DisplayMetrics()

    private val SCREEN_WIDTH by lazy { displayMetrics.widthPixels }
    private val SCREEN_HEIGHT by lazy { displayMetrics.heightPixels }

    ////Metrics
    val SEPERATION = 25.0F

    companion object {
        const val MAX_SPEED = 8.0f
        const val MAX_FORCE = 2f
        const val TAG = "BoidDebug"
    }


    fun updateAligned(align : Vector2D){

        val combinedForce = Vector2D(align.x, align.y)
        combinedForce.limit(MAX_FORCE)

        velocityX += combinedForce.x
        velocityY += combinedForce.y

        limitSpeed()

        posX += velocityX
        posY += velocityY
    }

    fun update(align: Vector2D) {

        val combinedForce = Vector2D(align.x, align.y)
        combinedForce.limit(MAX_FORCE)

        Log.d(TAG, "Combined force -> X : ${align.x} Y : ${align.y}")

        velocityX += combinedForce.x
        velocityY += combinedForce.y

        Log.d(TAG, "Velocity -> X : ${velocityX} Y : ${velocityY}")


        limitSpeed()

        posX += velocityX
        posY += velocityY

    }


    private fun limitSpeed() {
        // Limit the velocity to a maximum speed
        val speed = Vector2D.magnitude(velocityX, velocityY)
        if (speed > MAX_SPEED) {
            velocityX = (velocityX.div(speed)) * MAX_SPEED
            velocityY = (velocityY.div(speed)) * MAX_SPEED
        }
    }


}