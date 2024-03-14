package com.moutamid.peptidesadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.moutamid.peptidesadmin.databinding.ActivityAddProductBinding;
import com.moutamid.peptidesadmin.models.ProductModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private static final int IMAGE_PICKER = 1001;
    ActivityAddProductBinding binding;
    ProductModel productModel;
    Uri image;
    ProgressDialog progressDialog;
    ArrayAdapter<String> bodyGoals, categories;
    String[] bodyGoalsList, categoriesList;
    private static final String TAG = "AddProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> onBackPressed());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

     //   bodyGoalsList = getResources().getStringArray(R.array.bodyGoals);
        categoriesList = getResources().getStringArray(R.array.categories);

      //  bodyGoals = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bodyGoalsList);
        categories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesList);

       // binding.bodyGoalsList.setAdapter(bodyGoals);
        binding.categoryList.setAdapter(categories);

        productModel = (ProductModel) Stash.getObject(Constants.PASS, ProductModel.class);
        if (productModel != null) {
            updateView();
        }

        binding.cardImage.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .cropSquare()
                    .start(IMAGE_PICKER);
        });

        binding.upload.setOnClickListener(v -> {
            if (valid()) {
                progressDialog.show();
                if (productModel == null) uploadData();
                else {
                    if (image != null){
                        uploadData();
                    } else updateData(productModel.getImage());
                }
            }
        });

    }

    private void updateData(String imageLink) {
        if (productModel==null) {
            productModel = new ProductModel();
            productModel.setID(UUID.randomUUID().toString());
        }
        productModel.setImage(imageLink);
        productModel.setCategory(binding.category.getEditText().getText().toString());
        productModel.setBodyType(binding.bodyType.getEditText().getText().toString());
        productModel.setName(binding.name.getEditText().getText().toString());
        productModel.setShortDesc(binding.shortMsg.getEditText().getText().toString());
        productModel.setLongDesc(binding.longMsg.getEditText().getText().toString());

        Constants.databaseReference().child(Constants.PRODUCTS).child(productModel.getID()).setValue(productModel)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e  -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void uploadData() {
        String time = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date().getTime());
        Constants.storageReference().child("images").child(time).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    updateData(uri.toString());
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private boolean valid() {
        if (productModel == null && image == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.category.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Category is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.bodyType.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Body Type is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Product name is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.shortMsg.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Short Description is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.longMsg.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Short Description is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateView() {
        binding.category.getEditText().setText(productModel.getCategory());
        binding.bodyType.getEditText().setText(productModel.getBodyType());
        binding.name.getEditText().setText(productModel.getName());
        binding.shortMsg.getEditText().setText(productModel.getShortDesc());
        binding.longMsg.getEditText().setText(productModel.getLongDesc());
        Glide.with(AddProductActivity.this).load(productModel.getImage()).placeholder(R.drawable.image_upload_bro).into(binding.image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            image = data.getData();
            Glide.with(AddProductActivity.this).load(image).placeholder(R.drawable.image_upload_bro).into(binding.image);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.PASS);
    }
}