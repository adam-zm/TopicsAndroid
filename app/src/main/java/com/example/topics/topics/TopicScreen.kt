package com.example.topics.topics

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TopicScreen(
    topicID: Int?,
    topicScreenViewModel: TopicScreenViewModel = koinViewModel(),
    navController: NavController
) {
    LaunchedEffect(true) {
        topicScreenViewModel.fetchTopic(topicID)
        topicScreenViewModel.fetchComments(topicID)
    }

    var showAddCommentDialog = remember {
        mutableStateOf(false)
    }
    var state = rememberPullToRefreshState()
    val hazeState = rememberHazeState()
    val padding = WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
    val statusBarHeight = 105.dp
    val haptic = LocalHapticFeedback.current

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
                items(topicScreenViewModel.comments.value) { comment ->
                    Text(
                        text = comment.content,
                        modifier = Modifier.padding(20.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 25.sp
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
                        .padding(top = padding.calculateTopPadding() + 15.dp, bottom = 15.dp)
                )
            }
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(statusBarHeight)
                    .padding(bottom = padding.calculateBottomPadding(), start = 30.dp, end = 30.dp)
                    .fillMaxWidth(),
                color = Color.Transparent,
                shape = RoundedCornerShape(30.dp)
            ) {
                Row(
                    modifier = Modifier
                        .hazeEffect(
                            hazeState,
                            style = HazeMaterials.thin(),
                        )
                        .background(Color(0x11000000)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Status bar",
                        style = TextStyle(
                            fontSize = 23.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
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