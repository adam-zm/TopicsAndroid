package com.example.topics.topics

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.topics.Routes
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    topicID: Int?,
    topicScreenViewModel: TopicScreenViewModel = koinViewModel(),
    navController: NavController
) {
    LaunchedEffect(true) {
        topicScreenViewModel.fetchTopic(topicID)
//        topicScreenViewModel.fetchComments(topicID)
    }

    var showAddCommentDialog = remember {
        mutableStateOf(false)
    }
    var state = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        topicScreenViewModel.topic.value.title,
                        modifier = Modifier
                            .padding(start = 20.dp),
                        style = TextStyle(
                            fontSize = 30.sp
                        )
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete topic",
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    topicScreenViewModel.deleteTopicById()
                                    navController.navigate(Routes.HomeScreen){
                                        popUpTo(1)
                                    }
                                }
                            )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddCommentDialog.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "Add new comment"
                )
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PullToRefreshBox(
                isRefreshing = topicScreenViewModel.isRefreshing.value,
                onRefresh = {
//                    topicScreenViewModel.fetchComments(topicID)
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 30.dp, horizontal = 20.dp)
                        .pullToRefresh(
                            isRefreshing = topicScreenViewModel.isRefreshing.value,
                            onRefresh = {
//                                topicScreenViewModel.fetchComments(topicID)
                            },
                            state = state
                        ),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    items(topicScreenViewModel.comments.value) { comment ->
                        Text(text = comment.content)
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
        }
    }
}