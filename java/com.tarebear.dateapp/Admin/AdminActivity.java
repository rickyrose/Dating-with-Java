package com.tarebear.DatingApp.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.tarebear.DatingApp.R;
import com.tarebear.DatingApp.Users.UsersClass;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class AdminActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    String currentUser;
    LinearLayout linearLayoutAdminUsers;
    LinearLayout linearLayoutAdminPosts;
    LinearLayout linearLayoutAdminPages;
    LinearLayout linearLayoutAdminSupport;
    LinearLayout linearLayoutAdminReportUsers;
    LinearLayout linearLayoutAdminReportPosts;
    LinearLayout linearLayoutAdminReportComments;

    TextView textViewAdminToday;
    TextView textViewAdminYear;
    TextView textViewAdminMonth;

    TextView textViewAdminUsersTotal;
    TextView textViewAdminPostsTotal;
    TextView textViewAdminUsersReport;
    TextView textViewAdminPostsReport;
    TextView textViewAdminCommentsReport;
    TextView textViewAdminSupport;

    Toolbar toolbarAdminToolbar;

    ListenerRegistration listenerRegistrationOne;
    ListenerRegistration listenerRegistrationTwo;
    ListenerRegistration listenerRegistrationThree;
    ListenerRegistration listenerRegistrationFour;
    ListenerRegistration listenerRegistrationFive;
    ListenerRegistration listenerRegistrationSix;
    ListenerRegistration listenerRegistrationSeven;

    ArrayList<String> arrayListToday = new ArrayList<>();
    ArrayList<String> arrayListMonth = new ArrayList<>();
    ArrayList<String> arrayListYear = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseUser != null) {
            currentUser = firebaseUser.getUid();
        }

        toolbarAdminToolbar = findViewById(R.id.toolbarAdminToolbar);
        setSupportActionBar(toolbarAdminToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.admin_dashboard));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarAdminToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutAdminUsers = findViewById(R.id.linearLayoutAdminUsers);
        linearLayoutAdminPosts = findViewById(R.id.linearLayoutAdminPosts);
        linearLayoutAdminPages = findViewById(R.id.linearLayoutAdminPages);
        linearLayoutAdminSupport = findViewById(R.id.linearLayoutAdminSupport);
        linearLayoutAdminReportUsers = findViewById(R.id.linearLayoutAdminReportUsers);
        linearLayoutAdminReportPosts = findViewById(R.id.linearLayoutAdminReportPosts);
        linearLayoutAdminReportComments = findViewById(R.id.linearLayoutAdminReportComments);

        textViewAdminToday = findViewById(R.id.textViewAdminToday);
        textViewAdminYear = findViewById(R.id.textViewAdminYear);
        textViewAdminMonth = findViewById(R.id.textViewAdminMonth);

        textViewAdminUsersTotal = findViewById(R.id.textViewAdminUsersTotal);
        textViewAdminPostsTotal = findViewById(R.id.textViewAdminPostsTotal);
        textViewAdminUsersReport = findViewById(R.id.textViewAdminUsersReport);
        textViewAdminPostsReport = findViewById(R.id.textViewAdminPostsReport);
        textViewAdminCommentsReport = findViewById(R.id.textViewAdminCommentsReport);
        textViewAdminSupport = findViewById(R.id.textViewAdminSupport);

        linearLayoutAdminUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminUsersActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminPostsActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminPagesActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminSupportActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminReportUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminReportsUsersActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminReportPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminReportsPostsActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAdminReportComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminReportsCommentsActivity.class);
                startActivity(intent);
            }
        });

        UsersCount();
        TotalCount();

    }


    private void UsersCount() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

        String stringTodayDate = sdfDate.format(new Date());
        String stringTodayMonth = sdfMonth.format(new Date());
        String stringTodayYear = sdfYear.format(new Date());

        listenerRegistrationOne = firebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {

                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    UsersClass usersClass = doc.getDocument().toObject(UsersClass.class);

                                    String stringUserDate = sdfDate.format(usersClass.getUser_joined());
                                    String stringUserMonth = sdfMonth.format(usersClass.getUser_joined());
                                    String stringUserYear = sdfYear.format(usersClass.getUser_joined());

                                    if (stringUserYear.equals(stringTodayYear)) {
                                        if (stringUserMonth.equals(stringTodayMonth)) {
                                            if (stringUserDate.equals(stringTodayDate)) {
                                                arrayListToday.add(stringUserDate);
                                            }
                                        }
                                    }
                                    if (stringUserYear.equals(stringTodayYear)) {
                                        if (stringUserMonth.equals(stringTodayMonth)) {
                                            arrayListMonth.add(stringUserMonth);
                                        }
                                    }
                                    if (stringUserYear.equals(stringTodayYear)) {
                                        arrayListYear.add(stringTodayYear);
                                    }
                                }

                                textViewAdminToday.setText(String.valueOf(arrayListToday.size()));
                                textViewAdminMonth.setText(String.valueOf(arrayListMonth.size()));
                                textViewAdminYear.setText(String.valueOf(arrayListYear.size()));
                            }
                            listenerRegistrationOne.remove();
                        }
                    }
                });
    }

    private void TotalCount() {

        listenerRegistrationTwo = firebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminUsersTotal.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationTwo.remove();
                        }
                    }
                });

        listenerRegistrationThree = firebaseFirestore.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminPostsTotal.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationThree.remove();
                        }
                    }
                });

        listenerRegistrationFour = firebaseFirestore.collection("reports")
                .document("users")
                .collection("reports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminUsersReport.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationFour.remove();
                        }
                    }
                });

        listenerRegistrationFive = firebaseFirestore.collection("reports")
                .document("posts")
                .collection("reports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminPostsReport.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationFive.remove();
                        }
                    }
                });

        listenerRegistrationSix = firebaseFirestore.collection("reports")
                .document("comments")
                .collection("reports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminCommentsReport.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationSix.remove();
                        }
                    }
                });

        listenerRegistrationSeven = firebaseFirestore.collection("supports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            textViewAdminSupport.setText(String.valueOf(queryDocumentSnapshots.size()));
                            listenerRegistrationSeven.remove();
                        }
                    }
                });


    }
}
