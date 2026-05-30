package com.cec.launcher

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cec.launcher.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enterImmersiveMode()
        setupChakraPicker()
        setupAbout()

        binding.btnBack.setOnClickListener { finish() }
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
        )
    }

    private fun setupChakraPicker() {
        val prefs = getSharedPreferences("cec_prefs", MODE_PRIVATE)
        val currentChakra = prefs.getInt("chakra_theme", 6)

        val radioButtons = listOf(
            binding.radioRoot, binding.radioSacral, binding.radioSolar,
            binding.radioHeart, binding.radioThroat, binding.radioThirdEye,
            binding.radioCrown
        )

        radioButtons.getOrNull(currentChakra)?.isChecked = true

        radioButtons.forEachIndexed { index, rb ->
            val chakra = ChakraTheme.getChakra(index)
            rb.text = "${chakra.name} · ${chakra.hz}"
            rb.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    prefs.edit().putInt("chakra_theme", index).apply()
                    updateAccentPreview(ChakraTheme.getAccentColor(index))
                    Toast.makeText(
                        this,
                        "${chakra.name} chakra activated · ${chakra.hz}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateAccentPreview(color: Int) {
        binding.accentPreview.setBackgroundColor(color)
    }

    private fun setupAbout() {
        binding.tvAppVersion.text = "CEC Launcher v1.0"
        binding.tvAboutText.text =
            "Consciousness Evolution Center\n\nA sacred digital sanctuary for your awakening journey. " +
            "Built with love and intention.\n\nFrequency · Geometry · Presence"
    }

    override fun onResume() {
        super.onResume()
        enterImmersiveMode()
    }
}
