package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CheckoutActivity extends AppCompatActivity {

    EditText randomEditText;
    EditText randomEditText2;
    private ImageView barcode;
    private ImageView qrcode;
    Button completePurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Checkout");

        barcode = findViewById(R.id.barcode);
        qrcode = findViewById(R.id.qrcode);
        randomEditText = findViewById(R.id.randomEditText);
        randomEditText2 = findViewById(R.id.randomEditText2);
        //completePurchaseButton = findViewById(R.id.completePurchaseButton);

        Intent intent = getIntent();
        String stringTotalPrice = intent.getStringExtra("total_price");
        randomEditText.setText("Subtotal: " + stringTotalPrice);
        randomEditText.setEnabled(false);
        Double totalDouble = Double.parseDouble(stringTotalPrice);
        Double totalWithTax = totalDouble*1.14975;
        randomEditText2.setText("Total: " + totalWithTax);
        randomEditText2.setEnabled(false);

        getBarcode();
        getQRCode();


        /*completePurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save shopping event to past events
            }
        });*/

    }

    private void getBarcode(){
        try{
            setBarcode();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getQRCode(){
        try{
            setQRCode();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    public void setBarcode() throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(randomEditText.getText().toString().substring(7), BarcodeFormat.CODE_128,400,170,null);
        // first variable in .encode is what we want to receive, means the total price from
        // bag fragment, for now it is set to random edit text
        // second variable (BarcodeFormat.X), X is type of barcode (UPC, EAN8, EAN 13, CODE_128 etc)

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        barcode.setImageBitmap(bitmap);
        // set generated barcode (bitmap) to image view (barcode)
    }

    public void setQRCode() throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(randomEditText.getText().toString().substring(7), BarcodeFormat.QR_CODE,350,300,null);
        // first variable in .encode is what we want to receive, means the total price from
        // bag fragment, for now it is set to random edit text
        // second variable (BarcodeFormat.X), X is type of barcode (UPC, EAN8, EAN 13, CODE_128 etc)

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        qrcode.setImageBitmap(bitmap);
        // set generated barcode (bitmap) to image view (barcode)
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}