package fr.isen.bongarzone.androidsmartdevice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
/*question d*/
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import fr.isen.bongarzone.androidsmartdevice.ScanScreen


class ScanActivity : ComponentActivity() {
    private var isScanning by mutableStateOf(false) // État du scan géré ici
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen(
                    isScanning = isScanning,
                    onToggleScan = {
                        isScanning = !isScanning
                    } // Change l'état sans bloquer les threads
                )
            }
        }
    }
}



