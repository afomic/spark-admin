package com.afomic.sparkadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.afomic.sparkadmin.data.Constant.EXTRA_SIGN_UP_TYPE;
import static com.afomic.sparkadmin.data.Constant.TYPE_MAIL_AND_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_email)
    EditText emailEditText;

    @BindView(R.id.edt_password)
    EditText passwordEditText;

    PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        ButterKnife.bind(this);
        mPreferenceManager=new PreferenceManager(this);
    }

    public boolean isValidEntry(){
        if(Util.isEmpty(emailEditText)){
           Util.makeToast(LoginActivity.this,"Email field cannot be empty");
           return false;
        }
        if(Util.isEmpty(passwordEditText)){
            Util.makeToast(LoginActivity.this,"Password field cannot be empty");
            return false;
        }
        return  true;
    }
    @OnClick(R.id.btn_login)
    public void loginButtonClicked(){
        if(isValidEntry()){
            String email=Util.getString(emailEditText);
            String password=Util.getString(passwordEditText);
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Todo show MainActivity
                                String userId=task.getResult().getUser().getUid();
                                mPreferenceManager.setUserId(userId);
                                mPreferenceManager.setUserLogin(true);
                            }else {
                                Util.makeToast(LoginActivity.this,"Invalid email or password");
                            }
                        }
                    });
        }

    }
    @OnClick(R.id.btn_sign_up)
    public void signUpButtonClicked(){
        Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
        intent.putExtra(EXTRA_SIGN_UP_TYPE,TYPE_MAIL_AND_PASSWORD);
        startActivity(intent);
    }

}
