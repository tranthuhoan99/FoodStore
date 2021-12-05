package com.tranthuhoan.foodstore.manager;

import androidx.appcompat.app.AppCompatActivity;

import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.model.Manager;
import com.tranthuhoan.foodstore.retrofit.APIUtils;
import com.tranthuhoan.foodstore.retrofit.DataClient;
import com.tranthuhoan.foodstore.ui.activities.customer.CustomerLoginActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerLoginActivity extends AppCompatActivity {

    private EditText edtManagerLoginEmail, edtManagerLoginPassword;
    private Button btnManagerLogin;
    private TextView tvManagerLoginForgotPassword, tvManagerLoginToLoginCustomer;
    private ImageView ivManagerLoginClose;
    private CheckBox cbManagerLoginRememberMe;
    private SharedPreferences.Editor loginPrefsEditor;

    ArrayList<Manager> managerArr;
    String ManagerEmail, ManagerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        //Connect layout
        initUI();

        //Login When Enter - Done
        edtManagerLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    adminLogin();
                    rememberMe();
                }
                return false;
            }
        });


        //Remember Me
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean rememberMeCheck = loginPreferences.getBoolean("MANAGER_REMEMBER_ME", false);
        if (rememberMeCheck) {
            edtManagerLoginEmail.setText(loginPreferences.getString("MANAGER_EMAIL", ""));
            edtManagerLoginPassword.setText(loginPreferences.getString("MANAGER_PASSWORD", ""));
            cbManagerLoginRememberMe.setChecked(true);
        }

        // TView Forgot Password
        tvManagerLoginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerLoginActivity.this, ManagerForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Close
        ivManagerLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To Login Student
        tvManagerLoginToLoginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerLoginActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Button Login
        btnManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid(edtManagerLoginEmail)) {
                    adminLogin();
                    rememberMe();
                } else {
                    edtManagerLoginEmail.setError("Email address not valid");
                }
            }
        });
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if (email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void rememberMe() {
        if (cbManagerLoginRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("MANAGER_REMEMBER_ME", true);
            loginPrefsEditor.putString("MANAGER_EMAIL", ManagerEmail);
            loginPrefsEditor.putString("MANAGER_PASSWORD", ManagerPassword);
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.apply();
    }

    private void adminLogin() {
        ManagerEmail = edtManagerLoginEmail.getText().toString();
        ManagerPassword = edtManagerLoginPassword.getText().toString();
        if (ManagerEmail.length() > 0 && ManagerPassword.length() > 0) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<List<Manager>> callback = dataClient.LoginManagerData(ManagerEmail, ManagerPassword);
            callback.enqueue(new Callback<List<Manager>>() {
                @Override
                public void onResponse(Call<List<Manager>> call, Response<List<Manager>> response) {
                    managerArr = (ArrayList<Manager>) response.body();
                    if (managerArr.size() > 0) {
                        Intent intent = new Intent(ManagerLoginActivity.this, ManagerMenuActivity.class);
                        intent.putExtra("MANAGER_DATA_FROM_LOGIN_TO_MENU", managerArr);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ManagerLoginActivity.this, "Welcome " + managerArr.get(0).getMnName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Manager>> call, Throwable t) {
                    Log.d("Err Login: ", t.getMessage());
                    Toast.makeText(ManagerLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (ManagerEmail.length() == 0 && ManagerPassword.length() == 0) {
            Toast.makeText(ManagerLoginActivity.this, "Please type your email and password", Toast.LENGTH_SHORT).show();
        } else if (ManagerEmail.length() > 0 && ManagerPassword.length() == 0) {
            Toast.makeText(ManagerLoginActivity.this, "Please type your password", Toast.LENGTH_SHORT).show();
        } else if (ManagerEmail.length() == 0 && ManagerPassword.length() > 0) {
            Toast.makeText(ManagerLoginActivity.this, "Please type your email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ManagerLoginActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        edtManagerLoginEmail = findViewById(R.id.edt_manager_login_email);
        edtManagerLoginPassword = findViewById(R.id.edt_manager_login_password);
        btnManagerLogin = findViewById(R.id.btn_manager_login);
        tvManagerLoginForgotPassword = findViewById(R.id.tv_manager_login_forgot_password);
        tvManagerLoginToLoginCustomer = findViewById(R.id.tv_manager_login_to_login_customer);
        ivManagerLoginClose = findViewById(R.id.iv_manager_login_close);
        cbManagerLoginRememberMe = findViewById(R.id.cb_manager_login_remember_me);
    }
}