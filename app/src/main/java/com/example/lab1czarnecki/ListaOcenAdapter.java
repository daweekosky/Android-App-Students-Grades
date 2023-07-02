package com.example.lab1czarnecki;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaOcenAdapter extends RecyclerView.Adapter<WierszOcenyViewHolder> {
    private List<Przedmiot> listaPrzedmiotow;
    private LayoutInflater layoutInflater;
    public ListaOcenAdapter(List<Przedmiot> listaPrzedmiotow, LayoutInflater layoutInflater){
        this.listaPrzedmiotow = listaPrzedmiotow;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public WierszOcenyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wiersz = this.layoutInflater.inflate(R.layout.wiersz_oceny,null);
        return new WierszOcenyViewHolder(wiersz);
    }

    @Override
    public void onBindViewHolder(@NonNull WierszOcenyViewHolder holder, int position) {
        Przedmiot przedmiot = this.listaPrzedmiotow.get(position);
        RadioGroup ocenaRg = holder.getOcenaPrzedmiotuRg();
        ocenaRg.setTag(przedmiot);
        holder.getNazwaPrzedmiotuTv().setText(przedmiot.getNazwa());
        switch (przedmiot.getOcena()) {
            case 2:
                ocenaRg.check(R.id.ocena_2);
                break;
            case 3:
                ocenaRg.check(R.id.ocena_3);
                break;
            case 4:
                ocenaRg.check(R.id.ocena_4);
                break;
            case 5:
                ocenaRg.check(R.id.ocena_5);
                break;
        }
    }

    @Override
    public int getItemCount(){
        return this.listaPrzedmiotow.size();
    }
}
