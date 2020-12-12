package com.example.buttonbutton

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.buttonbutton.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val sharedPreferences = this.getSharedPreferences("button_button_prefs", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("user_blocked", false)) showBlocked()  else showButton()

        binding.button.setOnClickListener {
            showLoading()

            GlobalScope.launch(context = Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    binding.loadingMessage.text = getString(R.string.searching_user)
                }

                Thread.sleep(5000)
                withContext(Dispatchers.Main) {
                    binding.loadingMessage.text = getString(R.string.blocking_user)
                }

                Thread.sleep(4000)
                withContext(Dispatchers.Main) {
                    binding.loadingMessage.text = getString(R.string.user_blocked)
                }
                Thread.sleep(6000)

                sharedPreferences.edit().putBoolean("user_blocked", true).apply()
                withContext(Dispatchers.Main) {
                    showBlocked()
                }
            }
        }

        setContentView(binding.root)
    }

    private fun showLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
        binding.blockedLayout.visibility = View.GONE
        binding.buttonLayout.visibility = View.GONE
    }

    private fun showBlocked() {
        val shakeAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.shake)

        binding.blockedLayout.startAnimation(shakeAnimation)
        binding.blockedLayout.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        binding.buttonLayout.visibility = View.GONE
    }

    private fun showButton() {
        binding.buttonLayout.visibility = View.VISIBLE
        binding.blockedLayout.visibility = View.GONE
        binding.loadingLayout.visibility = View.GONE
    }

}