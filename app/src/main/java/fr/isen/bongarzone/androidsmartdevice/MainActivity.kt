package fr.isen.bongarzone.androidsmartdevice

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
/* import rajoutés*/
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/*v2*/
import android.os.Bundle
import androidx.activity.ComponentActivity
/*v3*/
import fr.isen.bongarzone.androidsmartdevice.ScanActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                MainScreen {
                    // Lancer l'activité ScanActivity lorsque le bouton est cliqué
                    startActivity(Intent(this, ScanActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun MainScreen(onScanClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icône de l'application
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon",
            modifier = Modifier.size(80.dp)
        )

        // Titre
        Text(
            text = "Android Smart Device",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Description
        Text(
            text = "Cette application permet de scanner les appareils BLE aux alentours.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Bouton pour lancer l'activité Scan
        Button(
            onClick = onScanClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Scanner les appareils BLE")
        }
    }
    }

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidSmartDeviceTheme {
        MainScreen {}
    }
}