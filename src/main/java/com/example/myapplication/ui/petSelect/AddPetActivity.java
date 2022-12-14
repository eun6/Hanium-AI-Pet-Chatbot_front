package com.example.myapplication.ui.petSelect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.ui.ServiceSetting.ServiceAPI;
import com.example.myapplication.ui.ServiceSetting.ServiceGenerator;
import com.example.myapplication.ui.setting.PetinfoData;
import com.example.myapplication.ui.setting.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetActivity extends AppCompatActivity {
    private ImageView petprofile;
    private TextView petAge;
    private EditText petBreed,petNickName;
    private Button man, woman, NeuteringYes, NeuteringNo, btnAge, btnSave, btnCancel, selectCatButton, selectDogButton;

    private ServiceAPI profileAPI = ServiceGenerator.createService(ServiceAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpetprofile);

        petprofile = findViewById(R.id.pic);
        btnAge = findViewById(R.id.btnAge);
        btnSave = findViewById(R.id.btnAddPetSave);
        btnCancel = findViewById(R.id.btnAddPetCancel);
        selectCatButton = findViewById(R.id.selectCat);
        selectDogButton = findViewById(R.id.selectDog);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        NeuteringYes = findViewById(R.id.Neuteringyes);
        NeuteringNo = findViewById(R.id.Neuteringno);

        petBreed = findViewById(R.id.petbreed);
        petNickName = findViewById(R.id.petNickname);
        petAge = findViewById(R.id.petAge);
        

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getPetinfo();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PetSelectActivity.class);
                startActivity(intent);
            }
        });

        //?????? ??????
        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder AgePicker = new AlertDialog.Builder(AddPetActivity.this);

                AgePicker.setTitle("????????????");
                final NumberPicker AP = new NumberPicker(AddPetActivity.this);
                AgePicker.setView(AP);

                AP.setMinValue(0);
                AP.setMaxValue(30);
                AP.setWrapSelectorWheel(false);
                AP.setValue(0);

                AP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    }
                });

                AgePicker.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        petAge.setText(String.valueOf(AP.getValue()));
                        dialog.dismiss();
                    }
                });
                AgePicker.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                AgePicker.show();
            }
        });
        // ???/????????? ?????? ?????? ?????? ?????? ???????????? ???????????? ??????
        selectCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petprofile.setImageResource(R.drawable.catface);
            }
        });
        selectDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petprofile.setImageResource(R.drawable.dogface);
            }
        });
    }
    //???????????? ?????? ??????
    public void getPetinfo(){
        String Name = petNickName.getText().toString().trim();
        String Age = petAge.getText().toString().trim();
        //String Breed = petBreed.getText().toString().trim();
        String Gender = null;
        String Neutering = null;
        if (man.isEnabled()) {
            Gender = man.getText().toString();
        } else if (woman.isEnabled()) {
            Gender = woman.getText().toString();
        }

        if (NeuteringYes.isEnabled()) {
            Neutering = NeuteringYes.getText().toString();
        } else if (NeuteringNo.isEnabled()) {
            Neutering = NeuteringNo.getText().toString();
        }

        PetinfoData petinfoData = new PetinfoData(Name, Age, null, Gender, Neutering);

        Call<ProfileResponse> call = profileAPI.getPetinfo(petinfoData);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (!response.equals(200)) {
                    Toast.makeText(getApplicationContext(),"?????????????????????.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddPetActivity.this, PetSelectActivity.class);
                    intent.putExtra("petName", Name);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPetActivity.this);
                builder.setTitle("??????")
                        .setMessage("?????? ?????? ?????? ??????????????????.")
                        .setPositiveButton("??????", null)
                        .create()
                        .show();
            }
        });
    }
}