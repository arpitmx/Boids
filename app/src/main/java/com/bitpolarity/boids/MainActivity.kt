package com.bitpolarity.boids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bitpolarity.boids.BoidView.BoidView
import com.bitpolarity.boids.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
       // val boidView = binding.boidView

    }
}