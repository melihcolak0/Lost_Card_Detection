package com.colak.sukkb.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.colak.sukkb.Activity.EkleActivity;
import com.colak.sukkb.Activity.SilActivity;
import com.colak.sukkb.R;


public class EkleFragment extends Fragment {

    private ImageButton imgBtn_ihbarEkle, imgBtn_ihbarSil;

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ekle, container, false);

        ImageButton ekleButton = findViewById(R.id.ekle_fragment_addButton);
        ekleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tıklama olayını burada işleyin
                btn_ihbarEkle(v);
            }
        });
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ekle, container, false);
        ImageButton imgBtn_ihbarEkle = view.findViewById(R.id.ekle_fragment_addButton);
        imgBtn_ihbarEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ihbarEkle(v);
            }
        });

        ImageButton imgBtn_ihbarSil = view.findViewById(R.id.ekle_fragment_trashButton);
        imgBtn_ihbarSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ihbarSil(v);
            }
        });

        return view;
    }

    private void btn_ihbarSil(View v) {
        Intent intentSil = new Intent(requireContext(), SilActivity.class);
        startActivity(intentSil);
    }

    public void btn_ihbarEkle(View view) {
        Intent intent = new Intent(requireContext(), EkleActivity.class);
        startActivity(intent);
    }
}