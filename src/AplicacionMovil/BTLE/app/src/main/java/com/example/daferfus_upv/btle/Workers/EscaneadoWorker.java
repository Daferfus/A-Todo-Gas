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
import android.os.Handler;
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
    private BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
    private BluetoothAdapter mAdaptadorBluetooth = bluetoothManager.getAdapter();
    private BluetoothLeScanner escanerBTLE = mAdaptadorBluetooth.getBluetoothLeScanner();
    private boolean escaneando = false;
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
        escanearDispositivoBTLE(true);
        Log.d("MedicionesWorker", "Escaner ejecutado");
        return Result.success();
    } // ()

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


    // --------------------------------------------------------------
    //                  escanearDispositivoBTLE() <-
    //                  <- Buleano
    //
    // Invocado desde: buscarTodosLosDispositivosBTLE()
    //                 buscarEsteDispositivoBTLE()
    //                 detenerBusquedaDispositivosBTLE()
    // Función: Activa o desactiva el escaneado de dispositivos BTLE.
    // --------------------------------------------------------------
    public void escanearDispositivoBTLE(final boolean activar) {

        // Se emplea la API de escaneado nueva surgida en Android Lollipop, por su ahorro
        // en consumo de bateria.
        //final BluetoothLeScanner escanerBTLE = mAdaptadorBluetooth.getBluetoothLeScanner();

        if (activar) {
            // Para de escanear después de un periodo de escaneado predefinido.
            int SCAN_PERIOD = 1000;
            int i = 0;
            while(i <= SCAN_PERIOD){
                escaneando = true;
                escanerBTLE.startScan(mLeScanCallback);
                i++;
            }
            escaneando = false;
            escanerBTLE.stopScan(mLeScanCallback);
        } else {
            escaneando = false;
            escanerBTLE.stopScan(mLeScanCallback);
        } // if()
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
