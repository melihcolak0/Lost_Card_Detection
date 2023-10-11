package com.colak.sukkb.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GunSilActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private DocumentReference docRef;

    private TextInputLayout inputAdSoyad, inputOgrEposta, inputFakulte, inputBolum;
    private EditText editAdSoyad, editOgrEposta, editFakulte, editBolum;
    private String txtAdSoyad, txtOgrEposta, txtFakulte, txtBolum;
    private Dialog kayitSilDialog;
    private ImageButton kayitSil, kayitSilme;
    private Window kayitSilWindow;
    private Context mContext;



    private ImageButton imgBtnGuncelle, imgBtnSil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gun_sil);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        inputAdSoyad = (TextInputLayout)findViewById(R.id.gun_sil_InputAdSoyad);
        inputOgrEposta = (TextInputLayout)findViewById(R.id.gun_sil_InputOgrEposta);
        inputFakulte = (TextInputLayout)findViewById(R.id.gun_sil_InputFakulte);
        inputBolum = (TextInputLayout)findViewById(R.id.gun_sil_InputBolum);

        editAdSoyad = (EditText)findViewById(R.id.gun_sil_EditAdSoyad);
        editOgrEposta = (EditText)findViewById(R.id.gun_sil_EditOgrEposta);
        editFakulte = (EditText)findViewById(R.id.gun_sil_EditFakulte);
        editBolum = (EditText)findViewById(R.id.gun_sil_EditBolum);

        imgBtnGuncelle = (ImageButton)findViewById(R.id.gun_sil_imgBtnGuncelle);
        imgBtnSil = (ImageButton)findViewById(R.id.gun_sil_imgBtnSil);

        mContext = this;

        imgBtnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { btnGuncelle(v); }
        });

        imgBtnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { btnSil(v); }
        });

    }

    private void  verileriGetir(String uid){
        docRef = mFirestore.collection("Kullanıcılar").document(uid);
        docRef.get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            mData = (HashMap)documentSnapshot.getData();

                            /*for(Map.Entry data : mData.entrySet())
                                System.out.println(data.getKey() + " = " + data.getValue());*/
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GunSilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void btnGuncelle(View view){

        txtAdSoyad = editAdSoyad.getText().toString();
        txtOgrEposta = editOgrEposta.getText().toString();
        txtFakulte = editFakulte.getText().toString();
        txtBolum = editBolum.getText().toString();

        mData = new HashMap<>();

        if (!TextUtils.isEmpty(txtAdSoyad)){
            mData.put("kullaniciAdSoyad", txtAdSoyad);
        }
        if (!TextUtils.isEmpty(txtOgrEposta)){
            mData.put("kullaniciEposta", txtOgrEposta);
        }
        if (!TextUtils.isEmpty(txtFakulte)){
            mData.put("kullaniciFakulte", txtFakulte);
        }
        if (!TextUtils.isEmpty(txtBolum)){
            mData.put("kullaniciBolum", txtBolum);
        }

        assert mUser != null;
        veriyiGuncelle(mData, mUser.getUid());
    }

    private void veriyiGuncelle(HashMap<String, Object> hashMap, final String uid){
        mFirestore.collection("Kullanıcılar").document(uid)
                .update(hashMap)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(GunSilActivity.this, "Veriler Başarıyla Güncellendi.", Toast.LENGTH_SHORT).show();
                            verileriGetir(uid);
                            finish();
                            startActivity(new Intent(GunSilActivity.this, MainActivity.class));
                        }
                    }
                });
    }

    private void btnSil(View view){

        kaydiSil(view);


    }

    private void kaydiSil (View view){
        kayitSilDialog = new Dialog(mContext);
        kayitSilDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        kayitSilWindow = kayitSilDialog.getWindow();
        kayitSilWindow.setGravity(Gravity.CENTER);
        kayitSilDialog.setContentView(R.layout.custom_dialog_kayit_sil);

        kayitSil = kayitSilDialog.findViewById(R.id.custom_dialog_kayit_sil_btnEvet);
        kayitSilme = kayitSilDialog.findViewById(R.id.custom_dialog_kayit_sil_btnHayir);

        kayitSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(GunSilActivity.this, "Silme işlemi yapılacak yer.", Toast.LENGTH_SHORT).show();
                mFirestore.collection("Kullanıcılar").document(mUser.getUid())
                        .delete()
                        .addOnCompleteListener(GunSilActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(GunSilActivity.this, "Veriler Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                if (mUser != null) {
                    mUser.reload()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if(mUser != null){
                                            mUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(GunSilActivity.this, "Profil Kaydınız Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent intentKyt = new Intent(GunSilActivity.this, GirisYapActivity.class);
                                                                startActivity(intentKyt);
                                                            }else{
                                                            Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        kayitSilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitSilDialog.dismiss();
            }
        });

        kayitSilWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        kayitSilDialog.show();
    }




}



/*mFirestore.collection("Kullanıcılar").document(mUser.getUid())
                .delete()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(GunSilActivity.this, "Veriler Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                                        //else
                                            //Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

        if (mUser != null) {
            mUser.reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if(mUser != null){
                                    mUser.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(GunSilActivity.this, "Profil Kaydınız Başarıyla Silindi.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        Intent intentKyt = new Intent(GunSilActivity.this, GirisYapActivity.class);
                                                        startActivity(intentKyt);
                                                    }//else{
                                                        //Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    //}
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(GunSilActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }*/