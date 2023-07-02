package com.example.lab1czarnecki;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OcenyActivity extends AppCompatActivity {
    private RecyclerView listaOcenRv;
    private List<Przedmiot> listaOcen;

    private Button obliczSredniaBt;
    private EditText imieEt;
    private EditText nazwiskoEt;
    private EditText ocenyEt;
    public static String OCENA_NR_KEY = "OCENA_NR_KEY";

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oceny);


        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        int liczbaPrzedmiotow = getIntent().getExtras().getInt(MainActivity.LICZBA_OCEN_KEY);
        String[] przedmioty = getResources().getStringArray(R.array.nazwy_przedmiotow_tab);

        String savedString = sharedPreferences.getString("ocenyString", "");
        int liczbaOcen;
        int[] savedOceny;
        if(savedString != ""){
            StringTokenizer st = new StringTokenizer(savedString, ",");
            liczbaOcen = sharedPreferences.getInt("liczbaOcen",0);
            savedOceny = new int[liczbaOcen];
            for (int i = 0; i < liczbaOcen; i++) {
                savedOceny[i] = Integer.parseInt(st.nextToken());
            }
        }else{
            liczbaOcen = 0;
            savedOceny = new int[0];
        }

        this.listaOcen= new ArrayList<>();
        if(liczbaPrzedmiotow >= liczbaOcen){
            for(int i = 0; i<liczbaOcen;i++){
                this.listaOcen.add(new Przedmiot(przedmioty[i],savedOceny[i]));
            }
            for (int i = liczbaOcen; i < liczbaPrzedmiotow; i++) {
                this.listaOcen.add(new Przedmiot(przedmioty[i]));
            }
        }else{
            for(int i = 0; i<liczbaPrzedmiotow;i++){
                this.listaOcen.add(new Przedmiot(przedmioty[i],savedOceny[i]));
            }
        }
        ListaOcenAdapter adapter = new ListaOcenAdapter(this.listaOcen, this.getLayoutInflater());
        this.listaOcenRv = findViewById(R.id.lista_ocen_rv);
        this.listaOcenRv.setAdapter(adapter);
        this.listaOcenRv.setLayoutManager(new LinearLayoutManager(this));
        this.obliczSredniaBt = findViewById(R.id.przycisk_srednia);
        this.obliczSredniaBt.setOnClickListener(view -> obliczSrednia());


    }


    private void obliczSrednia(){
        int[] oceny = new int[this.listaOcen.size()];
        SharedPreferences.Editor editor = sharedPreferences.edit();
        this.przygotujOceny(oceny);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < oceny.length; i++) {
            str.append(oceny[i]).append(",");
        }
        int liczbaOcen = this.listaOcen.size();
        editor.putInt("liczbaOcen",liczbaOcen);
        editor.putString("ocenyString",str.toString());
        editor.apply();
        Bundle pojemnik = new Bundle();
        this.przygotujDane(pojemnik);
        Intent intent = new Intent();
        intent.putExtras(pojemnik);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void przygotujDane(Bundle pojemnik){
        int liczbaOcen = this.listaOcen.size();
        pojemnik.putInt(MainActivity.LICZBA_OCEN_KEY,liczbaOcen);
        for(int i = 0; i<liczbaOcen; i++){
            pojemnik.putInt(this.OCENA_NR_KEY + i, this.listaOcen.get(i).getOcena());
        }
    }

    private void przygotujOceny(int[] listaOcen){
        int liczbaOcen = this.listaOcen.size();
        for(int i = 0; i<liczbaOcen; i++){
            listaOcen[i] = (this.listaOcen.get(i).getOcena());
        }
    }


}