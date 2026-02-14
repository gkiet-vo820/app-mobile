package com.example.appbanhang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanhang.R;

public class VnpayActivity extends AppCompatActivity {
    WebView webView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vnpay);

        webView = findViewById(R.id.webviewVnpay);
        toolbar = findViewById(R.id.toolbarVNPAY);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thanh toán VNPay");

        toolbar.setNavigationOnClickListener(v -> finish());

        String url = getIntent().getStringExtra("url");
        if (url != null) {
            setupWebView();
            webView.loadUrl(url);
        } else {
            finish();
        }


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack(); // Quay lại trang trước đó trong WebView
                } else {
                    finish(); // Nếu không còn trang nào để lùi thì đóng Activity
                }
            }
        });
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                // 1. Xử lý URL Redirect từ VNPay (http://success.sdk)
                if (url.contains("success.sdk")) {
                    handleVnpayResponse(url);
                    return true; // Chặn WebView không load trang này (tránh lỗi trắng trang)
                }

                // 2. Mở App VNPAY hoặc App Ngân hàng
                if (url.startsWith("vnpayapp://") || (!url.startsWith("http") && !url.startsWith("https"))) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        finish();
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void handleVnpayResponse(String url) {
        Uri uri = Uri.parse(url);
        String responseCode = uri.getQueryParameter("vnp_ResponseCode");

        String txnRef = uri.getQueryParameter("vnp_TxnRef");
        String orderId = (txnRef != null) ? txnRef.split("_")[0] : null;

        Intent intent = new Intent();
        intent.putExtra("vnp_ResponseCode", responseCode); // Trả về mã lỗi/thành công
        intent.putExtra("ORDER_ID", orderId);
        if ("00".equals(responseCode) && orderId != null) {
            setResult(RESULT_OK, intent);
//            Intent intent = new Intent();
//            intent.putExtra("ORDER_ID", orderId);
//            setResult(RESULT_OK, intent);
//            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Thanh toán thất bại (Mã lỗi: " + responseCode + ")", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }
        finish(); // Đóng Activity ngay lập tức
    }

}