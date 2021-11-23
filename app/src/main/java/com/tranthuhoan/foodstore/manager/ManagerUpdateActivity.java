package com.tranthuhoan.foodstore.manager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.model.Manager;
import com.tranthuhoan.foodstore.retrofit.APIUtils;
import com.tranthuhoan.foodstore.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerUpdateActivity extends AppCompatActivity {

    public static final int ADMIN_CHANGE_PASSWORD_ACTIVITY = 2;
    public static final int RESULT_CHANGE_PASSWORD_OK = 3;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;

    EditText edtManagerUpdateEmail, edtManagerUpdateName, edtManagerUpdatePhone;
    Button btnManagerUpdateTakePhoto, btnManagerUpdateChoosePhoto, btnManagerUpdateSave, btnManagerUpdateDelete, btnManagerUpdateExit, btnManagerChangePassword;
    ImageView ivManagerUpdateAvatar, ivManagerUpdateExit;

    ArrayList<Manager> managerArr;
    String managerEmail, managerName, managerPhone, managerAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_update);

        //Connect layout
        initUI();
        //Set on Views
        initView();

        //Button Delete
        btnManagerUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this admin account");
                builder.setMessage("Are you sure want to delete account admin " + managerArr.get(0).getMnName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccAdmin();
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
        });

        //Button Change Password
        btnManagerChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ManagerUpdateActivity.this, ManagerChangePasswordActivity.class);
//                intent.putExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", managerArr);
//                startActivityForResult(intent, ADMIN_CHANGE_PASSWORD_ACTIVITY);
            }
        });

        //Button Save
        btnManagerUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtManagerUpdateName)) {
                    edtManagerUpdateName.setError("Please enter manager's name");
                }
                if (isEmptyEditText(edtManagerUpdateEmail)) {
                    edtManagerUpdateEmail.setError("Please enter manager's email");
                }

                if (isEmailValid(edtManagerUpdateEmail)) {
                    managerName = edtManagerUpdateName.getText().toString();
                    managerEmail = edtManagerUpdateEmail.getText().toString();
                    managerPhone = edtManagerUpdatePhone.getText().toString();
                    if (managerName.length() > 0 && managerEmail.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    } else {
                        edtManagerUpdateEmail.setError("Email address not valid");
                    }
                }

            }
        });

        //Button Exit
        btnManagerUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivManagerUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button Choose Photo
        btnManagerUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnManagerUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
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

    private boolean isEmptyEditText(EditText editText) {
        String str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void deleteAccAdmin() {
        String currentAvatar;
        if (!managerArr.get(0).getMnAvatar().equals("")) {
            currentAvatar = managerArr.get(0).getMnAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_MANAGER_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteManagerData(managerArr.get(0).getMnId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("MANAGER_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(ManagerUpdateActivity.this, "Deleted Manager " + managerArr.get(0).getMnName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerUpdateActivity.this, ManagerLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("Delete Err", res.trim());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Retrofit response", t.getMessage());
            }
        });
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callbackPhoto = dataClient.UploadManagerPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    managerAvatar = response.body();
                    Log.d("Updated Ad Photo", managerAvatar);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Ad Photo", t.getMessage());
            }
        });

    }

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (managerArr.get(0).getMnAvatar().equals("")) {
            currentAvatar = "NO_DELETE_CURRENT_IMAGE";
            if (realPath.equals("")) {
                newAvatar = "";
            } else {
                newAvatar = APIUtils.BASE_URL + "manager/images/" + managerAvatar;
            }
        } else {

            if (realPath.equals("")) {
                currentAvatar = "NO_DELETE_CURRENT_IMAGE";
                newAvatar = managerArr.get(0).getMnAvatar();
            } else {
                currentAvatar = managerArr.get(0).getMnAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
                newAvatar = APIUtils.BASE_URL + "manager/images/" + managerAvatar;
            }
        }


        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.UpdateManagerData(managerArr.get(0).getMnId(), managerEmail, managerName, managerPhone, newAvatar, currentAvatar);
        managerArr.get(0).setMnEmail(managerEmail);
        managerArr.get(0).setMnName(managerName);
        managerArr.get(0).setMnPhone(managerPhone);
        managerArr.get(0).setMnAvatar(newAvatar);
        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Ad Info", result);
                if (result.trim().equals("MANAGER_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(ManagerUpdateActivity.this, "Successfully Updated Manager Information", Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Ad Info", t.getMessage());
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        managerArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE");
        edtManagerUpdateName.setText(managerArr.get(0).getMnName());
        edtManagerUpdateEmail.setText(managerArr.get(0).getMnEmail());
        edtManagerUpdatePhone.setText(managerArr.get(0).getMnPhone());
        if (!managerArr.get(0).getMnAvatar().equals("")) {
            Picasso.get()
                    .load(managerArr.get(0).getMnAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivManagerUpdateAvatar);
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                imageUri = data.getData();
                realPath = getRealPathFromURI(imageUri);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivManagerUpdateAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivManagerUpdateAvatar.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }

        //Change password
        else if (resultCode == RESULT_CHANGE_PASSWORD_OK) {
            if (requestCode == ADMIN_CHANGE_PASSWORD_ACTIVITY) {
                managerArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_CHANGE_PASSWORD_TO_UPDATE");
            }
        }
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivManagerUpdateAvatar.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image From Take Photo");
        values.put(MediaStore.Images.Media.BUCKET_ID, "image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "take photo and save to gallery");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        edtManagerUpdateEmail = findViewById(R.id.edt_admin_update_email);
        edtManagerUpdateName = findViewById(R.id.edt_admin_update_name);
        edtManagerUpdatePhone = findViewById(R.id.edt_admin_update_phone);
        btnManagerUpdateChoosePhoto = findViewById(R.id.btn_admin_update_choose_photo);
        btnManagerUpdateTakePhoto = findViewById(R.id.btn_admin_update_take_photo);
        btnManagerUpdateSave = findViewById(R.id.btn_admin_update_save);
        btnManagerUpdateDelete = findViewById(R.id.btn_admin_update_delete);
        btnManagerUpdateExit = findViewById(R.id.btn_admin_update_exit);
        btnManagerChangePassword = findViewById(R.id.btn_admin_update_change_password);
        ivManagerUpdateAvatar = findViewById(R.id.iv_admin_update_avt);
        ivManagerUpdateExit = findViewById(R.id.iv_admin_update_exit);
    }

    // Get Real Path when upload photo(from uri - image/mame_image)
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU", managerArr);
        setResult(ManagerMenuActivity.RESULT_OK, intent);
        finish();
    }
}