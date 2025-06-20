package com.example.topics.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.topics.Routes
import com.example.topics.SharedStateHandler
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinViewModel(),
    sharedStateHandler: SharedStateHandler = koinInject<SharedStateHandler>()
) {
    val uiState by sharedStateHandler.uiState.collectAsState()
    val showDialogNewTopic = remember {
        mutableStateOf(false)
    }
    val state = rememberPullToRefreshState()
    val insets = WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
    val statusBarMinHeight = 90.dp + insets.calculateBottomPadding()
    var statusBarMaxHeight = 0.dp
    var statusBarHeight by remember {
        mutableStateOf(statusBarMinHeight)
    }
    val animatedStatusBarHeight by animateDpAsState(statusBarHeight)
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()

//    val hazeState = rememberHazeState()

    with(LocalDensity.current) {
        statusBarMaxHeight = LocalWindowInfo.current.containerSize.height.toDp() - 110.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
//                    .hazeSource(hazeState)
                    .pullToRefresh(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = {
                            viewModel.fetchTopics()
                            if(uiState.useHapticFeedback){
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            }
                        },
                        state = state
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {
                item(
                    key = "topPadding"
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(insets.calculateTopPadding())
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Topics",
                            modifier = Modifier
                                .padding(start = 30.dp),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 20.dp),
                            shape = RoundedCornerShape(50.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ){
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(30.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
                items(
                    uiState.topics.dropLast(1)
                ) { topic ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickable(
                                onClick = {
                                    if(uiState.useHapticFeedback){
                                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    }
                                    navController.navigate(Routes.TopicScreen(topic.id))
                                }
                            )
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    topic.title,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            val textLength = topic.text.length
                            var shortText = topic.text
                            if(textLength > 45){
                                shortText = shortText.dropLast(textLength - 45).trimEnd() + "..."
                            }
                            Text(
                                text = shortText,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 17.sp
                                )
                            )
                        }

                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                items(
                    uiState.topics
                ) { topic ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickable(
                                onClick = {
                                    if(uiState.useHapticFeedback){
                                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    }
                                    navController.navigate(Routes.TopicScreen(topic.id))
                                }
                            )
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    topic.title,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            val textLength = topic.text.length
                            var shortText = topic.text
                            if(textLength > 45){
                                shortText = shortText.dropLast(textLength - 45).trimEnd() + "..."
                            }
                            Text(
                                text = shortText,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 17.sp
                                )
                            )
                        }

                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                item(
                    key = "bottomPadding"
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(statusBarHeight)
                    )
                }
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
//                        .hazeEffect(hazeState, style = HazeMaterials.ultraThin())
                        .clipToBounds(),
                    shape = RoundedCornerShape(30.dp),
//                    color = Color.Transparent
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
                                                        statusBarMaxHeight + 40.dp
                                                    )
                                            },
                                            onDragEnd = {
                                                if (statusBarHeight > statusBarMinHeight && statusBarHeight < statusBarMaxHeight + 41.dp) {
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
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .align(Alignment.Center)
                    .width(50.dp)
                    .height(50.dp)
                    .zIndex(2F)
            ) {
                AnimatedVisibility(
                    uiState.isRefreshing,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    if (showDialogNewTopic.value) {
        AlertDialog(
            title = {
                Text(text = "Add Topic")
            },
            text = {
                Column {
                    TextField(
                        value = viewModel.newTopicTitle.value,
                        onValueChange = {
                            viewModel.newTopicTitle.value = it
                        },
                        placeholder = {
                            Text("New topic title")
                        }
                    )
                }
            },
            onDismissRequest = {
                showDialogNewTopic.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addNewTopic(viewModel.newTopicTitle.value)
                        viewModel.newTopicTitle.value = ""
                        showDialogNewTopic.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialogNewTopic.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}