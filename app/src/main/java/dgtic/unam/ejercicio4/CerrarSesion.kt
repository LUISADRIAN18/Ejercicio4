package dgtic.unam.ejercicio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


enum class TipoProvedor {
    CORREO,
    GOOGLE,
    FACEBOOK
}

class CerrarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cerrar_sesion)
    }
}