import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import dl.initializeKoin
import org.jetbrains.compose.ui.tooling.preview.Preview
import domain.presentation.screen.HomeScreen

@Composable
@Preview
fun App() {

    initializeKoin()
    MaterialTheme {

        Navigator(HomeScreen())
    }
}