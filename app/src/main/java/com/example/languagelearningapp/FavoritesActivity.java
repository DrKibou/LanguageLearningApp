package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languagelearningapp.recycleviewers.FAV_RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class FavoritesActivity extends AppCompatActivity implements SelectListener {

    ArrayList<LearningLanguageModel> learningLanguageModels = new ArrayList<>();
    FAV_RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    ImageButton btnBack;
    TextToSpeech textToSpeech;
    DatabaseReference database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.fRecyclerView);
        searchView = findViewById(R.id.searchView);
        btnBack = findViewById(R.id.imageBack);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Favorites").child(user.getUid());

        adapter = new FAV_RecyclerViewAdapter(this, learningLanguageModels, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                learningLanguageModels.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    LearningLanguageModel learningLanguageModel = dataSnapshot.getValue(LearningLanguageModel.class);
                    learningLanguageModels.add(learningLanguageModel);

                    Collections.sort(learningLanguageModels, new Comparator<LearningLanguageModel>() {
                        @Override
                        public int compare(LearningLanguageModel o1, LearningLanguageModel o2) {
                            return (o1.getEnglishTxt() != null && o2.getEnglishTxt() != null) ?
                                    o1.getEnglishTxt().compareTo(o2.getEnglishTxt()) : 0;
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("fil"));
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setListBack(learningLanguageModels);
                filter(newText);
                return true;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void filter(String newText) {

        int result = binarySearch(learningLanguageModels, newText);

        if (result == -1) {

        } else {
            ArrayList<LearningLanguageModel> filteredList = new ArrayList<>();
            for (LearningLanguageModel item : learningLanguageModels) {
                if (item.getEnglishTxt().equals(learningLanguageModels.get(result).getEnglishTxt())) {
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        }
    }

    static int binarySearch(ArrayList<LearningLanguageModel> learningLanguageModels, String newText) {

        int l = 0, r = learningLanguageModels.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int res = newText.compareTo(learningLanguageModels.get(m).getEnglishTxt());

            // Check if x is present at mid
            if (res == 0)
                return m;

            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }
        return -1;
    }

    @Override
    public void onItemClicked(LearningLanguageModel learningLanguageModel) {
        String text = learningLanguageModel.getIlocanoTxt();
        if (text.equals("")) {
            Toast.makeText(FavoritesActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}