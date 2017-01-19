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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blueboxmicrosystems.abaco.MainActivity;
import com.blueboxmicrosystems.abaco.R;
import com.blueboxmicrosystems.abaco.controller.entiry.TagController;
import com.blueboxmicrosystems.abaco.model.ColorAdapter;
import com.blueboxmicrosystems.abaco.model.ListTagModel;
import com.blueboxmicrosystems.abaco.model.entity.Tag;

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
    private Spinner spColors;
    private Button btnCancel;
    private Button btnSave;
    private Integer id;
    private final TagController tagController = new TagController(MainActivity.abacoDataBase);

    public TagAddDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = 0;
        if (getArguments() != null) {
            this.id = getArguments().getInt("id");
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
        spColors = (Spinner) v.findViewById(R.id.spColors);

        ColorAdapter<Integer> spinnerArrayAdapter = new ColorAdapter<Integer>(getActivity(), MainActivity.getColors());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColors.setAdapter(spinnerArrayAdapter);
        spColors.setSelection(MainActivity.getColors().indexOf(0));

        if (MainActivity.abacoDataBase != null) {
            Tag tag = null;
            try {
                tag = tagController.findById(id);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT);
            }
            if (tag!=null) {
                txtTagName.setText(tag.getName());
                txtTagDescription.setText(tag.getDescription());
                if (MainActivity.getColors().indexOf(tag.getColor()) > 0) {
                    spColors.setSelection(MainActivity.getColors().indexOf(tag.getColor()));
                }
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

                        Tag tag = new Tag();
                        tag.setId(id);
                        tag.setName(txtTagName.getText().toString());
                        tag.setDescription(txtTagDescription.getText().toString());
                        tag.setColor(Integer.parseInt(spColors.getSelectedItem().toString()));

                        try {
                            if (tag.getId() == 0) {
                                tagController.create(tag);
                            } else {
                                tagController.update(tag);
                            }
                            actionListener.onSave(2);
                            dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }
                    }
                }
        );
        return builder.create();
    }
}