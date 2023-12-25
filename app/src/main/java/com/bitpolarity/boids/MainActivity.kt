package com.bitpolarity.boids

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bitpolarity.boids.BoidView.BoidView
import com.bitpolarity.boids.databinding.ActivityMainBinding
import java.util.concurrent.BlockingDeque

class MainActivity : AppCompatActivity(), BoidView.Updates {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    var isSettingVisible = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    val MAX_VIEW_RADIUS = 600
    val MAX_SEPERATION = 100
    val MAX_COHESION = 300

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViews() {
        val boidView = binding.boidView
        binding.seekBarViewRadius.progress = ((boidView.VIEWRADIUS / MAX_VIEW_RADIUS) * 100).toInt()
        binding.seekBarSeperation.progress = ((boidView.SEPERATION / MAX_SEPERATION) * 100).toInt()
        binding.seekBarViewRadius.progress = ((boidView.VIEWRADIUS / MAX_COHESION) * 100).toInt()

        binding.tvViewRadius.text = "View Radius : ${boidView.VIEWRADIUS}"
        binding.tvSeperationForce.text = "Separation Force : ${boidView.SEPERATION}"
        binding.tvCohesionForce.text = "Cohesion Force : ${boidView.VIEWRADIUS}"

        binding.seekBarViewRadius.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val metrics = convertProgressToValue(progress.toFloat(), MAX_VIEW_RADIUS.toFloat())
                binding.tvViewRadius.text = "View Radius : ${metrics}"
                boidView.VIEWRADIUS = metrics
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

        })


        binding.seekBarSeperation.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val metrics = convertProgressToValue(progress.toFloat(), MAX_SEPERATION.toFloat())
                binding.tvSeperationForce.text = "Seperation Force : ${metrics}"
                boidView.SEPERATION = metrics
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

        })



        binding.seekBarCohesion.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val metrics = convertProgressToValue(progress.toFloat(), MAX_COHESION.toFloat())
                binding.tvCohesionForce.text = "Cohesion Force: ${metrics}"
                boidView.COHESION = metrics
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

        })

        binding.resetButton.setOnClickListener{
            boidView.reset()
        }

        binding.settingButton.setOnClickListener {
                if(isSettingVisible){
                    setSettingVisibility(false)
                    boidView.isWallMode = true
                }else {
                    setSettingVisibility(true)
                    boidView.isWallMode = false
                }

        }

        binding.vertexVisibleSwitch.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                boidView.verticesVisible = true
            }else {
                boidView.verticesVisible = false
            }

        }

        binding.switch1.setOnCheckedChangeListener{_,isChecked ->
            if (isChecked){
                boidView.ishunterMode = true
                binding.switch1.text = "Hunter Spawn"
            }else {
                boidView.ishunterMode = false
                binding.switch1.text = "Bird Spawn"

            }
        }


    }

    fun setSettingVisibility(flag: Boolean) {
        if (flag) {
            binding.settingLayout.visibility = View.VISIBLE
            isSettingVisible = true
        } else {

            binding.settingLayout.visibility = View.GONE
            isSettingVisible = false

        }

    }

    fun convertProgressToValue(progress: Float, maxValue: Float): Float {
        // Calculate actual value based on progress, minValue, and maxValue
        return (progress * maxValue / 100F)
    }

    val MAXBOIDCOUNT = 1000

    override fun getBoidCount(count: Int) {
        binding.tvBoidCount.text = count.toString()

        if (count> MAXBOIDCOUNT){

                for(i in 0..50)
                    binding.boidView.boids.removeAt(i)

        }
    }
}





