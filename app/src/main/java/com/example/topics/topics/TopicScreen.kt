package com.example.topics.topics

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TopicScreen(
    topicID: Int?,
    topicScreenViewModel: TopicScreenViewModel = koinViewModel(),
    navController: NavController,
    sharedStateHandler: SharedStateHandler = koinInject<SharedStateHandler>()
) {
    LaunchedEffect(true) {
        topicScreenViewModel.fetchTopic(topicID)
        topicScreenViewModel.fetchComments(topicID)
    }

    var showAddCommentDialog = remember {
        mutableStateOf(false)
    }

    val insets = WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
    val statusBarMinHeight = 90.dp + insets.calculateBottomPadding()
    var statusBarMaxHeight = 0.dp
    var statusBarHeight by remember {
        mutableStateOf(statusBarMinHeight)
    }
    val animatedStatusBarHeight by animateDpAsState(statusBarHeight)

    var state = rememberPullToRefreshState()
    val hazeState = rememberHazeState()
    val padding = WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
    val haptic = LocalHapticFeedback.current

    val uiState by sharedStateHandler.uiState.collectAsState()

    with(LocalDensity.current) {
        statusBarMaxHeight = LocalWindowInfo.current.containerSize.height.toDp() - 110.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .pullToRefresh(
                        isRefreshing = topicScreenViewModel.isLoading.value,
                        onRefresh = {
                            topicScreenViewModel.fetchComments(topicID)
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        },
                        state = state
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(top = padding.calculateTopPadding() + 50.dp)
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 50.dp, horizontal = 30.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = topicScreenViewModel.topic.value.text,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
                items(topicScreenViewModel.comments.value) { comment ->
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(15.dp)
                    )
                    Text(
                        text = comment.content,
                        modifier = Modifier.padding(20.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp
                        )
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = padding.calculateBottomPadding() + statusBarHeight)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .hazeEffect(hazeState, style = HazeMaterials.ultraThin()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = topicScreenViewModel.topic.value.title,
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding() + 25.dp, bottom = 15.dp)
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
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .size(60.dp),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.inversePrimary
                                    ),
                                    onClick = {}
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        "Add new comment",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(5.dp)
                                    )
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
                                        topicScreenViewModel.toggleUseHapticFeedback(isChecked)
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
                    topicScreenViewModel.isLoading.value,
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
    if (showAddCommentDialog.value) {
        AlertDialog(
            title = {
                Text(text = "Add Comment")
            },
            text = {
                Column {
                    TextField(
                        value = topicScreenViewModel.newCommentText.value,
                        onValueChange = {
                            topicScreenViewModel.newCommentText.value = it
                        },
                        placeholder = {
                            Text("New topic title")
                        }
                    )
                }
            },
            onDismissRequest = {
                showAddCommentDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
//                                    topicScreenViewModel.addNewComment(topicScreenViewModel.newCommentText.value)
                        topicScreenViewModel.newCommentText.value = ""
                        showAddCommentDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddCommentDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}