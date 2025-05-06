package mdao.ahorraya

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mdao.ahorraya.AhorraYa.repository.AuthManager
import mdao.ahorraya.AhorraYa.repository.AuthManager.auth
import mdao.ahorraya.AhorraYa.repository.NavItemsList
import mdao.ahorraya.AhorraYa.ui.models.AppScreens
import mdao.ahorraya.AhorraYa.ui.views.BottomNavBar
import mdao.ahorraya.AhorraYa.ui.views.HistoryScreen
import mdao.ahorraya.AhorraYa.ui.views.HomeScreen
import mdao.ahorraya.AhorraYa.ui.views.NavigationWrapper
import mdao.ahorraya.AhorraYa.ui.views.RevenuesScreen
import mdao.ahorraya.ui.theme.AhorraYaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute !in listOf(
                AppScreens.InitialScreen.route,
                AppScreens.LoginScreen.route,
                AppScreens.SingUpScreen.route
            )

            val snackbarHostState = remember { SnackbarHostState() }

            AhorraYaTheme {
                var selectedIndex by remember { mutableIntStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = mdao.ahorraya.ui.theme.Coral),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Check",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = snackbarData.visuals.message,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(
                                navItemList = NavItemsList.navItemsList,
                                selectedIndex = selectedIndex,
                                onItemSelected = { index ->
                                    selectedIndex = index
                                    val route = when (index) {
                                        0 -> AppScreens.HomeScreen.route
                                        1 -> AppScreens.AddRevenues.route
                                        2 -> AppScreens.History.route
                                        else -> AppScreens.HomeScreen.route
                                    }
                                    navHostController.navigate(route)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavigationWrapper(
                            navHostController = navHostController,
                            auth = AuthManager.auth,
                            snackbarHostState = snackbarHostState
                        )

                        LaunchedEffect(Unit) {
                            if (AuthManager.isUserLoggedIn()) {
                                navHostController.navigate(AppScreens.HomeScreen.route)
                            }
                        }
                    }
                }
            }
        }

    }
}
