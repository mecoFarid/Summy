package com.mecofarid.summy.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mecofarid.summy.R
import com.mecofarid.summy.screens.main.GameFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    if (savedInstanceState == null)
      addGameFragment()
  }

  private fun addGameFragment(){
    supportFragmentManager.beginTransaction()
      .replace(R.id.container, GameFragment.newInstance())
      .commitNow()
  }
}