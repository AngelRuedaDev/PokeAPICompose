package com.example.pokeapicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pokeapicompose.ui.theme.PokeApiComposeTheme
import com.example.pokeapicompose.data.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Applies the custom app theme defined in PokeApiComposeTheme
            PokeApiComposeTheme {
                AppNavigation()
            }
        }
    }
}

