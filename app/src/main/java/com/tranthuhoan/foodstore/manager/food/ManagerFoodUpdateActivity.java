package com.tranthuhoan.foodstore.manager.food;

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

public class ManagerFoodUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String foodName, foodPrice, foodDes, foodImage, foodAvail, foodQuantity;

    private EditText edtAdFoodUpdateName, edtAdFoodUpdatePrice, edtAdFoodUpdateDes, edtAdFoodUpdateAvail, edtAdFoodUpdateQuantity;
    private Button btnAdFoodUpdateSave, btnAdFoodUpdateDelete, btnAdFoodUpdateExit, btnAdFoodUpdateTakePhoto, btnAdFoodUpdateChoosePhoto;

    private ImageView ivAdFoodUpdateImage, ivAdFoodUpdateExit;


    ArrayList<Food> foodArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_food_update);


        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdFoodViewDetails();

        //Set on View
        initView();


        //Button Choose Photo
        btnAdFoodUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdFoodUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdFoodUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdFoodUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdFoodUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerFoodUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this student account");
                builder.setMessage("Are you sure want to delete food " + foodArr.get(position).getFoodName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccStudent();
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

        //Button Save
        btnAdFoodUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdFoodUpdateName)) {
                    edtAdFoodUpdateName.setError("Please enter food's name");
                }
                if (isEmptyEditText(edtAdFoodUpdateDes)) {
                    edtAdFoodUpdateDes.setError("Please enter food's description");
                }
                if (isEmptyEditText(edtAdFoodUpdatePrice)) {
                    edtAdFoodUpdatePrice.setError("Please enter food's price");
                }


                foodName = edtAdFoodUpdateName.getText().toString();
                foodPrice = edtAdFoodUpdatePrice.getText().toString();
                foodDes = edtAdFoodUpdateDes.getText().toString();
                foodAvail = edtAdFoodUpdateAvail.getText().toString();
                foodQuantity = edtAdFoodUpdateQuantity.getText().toString();

                if (foodName.length() > 0 && foodPrice.length() > 0 && foodDes.length() > 0) {
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

    private void deleteAccStudent() {
        String currentImage;
        if (!foodArr.get(position).getFoodPhoto().equals("")) {
            currentImage = foodArr.get(position).getFoodPhoto();
            currentImage = currentImage.substring(currentImage.lastIndexOf("/"));
        } else {
            currentImage = "NO_IMAGE_FOOD_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteFoodData(foodArr.get(position).getFoodId(), currentImage);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("FOOD_DELETED_SUCCESSFUL")) {
                    Toast.makeText(ManagerFoodUpdateActivity.this, "Deleted Food " + foodArr.get(position).getFoodName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerFoodUpdateActivity.this, ManagerFoodViewAllActivity.class);
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


    private void receiveDataFromAdFoodViewDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("FOOD_DATA_FROM_AD_FOOD_VIEW_DETAILS_TO_UPDATE");
        if (bundle != null) {
            foodArr = bundle.getParcelableArrayList("FOOD_DATA_ARRAY");
            position = bundle.getInt("FOOD_DATA_POSITION");
        }
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callbackPhoto = dataClient.UploadFoodPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    foodImage = response.body();
                    Log.d("Updated Photo", foodImage);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Photo", t.getMessage());
            }
        });
    }

    private void uploadInfo() {
        String currentImage, newImage;
        if (foodArr.get(position).getFoodPhoto().equals("")) {
            //curAva = "", newAva=""
            currentImage = "NO_CURRENT_IMAGE_FOOD_UPDATE";
            if (realPath.equals("")) {
                newImage = "";
            } else {
                newImage = APIUtils.BASE_URL + "manager/food/images/" + foodImage;
            }
        } else {
            if (realPath.equals("")) {
                currentImage = "NO_CURRENT_IMAGE_FOOD_UPDATE";
                newImage = foodArr.get(position).getFoodPhoto();
            } else {
                currentImage = foodArr.get(position).getFoodPhoto();
                currentImage = currentImage.substring(currentImage.lastIndexOf("/"));
                newImage = APIUtils.BASE_URL + "manager/food/images/" + foodImage;
            }
        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.ManagerUpdateFoodData(foodArr.get(position).getFoodId(),
                foodName, foodPrice, foodDes, foodAvail, foodQuantity, newImage, currentImage);
        foodArr.get(position).setFoodName(foodName);
        foodArr.get(position).setFoodPrice(foodPrice);
        foodArr.get(position).setFoodDes(foodDes);
        foodArr.get(position).setFoodAvail(foodAvail);
        foodArr.get(position).setFoodQuantity(foodQuantity);
        foodArr.get(position).setFoodPhoto(newImage);

        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Food Info", result);
                if (result.trim().equals("FOOD_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(ManagerFoodUpdateActivity.this, "Successfully Updated Food Information " + foodName, Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Err Updated Food Info", t.getMessage());
            }
        });
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        startActivity(new Intent(ManagerFoodUpdateActivity.this, ManagerFoodViewAllActivity.class));
//        Intent intent = new Intent(ManagerFoodUpdateActivity.this, ManagerFoodViewDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("FOOD_DATA_ARRAY", foodArr);
//        bundle.putInt("FOOD_DATA_POSITION", position);
//        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
//        intent.putExtra("FOOD_DATA_FROM_ROOM_ADAPTER_TO_AD_FOOD_VIEW_DETAILS", bundle);
//        startActivity(intent);
        finish();
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
                    ivAdFoodUpdateImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdFoodUpdateImage.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdFoodUpdateImage.getDrawable()).getBitmap();
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
        ivAdFoodUpdateImage = findViewById(R.id.iv_ad_room_update_image);
        ivAdFoodUpdateExit = findViewById(R.id.iv_ad_room_update_exit);

        edtAdFoodUpdateName = findViewById(R.id.edt_ad_room_update_name);
        edtAdFoodUpdatePrice = findViewById(R.id.edt_ad_room_update_price);
        edtAdFoodUpdateDes = findViewById(R.id.edt_ad_room_update_des);
        edtAdFoodUpdateAvail = findViewById(R.id.edt_ad_room_update_avail);
        edtAdFoodUpdateQuantity = findViewById(R.id.edt_ad_room_update_quantity);

        btnAdFoodUpdateSave = findViewById(R.id.btn_ad_room_update_save);
        btnAdFoodUpdateDelete = findViewById(R.id.btn_ad_room_update_delete);
        btnAdFoodUpdateExit = findViewById(R.id.btn_ad_room_update_exit);
        btnAdFoodUpdateTakePhoto = findViewById(R.id.btn_ad_room_update_take_photo);
        btnAdFoodUpdateChoosePhoto = findViewById(R.id.btn_ad_room_update_choose_photo);
    }

    private void initView() {
        edtAdFoodUpdateName.setText(foodArr.get(position).getFoodName());
        edtAdFoodUpdatePrice.setText(foodArr.get(position).getFoodPrice());
        edtAdFoodUpdateDes.setText(foodArr.get(position).getFoodDes());
        edtAdFoodUpdateAvail.setText(foodArr.get(position).getFoodAvail());
        edtAdFoodUpdateQuantity.setText(foodArr.get(position).getFoodQuantity());


        if (!foodArr.get(position).getFoodPhoto().equals("")) {
            Picasso.get()
                    .load(foodArr.get(position).getFoodPhoto())
                    .placeholder(R.drawable.diet)
                    .error(R.drawable.diet)
                    .into(ivAdFoodUpdateImage);
        } else {
            ivAdFoodUpdateImage.setImageResource(R.drawable.diet);
        }

    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}
