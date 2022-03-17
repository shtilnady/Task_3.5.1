package com.example.task_351;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    float currentLight;
    float lastLight;
    Button playMusicBtn;
    SensorManager sensorManager;
    TextView light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playMusicBtn = findViewById(R.id.start);
        light = findViewById(R.id.lightValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        currentLight = 0;
        lastLight = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        loadSensorData(event);

        if (light == null) light = findViewById(R.id.lightValue);
        light.setText(String.valueOf(Math.round(Math.toDegrees(currentLight))));

        if (currentLight < lastLight) {
            startService(new Intent(this, MediaService.class));
            playMusicBtn.setEnabled(true);
        } else {
            stopService(new Intent(this, MediaService.class));
            playMusicBtn.setEnabled(false);
        }

        lastLight = currentLight;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadSensorData(SensorEvent event) {
        final int type = event.sensor.getType();
        float[] values;
        if (type == Sensor.TYPE_LIGHT) {
            values = event.values.clone();
            currentLight = values[0];
        }
    }
}