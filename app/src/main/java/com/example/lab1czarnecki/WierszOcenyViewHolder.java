package com.example.lab1czarnecki;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WierszOcenyViewHolder extends RecyclerView.ViewHolder {
    private TextView nazwaPrzedmiotuTv;
    private RadioGroup ocenaPrzedmiotuRg;

    public WierszOcenyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.nazwaPrzedmiotuTv = itemView.findViewById(R.id.nazwa_przedmiotu_tv);
        this.ocenaPrzedmiotuRg = itemView.findViewById(R.id.ocena_przedmiotu_rg);
        this.ocenaPrzedmiotuRg.setOnCheckedChangeListener(((radioGroup, i) -> {
            Przedmiot przedmiot = (Przedmiot) radioGroup.getTag();
            switch(i){
                case R.id.ocena_2:
                    przedmiot.setOcena(2);
                    break;
                case R.id.ocena_3:
                    przedmiot.setOcena(3);
                    break;
                case R.id.ocena_4:
                    przedmiot.setOcena(4);
                    break;
                case R.id.ocena_5:
                    przedmiot.setOcena(5);
                    break;
            }
        }));
    }



    public TextView getNazwaPrzedmiotuTv() {
        return nazwaPrzedmiotuTv;
    }

    public RadioGroup getOcenaPrzedmiotuRg() {
        return ocenaPrzedmiotuRg;
    }

    public void setOcenaPrzedmiotuRg(RadioGroup ocenaPrzedmiotuRg) {
        this.ocenaPrzedmiotuRg = ocenaPrzedmiotuRg;
    }

    public void setNazwaPrzedmiotuTv(TextView nazwaPrzedmiotuTv) {
        this.nazwaPrzedmiotuTv = nazwaPrzedmiotuTv;
    }
}
