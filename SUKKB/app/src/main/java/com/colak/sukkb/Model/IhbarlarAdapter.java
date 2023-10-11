package com.colak.sukkb.Model;

import static android.content.ContentValues.TAG;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colak.sukkb.Activity.EkleActivity;
import com.colak.sukkb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class IhbarlarAdapter extends RecyclerView.Adapter<IhbarlarAdapter.IhbarHolder> {

    private ArrayList<Ihbar> mIhbarList;
    private Context mContext;
    private View v;
    private Ihbar mIhbar;
    int kPos;
    private ImageView imgBuyut;

    private Dialog ihbarDialog;
    private ImageButton imgIptal;
    private ImageView imgKart;
    private TextView txtAdSoyad, txtOgrNo, txtBirim;
    private Window ihbarWindow;

    public IhbarlarAdapter(ArrayList<Ihbar> mIhbarList, Context mContext) {
        this.mIhbarList = mIhbarList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public IhbarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(mContext).inflate(R.layout.ihbar_item, parent, false);


        return new IhbarHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IhbarHolder holder, int position) {
        mIhbar = mIhbarList.get(position);
        holder.OgrAdi.setText("Kart Sahibi: " + mIhbar.getAdSoyad());

        imgBuyut = holder.itemView.findViewById(R.id.ihbar_item_imgBuyut);
        Picasso.get().load(mIhbar.getResimURL()).resize(66,66).into(holder.imgKart);

        String uId = mIhbarList.get(position).getuId();






        if (uId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ihbarRef = db.collection("Ihbarlar").document(uId);

            ihbarRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Timestamp olusturmaTarihi = document.getTimestamp("Tarih");
                            if (olusturmaTarihi != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedTarih = dateFormat.format(olusturmaTarihi.toDate());
                                holder.OlsTarih.setText("Oluşturulma Tarihi: "+formattedTarih);
                            }
                        } else {
                            Log.d(TAG, "Belge bulunamadı");
                        }
                    } else {
                        Log.d(TAG, "Veritabanı işlemi başarısız", task.getException());
                    }
                }
            });
        }












        holder.imgBuyut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kPos = holder.getAdapterPosition();
                if (kPos != RecyclerView.NO_POSITION) {
                    ihbarDetayDialog(mIhbarList.get(kPos));
                }
            }
        });

        /*-------------------Bu kodun hemen üstündekini kaldırıp bu aktifleştirildiğinde tüm CardView'e tıklanınca CustomDialog açılır.-------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kPos = holder.getAdapterPosition();

                if(kPos != RecyclerView.NO_POSITION)
                    ihbarDetayDialog(mIhbarList.get(kPos));

            }
        });*/

    }

    private void ihbarDetayDialog(Ihbar ihbar) {
        ihbarDialog = new Dialog(mContext);
        ihbarDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ihbarWindow = ihbarDialog.getWindow();
        ihbarWindow.setGravity(Gravity.CENTER);
        ihbarDialog.setContentView(R.layout.custom_dialog_ihbar_detay);

        imgIptal = ihbarDialog.findViewById(R.id.custom_dialog_ihbar_detay_btnKapat);
        imgKart = ihbarDialog.findViewById(R.id.customDialog_imageView);
        txtAdSoyad = ihbarDialog.findViewById(R.id.customDialog_textView_AdSoyad);
        txtOgrNo = ihbarDialog.findViewById(R.id.customDialog_textView_OgrNo);
        txtBirim = ihbarDialog.findViewById(R.id.customDialog_textView_Birim);

        Picasso.get().load(ihbar.getResimURL()).into(imgKart);
        txtAdSoyad.setText(ihbar.getAdSoyad());
        txtOgrNo.setText(ihbar.getOgrNo());
        txtBirim.setText(ihbar.getBirim());

        imgIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ihbarDialog.dismiss();
            }
        });

        ihbarWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        ihbarDialog.show();



    }

    @Override
    public int getItemCount() {
        return mIhbarList.size();
    }

    class IhbarHolder extends RecyclerView.ViewHolder{

        TextView OgrAdi, OlsTarih;
        CircleImageView imgKart;

        ImageView imgBuyut;

        public IhbarHolder(@NonNull View itemView) {
            super(itemView);

            OgrAdi = itemView.findViewById(R.id.ihbar_item_OgrAdi);
            imgKart = itemView.findViewById(R.id.ihbar_item_imgKart);
            imgBuyut = itemView.findViewById(R.id.ihbar_item_imgBuyut);
            OlsTarih = itemView.findViewById(R.id.ihbar_item_olusturmaTarihi);

        }
    }

}

/*--------------------------------Yanlış Tarih Ekleme-------------------------------
        if (uId != null) {
            // Null olmadığından emin olun
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ihbarRef = db.collection("Ihbarlar").document(uId);


            ihbarRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Veritabanı işlemi başarılı oldu
                        DocumentSnapshot document = task.getResult();
                        Toast.makeText(mContext, "Buraya geldi1", Toast.LENGTH_SHORT).show();
                        if (document.exists()) {
                            // Belge mevcut, işlemleri gerçekleştir
                            Timestamp olusturmaTarihi = document.getTimestamp("olusturmaTarihi");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String formattedTarih = dateFormat.format(olusturmaTarihi.toDate());
                            holder.OlsTarih.setText(formattedTarih);
                            Toast.makeText(mContext, "Buraya geldi2", Toast.LENGTH_SHORT).show();
                        } else {
                            // Belge mevcut değil
                            Log.d(TAG, "Belge bulunamadı");
                        }
                    } else {
                        // Veritabanı işlemi başarısız oldu
                        Log.d(TAG, "Veritabanı işlemi başarısız", task.getException());
                    }
                }
            });
        }*/
