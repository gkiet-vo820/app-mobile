package com.example.appbanhang.activity.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.admin.StatisticalService;
import com.example.appbanhang.model.Statistical;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticalActivity extends AppCompatActivity {
    Toolbar toolbarThongKe;
    TextView txtTongTien;
    BarChart barChart;
    StatisticalService statisticalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical);

        addControls();
        ActionBar();
        showBarChat();
    }

    private void ActionBar(){
        setSupportActionBar(toolbarThongKe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarThongKe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolbarThongKe = findViewById(R.id.toolbarThongKe);
        txtTongTien = findViewById(R.id.txtTongTien);
        barChart = findViewById(R.id.barChart);

        statisticalService = new StatisticalService(this);
    }

    private void showBarChat(){
        statisticalService.getRevenue7Days(new StatisticalService.RevenueCallback() {
            @Override
            public void onSuccess(List<Statistical> dsStatistical) {
                long total = 0;
                for (Statistical statistical : dsStatistical){
                    total += statistical.getAmount();
                }

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                txtTongTien.setText("Tổng doanh thu là: " + decimalFormat.format(total) + "VNĐ");

                drawChart(dsStatistical);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(StatisticalActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawChart(List<Statistical> dsStatistical){
        if (dsStatistical == null || dsStatistical.isEmpty()) {
            barChart.clear();
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < dsStatistical.size(); i++){
            entries.add(new BarEntry(i, (float) dsStatistical.get(i).getAmount())); // Trục Y: Số tiền (amount)

            String date = dsStatistical.get(i).getDate();
            strings.add(date != null ? date : "");
            //strings.add(dsStatistical.get(i).getData()); // Trục X: Lưu lại nhãn ngày tháng (date)
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Doanh thu (VNĐ)");
        barDataSet.setColor(Color.parseColor("#ff00dfff"));
        barDataSet.setValueTextSize(10f);

        barDataSet.setValueFormatter(new DefaultValueFormatter(0)); // Định dạng số hiển thị trên đầu mỗi cột

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Tinh chỉnh Trục X (Ngày tháng)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(strings.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(strings));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);


        // Tinh chỉnh Trục Y (Ẩn cột bên phải cho thoáng)
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(true);

        // Hiệu ứng mọc cột từ dưới lên
        barChart.setFitBars(true);
        barChart.animateY(1500);
        barChart.getDescription().setEnabled(false); // Ẩn dòng chữ nhỏ góc dưới
        barChart.invalidate();// Làm mới biểu đồ
    }

    @Override
    protected void onDestroy() {
        if (statisticalService != null) {
            statisticalService.clear();
        }
        super.onDestroy();
    }
}