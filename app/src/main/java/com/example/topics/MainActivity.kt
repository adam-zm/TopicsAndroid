package com.example.topics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.topics.home.HomeScreen
import com.example.topics.topics.TopicScreen
import com.example.topics.ui.theme.TopicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TopicsTheme {
                var currentPage = remember {
                    mutableIntStateOf(0)
                }
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentPage.intValue == 0,
                                onClick = {
                                    navController.navigate(Routes.HomeScreen){
                                        popUpTo(0)
                                    }
                                },
                                icon = {
                                    Icon(Icons.Filled.Home, "Home")
                                },
                                label = {
                                    Text("Home")
                                }
                            )
                            NavigationBarItem(
                                selected = currentPage.intValue == 1,
                                onClick = {
                                    navController.navigate(Routes.SettingsScreen){
                                        popUpTo(0)
                                    }
                                },
                                icon = {
                                    Icon(Icons.Filled.Settings, "Settings")
                                },
                                label = {
                                    Text("Settings")
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HomeScreen,
                        modifier = Modifier
                            .padding(innerPadding)
                    ){
                        composable<Routes.HomeScreen>{
                            HomeScreen(
                                navController = navController
                            )
                            currentPage.intValue = 0
                        }
                        composable<Routes.SettingsScreen> {
                            SettingsScreen()
                            currentPage.intValue = 1
                        }
                        composable<Routes.TopicScreen> { backStackEntry ->
                            val topicID = backStackEntry.toRoute<Routes.TopicScreen>().topicId
                            TopicScreen(topicID, navController = navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SettingsScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Settings")
        }
    }
}