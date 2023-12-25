package com.bitpolarity.boids.BoidView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bitpolarity.boids.BoidView.Helper.Vector2D
import com.bitpolarity.boids.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class BoidView(context: Context) : View(context) {

    //var boid: Boid
    private val boidDrawable: Drawable?
    val paintBall = Paint().apply {
        color = Color.DKGRAY

    }

    val paintLine = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
    }

    var boids: MutableList<Boid> = mutableListOf<Boid>()

    init {
        boids.clear()
        boidDrawable = ContextCompat.getDrawable(context, R.drawable.boid)
        handleClicks()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleClicks() {

        setOnTouchListener { v, event ->
            val posx = event.x
            val posy = event.y

            val newBoid = Boid(
                posx,
                posy,
                (Random.nextFloat() - 0.5f) * INITIAL_SPEED,
                (Random.nextFloat() - 0.5f) * INITIAL_SPEED
            )
            boids.add(newBoid)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (boid in boids) {
            boid.update(drawNearbyLines(canvas, boid, boids))
            drawBoid(canvas, boid)
        }

        invalidate()
    }

    val VIEWRADIUS = 100.0f
    val SEPERATION = 2.0f
    val COHESION = 10.0f

    private fun drawNearbyLines(canvas: Canvas, boid: Boid, boids: MutableList<Boid>): Vector2D {
        val avgVelocityVector = Vector2D(0f, 0f)
        var count = 0
        for (nearbyBoid in boids) {

            val vecThis = Vector2D(boid.posX, boid.posY)
            val vecNearby = Vector2D(nearbyBoid.posX, nearbyBoid.posY)
            val distance = Vector2D.dist(vecThis, vecNearby)

            //Alignment
            if (vecNearby != vecThis && distance > 0 && distance < VIEWRADIUS) {
                avgVelocityVector.add(nearbyBoid.velocityX, nearbyBoid.velocityY)
                canvas.drawLine(boid.posX, boid.posY, nearbyBoid.posX, nearbyBoid.posY, paintLine)
                count++
            }

            //Seperation
            if (vecNearby != vecThis && distance > 0 && distance <= SEPERATION){
                avgVelocityVector.add(vecThis.x.minus(vecNearby.x), vecThis.y.minus(vecNearby.y))
            }

            //Cohesion
            if (vecNearby != vecThis && distance > 0 && distance <= COHESION){
                avgVelocityVector.add(vecNearby.x,vecNearby.y)
            }
        }

        if (count > 0) {
            avgVelocityVector.divide(count.toFloat())
        }

        Log.d("AvgVelocity", "x :${avgVelocityVector.x} y : ${avgVelocityVector.y}")

        return avgVelocityVector

    }

    private fun drawBoid(canvas: Canvas, boid: Boid) {
        Log.d(TAGGlobal, "Pos X : ${boid.posX}, Pos Y : ${boid.posY}")

        limitBoidToScreen(boid, canvas.height.toFloat(), canvas.width.toFloat())
        canvas.drawCircle(boid.posX, boid.posY, BOID_RADIUS, paintBall)

        }

    val THREASHOLD = 100

    private fun limitBoidToScreen(boid: Boid, canvasHeight: Float, canvasWidth: Float) {

        Log.d(TAGScreenLimit, "px : ${boid.posX} py : ${boid.posY} H : $canvasHeight W : $canvasWidth"
        )
//
//        if (boid.posX < THREASHOLD) boid.velocityX = -boid.velocityX
//        if (boid.posX > canvasWidth - THREASHOLD) boid.velocityX = -boid.velocityX
//        if (boid.posY < THREASHOLD) boid.velocityY = -boid.velocityY
//        if (boid.posY > canvasHeight - THREASHOLD) boid.velocityY = -boid.velocityY


        if (boid.posX < 0) boid.posX = canvasWidth
        if (boid.posX > canvasWidth) boid.posX = 0f
        if (boid.posY < 0) boid.posY = canvasHeight
        if (boid.posY > canvasHeight) boid.posY = 0F

    }


    companion object{
        private const val INITIAL_SPEED = 20.0f
        private const val TRIANGLE_SIZE = 30
        private const val BOID_RADIUS = 8f
        private const val TAGGlobal = "BoidViewGlobal"
        private const val TAGScreenLimit = "BoidViewScreenLimit"
    }
}



    //         boidDrawable?.let {
//            it.setBounds(
//                (boid.posX - TRIANGLE_SIZE / 2).toInt(),
//                (boid.posY - TRIANGLE_SIZE / 2).toInt(),
//                (boid.posX + TRIANGLE_SIZE / 2).toInt(),
//                (boid.posY + TRIANGLE_SIZE / 2).toInt()
//            )
//            it.draw(canvas)
//        }




//        boidDrawable?.let {
//            it.setBounds(
//                (boid.posX - TRIANGLE_SIZE / 2).toInt(),
//                (boid.posY - TRIANGLE_SIZE / 2).toInt(),
//                (boid.posX + TRIANGLE_SIZE / 2).toInt(),
//                (boid.posY + TRIANGLE_SIZE / 2).toInt()
//            )
//            it.draw(canvas!!)
//        }