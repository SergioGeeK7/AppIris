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

import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Country;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.UserApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterForm extends AppCompatActivity {

    private final String TAG = RegisterForm.class.getName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("countries");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @BindView(R.id.city_list) Spinner spinner_city;
    @BindView(R.id.country_list) Spinner spinner_country;
    @BindView(R.id.gender_spinner) Spinner spinner_gender;
    @BindView(R.id.size) EditText text_size;
    @BindView(R.id.birth_date) EditText birth_date;
    @BindView(R.id.weigh) EditText text_weigh;
    @BindView(R.id.full_name) EditText full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        ButterKnife.bind(this);
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
        String weigh = text_weigh.getText().toString();
        String birthDate = birth_date.getText().toString();
        String fullName = full_name.getText().toString();

        UserApp userApp = new UserApp(size, weigh, gender, birthDate, country, city, fullName);
        DatabaseReference users_ref = database.getReference("users");
        Callback.taskManager(this,users_ref.child(user.getUid()).setValue(userApp));
        goToMainScreen(gender);
    }

    private void goToMainScreen(String gender){
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra(Gender.class.getName(), gender.equals("m") ? Gender.MAN: Gender.WOMAN);
        startActivity(intent);
    }

}
