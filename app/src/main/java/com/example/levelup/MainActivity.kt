import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.levelup.theme.LevelUpTheme
import com.example.levelup.ui.UsuariosNavHost
import com.example.levelup.viewModel.LoginViewModelFactory

class MainActivity : ComponentActivity() {


    private val vm: LoginViewModel by viewModels {
        LoginViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme{
                UsuariosNavHost(vm)
            }
        }
    }
}