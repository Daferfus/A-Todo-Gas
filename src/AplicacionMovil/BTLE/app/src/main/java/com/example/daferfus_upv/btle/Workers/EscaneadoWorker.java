// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.Workers;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.daferfus_upv.btle.Utilidades.TramaIBeacon;
import com.example.daferfus_upv.btle.Utilidades.Utilidades;

import java.util.List;
import java.util.Objects;

import static com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas.haLlegadoUnBeacon;
import static com.example.daferfus_upv.btle.Utilidades.TratamientoDeLecturas.mostrarInformacionDispositivoBTLE;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class EscaneadoWorker extends Worker {
    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context, WorkParameters
    //
    // Invocado desde: TratamientoDeLecturas::haLLegadoUnBeacon()
    // Función: Inicializa y configura la tarea de escaneado de beacons.
    // --------------------------------------------------------------
    public EscaneadoWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    } // ()

    @NonNull
    @Override
    public Result doWork() {

        // ------------------------------------------------------------------
        // ------------------------------------------------------------------
        ScanCallback mLeScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(ScanSettings.SCAN_MODE_LOW_POWER, result);
                TramaIBeacon tib = new TramaIBeacon(Objects.requireNonNull(result.getScanRecord()).getBytes());
                String uuidString = Utilidades.bytesToString(tib.getUUID());

                if (uuidString.compareTo(Utilidades.uuidToString(Utilidades.stringToUUID("EPSG-GTI-PROY-G2"))) == 0) {
                    mostrarInformacionDispositivoBTLE(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                    haLlegadoUnBeacon(tib);
                } else {
                    Log.d("MedicionesWorker", " * UUID buscado >" +
                            Utilidades.uuidToString(Utilidades.stringToUUID("EPSG-GTI-PROY-G2")) + "< no concuerda con este uuid = >" + uuidString + "<");
                } // if()
            } // ()

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            } // ()

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            } // ()
        }; // new ScanCallback
        // ------------------------------------------------------------------
        // ------------------------------------------------------------------

        BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mAdaptadorBluetooth = bluetoothManager.getAdapter();
        BluetoothLeScanner escanerBTLE = mAdaptadorBluetooth.getBluetoothLeScanner();
        escanerBTLE.startScan(mLeScanCallback);
        Log.d("MedicionesWorker", "Escaner ejecutado");

        return Result.success();
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
