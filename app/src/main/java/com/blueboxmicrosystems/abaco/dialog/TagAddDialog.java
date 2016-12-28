package com.blueboxmicrosystems.abaco.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.MainActivity;
import com.blueboxmicrosystems.abaco.R;
import com.blueboxmicrosystems.abaco.model.ListTagModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Manuel Emilio Araujo on 12/25/2016.
 */
// TODO: agregar comentario de funcionalidad
public class TagAddDialog extends DialogFragment {
    private ActionListener actionListener;
    private EditText txtTagName;
    private EditText txtTagDescription;
    Button btnCancel;
    Button btnSave;
    Integer id;

    public TagAddDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=0;
        if(getArguments()!=null){
            id = getArguments().getInt("id");
        }
        return createDialog();
    }

    // TODO: agregar comentario de funcionalidad
    public interface ActionListener {
        public abstract void onSave(Integer id);
        public abstract void onCancel();
    }

    // TODO: agregar comentario de funcionalidad
    public void setActionListener(ActionListener listener) {
        this.actionListener = listener;
    }

    // TODO: agregar comentario de funcionalidad
    private boolean validate(View view) {
        if (this.txtTagName.getText().toString().isEmpty()) {
            this.txtTagName.setError("Tag name does not be blank");
            return false;
        }
        return true;
    }

    // TODO: agregar comentario de funcionalidad
    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_tag_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        txtTagName = (EditText) v.findViewById(R.id.txtTagName);
        txtTagDescription = (EditText) v.findViewById(R.id.txtTagDescription);

        if (MainActivity.abacoDataBase != null) {
            Cursor c = MainActivity.abacoDataBase.rawQuery("select id,name,description from main.tag where id="+id, null);
            if (c.moveToFirst()) {
                txtTagName.setText(c.getString(1));
                txtTagDescription.setText(c.getString(2));
            }
        }

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validate(v)) {
                            actionListener.onCancel();
                            return;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("icon", "");
                        contentValues.put("name", txtTagName.getText().toString());
                        contentValues.put("description", txtTagDescription.getText().toString());
                        contentValues.put("color", "0xff4db6ac");
                        try {
                            if (id == 0) {
                                MainActivity.abacoDataBase.insertOrThrow("main.tag", null, contentValues);
                            } else {
                                MainActivity.abacoDataBase.update("main.tag", contentValues, "id=" + id, null);
                            }
                            actionListener.onSave(2);
                            dismiss();
                        } catch (Exception ex) {
                            // TODO: Cambiar a mensaje de alerta
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }
        );
        return builder.create();
    }
}