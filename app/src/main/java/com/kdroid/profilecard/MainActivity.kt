package com.kdroid.profilecard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kdroid.profilecard.ui.theme.LightGreen200
import com.kdroid.profilecard.ui.theme.LightRed200
import com.kdroid.profilecard.ui.theme.ProfileCardTheme

val TAG = MainActivity::class.simpleName

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardTheme {
                UserApplication()
            }
        }
    }
}

@Composable
fun UserApplication(userProfiles: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            UserListScreen(userProfiles, navController)
        }

        composable(
            "user_details/{userId}", arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            UserProfileDetailScreen(navBackStackEntry.arguments!!.getInt("userId"), navController)
        }
    }
}

@Composable
fun UserListScreen(userProfile: List<UserProfile>, navController: NavHostController?) {
    Scaffold(topBar = { AppBar(title = "UserLists", icon = Icons.Default.Home) { } }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(userProfile) { userProfile ->
                    ProfileCard(userProfile = userProfile) {
                        navController?.navigate("user_details/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(navigationIcon = {
        Icon(icon, "Home", modifier = Modifier
            .padding(5.dp)
            .clickable { iconClickAction.invoke() })
    }, title = { Text(text = title) })
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = 5.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(5)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.drawableId, userProfile.status)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier.padding(8.dp), horizontalAlignment = alignment
    ) {

        CompositionLocalProvider(
            LocalContentAlpha provides (if (onlineStatus) 1f else ContentAlpha.medium)
        ) {
            Text(text = userName, style = MaterialTheme.typography.h5)
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (onlineStatus) "Active now" else "Offline",
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Composable
fun ProfilePicture(picUrl: String, onlineStatus: Boolean, imageSize: Dp = 72.dp) {
    Log.d(
        TAG,
        "ProfilePicture() called with: picUrl = $picUrl, onlineStatus = $onlineStatus, imageSize = $imageSize"
    )
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp
    Card(
        shape = CircleShape, border = BorderStroke(
            width = 2.dp, color = if (onlineStatus) LightGreen200 else LightRed200
        ), modifier = Modifier.padding(12.dp), elevation = 4.dp
    ) {
        SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current).data(picUrl)
            .crossfade(true).transformations(CircleCropTransformation()).build(),
            contentDescription = "user",
            modifier = Modifier
                .size(imageSize)
                .border(
                    BorderStroke(borderWidth, rainbowColorsBrush),
                    shape = CircleShape,
                )
                .padding(borderWidth)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .size(imageSize),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationView(
                        raw = R.raw.loading, modifier = Modifier.size(imageSize)
                    )
                }
            })
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current).data(picUrl).crossfade(true)
//                .transformations(CircleCropTransformation()).build(),
//            contentDescription = "user",
//            modifier = Modifier
//                .size(imageSize)
//                .border(
//                    BorderStroke(borderWidth, rainbowColorsBrush),
//                    shape = CircleShape,
//                )
//                .padding(borderWidth)
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop,
//        )


    }
}

@Composable
fun LottieAnimationView(modifier: Modifier, raw: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = LottieConstants.IterateForever,
        contentScale = ContentScale.None,
    )
}

@Composable
fun UserProfileDetailScreen(userId: Int, navController: NavHostController?) {
    Log.d(TAG, "UserProfileDetailScreen() called with: userId = $userId")
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = {
        AppBar(title = "User profile details", icon = Icons.Default.ArrowBack) {
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(userProfile.drawableId, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileCardTheme {
        UserListScreen(userProfileList, null)
    }
}


@Preview(showBackground = true)
@Composable
fun UserProfileDetailPreview() {
    ProfileCardTheme {
        UserProfileDetailScreen(userId = 1, null)
    }
}