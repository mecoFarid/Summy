package com.mecofarid.summy.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.mecofarid.summy.SummyApp
import com.mecofarid.summy.common.extensions.activityViewModelBuilder
import java.util.Locale
import android.widget.LinearLayout
import com.mecofarid.summy.R
import com.mecofarid.summy.common.utils.formatTime
import com.mecofarid.summy.databinding.LayoutAddendBinding
import com.mecofarid.summy.databinding.MainFragmentBinding
import com.mecofarid.summy.utils.MINUTE_SECOND_PATTERN

class GameFragment : Fragment() {

  companion object {
    fun newInstance() = GameFragment()
  }

  private val viewModel by activityViewModelBuilder { SummyApp.appComponent.viewModelComponent.gameViewModel }
  private lateinit var binding: MainFragmentBinding
  private val overShootAnimation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.overshoot) }
  private val bounceAnimation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.bounce) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = MainFragmentBinding.inflate(inflater, container, false)
    startObserving()
    return binding.root
  }

  private fun startObserving(){
    observeElapsedTime()
    observeAddendTarget()
    observeSumAddendCounter()
    observeScreenState()
  }

  private fun observeElapsedTime(){
    viewModel.elapsedTime.observe(viewLifecycleOwner){
      binding.elapsedTime.text = it.formatTime(MINUTE_SECOND_PATTERN, Locale.getDefault())
    }
  }

  private fun observeAddendTarget(){
    viewModel.addendTarget.observe(viewLifecycleOwner){
      binding.target.text = it.target.toString()
      updateAddends(it.addends)
    }
  }

  private fun observeSumAddendCounter(){
    viewModel.sumMoveCounter.observe(viewLifecycleOwner){
      binding.apply {
        sum.startAnimation(overShootAnimation)
        sum.text = it.sum.toString()
        moveCounter.text = it.moveCounter.toString()
      }
    }
  }

  private fun observeScreenState(){
    viewModel.screenState.observe(viewLifecycleOwner){
      if (!it.isGameFinished())
        return@observe

      val dialog = ResultDialogFragment.newInstance(it){
        viewModel.restartGame()
      }
      dialog.show(parentFragmentManager, ResultDialogFragment.TAG)
    }
  }

  private fun updateAddends(addends: Collection<Int>) {
    val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1f
    )

    binding.addendsHolder.apply {
      removeAllViews()
      addends.forEach {
        addView(generateAddendView(it), param)
      }
    }
  }

  private fun generateAddendView(addend: Int) : View{
    val binding = LayoutAddendBinding.inflate(layoutInflater, null, false)
    binding.addend.apply {
      text = addend.toString()
      setOnClickListener {
        startAnimation(bounceAnimation)
        viewModel.onAdd(addend)
      }
    }

    return binding.root
  }
}