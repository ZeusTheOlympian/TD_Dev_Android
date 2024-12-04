package fr.isen.bongarzone.androidsmartdevice

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.mutableStateListOf


class ScanActivity : ComponentActivity() {

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
/*g*/
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var isScanning by mutableStateOf(false)
    private val scanResults = mutableStateListOf<ScanResult>()
    private val scanHandler = Handler(Looper.getMainLooper())

    private val scanTimeout: Long = 10000L // Timeout pour le scan (10 secondes)
/*g*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    /*g*/
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    /*g*/
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreenWithState(
                    isScanning = isScanning,
                    scanResults = scanResults,
                    onToggleScan = { toggleScan() }
                )
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
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponible sur cet appareil", Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }

        if (bluetoothAdapter?.isEnabled == false) {
            requestEnableBluetooth()
        } else {
            Toast.makeText(this, "Bluetooth activé. Prêt à scanner.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        bluetoothEnableLauncher.launch(enableBtIntent)
    }

    private val bluetoothEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Le Bluetooth est requis pour scanner les appareils", Toast.LENGTH_LONG).show()
                finish()
            }
        }
/*g*/
    private fun toggleScan() {
    if (!checkPermissions()) {
        Toast.makeText(this, "Permissions manquantes pour scanner.", Toast.LENGTH_SHORT).show()
        return
    }

    if (bluetoothAdapter?.isEnabled == false) {
        Toast.makeText(this, "Veuillez activer le Bluetooth pour scanner.", Toast.LENGTH_SHORT).show()
        return
    }

    if (isScanning) {
        stopScan()
    } else {
        startScan()
    }
}

    private fun startScan() {
        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter?.bluetoothLeScanner?.startScan(scanCallback)
            isScanning = true
            Toast.makeText(this, "Scan démarré", Toast.LENGTH_SHORT).show()

            // Arrêter le scan après le timeout
            scanHandler.postDelayed({
                stopScan()
            }, scanTimeout)
        } else {
            Toast.makeText(this, "Permission de scan Bluetooth manquante.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopScan() {
        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
            isScanning = false
            Toast.makeText(this, "Scan arrêté", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission de scan Bluetooth manquante.", Toast.LENGTH_SHORT).show()
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (!scanResults.any { it.device.address == result.device.address }) {
                scanResults.add(result)
            }
        }
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)
            if (results.isEmpty()) {
                Toast.makeText(this@ScanActivity, "Aucun appareil trouvé dans le scan en lot.", Toast.LENGTH_SHORT).show()
            }
            for (result in results) {
                if (!scanResults.any { it.device.address == result.device.address }) {
                    scanResults.add(result)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(this@ScanActivity, "Erreur de scan : $errorCode", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDeviceName(result: ScanResult): String {
        return if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            result.device?.name ?: "Inconnu"
        } else {
            "Permission requise"
        }
    }

    private fun getDeviceAddress(result: ScanResult): String {
        return if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            result.device?.address ?: "Adresse inconnue"
        } else {
            "Permission requise"
        }
    }
    data class DeviceInfo(
        val name: String,
        val address: String
    )

    val processedResults = scanResults.map { result ->
        DeviceInfo(
            name = getDeviceName(result),
            address = getDeviceAddress(result)
        )
    }


}
/*g*/

@Composable
fun ScanScreenWithState(
    isScanning: Boolean,
    scanResults: List<ScanResult>,
    onToggleScan: () -> Unit
) {
    ScanScreen(
        isScanning = isScanning,
        onToggleScan = onToggleScan,
        scanResults = scanResults
    )
}
