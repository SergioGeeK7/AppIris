package com.example.sergiogeek7.appiris;
import android.app.DatePickerDialog;
import java.util.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Country;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.UserApp;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterForm extends AppCompatActivity {


    @BindView(R.id.city_list) Spinner spinner_city;
    @BindView(R.id.country_list) Spinner spinner_country;
    @BindView(R.id.gender_spinner) Spinner spinner_gender;
    @BindView(R.id.size) EditText text_size;
    @BindView(R.id.birth_date) EditText birth_date;
    @BindView(R.id.weigh) EditText text_weigh;
    @BindView(R.id.full_name) EditText full_name;
    @BindView(R.id.email) EditText email_text;

    private final String TAG = RegisterForm.class.getName();
    public static final String GO_TO_MAIN_SCREEN = "GO_TO_MAIN_SCREEN";
    private boolean goToMainScreen;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("countries");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        ButterKnife.bind(this);
        this.goToMainScreen = getIntent().getBooleanExtra(GO_TO_MAIN_SCREEN, false);
        if(user.getDisplayName() != null && !user.getDisplayName().isEmpty()){
            full_name.setText(user.getDisplayName());
            full_name.setEnabled(false);
        }
        if(user.getEmail() != null && !user.getEmail().isEmpty()){
            email_text.setText(user.getEmail());
            email_text.setEnabled(false);
        }
        ref.addValueEventListener(Callback.valueEventListener(
                (err, data) -> {
                    if(err != null){
                        Log.e(TAG, err.getMessage());
                        return;
                    }

                    List<Country> countries = new ArrayList<>();
                    for (DataSnapshot postSnapshot: data.getChildren()) {
                        Country country = postSnapshot.getValue(Country.class);
                        country.setKey(postSnapshot.getKey());
                        countries.add(country);
                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<>(RegisterForm.this,
                            android.R.layout.simple_spinner_item, countries);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_country.setAdapter(adapter);
        }, RegisterForm.this));

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country country = (Country) parent.getSelectedItem();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterForm.this,
                        android.R.layout.simple_spinner_item, country.getCities());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_city.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    super.onBackPressed();
                });
    }

    public void onClickBirthView(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(RegisterForm.this,
                (view1, year, monthOfYear, dayOfMonth)
                        -> birth_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                , mYear, mMonth, mDay).show();
    }

    public void save(View view){
        String city = (String) spinner_city.getSelectedItem();
        String country =  ((Country) spinner_country.getSelectedItem()).getKey();
        String gender =  spinner_gender.getSelectedItemPosition() == 0 ? "m" : "f";
        String size = text_size.getText().toString();
        String weight = text_weigh.getText().toString();
        String birthDate = birth_date.getText().toString();
        String fullName = full_name.getText().toString();
        String email = email_text.getText().toString();

        if(!validate(size, weight, birthDate, fullName, email)){
            return;
        }
        UserApp userApp =
                new UserApp(size, weight, gender, birthDate, country, city, fullName, email);
        DatabaseReference users_ref = database.getReference("users");
        Callback.taskManager(this, users_ref.child(user.getUid()).setValue(userApp));
        exit(gender);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    boolean validate(String size, String weight, String birthDate,
                     String fullName, String email){
        String message = "";
        if(size.isEmpty()){
            message += "\n" + getString(R.string.missing_size);
        }
        if(email.isEmpty() || !isValidEmailAddress(email)){
            message += "\n" + getString(R.string.missing_email);
        }
        if(weight.isEmpty()){
            message += "\n" + getString(R.string.missing_weight);
        }
        if(birthDate.isEmpty()){
            message += "\n" + getString(R.string.missing_birth_date);
        }else{
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy",  Locale.ENGLISH).parse(birthDate);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -14);
                if(date.getTime() > cal.getTime().getTime()){
                    message += "\n" + getString(R.string.age_restriction);
                }
            }catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
        }
        if(fullName.isEmpty()){
            message += "\n" + getString(R.string.missing_name);
        }
        if(!message.isEmpty()){
            Toast.makeText(this, message, Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }
    
    private void exit(String gender){
        if(!this.goToMainScreen){
            finish();
            return;
        }
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra(Gender.class.getName(), gender.equals("m") ? Gender.MAN: Gender.WOMAN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
