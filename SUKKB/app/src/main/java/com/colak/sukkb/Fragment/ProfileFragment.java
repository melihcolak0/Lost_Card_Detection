package com.colak.sukkb.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.colak.sukkb.Activity.GunSilActivity;
import com.colak.sukkb.Activity.SilActivity;
import com.colak.sukkb.Model.Kullanici;
import com.colak.sukkb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {

    private EditText editAdSoyad, editEposta, editFakulte, editBolum;
    private View v;

    private FirebaseFirestore mFireStore;
    private DocumentReference mRef;
    private FirebaseUser mUser;
    private Kullanici user;
    private ImageButton imgBtnAyarlar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        editAdSoyad = v.findViewById(R.id.profile_fragment_editAdSoyad);
        editEposta = v.findViewById(R.id.profile_fragment_editEposta);
        editFakulte = v.findViewById(R.id.profile_fragment_editFakulte);
        editBolum = v.findViewById(R.id.profile_fragment_editBolum);

        imgBtnAyarlar = v.findViewById(R.id.profile_fragment_imgBtnAyarlar);

        imgBtnAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { btnAyarlar(v); }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        mRef = mFireStore.collection("Kullanıcılar").document(mUser.getUid());
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(value != null && value.exists()){
                    user = value.toObject(Kullanici.class);

                    if(user != null){
                        editAdSoyad.setText(user.getKullaniciAdSoyad());
                        editEposta.setText(user.getKullaniciEposta());
                        editFakulte.setText(user.getKullaniciFakulte());
                        editBolum.setText(user.getKullaniciBolum());


                    }
                }
            }
        });


        return v;
    }

    public void btnAyarlar (View view){
        Intent intent = new Intent(requireContext(), GunSilActivity.class);
        startActivity(intent);
    }
}