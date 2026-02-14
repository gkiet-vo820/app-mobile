package com.example.appbanhang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;


import com.example.appbanhang.R;
import com.example.appbanhang.adapter.OrdersAdapter;
import com.example.appbanhang.api.MomoService;
import com.example.appbanhang.api.OrdersService;
import com.example.appbanhang.api.VnpayService;
import com.example.appbanhang.model.DetailOrders;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.util.AddressHelper;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbarThanhToan;
    TextView txtTongTien, txtEmail, txtSDT;
    TextInputEditText edtSoNha;
    Spinner spnProvince, spnDistrict, spnWard;
    AppCompatButton btnXacNhan;
    RadioGroup radioGroupPayment;

    OrdersService ordersService;
    OrdersAdapter ordersAdapter;
    List<Orders> dsOrders;
    long tongtien;
    ProgressDialog progressDialog;
    MomoService momoService;
    VnpayService vnpayService;
    AddressHelper addressHelper;

    private static final int TYPE_CASH = 0;
    private static final int TYPE_MOMO = 1;
    private static final int TYPE_VNPAY = 2;

    // Request Code để phân biệt các app thanh toán
    private static final int REQ_MOMO = 1000;
    private static final int REQ_VNPAY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        addControls();
        getIntentData();
        addEvents();
        ActionBar();
        Log.d("CART_DEBUG", "Cart size = " + Utils.dsShoppingCart.size());

    }

    private void ActionBar() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addControls(){
        checkCart();

        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);

        edtSoNha = findViewById(R.id.edtSoNha);
        spnProvince = findViewById(R.id.spnProvince);
        spnDistrict = findViewById(R.id.spnDistrict);
        spnWard = findViewById(R.id.spnWard);

        addressHelper = new AddressHelper(this, spnProvince, spnDistrict, spnWard);
        addressHelper.setupAddressSpinners();

        btnXacNhan = findViewById(R.id.btnXacNhan);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);

        dsOrders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, dsOrders);
        ordersService = new OrdersService(this, ordersAdapter, dsOrders);
        progressDialog = new ProgressDialog(this);

        momoService = new MomoService(this);
        vnpayService = new VnpayService(this);
    }

    private void checkCart() {
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Utils.dsShoppingCart = CartStorage.loadCart(this);
        }
        if (Utils.dsShoppingCart == null) {
            Utils.dsShoppingCart = new ArrayList<>();
        }
    }

    private void getIntentData(){
        Intent intent = getIntent();
        tongtien = intent.getLongExtra("tongtien", 0);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien)+"Đ");
        txtEmail.setText(Utils.user_current.getEmail());
        txtSDT.setText(Utils.user_current.getSdt());
    }
    private void addEvents(){
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = radioGroupPayment.getCheckedRadioButtonId();
                int type = TYPE_CASH;
                if (checkedId == R.id.rbtMomo) {
                    type = TYPE_MOMO;
                } else if (checkedId == R.id.rbtVnpay) {
                    type = TYPE_VNPAY;
                }

                if (type == TYPE_MOMO && !momoService.isAppInstalled()) {
                    Toast.makeText(PaymentActivity.this, "Vui lòng cài đặt MoMo!", Toast.LENGTH_SHORT).show();
                    return;
                }

                order(type);
            }
        });

