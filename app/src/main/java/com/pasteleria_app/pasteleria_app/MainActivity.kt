package com.pasteleria_app.pasteleria_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.navigation.Navigation
import com.pasteleria_app.pasteleria_app.presentation.ui.theme.Pasteleria_APPTheme
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pasteleria_APPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 🧩 Hilt crea e inyecta automáticamente el ViewModel con el repositorio
                    val carritoViewModel: CarritoViewModel = hiltViewModel()

                    // 🚀 Navegación principal (pasamos el viewmodel global)
                    Navigation(carritoViewModel = carritoViewModel)
                }
            }
        }
    }
}
