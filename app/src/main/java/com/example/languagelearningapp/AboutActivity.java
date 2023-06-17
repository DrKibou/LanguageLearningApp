package com.example.languagelearningapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AboutActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    TextView textViewDisplayEmail, triviaTxt;
    LinearLayout home, about, logout, email, txttoSpeech;
    ImageView menu, trivia;
    Dialog dialog;
    Button okbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        email = findViewById(R.id.emaillayout);
        txttoSpeech = findViewById(R.id.txtTospeech);
        trivia = findViewById(R.id.trivia);
        textViewDisplayEmail = findViewById(R.id.displayEmail);
        textViewDisplayEmail.setText(user.getEmail());
        DatabaseReference triviaRef = FirebaseDatabase.getInstance().getReference("Trivia");


        dialog = new Dialog(AboutActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.diag_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        okbtn = dialog.findViewById(R.id.okaybtn);
        triviaTxt = dialog.findViewById(R.id.triviatxt);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        trivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                triviaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> triviaList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String triviaText = dataSnapshot.getValue(String.class);
                            triviaList.add(triviaText);
                        }

                        // Randomized quicksort algorithm
                        randomizedQuicksort(triviaList, 0, triviaList.size() - 1);

                        if (!triviaList.isEmpty()) {
                            Random random = new Random();
                            int randomIndex = random.nextInt(triviaList.size());
                            String randomTrivia = triviaList.get(randomIndex);
                            triviaTxt.setText(randomTrivia);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error cases
                    }
                });
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AboutActivity.this, Home.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        txttoSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AboutActivity.this, TextToSpeechActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), FrontPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closedDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closedDrawer(drawerLayout);
    }
    private void randomizedQuicksort(List<String> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            randomizedQuicksort(list, low, pivotIndex - 1);
            randomizedQuicksort(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<String> list, int low, int high) {
        Random random = new Random();
        int randomIndex = random.nextInt(high - low + 1) + low;
        swap(list, randomIndex, high);

        String pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) < 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private void swap(List<String> list, int i, int j) {
        String temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}