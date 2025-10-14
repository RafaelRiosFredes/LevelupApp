package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels

import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {


    private val vm: ProductosViewModel by viewModels {
        ProductosViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductosAppTheme {
                ProductosNavHost(vm)
            }
        }
    }
}    