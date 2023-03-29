package com.mujapps.jetpackcapstone.screens.splash

import android.view.animation.OvershootInterpolator
import android.widget.Space
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mujapps.jetpackcapstone.components.ReaderLogo
import com.mujapps.jetpackcapstone.navigation.ReaderScreens
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavController) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        delay(2000L)
        //Navigation after 2 seconds
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(ReaderScreens.LoginScreen.name)
        } else {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }

        //navController.navigate(ReaderScreens.LoginScreen.name)
    }

    Surface(
        modifier = Modifier
            .padding(16.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White, border = BorderStroke(2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Read, Save, Enjoy",
                style = MaterialTheme.typography.h5,
                color = Color.Green.copy(alpha = 0.5f)
            )
        }
    }
}