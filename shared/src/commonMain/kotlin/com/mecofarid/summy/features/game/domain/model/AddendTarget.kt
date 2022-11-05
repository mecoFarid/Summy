package com.mecofarid.summy.features.game.domain.model

data class AddendTarget(
  val addends: Collection<Int> = emptyList(),
  val target: Int  = 0
)