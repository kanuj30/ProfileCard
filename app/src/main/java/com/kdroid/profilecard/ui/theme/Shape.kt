package com.kdroid.profilecard.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
)
val CutCornerShape = Shapes(
    small = CutCornerShape(topEnd = 10.dp, bottomEnd = 0.dp, topStart = 0.dp, bottomStart = 0.dp),
    medium = CutCornerShape(topEnd = 15.dp),
    large = CutCornerShape(topEnd = 20.dp)
)