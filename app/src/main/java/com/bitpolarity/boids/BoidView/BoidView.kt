package com.bitpolarity.boids.BoidView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bitpolarity.boids.BoidView.Helper.Vector2D
import com.bitpolarity.boids.R
import kotlin.random.Random


class BoidView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {


    private var updates : Updates


    //var boid: Boid
    private val boidDrawable: Drawable?
    val birdBall = Paint().apply {
        color = context.getColor(R.color.boidColor)
    }

    val hunterBall = Paint().apply {
        color = Color.RED
    }

    val wallPaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 10f
    }

    val paintLine = Paint().apply {
        color = context.getColor(R.color.boidColor)
        strokeWidth = 0.8f
    }

    var boids: MutableList<Boid> = mutableListOf<Boid>()
    var hunters: MutableList<Boid> = mutableListOf<Boid>()
    var walls: MutableList<Boid> = mutableListOf<Boid>()
    var verticesVisible = false
    var ishunterMode = false
    var isWallMode = false

    init {
        updates = context as Updates
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

            if (isWallMode){
                val wall = Boid(
                    posx,
                    posy,
                    0F,
                    0F
                )
                walls.add(wall)
            }else {
                if (ishunterMode){
                    hunters.add(newBoid)
                }else if (!ishunterMode){
                    boids.add(newBoid)
                }
            }


            updates.getBoidCount(boids.size)
            true
        }
    }


    fun reset(){
        boids.clear()
        hunters.clear()
        walls.clear()
        updates.getBoidCount(0)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (boid in boids) {
            boid.update(drawNearbyLinesBoids(canvas, boid))
            drawBoid(canvas, boid)
        }

        for (hunter in hunters){
            hunter.update(drawNearbyLinesHunter(canvas, hunter))
            drawHunter(canvas, hunter)
        }

        for (wall in walls){
            drawWall(canvas, wall)
        }

        invalidate()
    }

    var VIEWRADIUS = 100.0f
    var SEPERATION = 2.0f
    var COHESION = 10.0f
    var MIN_BOID_SAFE = 5
    var WALLSEPERATION = 100F
    var BOID_RADIUS = 8f
    var BORDER_MODE = false


    var SEPERATION_HUNTERS = 200F
    var ALIGNMENT_HUNTERS = 100f



    private fun drawNearbyLinesBoids(canvas: Canvas, boid: Boid): Vector2D {

        val avgVelocityVector = Vector2D(0f, 0f)
        var count = 0


        fun processNearbyBoid(nearbyBoid: Boid) {
            avgVelocityVector.add(nearbyBoid.velocityX, nearbyBoid.velocityY)
            if (verticesVisible) {
                canvas.drawLine(boid.posX, boid.posY, nearbyBoid.posX, nearbyBoid.posY, paintLine)
            }
            count++
        }

        for (nearbyBoid in boids) {
            val distance = Vector2D.dist(boid.posX, boid.posY, nearbyBoid.posX, nearbyBoid.posY)

            when {
                distance > 0 && distance < VIEWRADIUS -> processNearbyBoid(nearbyBoid)
                distance > 0 && distance < SEPERATION -> avgVelocityVector.add(boid.posX - nearbyBoid.posX, boid.posY - nearbyBoid.posY)
                distance > 0 && distance < COHESION -> avgVelocityVector.add(nearbyBoid.posX, nearbyBoid.posY)
            }
        }

        if (count>0) {
            avgVelocityVector.divide(count.toFloat())
        }

        for (nearbyHunter in hunters) {

            val distance = Vector2D.dist(boid.posX, boid.posY, nearbyHunter.posX, nearbyHunter.posY)

            when {
                distance > 0 && distance <= SEPERATION_HUNTERS -> avgVelocityVector.add(boid.posX - nearbyHunter.posX, boid.posY - nearbyHunter.posY)
                distance > 0 && distance <= ALIGNMENT_HUNTERS -> {
                    if (verticesVisible) {
                        canvas.drawLine(boid.posX, boid.posY, nearbyHunter.posX, nearbyHunter.posY, paintLine)
                    }
                    if (count > MIN_BOID_SAFE) {
                        hunters.remove(nearbyHunter)
                    }
                }
            }
        }


        for (wall in walls ){
            val distance = Vector2D.dist(boid.posX, boid.posY, wall.posX, wall.posY)
            //Seperation
            if (distance > 0 && distance < WALLSEPERATION) {
                avgVelocityVector.add(boid.posX - wall.posX, boid.posY - wall.posY)
            }
        }

        //Log.d("AvgVelocity", "x :${avgVelocityVector.x} y : ${avgVelocityVector.y}")
        return avgVelocityVector

    }

    private fun drawNearbyLinesHunter(canvas: Canvas, hunter: Boid): Vector2D {
        val avgVelocityVector = Vector2D(0f, 0f)
        var count = 0
        for (nearbyBoid in boids) {

            val vecThis = Vector2D(hunter.posX, hunter.posY)
            val vecNearby = Vector2D(nearbyBoid.posX, nearbyBoid.posY)
            val distance = Vector2D.dist(vecThis, vecNearby)

            //Alignment
            if (vecNearby != vecThis && distance > 0 && distance < ALIGNMENT_HUNTERS) {
                avgVelocityVector.add(nearbyBoid.velocityX, nearbyBoid.velocityY)

                if (verticesVisible) {
                    canvas.drawLine(
                        hunter.posX,
                        hunter.posY,
                        nearbyBoid.posX,
                        nearbyBoid.posY,
                        hunterBall
                    )
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    boids.remove(nearbyBoid)
                },1000)
                updates.getBoidCount(boids.size)

                count++
            }

            //Seperation
//            if (vecNearby != vecThis && distance > 0 && distance <= SEPERATION){
//                avgVelocityVector.add(vecThis.x.minus(vecNearby.x), vecThis.y.minus(vecNearby.y))
//            }


        }

        for (wall in walls ){
            val vecThis = Vector2D(hunter.posX, hunter.posY)
            val vecNearby = Vector2D(wall.posX, wall.posY)
            val distance = Vector2D.dist(vecThis, vecNearby)


            //Seperation
            if (vecNearby != vecThis && distance > 0 && distance < WALLSEPERATION){
                avgVelocityVector.add(vecThis.x.minus(vecNearby.x), vecThis.y.minus(vecNearby.y))
            }
        }

        if (count > 0) {
            avgVelocityVector.divide(count.toFloat())
        }

        //Log.d("AvgVelocity", "x :${avgVelocityVector.x} y : ${avgVelocityVector.y}")

        return avgVelocityVector

    }



    private fun drawBoid(canvas: Canvas, boid: Boid) {
        Log.d(TAGGlobal, "Pos X : ${boid.posX}, Pos Y : ${boid.posY}")

        limitBoidToScreen(boid, canvas.height.toFloat(), canvas.width.toFloat())
        canvas.drawCircle(boid.posX, boid.posY, BOID_RADIUS, birdBall)
        }

    private fun drawHunter(canvas: Canvas, boid: Boid) {
        Log.d(TAGGlobal, "Pos X : ${boid.posX}, Pos Y : ${boid.posY}")

        limitBoidToScreen(boid, canvas.height.toFloat(), canvas.width.toFloat())
        canvas.drawCircle(boid.posX, boid.posY, BOID_RADIUS, hunterBall)
    }

    private fun drawWall(canvas: Canvas, wall: Boid) {
        canvas.drawCircle(wall.posX, wall.posY, WALL_RADIUS, wallPaint)
    }

    val THREASHOLD = 100

    private fun limitBoidToScreen(boid: Boid, canvasHeight: Float, canvasWidth: Float) {

        Log.d(TAGScreenLimit, "px : ${boid.posX} py : ${boid.posY} H : $canvasHeight W : $canvasWidth"
        )

        if (BORDER_MODE){
            if (boid.posX < THREASHOLD) boid.velocityX = -boid.velocityX
            if (boid.posX > canvasWidth - THREASHOLD) boid.velocityX = -boid.velocityX
            if (boid.posY < THREASHOLD) boid.velocityY = -boid.velocityY
            if (boid.posY > canvasHeight - THREASHOLD) boid.velocityY = -boid.velocityY

        }else{
            if (boid.posX < 0) boid.posX = canvasWidth
            if (boid.posX > canvasWidth) boid.posX = 0f
            if (boid.posY < 0) boid.posY = canvasHeight
            if (boid.posY > canvasHeight) boid.posY = 0F
        }





    }

    private fun randomFloat(
        minValue: Float,
        maxValue: Float
    ): Float {
        val random = Random
        val randomFloat_0_to_1 = random.nextFloat()

        return minValue + randomFloat_0_to_1 * (maxValue - minValue)
    }


    companion object{
        private const val INITIAL_SPEED = 150.0f
        private const val TRIANGLE_SIZE = 30
        private const val WALL_RADIUS = 12f
        private const val TAGGlobal = "BoidViewGlobal"
        private const val TAGScreenLimit = "BoidViewScreenLimit"
    }

    interface Updates{
        fun getBoidCount(count: Int)
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