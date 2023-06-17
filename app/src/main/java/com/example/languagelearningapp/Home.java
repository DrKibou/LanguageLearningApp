package com.example.languagelearningapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView textViewDisplayEmail, triviaTxt;

    FirebaseUser user;
    ImageButton btnGreetings, btnNumbers, btnGenQuestions, btnDirections,
            btnTimeandDate, btnBasicPhrases, btnRequestCommand, btnFavorites;
    DrawerLayout drawerLayout;
    LinearLayout home, about, logout, email, txttoSpeech;
    ImageView menu, trivia;
    Dialog dialog;
    Button okbtn;

    public static String refDatabase = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btnGreetings = findViewById(R.id.imageButton);
        btnNumbers = findViewById(R.id.imageButton1);
        btnGenQuestions = findViewById(R.id.imageButton2);
        btnDirections = findViewById(R.id.imageButton3);
        btnBasicPhrases = findViewById(R.id.imageButton4);
        btnTimeandDate = findViewById(R.id.imageButton5);
        btnRequestCommand = findViewById(R.id.imageButton6);
        btnFavorites = findViewById(R.id.imageButton7);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        trivia = findViewById(R.id.trivia);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        txttoSpeech = findViewById(R.id.txtTospeech);
        logout = findViewById(R.id.logout);
        email = findViewById(R.id.emaillayout);
        textViewDisplayEmail = findViewById(R.id.displayEmail);
        DatabaseReference triviaRef = FirebaseDatabase.getInstance().getReference("Trivia");

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), FrontPage.class);
            startActivity(intent);
            finish();
        } else {
            textViewDisplayEmail.setText(user.getEmail());
        }

        dialog = new Dialog(Home.this);
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
                recreate();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, AboutActivity.class);
            }
        });
        txttoSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Home.this, TextToSpeechActivity.class);
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
        btnGreetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "Greetings";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "Numbers";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnGenQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "GeneralQuestions";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "Directions";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnTimeandDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "TimeAndDate";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnBasicPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refDatabase = "BasicPhrases";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRequestCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDatabase = "RequestAndCommands";
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
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