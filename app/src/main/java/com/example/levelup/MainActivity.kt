import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.levelup.viewModel.LoginViewModelFactory

class MainActivity : ComponentActivity() {


    private val vm: LoginViewModelFactory by viewModels {
        LoginViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //
                //
        }
    }
}