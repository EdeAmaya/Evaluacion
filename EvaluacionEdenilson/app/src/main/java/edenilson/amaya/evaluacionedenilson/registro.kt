package edenilson.amaya.evaluacionedenilson

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnPantallaInicioSesion = findViewById<Button>(R.id.btnPantallaInicioSesion)

        btnPantallaInicioSesion.setOnClickListener {
            val pantallaInicioSesion = Intent(this, MainActivity ::class.java)
            startActivity(pantallaInicioSesion)
        }
    }
}