package fr.isen.bongarzone.androidsmartdevice

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import android.bluetooth.le.ScanResult

@Composable
fun ScanScreen(
    isScanning: Boolean,
    onToggleScan: () -> Unit,
    scanResults: List<ScanResult>,
    onDeviceClick: (ScanResult) -> Unit, // Ajout du callback pour le clic
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Titre
        Text(
            text = "Scan des appareils BLE",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Message pour indiquer l'état du scan
        Text(
            text = if (isScanning) "Scanning en cours..." else "Scan arrêté",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Image pour lancer/arrêter le scan
        Image(
            painter = painterResource(id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_start),
            contentDescription = if (isScanning) "Arrêter le scan" else "Démarrer le scan",
            modifier = Modifier
                .size(64.dp)
                .clickable { onToggleScan() }
                .padding(bottom = 16.dp)
        )

        // Liste des appareils détectés
        Text(text = "Appareils détectés :")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(scanResults) { result ->
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onDeviceClick(result) } // Clic redirigeant
                ) {
                    Text(text = "Nom : ${result.device?.name ?: "Inconnu"}")
                    Text(text = "Adresse : ${result.device?.address ?: "Adresse inconnue"}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    AndroidSmartDeviceTheme {
        ScanScreen(
            isScanning = false,
            onToggleScan = {},
            scanResults = emptyList(),
            onDeviceClick = {}
        )
    }
}
