package mdao.ahorraya.AhorraYa.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import mdao.ahorraya.AhorraYa.ui.models.AppScreens
import mdao.ahorraya.AhorraYa.ui.models.NavItems
import mdao.ahorraya.ui.theme.SelectedNavbar

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth, snackbarHostState: SnackbarHostState) {

    NavHost(navController = navHostController, startDestination= AppScreens.InitialScreen.route){
        composable(AppScreens.InitialScreen.route){
            InitialScreen(
                navigateToLogin = { navHostController.navigate(AppScreens.LoginScreen.route) },
                navigateToSignUp = { navHostController.navigate(AppScreens.SingUpScreen.route) }
            )
        }
        composable(AppScreens.LoginScreen.route){
            LoginScreen(
                auth,
                navigateToBack = { navHostController.popBackStack() },
                navigateToSignUp = { navHostController.navigate(AppScreens.SingUpScreen.route) },
                navigateToHome = { navHostController.navigate(AppScreens.HomeScreen.route) }
            )
        }
        composable(AppScreens.SingUpScreen.route){
            SignUpScreen(auth,
                navigateToBack = { navHostController.popBackStack() },
                navigateToLogin = { navHostController.navigate(AppScreens.LoginScreen.route) },
            )
        }
        composable(AppScreens.HomeScreen.route){
            HomeScreen(auth)
        }
        composable(AppScreens.AddRevenues.route){
            RevenuesScreen(auth = auth, snackbarHostState = snackbarHostState)
        }
        composable(AppScreens.History.route) {
            HistoryScreen(auth = auth, snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
fun BottomNavBar(
    navItemList: List<NavItems>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = Color.White
    ) {
        navItemList.forEachIndexed { index, navItem ->
            val isSelected = selectedIndex == index

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label,
                            tint = if (isSelected) SelectedNavbar else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = navItem.label,
                            fontSize = 16.sp,
                            color = if (isSelected) SelectedNavbar else Color.Gray
                        )
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0x1AFF6B3D)
                )
            )
        }
    }
}
