package com.example.a5asec.ui.main.view;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.example.a5asec.R;
import com.example.a5asec.databinding.ActivitySuccessBinding;
import com.google.android.material.snackbar.Snackbar;

public class SuccessActivity extends AppCompatActivity
{
  ActivitySuccessBinding binding;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_success);
   binding = DataBindingUtil.setContentView(this, R.layout.activity_success);
    binding.btnSuccess.setOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View v)
      {
        Intent weatherDetailIntent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(weatherDetailIntent);
      }
    });
  }

  @Override public void onBackPressed()
  {
    Snackbar.make(binding.btnSuccess, "please click in your success", Snackbar.LENGTH_LONG)
        .setAnchorView(binding.btnSuccess).show();
  }
}