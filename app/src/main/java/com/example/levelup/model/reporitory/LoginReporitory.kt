import com.example.levelup.model.local.LoginDao
import com.example.levelup.model.local.LoginEntity

class LoginReporitory(private val loginDao: LoginDao) {

    suspend fun getUserByCorreoAndContrasena(correo: String, contrasena: String): LoginEntity? {
        return loginDao.getUserByCorreoAndContrasena(correo, contrasena)
    }
}
