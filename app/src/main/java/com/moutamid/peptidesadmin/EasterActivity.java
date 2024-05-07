package com.moutamid.peptidesadmin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.peptidesadmin.databinding.ActivityEasterBinding;

import java.util.HashMap;
import java.util.Map;

public class EasterActivity extends AppCompatActivity {
    ActivityEasterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEasterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.easter.setOnCheckedChangeListener((buttonView, isChecked) -> Constants.databaseReference().child(Constants.EASTER).setValue(isChecked));
        binding.forAll.setOnCheckedChangeListener((buttonView, isChecked) -> Constants.databaseReference().child(Constants.EASTER_FOR_ALL).setValue(isChecked));

        Constants.databaseReference().get().addOnSuccessListener(dataSnapshot -> {
           boolean easter = Boolean.TRUE.equals(dataSnapshot.child(Constants.EASTER).getValue(Boolean.class));
           boolean forAll = Boolean.TRUE.equals(dataSnapshot.child(Constants.EASTER_FOR_ALL).getValue(Boolean.class));
           String email = dataSnapshot.child(Constants.EMAIL).getValue(String.class);
           String promo = dataSnapshot.child(Constants.PROMO).getValue(String.class);

           binding.easter.setChecked(easter);
           binding.forAll.setChecked(forAll);
           binding.email.getEditText().setText(email);
           binding.promo.getEditText().setText(promo);
        });

        binding.upload.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.EMAIL, binding.email.getEditText().getText().toString());
            map.put(Constants.PROMO, binding.promo.getEditText().getText().toString());
            Constants.databaseReference().updateChildren(map);
            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        });

    }
}