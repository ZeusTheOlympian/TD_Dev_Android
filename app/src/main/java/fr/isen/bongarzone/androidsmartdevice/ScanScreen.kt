package fr.isen.bongarzone.androidsmartdevice



import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

@Composable
fun ScanScreen(
    isScanning: Boolean, // Paramètre pour l'état du scan
    onToggleScan: () -> Unit, // Callback pour démarrer/arrêter le scan
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

        // Liste des appareils détectés (vide pour le moment)
        Text(text = "Appareils détectés :")
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)) {
            item {
                Text(text = "Aucun appareil détecté", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    AndroidSmartDeviceTheme {
        ScanScreen(
            isScanning = false, // Valeur par défaut
            onToggleScan = {}   // Lambda vide par défaut
        )
    }
}
