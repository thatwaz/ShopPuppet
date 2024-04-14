package com.thatwaz.shoppuppet.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.thatwaz.shoppuppet.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Start fade-in animation
        val imageView = findViewById<ImageView>(R.id.splash_image)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imageView.startAnimation(fadeInAnimation)

        // Delay for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity after the delay
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 1000) // 1000 ms for 1 second delay

    }
}
