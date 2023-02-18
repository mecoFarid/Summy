package com.mecofarid.summy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mecofarid.summy.screens.game.GameFragment

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