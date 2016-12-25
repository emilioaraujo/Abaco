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
 * Created by MARAUJO on 12/25/2016.
 */

public class CategoryAddDialog extends DialogFragment {
    private ActionListener actionListener;
    Button btnCancel;
    Button btnSave;
    private EditText txtCategoryName;
    private EditText txtCategoryDescription;
    Integer id;
    Integer transactionTypeId;

    public CategoryAddDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=0;
        if(getArguments()!=null){
            id = getArguments().getInt("id");
            transactionTypeId=getArguments().getInt("transactionTypeId");
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
        if (this.txtCategoryName.getText().toString().isEmpty()) {
            this.txtCategoryName.setError("Category name does not be blank");
            return false;
        }
        return true;
    }

    // TODO: agregar comentario de funcionalidad
    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_category_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        txtCategoryName = (EditText) v.findViewById(R.id.txtCategoryName);
        txtCategoryDescription = (EditText) v.findViewById(R.id.txtCategoryDescription);

        if (MainActivity.abacoDataBase != null) {
            Cursor c = MainActivity.abacoDataBase.rawQuery("select id,transaction_type_id, name, description from main.transaction_category where id=" + id, null);
            if (c.moveToFirst()) {
                transactionTypeId=c.getInt(1);
                txtCategoryName.setText(c.getString(2));
                txtCategoryDescription.setText(c.getString(3));
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
                        contentValues.put("transaction_type_id", transactionTypeId);
                        contentValues.put("name", txtCategoryName.getText().toString());
                        contentValues.put("description", txtCategoryDescription.getText().toString());
                        try {
                            if (id == 0) {
                                MainActivity.abacoDataBase.insertOrThrow("transaction_category", null, contentValues);
                            } else {
                                MainActivity.abacoDataBase.update("transaction_category", contentValues, "id=" + id, null);
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
