package edenilson.amaya.evaluacionedenilson

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
       val btnIniciaSesion = findViewById<Button>(R.id.btnIniciarSesion)
       val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
       val txtCorreo = findViewById<TextView>(R.id.txtCorreo)
       val txtContrasena = findViewById<TextView>(R.id.txtContrasena)


        btnRegistrarse.setOnClickListener {
            val pantallaRegistro = Intent(this, registro ::class.java)
            startActivity(pantallaRegistro)
        }
        btnIniciaSesion.setOnClickListener {
            val pantallaTicket = Intent(this, Ticket ::class.java)
            startActivity(pantallaTicket)
        }
    }
}