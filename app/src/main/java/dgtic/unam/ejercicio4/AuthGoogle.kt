package dgtic.unam.ejercicio4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dgtic.unam.ejercicio4.databinding.ActivityAuthGoogleBinding

class AuthGoogle : AppCompatActivity() {
    private lateinit var binding: ActivityAuthGoogleBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validate()





    }

    private fun validate(){

        iniciarActividad()
        binding.google.setOnClickListener {
            val config=
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                    getString(R.string.default_web_client_id)
                ).requestEmail().build()
            val clienteGoogle = GoogleSignIn.getClient(this,config)
            clienteGoogle.signOut()
            var signIn : Intent = clienteGoogle.signInIntent
            activityResultLauncher.launch(signIn)

        }

    }
    private fun iniciarActividad() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        Toast.makeText(this, "conexion con exito", Toast.LENGTH_SHORT).show()
                        if (account != null) {
                            var credenciales =
                                GoogleAuthProvider.getCredential(account.idToken, null)
                            FirebaseAuth.getInstance().signInWithCredential(credenciales)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        opciones(account.email ?: "", TipoProvedor.GOOGLE)
                                    } else {
                                        alert()
                                    }
                                }
                        }
                    } catch (e: ApiException) {
                        Toast.makeText(this, "Error al conectar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }



    private fun opciones(email: String, provedor: TipoProvedor) {

        var pasos = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provedor", provedor.name)
        }
        startActivity(pasos)



    }
    private fun alert() {
        val bulder = AlertDialog.Builder(this)
        bulder.setTitle("Mensaje")
        bulder.setMessage("Se produjo un error, contacte al provesor")
        bulder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = bulder.create()
        dialog.show()
    }



}