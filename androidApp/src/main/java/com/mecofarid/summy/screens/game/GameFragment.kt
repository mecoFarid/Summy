package com.mecofarid.summy.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.mecofarid.summy.R
import com.mecofarid.summy.common.ext.observeAsLiveData
import com.mecofarid.summy.common.utils.formatTime
import com.mecofarid.summy.databinding.LayoutAddendBinding
import com.mecofarid.summy.databinding.MainFragmentBinding
import com.mecofarid.summy.features.game.data.source.local.GameplayLocalDatasource
import com.mecofarid.summy.features.game.domain.interactor.GetGameStateInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetGameplayInteractor
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.features.game.utils.MINUTE_SECOND_PATTERN
import java.util.*

class GameFragment : Fragment() {

  companion object {
    fun newInstance() = GameFragment()
  }

  private val viewModel = GameViewModel(
    GetGameplayInteractor(GameplayLocalDatasource()),
    GetGameStateInteractor(),
  )
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

  private fun startObserving() {
    observeElapsedTime()
    observeAddendTarget()
    observeSumAddendCounter()
    observeScreenState()
  }

  private fun observeElapsedTime() {
    viewModel.elapsedTime.observeAsLiveData(viewLifecycleOwner) {
      binding.elapsedTime.text = it.formatTime(MINUTE_SECOND_PATTERN, Locale.getDefault())
    }
  }

  private fun observeAddendTarget() {
    viewModel.gameplay.observeAsLiveData(viewLifecycleOwner) {
      binding.target.text = it.target.toString()
      updateAddends(it.addends)
    }
  }

  private fun observeSumAddendCounter() {
    viewModel.gameProgress.observeAsLiveData(viewLifecycleOwner) {
      binding.apply {
        sum.startAnimation(overShootAnimation)
        sum.text = it.sum.toString()
        moveCounter.text = it.moveCounter.toString()
      }
    }
//    viewModel.gameProgess.observeAsLiveData(viewLifecycleOwner) {
//      binding.apply {
//        sum.startAnimation(overShootAnimation)
//        sum.text = it.sum.toString()
//        moveCounter.text = it.moveCounter.toString()
//      }
//    }
  }

  private fun observeScreenState() {
    viewModel.screenState.observeAsLiveData(viewLifecycleOwner) {
      if (!it.isGameFinished())
        return@observeAsLiveData

      val dialog = GameResultDialogFragment.newInstance(it) {
        viewModel.restartGame()
      }
      dialog.show(parentFragmentManager, GameResultDialogFragment.TAG)
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

  private fun generateAddendView(addend: Int): View {
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
