package com.example.creatchatwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText username,email,password;
    Button btn_reg;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar =findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username =findViewById(R.id.username);
        email= findViewById(R.id.email);
        password =findViewById(R.id.password);
        btn_reg =findViewById(R.id.btn_reg);
        auth =FirebaseAuth.getInstance();
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tx_user =username.getText().toString();
                String tx_email =email.getText().toString();
                String tx_pass =password.getText().toString();
                if(TextUtils.isEmpty(tx_user)|| TextUtils.isEmpty(tx_email)||TextUtils.isEmpty(tx_pass)){
                    Toast.makeText(RegisterActivity.this,"All Field is empty",Toast.LENGTH_SHORT).show();

                }else if (tx_pass.length()<6){
                    Toast.makeText(RegisterActivity.this,"atLeast",Toast.LENGTH_SHORT).show();

                }else {
                    reg(tx_user,tx_email,tx_pass);
                }
            }
        });




        
    }
    private void reg (final String username, String email, String password){
        auth.createUserWithEmailAndPassword(password,email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid =firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("user").child(userid);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageurl","default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent =new Intent(RegisterActivity.this,StartActivity.class);
                          //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this,"all eror",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}