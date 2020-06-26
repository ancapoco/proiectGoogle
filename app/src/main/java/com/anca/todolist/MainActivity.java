package com.anca.todolist;

import android.animation.FloatArrayEvaluator;
import android.graphics.Paint;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.anca.todolist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> inputList;
    Map<String, Note>  notes = new HashMap<>();

    List<String> toDoList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    EditText editText;
    Boolean strike = false;


    @Override
    public void onStart() {
        super.onStart();
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("aaa", "signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("bbb", "signInAnonymously:failure", task.getException());

                            }
                        }
                    });
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout,toDoList);
        listView = findViewById(R.id.id_list_view);



        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;

                if(strike)
                {
                    text.setPaintFlags(text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

                }
                else {
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }
                strike = !strike;


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("aa", "onItemLongClick: " + position);

                arrayAdapter.remove(arrayAdapter.getItem(position));
                arrayAdapter.notifyDataSetChanged();
                notes.remove("" + position);
                return true;
            }
        });

        editText = findViewById(R.id.idEditText);



    }



    public void addItemToList(View view){
            if (editText.getText().toString().length() > 0) {
                toDoList.add(editText.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    notes.put(Integer.toString(notes.size()), new Note(editText.getText().toString()));
            }
            else
                Toast.makeText(getApplicationContext(),"Please enter something",Toast.LENGTH_SHORT).show();

        db.collection("notes")
                .add(notes.get(Integer.toString(notes.size() - 1)))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("shs", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dfg", "Error adding document", e);
                    }
                });

        editText.setText("");


    }




}
