package com.colak.sukkb.Fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.colak.sukkb.Model.Ihbar;
import com.colak.sukkb.Model.IhbarlarAdapter;
import com.colak.sukkb.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class IhbarFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private View v;
    private IhbarlarAdapter mAdapter;
    private Query mQuery;
    private FirebaseFirestore mFirestore;
    private ArrayList<Ihbar> mIhbarList;
    private Ihbar mIhbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ihbar, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        mRecyclerView = v.findViewById(R.id.ihbar_fragment_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL, false));

        mIhbarList = new ArrayList<>();

        mQuery = mFirestore.collection("Ihbarlar")
                    .orderBy("Tarih", Query.Direction.DESCENDING);
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(value != null){
                    mIhbarList.clear();

                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        mIhbar = snapshot.toObject(Ihbar.class);

                        assert mIhbar != null;
                        mIhbarList.add(mIhbar);
                    }

                    mRecyclerView.addItemDecoration(new LinearDecoration(20, mIhbarList.size()));
                    mAdapter = new IhbarlarAdapter(mIhbarList, v.getContext());
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
        });

        return v;
    }

    class LinearDecoration extends RecyclerView.ItemDecoration{
        private int boslukMiktari;
        private int veriSayisi;

        public LinearDecoration(int boslukMiktari, int veriSayisi) {
            this.boslukMiktari = boslukMiktari;
            this.veriSayisi = veriSayisi;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);

            if(pos != (veriSayisi - 1))
                outRect.bottom = boslukMiktari;
        }
    }

}