package fr.isen.bongarzone.androidsmartdevice

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import fr.isen.bongarzone.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
/*E*/
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class ScanActivity : ComponentActivity() {

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreenWithState()
            }
        }

        if (checkPermissions()) {
            initBLEScan()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return requiredPermissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionsLauncher.launch(requiredPermissions)
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value == true }
            if (!allGranted) {
                Toast.makeText(this, "Toutes les permissions sont requises pour utiliser l'application", Toast.LENGTH_LONG).show()
                finish()
            } else {
                initBLEScan()
            }
        }

    private fun initBLEScan() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponible sur cet appareil", Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            requestEnableBluetooth()
        } else {
            // Ajouter ici la logique de démarrage du scan si nécessaire
            Toast.makeText(this, "Bluetooth activé. Prêt à scanner.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        bluetoothEnableLauncher.launch(enableBtIntent)
        Toast.makeText(this, "Veuillez activer le Bluetooth pour continuer.", Toast.LENGTH_SHORT).show()
    }

    private val bluetoothEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Le Bluetooth est requis pour scanner les appareils", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Bluetooth activé. Prêt à scanner.", Toast.LENGTH_SHORT).show()
            }
        }
}
@Composable
fun ScanScreenWithState() {
    var isScanning by remember { mutableStateOf(false) }
    ScanScreen(
        isScanning = isScanning,
        onToggleScan = { isScanning = !isScanning }
    )
}
