package com.tranthuhoan.foodstore.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.model.Manager;

import java.util.ArrayList;

public class ManagerMenuActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    Button addStudent, viewStudent, noticeStudent, reportStudent, btnHomeMenuLogout, btnManagerEdit;
    ImageView ivManagerAvt, ivAdNavHeader;
    TextView tvManagerName, tvAdNavHeaderName, tvAdNavHeaderEmail;
    ArrayList<Manager> managerArr;

    // Activity need back home menu
    public static final int ADMIN_UPDATE_ACTIVITY = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        // Connect Layout
        initUI();
        //Receive Data From Login
        receiveDataFromLogin();
        // Set on View
        initView();
        //Navigation Drawer
        navigationDrawer();


        //Logout Button
        btnHomeMenuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //View All Button
        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerMenuActivity.this, ManagerFoodViewAllActivity.class));
            }
        });
//
//        //Add Student Button
//        addStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ManagerMenuActivity.this, ManagerStudentAddActivity.class));
//            }
//        });
//
//        //Button Notice
//        noticeStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ManagerMenuActivity.this, ManagerNoticeToStudentActivity.class));
//            }
//        });
//
//        //Button Report
//        reportStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ManagerMenuActivity.this, ManagerStudentReportActivity.class));
//            }
//        });
//
        //Button Edit
        btnManagerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerMenuActivity.this, ManagerUpdateActivity.class);
                intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", managerArr);
                startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
            }
        });

        //Nav
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Fragment fragment = null;
                switch (id) {
                    case R.id.it_manager_nav_dra_menu_dashboard:
//                        fragment = new DashBoardFragment();
//                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
//                    case R.id.it_manager_nav_dra_menu_add_student:
//                        startActivity(new Intent(ManagerMenuActivity.this, ManagerStudentAddActivity.class));
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
                    case R.id.it_manager_nav_dra_menu_view_all_food:
                        startActivity(new Intent(ManagerMenuActivity.this, ManagerFoodViewAllActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
//                    case R.id.it_manager_nav_dra_menu_notice:
//                        startActivity(new Intent(ManagerMenuActivity.this, ManagerNoticeToStudentActivity.class));
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//                    case R.id.it_manager_nav_dra_menu_report:
//                        startActivity(new Intent(ManagerMenuActivity.this, ManagerStudentReportActivity.class));
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
                    case R.id.it_manager_nav_dra_menu_manager_profile:
//                        Intent intent = new Intent(ManagerMenuActivity.this, ManagerUpdateActivity.class);
//                        intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", managerArr);
//                        startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
//                    case R.id.it_manager_nav_dra_menu_change_password:
//                        Intent intent1 = new Intent(ManagerMenuActivity.this, ManagerChangePasswordActivity.class);
//                        //Replace
//                        intent1.putExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", managerArr);
//                        startActivityForResult(intent1, RESULT_OK);
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
                    case R.id.it_manager_nav_dra_menu_logout:
                        logout();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        
    }

    private void navigationDrawer() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Load Fragment
//    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment).commit();
//        drawerLayout.closeDrawer(GravityCompat.START);
//        fragmentTransaction.addToBackStack(null);
//    }

    //Receive Data From Login
    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        managerArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_LOGIN_TO_MENU");
    }

    // Connect Layout
    public void initUI() {
        btnManagerEdit = findViewById(R.id.btn_admin_edit);
        ivManagerAvt = findViewById(R.id.iv_admin_avt);
        tvManagerName = findViewById(R.id.tv_admin_name);
        btnHomeMenuLogout = findViewById(R.id.btn_home_menu_logout);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
        noticeStudent = findViewById(R.id.btn_student_notice);
        reportStudent = findViewById(R.id.btn_student_report);
        drawerLayout = findViewById(R.id.dl_admin_drawer);
        toolbar = findViewById(R.id.tb_admin_toolBar);
        navigationView = findViewById(R.id.nv_admin);
        View hView = navigationView.getHeaderView(0);
        tvAdNavHeaderEmail = hView.findViewById(R.id.tv_admin_nav_header_email);
        tvAdNavHeaderName = hView.findViewById(R.id.tv_admin_nav_header_name);
        ivAdNavHeader = hView.findViewById(R.id.iv_ad_nav_header);
    }

    //Set on View
    private void initView() {
        tvManagerName.setText(managerArr.get(0).getMnName());
        tvAdNavHeaderName.setText(managerArr.get(0).getMnName());
        tvAdNavHeaderEmail.setText(managerArr.get(0).getMnEmail());
        if (!managerArr.get(0).getMnAvatar().equals("")) {
            Picasso.get()
                    .load(managerArr.get(0).getMnAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivManagerAvt);
            Picasso.get()
                    .load(managerArr.get(0).getMnAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdNavHeader);
        }

    }

    @Override
    protected void onResume() {
        navigationView.getMenu().findItem(R.id.it_manager_nav_dra_menu_dashboard).setChecked(true);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADMIN_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                managerArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }

    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(managerArr.get(0).getMnName() + ", are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ManagerMenuActivity.this, ManagerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
}