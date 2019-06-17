package mikaeldionitoharbigantara.gmail.com;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {
    // Mendeklarasikan sebuah class MainActivity dengan menimplement sebuah fungsi LocationListener (Untuk mengetahui apabila ada update dari lokasi kita terkini) dan SensorEventListener (Untuk Memanggil Fungsi Sensor yang ada pada Device yang digunakan dengan fungsi onResume untuk memasuki Activity dan onPause saat Keluar).
private TextView latituteField;
private TextView longitudeField;
// Mendeklarasikan sebuah variabel dari Textview yaitu latitudeField dan longtitudeField yang bersifat private.

private LocationManager locationManager;
// mendeklarasikan sebuah variabel dari LocationManager yaitu locationManager yang bersifat private.
private String provider;
// mendeklarasikan sebuah variabel dari tipe data String yaitu provider yang bersifat private.
private ImageView image;
// mendeklarasikan sebuah variabel dari ImageView yaitu image yang bersifat private.
private float currentDegree = 0f;
// mendeklarasikan sebuah variabel dengan tipedata float yaitu currentDegree dengan nilai awal 0f yang bersifat private.
private SensorManager mSensorManager;
// mendeklarasikan sebuah variabel dari SensorManager yaitu mSensorManager yang bersifat private.
TextView tvHeading;
// mendeklarasikan sebuah variabel dengan tipe data TextView yaitu tvHeading.
Button button;
// mendeklarasikan sebuah variabel dari Button yaitu button.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mendeklarasikan sebuah fungsi onCreate yang ditujukan untuk mengakses layout yaitu activity_main.xml

        button = (Button) findViewById(R.id.button);
        // mendeklarasikan sebuah variabel button dengan memanggil sebuah fungsi Button dengan id button pada layout activity_main.xml
        button.setOnClickListener(new View.OnClickListener() {
            // mendeklarasikan sebuah fungsi setOnClickListener pada variabel button bila di click maka akan menjalankan fungsi akan mendirect posisi longtitude dan latitude ke orang lain ke bentuk String dengan aturn :
            @Override
            public void onClick(View v) {
                String text = latituteField.getText().toString();
                String text2 = longitudeField.getText().toString();
                // mendeklarasikan variabel text untuk latitudeField dan text2 untuk longtitudeField dimana setiap variabel akan memanggil Text yang ditampilkan akan diubah ke bentuk String untuk dikirim ke orang lain.
                Intent intent = new Intent(Intent.ACTION_SEND);
                // mendeklarasikan fungsi Intent dengan nama intent dimana Inten pada fungsi ini akan menggunakan fungsi ACTION_SEND yang digunakan untuk berbagi melalui aplikasi lain yang terdapat pada device yang terinstall.
                intent.setType("text/plain");
                // mendeklarasikan sebuah tipe dari intent yang akan dikirim yaitu berbentuk text/plain.
                intent.putExtra(Intent.EXTRA_TEXT,"Latitude = "+ text + " & Longitude = " +text2);
                // mendeklarasikan sebuah fungsi intent yaitu putExtra dengan menampilkan text apa yang akan dikirim ke orang lain yaitu menampilkan Latitude(text) dan Longitude(text2).
                startActivity(Intent.createChooser(intent, "Share Via : "));
                // menjalankan activity intent.
            }
        });

        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        // mendeklarasikan fungsi TextView dengan variabel latitudeField dengan akses ke id TextView02 dan longitudeField ke id TextView04 pada layout activity_main.xml
        image = (ImageView) findViewById(R.id.imageViewCompass);
        // mendeklarsaikan sebuah ImageView dengan variabel image yang mengakses ImageView dengan id imageViewCompas pada layout activity_main.xml
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        // mendeklarsaikan sebuah TextView dengan variabel tvHeading yang mengakses TextView dengan id tvHeading pada layout activity_main.xml
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // mendeklarasikan variabel locationManager yang mengakses LOCATION_SERVICE
        Criteria criteria = new Criteria();
        // mendeklarasikan sebuah fungsi Criteria dengan variabel criteria.
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // mendeklarasikan variabel mSensorManager yang mengakses SENSOR_SERVICE.

        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // mendeklarasikan peermintaan izin dan check permision pada variabel provider untuk akses locationManager dengan pendeklarasian semua akses adalah pada Manifest yaitu akses Lokasi, Internet, Provider dan GPS
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null){
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            // jika fungsi location tersedia maka akan menampilkan sebuah notif Provider has been selected
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
            // jika location tidak tesedia maka pada latitudeField dan longitudeField akan menampilkan Location not available.
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // mendeklarasikan peermintaan izin dan registerListener pada variabel mSensorManager untuk akses SensorManager dengan pendeklarasian semua akses adalah pada Manifest yaitu akses Lokasi, Internet, Provider dan GPS
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void
    onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        mSensorManager.unregisterListener(this);
    }
    // mendeklarasikan fungsi onPause dengan mengset ulang pada locationManager dan mSensorManager.


    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);
        image.startAnimation(ra);
        currentDegree = -degree;
    }
    // mendeklarasikan aturan pada onSensorChanged dengan aturan titik awal Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f.

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        // mendeklarasikan aturan pada inLocationChanged dengan isian 2 variabel bertipedata double yaitu lat untuk mendeklarasikan location dengan mengambil nilai latitude den lng mengambil nilai longitude.
        // dan mendeklarasikan variabel latitudeField mengambil value lat dan longitudeField pada value lng.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
        // mendeklarsaikan toast Enable new provider jika provider pada device mati.
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
        // mendeklarsaikan toast Disable provider jika provider pada device hidup.
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
