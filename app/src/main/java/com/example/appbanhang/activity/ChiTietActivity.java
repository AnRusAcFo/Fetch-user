package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota, txtdanhgia;
    Button btnthem, btnytb, btndanhgia;
    ImageView imghinhanh, search;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    FrameLayout frameLayoutGioHang;
    ApiBanHang apiBanHang;
    EditText danhgia;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolbar();
        initData();
        intitControl();
    }

    private void intitControl() {
        btnthem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                themGioHang();
                Paper.book().write("giohang", Utils.manggiohang);
            }
        });
        btnytb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), YoutubeActivity.class);
                intent.putExtra("linkyoutube", sanPhamMoi.getLinkyoutube());
                startActivity(intent);
            }
        });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0) {
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                if (Utils.manggiohang.get(i).getId() == sanPhamMoi.getId()) {
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());

                    flag = true;
                }
            }
            if (flag == false) {
                Long gia = Long.parseLong(sanPhamMoi.getGia()) ;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setId(sanPhamMoi.getId());
                gioHang.setTen(sanPhamMoi.getTensp());
                gioHang.setHinh(sanPhamMoi.getHinhanh());
                gioHang.setSoluongkho(sanPhamMoi.getSoluongkho());
                Utils.manggiohang.add(gioHang);
            }
        }
        else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            Long gia = Long.parseLong(sanPhamMoi.getGia());
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setId(sanPhamMoi.getId());
            gioHang.setTen(sanPhamMoi.getTensp());
            gioHang.setHinh(sanPhamMoi.getHinhanh());
            gioHang.setSoluongkho(sanPhamMoi.getSoluongkho());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0 ;
        for(int i = 0 ; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra(  "chitiet");
//        slk.setText(sanPhamMoi.getSoluongkho());
//        danhgia.setText(sanPhamMoi.getDanhgia());
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        txtdanhgia.setText(sanPhamMoi.getDanhgia());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText(decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia())) + " đ");
        List<Integer> so = new ArrayList<>();
        for (int i = 1; i<sanPhamMoi.getSoluongkho()+1; i++){
            so.add(i);
        }
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);
//        btndanhgia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AddComent();
//            }
//        });
    }

    private void AddComent() {
        String str_danhgia = danhgia.getText().toString().trim();
        compositeDisposable.add(apiBanHang.themdanhgia(sanPhamMoi.getId(), str_danhgia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Đáng giá sản phẩm thành công", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Không thành công", Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void initView() {
//        slk = findViewById(R.id.txtslk);
//        btndanhgia = findViewById(R.id.btndanhgia);
        txtdanhgia = findViewById(R.id.txtdanhgia);
        search = findViewById(R.id.search);
        danhgia = findViewById(R.id.danhgiasp);
        btnytb = findViewById(R.id.btnyoutube);
        tensp = findViewById(R.id.txtten);
        giasp = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toobar);
        badge = findViewById(R.id.menu_sl);
        frameLayoutGioHang = findViewById(R.id.framegiohang);
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
        if(Utils.manggiohang != null){
            int totalItem = 0 ;
            for(int i = 0 ; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        imghinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HinhAnh3DActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TImKiemActivity.class);
                startActivity(intent);
            }
        });
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}