package com.mecofarid.summy.screens.main

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mecofarid.summy.R
import com.mecofarid.summy.databinding.AlertDialogBinding

class ResultDialogFragment : DialogFragment() {

  companion object {
    const val TAG = "AlertDialogFragment"

    fun newInstance(
      screenState: GameViewModel.ScreenState,
      onDismiss: () -> Unit
    ): ResultDialogFragment {
      return ResultDialogFragment().also {
        it.onDismiss = onDismiss
        it.screenState = screenState
      }
    }
  }

  private lateinit var binding: AlertDialogBinding
  private lateinit var screenState: GameViewModel.ScreenState
  private lateinit var onDismiss: () -> Unit

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.Widget_Summy_DialogTheme)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    super.onCreateView(inflater, container, savedInstanceState)
    AlertDialogBinding.inflate(inflater, container, false).also { binding = it }
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    updateTitle()
    updateImage()
    updateButton()
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    onDismiss()
  }

  private fun updateTitle(){
    binding.title.apply {
      setText(getTitle())
      setTextColor(getStateColor())
    }
  }

  private fun getTitle() =
    when(screenState){
      is GameViewModel.ScreenState.Failed -> R.string.alert_failure
      is GameViewModel.ScreenState.Succeeded -> R.string.alert_success
      GameViewModel.ScreenState.Running,
      GameViewModel.ScreenState.Loading -> throw IllegalStateException()
    }

  private fun updateImage(){
    val imageResId = when(screenState){
      is GameViewModel.ScreenState.Failed -> R.drawable.failure
      is GameViewModel.ScreenState.Succeeded -> R.drawable.success
      GameViewModel.ScreenState.Running,
      GameViewModel.ScreenState.Loading -> throw IllegalStateException()
    }

    binding.image.setImageResource(imageResId)
  }

  private fun updateButton(){
    binding.button.apply {
      setOnClickListener { dismiss() }
      backgroundTintList = ContextCompat.getColorStateList(requireContext(), getStateColor())
    }
  }

  private fun getStateColor() =
    when(screenState){
      is GameViewModel.ScreenState.Failed -> R.color.color_alert_failure_color
      is GameViewModel.ScreenState.Succeeded -> R.color.color_alert_success_color
      GameViewModel.ScreenState.Running,
      GameViewModel.ScreenState.Loading -> throw IllegalStateException()
    }
}
