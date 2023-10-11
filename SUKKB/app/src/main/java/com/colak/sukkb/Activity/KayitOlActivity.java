package com.colak.sukkb.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colak.sukkb.Model.Kullanici;
import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class KayitOlActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Kullanici mKullanici;
    private LinearLayout mLinear;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextInputLayout inputAdSoyad, inputEposta, inputFakulte, inputBolum, inputSifre;
    private EditText editAdSoyad, editEposta, editFakulte, editBolum, editSifre;
    private String txtAdSoyad, txtEposta, txtFakulte, txtBolum, txtSifre;

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mLinear = (LinearLayout)findViewById(R.id.kayit_ol_linear);

        inputAdSoyad = (TextInputLayout)findViewById(R.id.kayit_ol_InputAdSoyad);
        inputEposta = (TextInputLayout)findViewById(R.id.kayit_ol_InputEposta);
        inputFakulte = (TextInputLayout)findViewById(R.id.kayit_ol_InputFakulte);
        inputBolum = (TextInputLayout)findViewById(R.id.kayit_ol_InputBolum);
        inputSifre = (TextInputLayout)findViewById(R.id.kayit_ol_InputSifre);

        editAdSoyad = (EditText)findViewById(R.id.kayit_ol_EditAdSoyad);
        editEposta = (EditText)findViewById(R.id.kayit_ol_EditEposta);
        editFakulte = (EditText)findViewById(R.id.kayit_ol_EditFakulte);
        editBolum = (EditText)findViewById(R.id.kayit_ol_EditBolum);
        editSifre = (EditText)findViewById(R.id.kayit_ol_EditSifre);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        init();
    }

    public void btnKayitOl(View v){
        txtAdSoyad = editAdSoyad.getText().toString();
        txtEposta = editEposta.getText().toString();
        txtFakulte = editFakulte.getText().toString();
        txtBolum = editBolum.getText().toString();
        txtSifre = editSifre.getText().toString();

        /*----------Çalışmayan ProgressDialog------------
          if(!TextUtils.isEmpty(txtAdSoyad)){
            if(!TextUtils.isEmpty(txtEposta)){
                if(!TextUtils.isEmpty(txtFakulte)){
                    if(!TextUtils.isEmpty(txtBolum)){
                        if(!TextUtils.isEmpty(txtSifre)){
                            mProggress = new ProgressDialog(this);
                            mProggress.setTitle("Kayıt Olunuyor...");
                            mProggress.show();

                            mAuth.createUserWithEmailAndPassword(txtEposta, txtSifre)
                                    .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                mUser = mAuth.getCurrentUser();

                                                if(mUser != null){
                                                    mKullanici = new Kullanici(txtAdSoyad, txtEposta, mUser.getUid());

                                                    mFireStore.collection("Kullanıcılar").document(mUser.getUid())
                                                            .set(mKullanici)
                                                            .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        proggresAyar();
                                                                        Toast.makeText(KayitOlActivity.this, "Başarıyla Kayıt Oldunuz.", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(new Intent(KayitOlActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    }else{
                                                                        proggresAyar();
                                                                        Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }else{
                                                proggresAyar();
                                                Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else
                            inputSifre.setError("Lütfen Geçerli Bir Şifre Belirleyiniz.");
                    }else
                        inputBolum.setError("Lütfen Geçerli Bir Bölüm Giriniz.");
                }else
                    inputFakulte.setError("Lütfen Geçerli Bir Fakülte Giriniz.");
            }else
                inputEposta.setError("Lütfen Geçerli Bir E-posta Adresi Giriniz.");
        }else
            inputAdSoyad.setError("Lütfen Geçerli Ad ve Soyad Giriniz.");*/

        if (!TextUtils.isEmpty(txtAdSoyad)) {
            if (!TextUtils.isEmpty(txtEposta)) {
                if (!TextUtils.isEmpty(txtFakulte)) {
                    if (!TextUtils.isEmpty(txtBolum)) {
                        if (!TextUtils.isEmpty(txtSifre)) {
                            progressBar = new ProgressBar(this);
                            progressBar.setVisibility(View.VISIBLE);

                            mAuth.createUserWithEmailAndPassword(txtEposta, txtSifre)
                                    .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                mUser = mAuth.getCurrentUser();

                                                if (mUser != null) {
                                                    mKullanici = new Kullanici(txtAdSoyad, txtEposta, mUser.getUid(), txtFakulte, txtBolum);

                                                    mFireStore.collection("Kullanıcılar").document(mUser.getUid())
                                                            .set(mKullanici)
                                                            .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressAyar();
                                                                        Toast.makeText(KayitOlActivity.this, "Başarıyla Kayıt Oldunuz.", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(new Intent(KayitOlActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    } else {
                                                                        progressAyar();
                                                                        Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else {
                                                progressAyar();
                                                Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            inputSifre.setError("Lütfen Geçerli Bir Şifre Belirleyiniz.");
                        }
                    } else {
                        inputBolum.setError("Lütfen Geçerli Bir Bölüm Giriniz.");
                    }
                } else {
                    inputFakulte.setError("Lütfen Geçerli Bir Fakülte Giriniz.");
                }
            } else {
                inputEposta.setError("Lütfen Geçerli Bir E-posta Adresi Giriniz.");
            }
        } else {
            inputAdSoyad.setError("Lütfen Geçerli Ad ve Soyad Giriniz.");
        }
    }

    /*----------Çalışmayan ProgressDialog------------
    private void proggresAyar(){
        if(mProggress.isShowing())
            mProggress.dismiss();
    }*/

    private void progressAyar() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

}