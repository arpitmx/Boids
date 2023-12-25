package com.bitpolarity.boids.BoidView.Helper
import kotlin.math.sqrt


class Vector2D(var x: Float, var y: Float) {

    // Basic vector operations

    fun add(otherX: Float, otherY: Float) {
        x += otherX
        y += otherY
    }

    fun subtract(otherX: Float, otherY: Float) {
        x -= otherX
        y -= otherY
    }

    fun divide(value: Float) {
        if (value != 0.0f) {
            x /= value
            y /= value
        }
    }

    fun magnitude(): Float {
        return sqrt(x * x + y * y)
    }



    fun normalize() {
        val mag = magnitude()
        if (mag != 0.0f) {
            divide(mag)
        }
    }

    fun limit(max: Float) {
        val mag = magnitude()
        if (mag > max && mag != 0.0f) {
            divide(mag / max)
        }
    }

    fun isZero(): Boolean {
        return x == 0.0f && y == 0.0f
    }

    companion object {

        // Additional vector operations

        fun magnitude(otherX: Float, otherY: Float): Float {
            return sqrt((otherX*otherX) + (otherY*otherY))

        }
        fun subtract(v1: Vector2D, v2: Vector2D): Vector2D {
            return Vector2D(v1.x - v2.x, v1.y - v2.y)
        }

        fun dist(v1: Vector2D, v2: Vector2D): Float {
            val dx = v2.x - v1.x
            val dy = v2.y - v1.y
            return sqrt(dx * dx + dy * dy)
        }
    }
}