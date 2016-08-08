package hu.ait.android.keely.migrainetracker.Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import hu.ait.android.keely.migrainetracker.R;

/**
 * Created by Keely on 5/13/15.
 */
public class CreateFoodActivity extends Activity {

    public static final String KEY_EDIT_FOOD = "KEY_EDIT_FOOD";
    public static final String KEY_FOOD = "KEY_FOOD";
    public static final String KEY_EDIT_ID = "KEY_EDIT_ID";

    private boolean inEditMode = false;
    private EditText etDesc;
    private TextView tvDate;

    private int foodToEditId = 0;
    private Food foodToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);

        etDesc = (EditText) findViewById(R.id.etDesc);
        tvDate= (TextView) findViewById(R.id.tvDate);

        //Edit a food item in list
        if (getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(KEY_EDIT_FOOD)) {

            inEditMode = true;
            foodToEdit = (Food) getIntent().getSerializableExtra(KEY_EDIT_FOOD);
            foodToEditId = getIntent().getIntExtra(KEY_EDIT_ID, -1);

            etDesc.setText(foodToEdit.getDesc());



        }

        //Update and save
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEditMode) {
                    updateFood();
                } else {
                    saveFood();
                }
            }
        });
    }

    private void updateFood() {
        foodToEdit.setDesc(etDesc.getText().toString());

        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_FOOD, foodToEdit);
        intentResult.putExtra(KEY_EDIT_ID, foodToEditId);
        setResult(RESULT_OK, intentResult);
        finish();
    }


    private void saveFood() {
        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_FOOD,
                new Food(etDesc.getText().toString(),
                        new Date(System.currentTimeMillis())));

        setResult(RESULT_OK, intentResult);
        finish();
    }
}
