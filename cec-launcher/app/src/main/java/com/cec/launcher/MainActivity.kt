package com.cec.launcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cec.launcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDrawerFragment: AppDrawerFragment
    private var isDrawerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enterImmersiveMode()
        applyChakraTheme()
        setupHomeScreen()
        setupAppDrawer()
        setupButtons()
    }

    private fun enterImmersiveMode() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }

    private fun applyChakraTheme() {
        val prefs = getSharedPreferences("cec_prefs", MODE_PRIVATE)
        val chakraIndex = prefs.getInt("chakra_theme", 6)
        val accentColor = ChakraTheme.getAccentColor(chakraIndex)
        binding.mandalaView.setAccentColor(accentColor)
        binding.breathingOrb.setOrbColor(accentColor)
        binding.fabAppDrawer.backgroundTintList =
            android.content.res.ColorStateList.valueOf(accentColor)
    }

    private fun setupHomeScreen() {
        binding.frequencyClock.start()
        binding.breathingOrb.startBreathing()
        binding.dailyIntention.text = Affirmations.getTodayAffirmation()
    }

    private fun setupAppDrawer() {
        appDrawerFragment = AppDrawerFragment()
        supportFragmentManager.beginTransaction()
            .add(binding.drawerContainer.id, appDrawerFragment)
            .commit()
        binding.drawerContainer.visibility = View.GONE
    }

    private fun setupButtons() {
        binding.fabAppDrawer.setOnClickListener {
            if (isDrawerOpen) closeDrawer() else openDrawer()
        }
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.drawerDimOverlay.setOnClickListener {
            closeDrawer()
        }
    }

    private fun openDrawer() {
        isDrawerOpen = true
        binding.drawerContainer.visibility = View.VISIBLE
        binding.drawerDimOverlay.visibility = View.VISIBLE
        binding.drawerContainer.startAnimation(
            android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up)
        )
        binding.drawerDimOverlay.animate().alpha(1f).setDuration(300).start()
    }

    private fun closeDrawer() {
        isDrawerOpen = false
        val anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_down)
        anim.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(a: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(a: android.view.animation.Animation?) {}
            override fun onAnimationEnd(a: android.view.animation.Animation?) {
                binding.drawerContainer.visibility = View.GONE
                binding.drawerDimOverlay.visibility = View.GONE
                binding.drawerDimOverlay.alpha = 0f
            }
        })
        binding.drawerContainer.startAnimation(anim)
        binding.drawerDimOverlay.animate().alpha(0f).setDuration(300).start()
    }

    override fun onResume() {
        super.onResume()
        enterImmersiveMode()
        applyChakraTheme()
        binding.mandalaView.resume()
        binding.breathingOrb.resume()
        binding.frequencyClock.start()
    }

    override fun onPause() {
        super.onPause()
        binding.mandalaView.pause()
        binding.breathingOrb.pause()
        binding.frequencyClock.stop()
    }

    override fun onBackPressed() {
        if (isDrawerOpen) closeDrawer()
        // Launchers should not exit on back press
    }
}
