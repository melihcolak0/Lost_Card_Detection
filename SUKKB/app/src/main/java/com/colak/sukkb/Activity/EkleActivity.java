package com.colak.sukkb.Activity;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.colak.sukkb.Fragment.EkleFragment;
import com.colak.sukkb.Fragment.IhbarFragment;
import com.colak.sukkb.Model.Kullanici;
import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.FieldValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EkleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Kullanici ihbarData;
    private TextInputLayout inputAdSoyad, inputOgrNo, inputBirim;
    private EditText editAdSoyad, editOgrNo, editBirim;
    private ImageView ekleImgView;

    private static final int imgIzinAlmaKodu = 1;
    private static final int imgIzinVerildiKodu = 2;

    private Bitmap secilenResim, kucultulenResim, defaultImage;
    private String txtAdSoyad, txtOgrNo, txtBirim, uId;
    private Button btnIhbarOlustur;

    private IhbarFragment ihbarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekle);

        inputAdSoyad = (TextInputLayout)findViewById(R.id.ekle_act_inputAdSoyad);
        inputOgrNo = (TextInputLayout)findViewById(R.id.ekle_act_inputOgrNo);
        inputBirim = (TextInputLayout)findViewById(R.id.ekle_act_inputBirim);

        editAdSoyad = (EditText)findViewById(R.id.ekle_act_editAdSoyad);
        editOgrNo = (EditText)findViewById(R.id.ekle_act_editOgrNo);
        editBirim = (EditText)findViewById(R.id.ekle_act_editBirim);

        ekleImgView = (ImageView)findViewById(R.id.ekle_act_imgView);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnIhbarOlustur = (Button)findViewById(R.id.ekle_act_btnIhbarOlustur);



        btnIhbarOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtAdSoyad = editAdSoyad.getText().toString();
                txtOgrNo = editOgrNo.getText().toString();
                txtBirim = editBirim.getText().toString();

                if (!TextUtils.isEmpty(txtAdSoyad)) {
                    if (!TextUtils.isEmpty(txtOgrNo)) {
                        if (!TextUtils.isEmpty(txtBirim)) {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            secilenResim.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            byte[] resimData = baos.toByteArray();

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            String resimPath = "KartResimleri/" + UUID.randomUUID().toString() + ".jpg";
                            StorageReference resimRef = storageRef.child(resimPath);

                            UploadTask uploadTask = resimRef.putBytes(resimData);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Resim yükleme başarılı olduğunda Firestore veritabanına kaydetme
                                            resimRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String resimUrl = uri.toString();

                                                    Map<String, Object> ihbarData = new HashMap<>();
                                                    ihbarData.put("AdSoyad", txtAdSoyad);
                                                    ihbarData.put("OgrNo", txtOgrNo);
                                                    ihbarData.put("Birim", txtBirim);
                                                    ihbarData.put("ResimURL", resimUrl);

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    db.collection("Ihbarlar")
                                                            .add(ihbarData)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    // Oluşturulan ihbar belgesinin uId'sini alın
                                                                    String ihbarId = documentReference.getId();

                                                                    // Oluşturulan belgenin üzerine güncelleme yaparak uId değerini yazın
                                                                    Map<String, Object> updateData = new HashMap<>();
                                                                    updateData.put("uId", ihbarId);
                                                                    updateData.put("Tarih", FieldValue.serverTimestamp());
                                                                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                                                    if (currentUser != null) {
                                                                        String kullaniciId = currentUser.getUid();
                                                                        updateData.put("KullaniciId", kullaniciId);
                                                                    }
                                                                    db.collection("Ihbarlar").document(ihbarId)
                                                                            .update(updateData)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Toast.makeText(EkleActivity.this, "İhbar başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
                                                                                    finish();
                                                                                    startActivity(new Intent(EkleActivity.this, MainActivity.class));
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(EkleActivity.this, "İhbar oluşturulamadı.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(EkleActivity.this, "İhbar oluşturulamadı.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Resim yükleme başarısız olduğunda hata işleme
                                            Toast.makeText(EkleActivity.this, "Resim yükleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else
                            inputBirim.setError("Lütfen Geçerli Bir Birim Giriniz");
                    } else
                        inputOgrNo.setError("Lütfen Geçerli Bir Öğrenci Numarası Giriniz.");
                } else
                    inputAdSoyad.setError("Lütfen Geçerli Ad ve Soyad Giriniz.");
            }
        });

        ekleImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EkleActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EkleActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, imgIzinAlmaKodu);
                } else {
                    Intent resmiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mGetContent.launch(resmiAl);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == imgIzinVerildiKodu) {
            if (data != null) {
                try {
                    Uri resimUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resimUri);
                    ekleImgView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Seçilen resmin içeriği boş", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            assert data != null;
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source resimSource = ImageDecoder.createSource(this.getContentResolver(), data.getData());
                    secilenResim = ImageDecoder.decodeBitmap(resimSource);
                    ekleImgView.setImageBitmap(secilenResim);
                } else {
                    secilenResim = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    ekleImgView.setImageBitmap(secilenResim);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == imgIzinAlmaKodu) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzinler alındıktan sonra resim seçme işlemini gerçekleştir
                Intent resmiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch(resmiAl);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

//---------------------------------------Ekle Activity Kod Sonu-----------------------------------

/*//----------------------ImageView hariç edit text içindekilerin Cloud Firestore'a aktarılması--------------------------
        Map<String, Object> ihbarData = new HashMap<>();
        ihbarData.put("Ad Soyad", txtAdSoyad);
        ihbarData.put("Ogr No", txtOgrNo);
        ihbarData.put("Birim", txtBirim);

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ihbarlar")
                .add(ihbarData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(EkleActivity.this, "İhbar başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(EkleActivity.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EkleActivity.this, "İhbar oluşturulamadı.", Toast.LENGTH_SHORT).show();
                    }
                });
//----------------------ImageView hariç edit text içindekilerin Cloud Firestore'a aktarılması--------------------------*/


/*-------------Başarısız Cloud Firestore Collection Ekleme-1
    public void btnIhbarOlustur(View v){
        //Toast.makeText(EkleActivity.this, "Ekle butonuna tıklandı.", Toast.LENGTH_SHORT).show();

        txtAdSoyad = editAdSoyad.getText().toString();
        txtOgrNo = editOgrNo.getText().toString();
        txtBirim = editBirim.getText().toString();

        if(!TextUtils.isEmpty(txtAdSoyad)){
            if(!TextUtils.isEmpty(txtOgrNo)){
                if(!TextUtils.isEmpty(txtBirim)){
                    // Koleksiyon oluşturma işlemleri
                    Map<String, Object> ihbarData = new HashMap<>();
                    ihbarData.put("AdSoyad", txtAdSoyad);
                    ihbarData.put("OgrNo", txtOgrNo);
                    ihbarData.put("Birim", txtBirim);

                    // Cloud Firestore referansı
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Kullanıcının UID'si
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // "Ihbarlar" koleksiyonu ve UID ile belge oluşturma
                    db.collection("Ihbarlar").document(uid)
                            .set(ihbarData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // İşlem başarılı olduğunda yapılacaklar
                                    Toast.makeText(EkleActivity.this, "İhbar başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // İşlem başarısız olduğunda yapılacaklar
                                    Toast.makeText(EkleActivity.this, "İhbar oluşturma hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }else
                    inputBirim.setError("Lütfen Geçerli Bir Birim Giriniz");
            }else
                inputOgrNo.setError("Lütfen Geçerli Bir Öğrenci Numarası Giriniz.");
        }else
            inputAdSoyad.setError("Lütfen Geçerli Ad ve Soyad Giriniz.");


    }*/


/*-------------Başarısız Cloud Firestore Collection Ekleme-2
if(!TextUtils.isEmpty(txtAdSoyad)){
                    if(!TextUtils.isEmpty(txtOgrNo)){
                        if(!TextUtils.isEmpty(txtBirim)){
                            ihbarData = new Kullanici(txtAdSoyad, txtOgrNo, txtBirim);

                            Map<String, Object> Ihbarlar = new HashMap<>();
                            Ihbarlar.put("Ad Soyad", txtAdSoyad);
                            Ihbarlar.put("Ogr No", txtOgrNo);
                            Ihbarlar.put("Birim", txtBirim);

                            db.collection("Ihbarlar")
                                    .add(Ihbarlar)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(EkleActivity.this, "İhbar başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EkleActivity.this, "İhbar oluşturulamadı. Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(EkleActivity.this, "İhbar oluşturulamadı.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else
                            inputBirim.setError("Lütfen Geçerli Bir Birim Giriniz");
                    }else
                        inputOgrNo.setError("Lütfen Geçerli Bir Öğrenci Numarası Giriniz.");
                }else
                    inputAdSoyad.setError("Lütfen Geçerli Ad ve Soyad Giriniz.");*/