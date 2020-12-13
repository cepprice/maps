package ru.cepprice.maps.ui.contries;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ru.cepprice.maps.R;
import ru.cepprice.maps.databinding.ActivityCountryListBinding;
import ru.cepprice.maps.utils.InternalStorageHelper;

public class CountryListActivity extends AppCompatActivity {

    private ActivityCountryListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDeviceMemoryInfo();
    }

    private void setupDeviceMemoryInfo() {
        double free = InternalStorageHelper.getFreeGigabytes();
        double total = InternalStorageHelper.getTotalGigabytes();

        String formattedGigabytes = String.format("%.2f", free).replace(',', '.');
        String text = getString(R.string.countries_label_free_gigabytes, formattedGigabytes);
        binding.tvFreeGb.setText(text);

        int totalProgress = 100;
        int progress = totalProgress - (int) (free / total * 100);
        binding.progressFreeSpace.setProgress(progress);
    }

}
