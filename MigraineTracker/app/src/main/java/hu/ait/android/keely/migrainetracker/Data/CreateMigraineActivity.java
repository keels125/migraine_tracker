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
 * Created by Keely on 5/9/15.
 */
public class CreateMigraineActivity extends Activity {

    public static final String KEY_EDIT_MIGRAINE = "KEY_EDIT_MIGRAINE";
    public static final String KEY_MIGRAINE = "KEY_MIGRAINE";
    public static final String KEY_EDIT_ID="KEY_EDIT_ID";

    private boolean inEditMode = false;
    private EditText etDesc;
    private EditText etDur;
    private int migraineToEditId = 0;
    private Migraine migraineToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_migraine);

        etDesc= (EditText) findViewById(R.id.etDesc);
        etDur= (EditText) findViewById(R.id.etDur);

        if (getIntent().getExtras()!=null&&
                getIntent().getExtras().containsKey(KEY_EDIT_MIGRAINE)) {

            inEditMode=true;
            migraineToEdit= (Migraine) getIntent().getSerializableExtra(KEY_EDIT_MIGRAINE);
            migraineToEditId=getIntent().getIntExtra(KEY_EDIT_ID, -1);

            etDesc.setText(migraineToEdit.getDesc());
            etDur.setText(migraineToEdit.getDur());
        }

        //Update and Save if editing, otherwise just save
        Button btnSave= (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEditMode){
                    updateMigraine();
                }
                else{
                    saveMigraine();
                }
            }
        });
    }

    private void updateMigraine() {
        migraineToEdit.setDesc(etDesc.getText().toString());
        migraineToEdit.setDur(etDur.getText().toString()+" hours");


        Intent intentResult=new Intent();
        intentResult.putExtra(KEY_MIGRAINE, migraineToEdit);
        intentResult.putExtra(KEY_EDIT_ID, migraineToEditId);
        setResult(RESULT_OK, intentResult);
        finish();
    }


    private void saveMigraine() {
        Intent intentResult=new Intent();
        intentResult.putExtra(KEY_MIGRAINE,
                new Migraine(etDesc.getText().toString(),
                        etDur.getText().toString()+" hours",
                        new Date(System.currentTimeMillis())));
        setResult(RESULT_OK, intentResult);
        finish();
    }


}
