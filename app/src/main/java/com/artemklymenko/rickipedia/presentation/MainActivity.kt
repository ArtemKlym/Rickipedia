package com.artemklymenko.rickipedia.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.artemklymenko.rickipedia.presentation.navigation.MainNavigation
import com.artemklymenko.rickipedia.presentation.navigation.NavDestination
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import com.artemklymenko.rickipedia.presentation.ui.theme.RickPrimary
import com.artemklymenko.rickipedia.presentation.ui.theme.RickipediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val items = listOf(NavDestination.Home, NavDestination.Episodes, NavDestination.Search)
            RickipediaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = RickPrimary,
                    bottomBar = {
                        MainNavigationBar(items, navController)
                    }
                ) { innerPadding ->
                    MainNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    private fun MainNavigationBar(
        items: List<NavDestination>,
        navController: NavHostController
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBar(containerColor = RickPrimary) {
            items.forEach { navDestination ->
                val selected = navDestination.route == currentRoute
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = navDestination.icon,
                            contentDescription = navDestination.title
                        )
                    },
                    label = {
                        Text(text = navDestination.title)
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(navDestination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RickAction,
                        selectedTextColor = RickAction,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}