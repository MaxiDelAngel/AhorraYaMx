package mdao.ahorraya.AhorraYa.ui.models

sealed class AppScreens(val route: String) {
    object HomeScreen: AppScreens("home_screen")
    object InitialScreen: AppScreens("initial_screen")
    object LoginScreen: AppScreens("login_screen")
    object SingUpScreen: AppScreens("singup_screen")
    object AddRevenues: AppScreens("add_revenues")
    object History: AppScreens("history")
}