//        btnThanhToanMomo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (momoService.isAppInstalled()) {
//                    order(TYPE_MOMO);
//                } else {
//                    Toast.makeText(PaymentActivity.this, "Vui lòng cài đặt App MoMo Sandbox trước khi thanh toán!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        btnThanhToanVnpay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                order(TYPE_VNPAY);
//            }
//        });
    }

    private final ActivityResultLauncher<Intent> vnpayInPaymentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String orderIdStr = result.getData().getStringExtra("ORDER_ID");
                    if (orderIdStr != null) {
                        int orderId = Integer.parseInt(orderIdStr);
                        updateStatusToBackend(orderId);
                    }
                } else {
                    Toast.makeText(this, "Thanh toán không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
    );



    private void order(int paymentType){
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String soNha = edtSoNha.getText().toString().trim();
        String phuong = spnWard.getSelectedItem() != null ? spnWard.getSelectedItem().toString() : "";
        String quan = spnDistrict.getSelectedItem() != null ? spnDistrict.getSelectedItem().toString() : "";
        String tinh = spnProvince.getSelectedItem() != null ? spnProvince.getSelectedItem().toString() : "";
        if(soNha.isEmpty() || tinh.equals("Chọn Tỉnh/Thành")){
            Toast.makeText(this, "Vui lòng chọn đầy đủ địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }
        String diachiFull = soNha + ", " + phuong + ", " + quan + ", " + tinh;

        List<DetailOrders> details = new ArrayList<>();
        int totalQty = 0;
        for (ShoppingCart item : Utils.dsShoppingCart) {
            Log.d("ORDER_CHECK", "Sản phẩm: " + item.getTensp() + " | isSelected: " + item.isSelected());
            if(item.isSelected()){
                DetailOrders detail = new DetailOrders();
                detail.setProductId(item.getId());
                detail.setQuantity(item.getSoluong());
                detail.setPrice(item.getGiasp());
                details.add(detail);
                totalQty += item.getSoluong();
            }
        }
        if (details.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống hoặc chưa có sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Orders order = new Orders();
        order.setUserId(Utils.user_current.getId());
        order.setEmail(Utils.user_current.getEmail());
        order.setNumberphone(Utils.user_current.getSdt());
        order.setAddress(diachiFull);
        order.setQuantity(totalQty);
        order.setTotalpayment(tongtien);

        if (paymentType == TYPE_VNPAY) {
            order.setPaymentMethod("VNPAY");
        } else if (paymentType == TYPE_MOMO) {
            order.setPaymentMethod("MOMO");
        } else {
            order.setPaymentMethod("COD");
        }

        order.setDetailOrders(details);

        progressDialog.setMessage("Đang xử lý đơn hàng...");
        progressDialog.setCancelable(false);
        ordersService.postOrder(order, new OrdersService.OrderCallback(){
            @Override
            public void onStart() {
                progressDialog.setMessage("Đang tạo đơn hàng...");
                progressDialog.show();
            }

            @Override
            public void onSuccess(int orderId){
                if(orderId > 0){
                    Paper.book().write("last_order_id", orderId);
                }
                if (paymentType == TYPE_MOMO){
                    momoService.momoPayment((int) tongtien, String.valueOf(orderId));
                } else if (paymentType == TYPE_VNPAY) {
                    vnpayService.getPaymentUrl(orderId, tongtien, new VnpayService.VnpayUrlCallback() {
                        @Override
                        public void onUrlReceived(String url) {
                            Intent intent = new Intent(PaymentActivity.this, VnpayActivity.class);
                            intent.putExtra("url", url);
                            // QUAN TRỌNG: Phải dùng Launcher này để ShoppingCartActivity nhận được kết quả
                            vnpayInPaymentLauncher.launch(intent);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handleSuccessOrder("Đặt hàng thành công!");
                }

            }

            @Override
            public void onError(String message) {
                Toast.makeText(PaymentActivity.this, "Lỗi đặt hàng: " + message, Toast.LENGTH_LONG).show();
                Log.e("ORDER_ERROR", "Error: " + message);
            }

            @Override
            public void onFinish() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void cleanCartAfterOrder() {
        if (Utils.dsShoppingCart != null) {
            // Cách xóa an toàn: dùng Iterator hoặc list phụ
            List<ShoppingCart> itemsToRemove = new ArrayList<>();
            for (ShoppingCart item : Utils.dsShoppingCart) {
                if (item.isSelected()) {
                    itemsToRemove.add(item);
                }
            }
            Utils.dsShoppingCart.removeAll(itemsToRemove);
            // Lưu lại giỏ hàng mới (đã trừ các món đã đặt) vào Paper/SharedPref
            CartStorage.saveCart(this, Utils.dsShoppingCart);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_MOMO && data != null) {
            int status = data.getIntExtra("status", -1);
            if (status == 0) {
                int newOrderId = Paper.book().read("last_order_id", 0);
                if (newOrderId > 0) {
                    updatePaymentStatus(newOrderId, "Momo");
                }
            } else {
                Toast.makeText(this, "Thanh toán thất bại hoặc đã hủy", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_VNPAY && resultCode == RESULT_OK && data != null) {
            String vnpCode = data.getStringExtra("vnp_ResponseCode");
            if ("00".equals(vnpCode)) {
                updatePaymentStatus(Paper.book().read("last_order_id", 0), "VNPay");
            } else {
                Toast.makeText(this, "Thanh toán VNPay thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePaymentStatus(int orderId, String method){
        ordersService.confirmPayment(orderId, new OrdersService.StatusUpdateListener() {
            @Override
            public void onUpdateSuccess(int position, int newStatus) {
                handleSuccessOrder("Thanh toán qua " + method + " thành công!");
            }

            @Override
            public void onUpdateError(String message) {
                handleSuccessOrder("Đã thanh toán nhưng lỗi cập nhật hệ thống: " + message);
            }
        });
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        handleVnpayResponse(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleVnpayResponse(getIntent());
    }

    private void handleVnpayResponse(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && "vnpayapp".equals(uri.getScheme())) {
            String responseCode = uri.getQueryParameter("vnp_ResponseCode");
            String vnpTxnRef = uri.getQueryParameter("vnp_TxnRef"); // Dạng "123_164440..."

            if ("00".equals(responseCode) && vnpTxnRef != null) {
                // TÁCH LẤY ID THỰC (Phần trước dấu _)
                String realOrderId = vnpTxnRef.contains("_") ? vnpTxnRef.split("_")[0] : vnpTxnRef;

                updateStatusToBackend(Integer.parseInt(realOrderId));
                cleanCartAfterOrder();

                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();

                // Quay về màn hình chính và xóa các Activity trước đó
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            } else if (responseCode != null) {
                Toast.makeText(this, "Giao dịch thất bại. Mã lỗi: " + responseCode, Toast.LENGTH_SHORT).show();
            }
            // Xóa data để không bị lặp lại logic khi xoay màn hình
            intent.setData(null);
        }
    }
    private void updateStatusToBackend(int orderId){
        ordersService.confirmPayment(orderId, new OrdersService.StatusUpdateListener() {
            @Override
            public void onUpdateSuccess(int position, int newStatus) {
                handleSuccessOrder("Thanh toán thành công!");
            }

            @Override
            public void onUpdateError(String message) {
                // Log lỗi nhưng vẫn có thể cho qua vì tiền đã trừ
                Log.e("VNPAY", "Backend confirm error: " + message);
                handleSuccessOrder("Thanh toán hoàn tất!");
            }
        });
    }

    private void handleSuccessOrder(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        cleanCartAfterOrder();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (ordersService != null) {
            ordersService.clear();
        }
        super.onDestroy();
    }
}