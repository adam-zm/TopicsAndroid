package com.example.topics.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import com.example.topics.SharedStateHandler
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.topics.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = koinViewModel(),
    sharedStateHandler: SharedStateHandler = koinInject<SharedStateHandler>(),
    navController: NavController
) {
    val uiState by sharedStateHandler.uiState.collectAsState()

    val insets = WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
    var statusBarHeight by remember {
        mutableStateOf(90.dp + insets.calculateBottomPadding())
    }
    val animatedStatusBarHeight by animateDpAsState(statusBarHeight)
    val haptic = LocalHapticFeedback.current

    val statusBarMinHeight = 90.dp + insets.calculateBottomPadding()
    var statusBarMaxHeight = 0.dp

    with(LocalDensity.current) {
        statusBarMaxHeight = LocalWindowInfo.current.containerSize.height.toDp() - 100.dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val haptic = LocalHapticFeedback.current
            val sliderState = remember { mutableFloatStateOf(0.4F) }

            Text(
                "Settings",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Slider(
                value = sliderState.floatValue,
                onValueChange = { float ->
                    sliderState.floatValue = float
                    if(uiState.useHapticFeedback){
                        haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Surface(
                modifier = Modifier
                    .height(animatedStatusBarHeight)
                    .fillMaxWidth()
                    .clipToBounds(),
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = insets.calculateBottomPadding(), top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier
                                .height(5.dp)
                                .width(40.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            val dragDp = with(density) { -dragAmount.y.toDp() }
                                            statusBarHeight = (statusBarHeight + dragDp)
                                                .coerceIn(
                                                    statusBarMinHeight,
                                                    statusBarMaxHeight
                                                )
                                        },
                                        onDragEnd = {
                                            if (statusBarHeight > statusBarMinHeight && statusBarHeight < statusBarMaxHeight) {
                                                if (statusBarHeight > statusBarMaxHeight / 2) {
                                                    statusBarHeight = statusBarMaxHeight
                                                    if(uiState.useHapticFeedback){
                                                        haptic.performHapticFeedback(
                                                            HapticFeedbackType.GestureEnd
                                                        )
                                                    }
                                                } else {
                                                    statusBarHeight = statusBarMinHeight
                                                    if(uiState.useHapticFeedback){
                                                        haptic.performHapticFeedback(
                                                            HapticFeedbackType.GestureEnd
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                },
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize())
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            navController.navigate(Routes.HomeScreen) {
                                                popUpTo(Routes.HomeScreen)
                                            }
                                        }
                                    )
                                    .width(50.dp)
                                    .height(55.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Home,
                                    "Home",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(45.dp)
                                        .padding(bottom = 7.dp)
                                )
                                if(uiState.currentScreen == 0){
                                    Surface(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(15.dp)
                                            .align(Alignment.BottomCenter),
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(30.dp)
                                    ){
                                        Box(modifier = Modifier.fillMaxSize())
                                    }
                                }
//                            Text("Home", modifier = Modifier.align(Alignment.BottomCenter))
                            }
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            navController.navigate(Routes.SettingsScreen) {
                                                popUpTo(Routes.SettingsScreen)
                                            }
                                        }
                                    )
                                    .width(50.dp)
                                    .height(55.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Settings,
                                    "Settings",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(45.dp)
                                        .padding(bottom = 7.dp)
                                )
                                if(uiState.currentScreen == 1){
                                    Surface(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(15.dp)
                                            .align(Alignment.BottomCenter),
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(30.dp)
                                    ){
                                        Box(modifier = Modifier.fillMaxSize())
                                    }
                                }
//                            Text("Settings", modifier = Modifier.align(Alignment.BottomCenter))
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .clipToBounds(),
                            color = MaterialTheme.colorScheme.onBackground,
                            thickness = 3.dp,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp, vertical = 20.dp)
                                .clipToBounds(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = "Quick settings",
                                style = TextStyle(
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp)
                                .clipToBounds(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Switch(
                                onCheckedChange = { isChecked ->
                                    viewModel.toggleUseHapticFeedback(isChecked)
                                    if(!uiState.useHapticFeedback){
                                        haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                    }
                                },
                                checked = uiState.useHapticFeedback,
                            )
                            Text(
                                text = "Use haptic feedback",
                                style = TextStyle(
                                    fontSize = 19.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}