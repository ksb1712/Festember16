package com.festember16.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String spinnerItem="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Spinner comeAcrossSpinner = (Spinner) findViewById(R.id.spinner_knowing_about);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FeedbackActivity.this,
                R.array.come_across_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comeAcrossSpinner.setAdapter(adapter);

        final RatingBar ratingQuestion1 = (RatingBar) findViewById(R.id.question_1_rating);
        final RatingBar ratingQuestion2 = (RatingBar) findViewById(R.id.question_2_rating);
        final RatingBar ratingQuestion3 = (RatingBar) findViewById(R.id.question_3_rating);
        final RatingBar ratingQuestion4 = (RatingBar) findViewById(R.id.question_4_rating);

        final Switch accommodationSwitch = (Switch) findViewById(R.id.switch_accommodation);
        accommodationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ratingQuestion4.setClickable(isChecked);
            }
        });

        final Switch participationSwitch = (Switch) findViewById(R.id.switch_participation);

        final EditText suggestionsComplaintsText = (EditText) findViewById(R.id.edittext_suggestions_complaints);

        Button submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answer1 = ratingQuestion1.getNumStars();
                int answer2 = ratingQuestion2.getNumStars();
                int answer3 = ratingQuestion3.getNumStars();
                    String isAccommodation = accommodationSwitch.getText().toString();
                int answer4 = ratingQuestion4.getNumStars();
                String isParticipation = participationSwitch.getText().toString();
                String source = spinnerItem;
                String comments = suggestionsComplaintsText.getText().toString();

                //API CALL HERE

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        spinnerItem = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}