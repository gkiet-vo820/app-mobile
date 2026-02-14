package com.example.appbanhang.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddressHelper {
    private Context context;
    private Spinner spnProvince, spnDistrict, spnWard;
    private List<Province> provinceList;

    public AddressHelper(Context context, Spinner spnProvince, Spinner spnDistrict, Spinner spnWard) {
        this.context = context;
        this.spnProvince = spnProvince;
        this.spnDistrict = spnDistrict;
        this.spnWard = spnWard;
    }

    class Province {
        String name;
        List<District> districts;
        @Override public String toString() { return name; }
    }

    class District {
        String name;
        List<String> wards;
        @Override public String toString() { return name; }
    }

    public void setupAddressSpinners() {

        String json = loadJSONFromAsset();
        provinceList = new Gson().fromJson(json, new TypeToken<List<Province>>(){}.getType());

        if (provinceList == null) provinceList = new ArrayList<>();
        setupAdapter(spnProvince, provinceList);
        // 1. Tạo dữ liệu mẫu (Sau này bạn có thể thay bằng file JSON)
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<District> districts = provinceList.get(position).districts;
                updateDistrictSpinner(districts);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void updateDistrictSpinner(List<District> districts) {
        setupAdapter(spnDistrict, districts);

        // 4. Sự kiện chọn Huyện -> Cập nhật Xã
        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> wards = districts.get(position).wards;
                updateWardSpinner(wards);

            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void updateWardSpinner(List<String> wards) {
        setupAdapter(spnWard, wards);
    }

    private <T> void setupAdapter(Spinner spinner, List<T> data) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private String loadJSONFromAsset() {
        try {
            InputStream is = context.getAssets().open("provinces.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) { return "[]"; }
    }
}
