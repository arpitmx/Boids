package com.bitpolarity.boids

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bitpolarity.boids.BoidView.BoidView
import com.bitpolarity.boids.ExtensionsUtil.animSlideDown
import com.bitpolarity.boids.ExtensionsUtil.animSlideUp
import com.bitpolarity.boids.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BoidView.Updates {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mediaPlayer: MediaPlayer? = null
    var isSettingVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContentView(binding.root)
        setMediaPlayer()
        setViews()
    }

    private fun hideSystemUI() {

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun setMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this,R.raw.bgm)
        mediaPlayer?.isLooping = true
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }

    }

    val MAX_VIEW_RADIUS = 600
    val MAX_SEPERATION = 300
    val MAX_COHESION = 100
    val MAX_BOID_RADIUS = 20

    private fun setViews() {


        setSettingVisibility(isSettingVisible)
        val boidView = binding.boidView

        binding.tvBoidCount.setTextColor(this.getColor(R.color.boidCounter))

        binding.seekBarViewRadius.progress = ((boidView.VIEWRADIUS / MAX_VIEW_RADIUS) * 100F).toInt()
        binding.seekBarSeperation.progress = ((boidView.SEPERATION / MAX_SEPERATION) * 100F).toInt()
        binding.seekBarCohesion.progress = ((boidView.COHESION / MAX_COHESION) * 100F).toInt()
        binding.boidRadiusSeekbar.progress = ((boidView.BOID_RADIUS / MAX_BOID_RADIUS) * 100F).toInt()

        binding.tvViewRadius.text = "View Radius : ${boidView.VIEWRADIUS}"
        binding.tvSeperationForce.text = "Separation Force : ${boidView.SEPERATION}"
        binding.tvCohesionForce.text = "Cohesion Force : ${boidView.COHESION}"
        binding.tvboidRadius.text = "Boid Radius : ${boidView.BOID_RADIUS}"

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


        binding.boidRadiusSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val metrics = convertProgressToValue(progress.toFloat(), MAX_BOID_RADIUS.toFloat())
                binding.tvboidRadius.text = "Boid Radius: ${metrics}"
                boidView.BOID_RADIUS = metrics
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

        })

        // Reset button
        binding.resetButton.setOnClickListener{
            boidView.reset()
        }

        // Setting button
        binding.settingButton.setOnClickListener {
                if(isSettingVisible){
                    setSettingVisibility(false)
                }else {
                    setSettingVisibility(true)
                }
        }

        // Vertex visibility
        binding.vertexVisibleSwitch.setOnCheckedChangeListener{_, isChecked ->
            boidView.verticesVisible = isChecked
        }

        // Spawn mode
        binding.switch1.setOnCheckedChangeListener{_,isChecked ->
            if (isChecked){
                boidView.ishunterMode = true
                binding.switch1.text = "Hunter"
            }else {
                boidView.ishunterMode = false
                binding.switch1.text = "Bird"

            }
        }


        // Wall Mode
        binding.wallModeSwitch.setOnCheckedChangeListener{_,isChecked ->
            boidView.isWallMode = isChecked
        }

        // Border mode
        binding.borderSwitch.setOnCheckedChangeListener{_, isChecked ->
            boidView.BORDER_MODE = isChecked
        }

    }

    override fun onResume() {
        super.onResume()

        hideSystemUI()
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }

    }
    override fun onPause() {
        super.onPause()

        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }


    fun setSettingVisibility(flag: Boolean) {
        if (flag) {

            binding.settingLayout.animSlideUp(this,200)
            binding.settingLayout.visibility = View.VISIBLE
            isSettingVisible = true

        } else {
            binding.settingLayout.animSlideDown(this,200)
            binding.settingLayout.visibility = View.GONE
            isSettingVisible = false

        }

    }

    fun convertProgressToValue(progress: Float, maxValue: Float): Float {
        return ((progress * maxValue) / 100F)
    }

    val MAXBOIDCOUNT = 1000

    override fun getBoidCount(count: Int) {
        binding.tvBoidCount.text = count.toString()

        if (count> MAXBOIDCOUNT){

                for(i in 0..50)
                    binding.boidView.boids.removeAt(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}





