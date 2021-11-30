package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class BudgetActivity extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;
    TextView one,two,three,four,five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Past Shopping Event");

        barChart = findViewById(R.id.idBarChart);

        TinyDB tinyDB = new TinyDB(getApplicationContext());

        barEntriesArrayList = new ArrayList<>();
        barEntriesArrayList.add(new BarEntry(1f, Float.parseFloat(tinyDB.getString("subtotal0"))));
        barEntriesArrayList.add(new BarEntry(2f, Float.parseFloat(tinyDB.getString("subtotal1"))));
        barEntriesArrayList.add(new BarEntry(3f, Float.parseFloat(tinyDB.getString("subtotal2"))));
        barEntriesArrayList.add(new BarEntry(4f, Float.parseFloat(tinyDB.getString("subtotal3"))));
        barEntriesArrayList.add(new BarEntry(5f, Float.parseFloat(tinyDB.getString("subtotal4"))));


        barDataSet = new BarDataSet(barEntriesArrayList, "Your 5 Most Recent Shopping Totals");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

        /*GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, Float.parseFloat(tinyDB.getString("subtotal0"))),
                new DataPoint(1, Float.parseFloat(tinyDB.getString("subtotal1"))),
                new DataPoint(2, Float.parseFloat(tinyDB.getString("subtotal2"))),
                new DataPoint(3, Float.parseFloat(tinyDB.getString("subtotal3"))),
                new DataPoint(4, Float.parseFloat(tinyDB.getString("subtotal4")))
        });
        graph.addSeries(series);*/


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