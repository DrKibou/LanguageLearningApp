package com.example.languagelearningapp.recycleviewers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languagelearningapp.LearningLanguageModel;
import com.example.languagelearningapp.R;
import com.example.languagelearningapp.SelectListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FAV_RecyclerViewAdapter extends RecyclerView.Adapter<FAV_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    static ArrayList<LearningLanguageModel> learningLanguageModels;
    private SelectListener selectListener;

    public FAV_RecyclerViewAdapter(Context context, ArrayList<LearningLanguageModel> learningLanguageModels, SelectListener selectListener) {
        this.context = context;
        this.learningLanguageModels = learningLanguageModels;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fav_recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String engTxt = learningLanguageModels.get(position).getEnglishTxt();
        String iloTxt = learningLanguageModels.get(position).getIlocanoTxt();

        holder.tvEnglish.setText(engTxt);
        holder.tvIlocano.setText(iloTxt);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(learningLanguageModels.get(position));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference fRef = FirebaseDatabase.getInstance().getReference("Favorites").child(user.getUid());


        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean dataDeleted = false;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String engData = dataSnapshot.child("englishTxt").getValue(String.class);
                            if (engData != null && engData.equals(engTxt)) {
                                dataSnapshot.getRef().removeValue();
                                dataDeleted = true;
                            }

                            if (dataDeleted) {
                                Toast.makeText(v.getContext(), "Data has been deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error cases
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return learningLanguageModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEnglish, tvIlocano;
        CardView cardView;
        ImageButton delBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEnglish = itemView.findViewById(R.id.textViewEng);
            tvIlocano = itemView.findViewById(R.id.textViewIlo);
            cardView = itemView.findViewById(R.id.cv_container);
            delBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    public void filterList(ArrayList<LearningLanguageModel> filteredList) {
        learningLanguageModels = filteredList;
        notifyDataSetChanged();
    }

    public void setListBack(ArrayList<LearningLanguageModel> setlistback) {
        learningLanguageModels = setlistback;
        notifyDataSetChanged();
    }
}
