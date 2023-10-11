package com.colak.sukkb.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SilActivity extends AppCompatActivity {

    private TextInputLayout inputOgrNo;
    private EditText editOgrNo;
    private Button btnIhbarSil;

    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sil);

        inputOgrNo = (TextInputLayout)findViewById(R.id.sil_act_inputOgrNo);
        editOgrNo = (EditText)findViewById(R.id.sil_act_editOgrNo);
        btnIhbarSil = (Button)findViewById(R.id.sil_act_btnIhbarSil);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        btnIhbarSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SilActivity.this, "İhbar Sil butonuna basıldı.", Toast.LENGTH_SHORT).show();
                String OgrNo = editOgrNo.getText().toString();

                if (!TextUtils.isEmpty(OgrNo)) {
                    mFirestore.collection("Ihbarlar")
                            .whereEqualTo("OgrNo", OgrNo)
                            .whereEqualTo("KullaniciId", mUser.getUid())
                            .get()
                            .addOnCompleteListener(SilActivity.this, new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            Toast.makeText(SilActivity.this, "Silme işlemi için uygun veri bulunamadı.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        for (DocumentSnapshot document : task.getResult()) {
                                            document.getReference().delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> deleteTask) {
                                                            if (deleteTask.isSuccessful()) {
                                                                Toast.makeText(SilActivity.this, "İhbar başarıyla silindi.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(new Intent(SilActivity.this, MainActivity.class));
                                                            } else {
                                                                Toast.makeText(SilActivity.this, "İhbar silinirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(SilActivity.this, "Sorgu işlemi başarısız oldu.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else
                    inputOgrNo.setError("Lütfen Geçerli Öğrenci Numarasını Giriniz.");
            }
        });

    }
}