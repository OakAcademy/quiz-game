package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button SignIn;
    SignInButton SigninGoogle;
    TextView signUp;
    TextView forgotPassword;
    ProgressBar progressBarSignin;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        SignIn = findViewById(R.id.buttonLoginSignin);
        SigninGoogle = findViewById(R.id.buttonLoginGoogleSignin);
        signUp = findViewById(R.id.textViewLoginSignUp);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
        progressBarSignin = findViewById(R.id.progressBarSignin);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userMail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signInWithFirebase(userMail, userPassword);
            }
        });

        SigninGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_Page.this, sign_up_page.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Page.this, Forgot_Password.class);
                startActivity(i);
            }
        });
    }

    public void signInWithFirebase(String userMail, String userPassword)
    {
        progressBarSignin.setVisibility(View.VISIBLE);
        SignIn.setClickable(false);
        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                          Intent i = new Intent(Login_Page.this, MainActivity.class);
                          startActivity(i);
                          finish();
                          progressBarSignin.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login_Page.this, "Sign in is Successful",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Login_Page.this, "Sign in is not Successful",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null)
        {
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void signInGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn();
    }

    public void signIn()
    {
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }
    }


    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(Login_Page.this, "Signed In Successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        }
        catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(Login_Page.this, "Signed In is not Successfull", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account)
    {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();
                }
                else{

                }
            }
        });
    }
}
