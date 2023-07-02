package com.example.lab1czarnecki;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText imieEt;
    private EditText nazwiskoEt;
    private EditText ocenyEt;
    private Button potwierdzBt;
    private TextView sredniaTv;
    private Button sukcesBt;
    private Button porazkaBt;

    public static String LICZBA_OCEN_KEY = "LICZBA_OCEN";

    private ActivityResultLauncher<Intent> launcher;

    Bundle pojemnik;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imieCheck();
        nazwiskoCheck();
        liczbaOcenCheck();
        przyciskOceny();


        imieEt = findViewById(R.id.imie_et);
        nazwiskoEt = findViewById(R.id.nazwisko_et);
        sredniaTv = findViewById(R.id.srednia_tv);
        potwierdzBt = findViewById(R.id.przycisk_oceny);
        sukcesBt = findViewById(R.id.sukces_bt);
        porazkaBt = findViewById(R.id.porazka_bt);
        ocenyEt = findViewById(R.id.liczba_ocen_et);

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        String imie = sharedPreferences.getString("imie", "");
        imieEt.setText(imie);
        String nazwisko = sharedPreferences.getString("nazwisko", "");
        nazwiskoEt.setText(nazwisko);
        String ocena = sharedPreferences.getString("oceny", "");
        ocenyEt.setText(ocena);

        potwierdzBt.setOnClickListener( v -> {
            Intent intent = new Intent(this,OcenyActivity.class);
            Bundle pojemnik = new Bundle();
            pojemnik.putInt(this.LICZBA_OCEN_KEY,Integer.valueOf(this.ocenyEt.getText().toString()));
            intent.putExtras(pojemnik);
            this.launcher.launch(intent);
        });



        this.launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            this.obliczSrednia(result);
        });

        przycisk_koncowy();


    }

    private void przycisk_koncowy(){
        sukcesBt = findViewById(R.id.sukces_bt);
        this.sukcesBt.setOnClickListener(view ->{
            Toast.makeText(this,getString(R.string.sukces),Toast.LENGTH_SHORT).show();
            finish();
        });

        porazkaBt = findViewById(R.id.porazka_bt);
        this.porazkaBt.setOnClickListener(view ->{
            Toast.makeText(this,getString(R.string.porazka),Toast.LENGTH_SHORT).show();
            finish();
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        float srednia = sharedPreferences.getFloat("srednia", 0);
        if(srednia >= 2){
            this.sredniaTv.setText(String.format(getString(R.string.srednia),srednia));
            this.sredniaTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imie", imieEt.getText().toString());
        editor.putString("nazwisko", nazwiskoEt.getText().toString());
        editor.putString("oceny", ocenyEt.getText().toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("imie");
        editor.remove("nazwisko");
        editor.remove("oceny");
        editor.remove("srednia");
        editor.remove("ocenyString");
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = sharedPreferences.getString("imie", "");
        imieEt.setText(name);

        String surname = sharedPreferences.getString("nazwisko", "");
        nazwiskoEt.setText(surname);

        String number = sharedPreferences.getString("oceny", "");
        ocenyEt.setText(number);

        float srednia = sharedPreferences.getFloat("srednia", 0);
        if(srednia >= 2){
            this.sredniaTv.setText(String.format(getString(R.string.srednia),srednia));
            this.sredniaTv.setVisibility(View.VISIBLE);
        }
    }

    private void obliczSrednia(ActivityResult result) {
        if(result.getResultCode()==RESULT_OK){
            pojemnik = result.getData().getExtras();
            int liczbaOcen = pojemnik.getInt(this.LICZBA_OCEN_KEY);
            int[] oceny = new int[liczbaOcen];
            for(int i=0 ; i < liczbaOcen ; i++){
                oceny[i] = pojemnik.getInt(OcenyActivity.OCENA_NR_KEY + i);
            }
            float srednia =(float) Arrays.stream(oceny).sum()/(float)liczbaOcen;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("srednia", srednia);
            editor.apply();
            this.sredniaTv.setText(String.format(getString(R.string.srednia),srednia));
            this.sredniaTv.setVisibility(View.VISIBLE);
            if(srednia >= 3.0){
                this.sukcesBt.setVisibility(View.VISIBLE);
                this.porazkaBt.setVisibility(View.GONE);
                this.sukcesBt.setText(R.string.srednia_ok);
            }else{
                this.porazkaBt.setVisibility(View.VISIBLE);
                this.sukcesBt.setVisibility(View.GONE);
                this.porazkaBt.setText(R.string.srednia_bad);
            }
        }
        else{
            this.sredniaTv.setVisibility(View.GONE);
            this.potwierdzBt.setVisibility(View.GONE);
        }
    }

    public void imieCheck(){
        EditText imieEt = findViewById(R.id.imie_et);
        imieEt.setOnFocusChangeListener((view, b) -> {
            if(b){
                return;
            }
            else{
                if(!poprawneImie(imieEt.getText().toString())){
                    imieEt.setError(getString(R.string.blad_imie));
                    Toast toast = Toast.makeText(this,R.string.blad_imie,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void nazwiskoCheck(){
        EditText nazwiskoEt = findViewById(R.id.nazwisko_et);
        nazwiskoEt.setOnFocusChangeListener((view, b) -> {
            if(b){
                return;
            }
            else{
                if(!poprawneNazwisko(nazwiskoEt.getText().toString())){
                    nazwiskoEt.setError(getString(R.string.blad_nazwisko));
                    Toast toast = Toast.makeText(this,R.string.blad_nazwisko,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void liczbaOcenCheck(){
        EditText liczbaOcenEt = findViewById(R.id.liczba_ocen_et);
        liczbaOcenEt.setOnFocusChangeListener((view, b) -> {
            if(b){
                return;
            }
            else{
                if(!poprawnaLiczba(liczbaOcenEt.getText().toString())){
                    liczbaOcenEt.setError(getString(R.string.blad_liczbaOcen));
                    Toast toast = Toast.makeText(this,R.string.blad_liczbaOcen,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void przyciskOceny(){
        EditText imieEt = findViewById(R.id.imie_et);
        EditText nazwiskoEt = findViewById(R.id.nazwisko_et);
        EditText liczbaOcenEt = findViewById(R.id.liczba_ocen_et);
        Button przyciskOcenyEt = findViewById(R.id.przycisk_oceny);


        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isValid = poprawneImie(imieEt.getText().toString())
                        && poprawneNazwisko(nazwiskoEt.getText().toString())
                        && poprawnaLiczba(liczbaOcenEt.getText().toString());
                if (isValid) {
                    przyciskOcenyEt.setVisibility(View.VISIBLE);
                } else {
                    przyciskOcenyEt.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        imieEt.addTextChangedListener(inputWatcher);
        nazwiskoEt.addTextChangedListener(inputWatcher);
        liczbaOcenEt.addTextChangedListener(inputWatcher);

    }
    public boolean poprawneImie(String imie){
        return imie.matches("^[A-Z]{1}[a-z]{2,8}$");
    }

    public boolean poprawneNazwisko(String nazwisko){
        return nazwisko.matches("^([A-Z]{1}[a-z]{1,15}|[A-Z]{1}[a-z]{1,15}-[A-Z]{1}[a-z]{1,15})$");
    }

    public boolean poprawnaLiczba(String liczbaOcen){
        int value_int;
        try{
            value_int = Integer.parseInt(liczbaOcen);
        } catch(NumberFormatException e){
            value_int = 0;
        }
        return value_int<=15 && value_int>=5;
    }


}