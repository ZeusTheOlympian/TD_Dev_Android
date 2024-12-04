package fr.isen.bongarzone.androidsmartdevice

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ConnectDeviceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val deviceName = intent.getStringExtra("deviceName") ?: "Inconnu"
        val deviceAddress = intent.getStringExtra("deviceAddress") ?: "Adresse inconnue"

        Toast.makeText(this, "Connexion à $deviceName ($deviceAddress)...", Toast.LENGTH_SHORT).show()

        setContent {
            AndroidSmartDeviceTheme {
                DeviceConnectionScreen(deviceName)
            }
        }
    }
}

@Composable
fun DeviceConnectionScreen(deviceName: String) {
    Text(text = "Connexion à $deviceName en cours...")
}
