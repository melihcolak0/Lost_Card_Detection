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

import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisYapActivity extends AppCompatActivity {

    //private ProgressDialog mProgress;

    private FirebaseUser mUser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private LinearLayout mLinear;
    private TextInputLayout inputEposta, inputSifre;
    private EditText editEposta, editSifre;
    private String txtEmail, txtSifre;

    private void init(){
        mLinear = (LinearLayout)findViewById(R.id.giris_yap_linear);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        inputEposta = (TextInputLayout)findViewById(R.id.giris_yap_InputEposta);
        inputSifre = (TextInputLayout)findViewById(R.id.giris_yap_InputSifre);

        editEposta = (EditText)findViewById(R.id.giris_yap_EditEposta);
        editSifre = (EditText)findViewById(R.id.giris_yap_EditSifre);
    }

    @Override
    public void onBackPressed() {
        // Geri tuşuna basıldığında yapılacak işlemler
        // Örneğin, uygulamadan çıkmak için finish() yöntemini çağırabilirsiniz
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        init();

        if(mUser != null){
            finish();
            startActivity(new Intent(GirisYapActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    public void btnGirisYap(View v){
        txtEmail = editEposta.getText().toString();
        txtSifre = editSifre.getText().toString();

        /*----------Çalışmayan ProgressDialog------------

        if(!TextUtils.isEmpty(txtEmail)){
            if(!TextUtils.isEmpty(txtSifre)){
                mProgress = new ProgressDialog(this);
                mProgress.setTitle("Giriş Yapılıyor...");
                mProgress.show();

                mAuth.signInWithEmailAndPassword(txtEmail, txtSifre)
                        .addOnCompleteListener(GirisYapActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressAyar();
                                    Toast.makeText(GirisYapActivity.this, "Başarıyla Giriş Yaptınız.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(GirisYapActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }else{
                                    progressAyar();
                                    Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else
                inputEposta.setError("Lütfen Geçerli Bir Şifre Giriniz. ");
        }else
            inputEposta.setError("Lütfen Geçerli Bir E-posta Adresi Giriniz. ");*/

        if (!TextUtils.isEmpty(txtEmail)) {
            if (!TextUtils.isEmpty(txtSifre)) {
                ProgressBar progressBar = new ProgressBar(this);
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(txtEmail, txtSifre)
                        .addOnCompleteListener(GirisYapActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressAyar();
                                    Toast.makeText(GirisYapActivity.this, "Başarıyla Giriş Yaptınız.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(GirisYapActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else {
                                    progressAyar();
                                    Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                inputEposta.setError("Lütfen Geçerli Bir Şifre Giriniz. ");
            }
        } else {
            inputEposta.setError("Lütfen Geçerli Bir E-posta Adresi Giriniz. ");
        }
    }

    public void btnGitKayitOl(View v){
        startActivity(new Intent(GirisYapActivity.this, KayitOlActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /*----------Çalışmayan ProgressDialog------------
    private void progressAyar(){
        if(mProgress.isShowing())
            mProgress.dismiss();
    }*/

    private void progressAyar() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }
}