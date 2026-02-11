package com.example.assignment4temperature;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvAverageTemperature, tvStatus;
    private ImageView ivStatusImage;
    private EditText etInputTemperature;
    private Button btnReport;
    private Switch switchUnit;

    private ArrayList<Double> temperatures = new ArrayList<>();
    private boolean isCelsius = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAverageTemperature = findViewById(R.id.tvAverageTemperature);
        tvStatus = findViewById(R.id.tvStatus);
        ivStatusImage = findViewById(R.id.ivStatusImage);
        etInputTemperature = findViewById(R.id.etInputTemperature);
        btnReport = findViewById(R.id.btnReport);
        switchUnit = findViewById(R.id.switchUnit);

        btnReport.setOnClickListener(v -> {
            String input = etInputTemperature.getText().toString().trim();
            if (input.isEmpty()) return;
            try {
                double tempInput = Double.parseDouble(input);
                if (isCelsius) {
                    tempInput = tempInput * 9 / 5 + 32;
                }
                temperatures.add(tempInput);
                updateUI(calculateAverage());
                etInputTemperature.setText("");
            } catch (NumberFormatException ignored) {}
        });

        switchUnit.setOnCheckedChangeListener((buttonView, checked) -> {
            isCelsius = checked;
            updateUI(calculateAverage());
            etInputTemperature.setHint(isCelsius ? "°C" : "°F");
        });

        etInputTemperature.setHint("°F");
        updateUI(Double.NaN);
    }

    private double calculateAverage() {
        if (temperatures.isEmpty()) return Double.NaN;
        double sum = 0;
        for (double t : temperatures) sum += t;
        return sum / temperatures.size();
    }

    private void updateUI(double avgFahrenheit) {
        if (Double.isNaN(avgFahrenheit)) {
            tvAverageTemperature.setText("N/A " + (isCelsius ? "°C" : "°F"));
            tvStatus.setText("No data yet");
            ivStatusImage.setImageResource(R.drawable.notemp);
            return;
        }

        double displayTemp = isCelsius ? (avgFahrenheit - 32) * 5 / 9 : avgFahrenheit;
        String unit = isCelsius ? "°C" : "°F";
        tvAverageTemperature.setText(String.format("%.1f %s", displayTemp, unit));

        if (avgFahrenheit >= 97.2 && avgFahrenheit <= 100.4) {
            tvStatus.setText("Normal");
            ivStatusImage.setImageResource(R.drawable.normaltemp);
        } else if (avgFahrenheit > 100.4 && avgFahrenheit <= 103.1) {
            tvStatus.setText("Fever");
            ivStatusImage.setImageResource(R.drawable.mildtemp);
        } else if (avgFahrenheit > 103.1) {
            tvStatus.setText("High Fever");
            ivStatusImage.setImageResource(R.drawable.hightemp);
        } else {
            tvStatus.setText("No data yet");
            ivStatusImage.setImageResource(R.drawable.notemp);
        }
    }
}
