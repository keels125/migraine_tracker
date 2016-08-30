package hu.ait.android.keely.migrainetracker.Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import hu.ait.android.keely.migrainetracker.R;

/**
 * Activity that handles adding, deleting, and editing food entries
 */
public class CreateMigraineActivity extends Activity {

    private boolean inEditMode = false;
    private EditText etDesc;
    private EditText etDur;
    private int migraineToEditId = 0;
    private Migraine migraineToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_migraine);

        etDesc = (EditText) findViewById(R.id.etDesc);
        etDur = (EditText) findViewById(R.id.etDur);

        if (getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(getString(R.string.KEY_EDIT_MIGRAINE))) {

            inEditMode = true;
            migraineToEdit = (Migraine) getIntent().getSerializableExtra(getString(R.string.KEY_EDIT_MIGRAINE));
            migraineToEditId = getIntent().getIntExtra(getString(R.string.KEY_EDIT_ID), -1);

            etDesc.setText(migraineToEdit.getDesc());
            etDur.setText(migraineToEdit.getDur());
        }

        //Update and Save if editing, otherwise just save
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEditMode) {
                    updateMigraine();
                } else {
                    saveMigraine();
                }
            }
        });
    }

    private void updateMigraine() {
        migraineToEdit.setDesc(etDesc.getText().toString());
        migraineToEdit.setDur(etDur.getText().toString() + " hours");

        Intent intentResult = new Intent();
        intentResult.putExtra(getString(R.string.KEY_MIGRAINE), migraineToEdit);
        intentResult.putExtra(getString(R.string.KEY_EDIT_ID), migraineToEditId);
        setResult(RESULT_OK, intentResult);
        finish();
    }


    private void saveMigraine() {
        Intent intentResult = new Intent();
        Migraine m = new Migraine(etDesc.getText().toString(),
                etDur.getText().toString(),
                new Date(System.currentTimeMillis()));
        intentResult.putExtra(getString(R.string.KEY_MIGRAINE), m);

        etDur.setText(etDur.getText().toString()+" hours");

        setResult(RESULT_OK, intentResult);
        finish();
    }


}
