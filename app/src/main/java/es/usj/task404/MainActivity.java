package es.usj.task404;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    public static final int PERMISSION_ASK = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listView);
    }

    public void visible(View view) {
        Intent getVisible = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void list(View view) {
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
    }

    public void off(View view) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    /*
    Bluetooth needs to turn on. This example is to do it for Android 6.0
    and above.
    */
    public void on(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (isBluetoothPermissionGranted()) {
                // app already has required permissions
                // do some task here
            } else {
                // app does not have permission yet.
                // ask for permissions
                askForBluetoothPermissions();
            }
        } else {
            // Android version below 6.0, no need to check or ask for permission
            // do some task here
        }
    }

    /*
    Checking if application already has the required permission:
    */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isBluetoothPermissionGranted() {
        boolean granted = false;
        int bluetoothGranted = checkSelfPermission(Manifest.permission.BLUETOOTH);
        int bluetoothAdminGranted = checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN);
        if (bluetoothGranted == PackageManager.PERMISSION_GRANTED && bluetoothAdminGranted == PackageManager.PERMISSION_GRANTED) {
            granted = true;
        }
        return granted;
    }

    /*
    If required permission is not granted, here's how you would ask
   for it:
    */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForBluetoothPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        requestPermissions(permissions, PERMISSION_ASK);
    }

    /*
    how you would confirm if the user granted you the permission(s)
   or not:
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ASK:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // all requested permissions were granted
                    // perform your task here
                } else {
                    // permissions not granted
                    // DO NOT PERFORM THE TASK, it will fail/crash
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,
                        permissions, grantResults);
                break;
        }
    }
}
