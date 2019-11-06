package com.example.stories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Int3;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ListView snapsListView;
    ArrayList<String> emails = new ArrayList();
    ArrayList<String> userNames = new ArrayList();
    ArrayList<DataSnapshot> snaps = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("Recieved Flashes");

        snapsListView = findViewById(R.id.snapsListView);
        snapsListView.getBackground().setAlpha(120);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,userNames);
        snapsListView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userNames.add(dataSnapshot.child("senderName").getValue().toString());
                emails.add(dataSnapshot.child("from").getValue().toString());
                snaps.add(dataSnapshot);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index =0 ;
                for(DataSnapshot snap : snaps) {
                    if (snap.getKey() == dataSnapshot.getKey()) {
                        Log.i("removing",""+index);
                        snaps.remove(index);
                        emails.remove(index);
                        userNames.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                    index++;
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataSnapshot snapshot = snaps.get(i);
                Intent intent = new Intent(UserListActivity.this,ViewSnapActivity.class);
                Log.i("mymess",""+snapshot.child("imageURL").getValue().toString());
                intent.putExtra("imageName",snapshot.child("imageName").getValue().toString());
                intent.putExtra("imageURL",snapshot.child("imageURL").getValue().toString());
                intent.putExtra("message",snapshot.child("message").getValue().toString());
                intent.putExtra("snapKey",snapshot.getKey());
                startActivity(intent);
                finish();
            }
        });



    }
    public void createFlash(View view){
        Intent intent = new Intent(this,CreateSnapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Click BACK again to Logout!!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }
}
