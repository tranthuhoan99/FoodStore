package com.tranthuhoan.foodstore.manager.food;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.model.Food;
import com.tranthuhoan.foodstore.retrofit.APIUtils;
import com.tranthuhoan.foodstore.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerFoodAddActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String foodItemName, foodItemPrice, foodItemDes, foodItemImage, foodItemAvail, foodItemQuantity;

    private EditText edtAdFoodItemAddName, edtAdFoodItemAddPrice, edtAdFoodItemAddDes, edtAdFoodItemAddAvail, edtAdFoodItemAddQuantity;
    private Button btnAdFoodItemAddSave, btnAdFoodItemAddExit, btnAdFoodItemAddTakePhoto, btnAdFoodItemAddChoosePhoto;

    private ImageView ivAdFoodItemAddImage, ivAdFoodItemAddExit;

    ArrayList<Food> foodItemArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_food_add);

        //Connect layout
        initUI();


        //Button Choose Photo
        btnAdFoodItemAddChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdFoodItemAddTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdFoodItemAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ImageView Exit
        ivAdFoodItemAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnAdFoodItemAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdFoodItemAddName)) {
                    edtAdFoodItemAddName.setError("Please enter foodItem's name");
                }
                if (isEmptyEditText(edtAdFoodItemAddPrice)) {
                    edtAdFoodItemAddPrice.setError("Please enter foodItem's price");
                }
                if (isEmptyEditText(edtAdFoodItemAddDes)) {
                    edtAdFoodItemAddDes.setError("Please enter foodItem's description");
                }

                foodItemName = edtAdFoodItemAddName.getText().toString();
                foodItemPrice = edtAdFoodItemAddPrice.getText().toString();
                foodItemDes = edtAdFoodItemAddDes.getText().toString();
                foodItemAvail = edtAdFoodItemAddAvail.getText().toString();
                foodItemQuantity = edtAdFoodItemAddQuantity.getText().toString();
                if (foodItemName.length() > 0 && foodItemPrice.length() > 0 && foodItemDes.length() > 0) {
                    if (!realPath.equals("")) {
                        uploadInfoWithPhoto();
                    } else {
                        uploadInfo();
                    }
                }
            }
        });
    }

    private boolean isEmptyEditText(EditText editText) {
        String str = editText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    private void uploadInfo() {
        DataClient insertData = APIUtils.getData();
        Call<String> callback;
        if (!realPath.equals("")) {
            callback = insertData.ManagerAddFoodData(foodItemName, foodItemPrice, foodItemDes, foodItemAvail, foodItemQuantity, APIUtils.BASE_URL + "manager/food/images/" + foodItemImage);
        } else {
            callback = insertData.ManagerAddFoodData(foodItemName, foodItemPrice, foodItemDes, foodItemAvail, foodItemQuantity, "NO_IMAGE_ADD_FOOD_ITEM");
        }
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Ad Food Add Info", result);
                if (result.trim().equals("ADD_FOOD_ITEM_SUCCESSFUL")) {
                    Toast.makeText(ManagerFoodAddActivity.this, "Food " + foodItemName + " Added Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Er Ad Food Add Info", t.getMessage());
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
        retrofit2.Call<String> callback = dataClient.UploadFoodPhoto(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    foodItemImage = response.body();
                    uploadInfo();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Err Updated Food Photo", t.getMessage());
            }
        });
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
                    ivAdFoodItemAddImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdFoodItemAddImage.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }
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

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivAdFoodItemAddImage.getDrawable()).getBitmap();
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
        ivAdFoodItemAddImage = findViewById(R.id.iv_ad_room_add_image);
        ivAdFoodItemAddExit = findViewById(R.id.iv_ad_room_add_exit);

        edtAdFoodItemAddName = findViewById(R.id.edt_ad_room_add_name);
        edtAdFoodItemAddPrice = findViewById(R.id.edt_ad_room_add_price);
        edtAdFoodItemAddDes = findViewById(R.id.edt_ad_room_add_des);
        edtAdFoodItemAddAvail = findViewById(R.id.edt_ad_room_add_avail);
        edtAdFoodItemAddQuantity = findViewById(R.id.edt_ad_room_add_quantity);


        btnAdFoodItemAddSave = findViewById(R.id.btn_ad_room_add_save);
        btnAdFoodItemAddExit = findViewById(R.id.btn_ad_room_add_exit);
        btnAdFoodItemAddTakePhoto = findViewById(R.id.btn_ad_room_add_take_photo);
        btnAdFoodItemAddChoosePhoto = findViewById(R.id.btn_ad_room_add_choose_photo);

    }
}