package com.example.topics.home

import android.graphics.Rect
import android.view.View
import android.view.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics.Routes
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val hazeState = rememberHazeState()
    val context = LocalContext.current
    val showDialogNewTopic = remember {
        mutableStateOf(false)
    }
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    val insets = WindowInsets.statusBars.asPaddingValues(LocalDensity.current)
    val statusBarHeight = 85.dp

    PullToRefreshBox(
        isRefreshing = viewModel.isRefreshing.value,
        onRefresh = viewModel::fetchTopics
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .pullToRefresh(
                            isRefreshing = viewModel.isRefreshing.value,
                            onRefresh = viewModel::fetchTopics,
                            state = state
                        )
                        .hazeSource(hazeState),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(insets.calculateTopPadding())
                        )
                    }
                    items(viewModel.topics.value) { topic ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable(
                                    onClick = {
                                        navController.navigate(Routes.TopicScreen(topic.id))
                                    }
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(topic.title)
                            }
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(insets.calculateBottomPadding() + statusBarHeight)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ){
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .padding(18.dp)
                    ){
                        IconButton(
                            modifier = Modifier
                                .padding(5.dp),
                            onClick = {
                                showDialogNewTopic.value = true
                            },
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add topic")
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(statusBarHeight)
                            .hazeEffect(hazeState, style = HazeMaterials.ultraThin()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Surface(
                            onClick = {
                                navController.navigate(Routes.HomeScreen){
                                    popUpTo(Routes.HomeScreen)
                                }
                            },
                            color = Color.Transparent
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Icon(Icons.Outlined.Home, "Home")
                                Text("Home")
                            }
                        }
                        Surface(
                            onClick = {
                                navController.navigate(Routes.SettingsScreen){
                                    popUpTo(Routes.SettingsScreen)
                                }
                            },
                            color = Color.Transparent
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Icon(Icons.Outlined.Settings, "Settings")
                                Text("Settings")
                            }
                        }
                    }
                }
            }
        }
    }

    if(showDialogNewTopic.value){
        AlertDialog(
            title = {
                Text(text = "Add Topic")
            },
            text = {
                Column{
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