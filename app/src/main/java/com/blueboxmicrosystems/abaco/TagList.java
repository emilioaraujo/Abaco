package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.model.ListAccountModel;
import com.blueboxmicrosystems.abaco.model.ListCategoryModel;
import com.blueboxmicrosystems.abaco.model.ListTagModel;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link TagList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TagList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;
    Button btnCancel;
    Button btnSave;
    ListView list;
    View view;
    private EditText txtTagName;
    private EditText txtTagDescription;
    private TagList.OnFragmentInteractionListener mListener;
    private Integer currentTagId;
    AlertDialog tagDialog;
    public TagList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountList.
     */
    // TODO: Rename and change types and number of parameters
    public static TagList newInstance(String param1, String param2) {
        TagList fragment = new TagList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Tag List");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddTag);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "Boton add category presionado");
                currentTagId = 0;
                tagDialog = getTagDialog();
                tagDialog.show();
            }
        });

        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
         list = (ListView) view.findViewById(R.id.lvTagList);
        return view;
    }

    public void configureTagList() {
        if (MainActivity.abacoDataBase != null) {

            Cursor c = MainActivity.abacoDataBase.rawQuery("select id,name,description from main.tag order by id desc", null);

            final ArrayList<Integer> tagId = new ArrayList<Integer>();
            final ArrayList<Integer> tagImageId = new ArrayList<Integer>();
            final ArrayList<String> tagName= new ArrayList<String>();
            final ArrayList<String> tagDescription= new ArrayList<String>();

            if (c.moveToFirst()) {
                do {
                    Integer id = c.getInt(0);
                    String name = c.getString(1);
                    String description = c.getString(2);

                    tagId.add(id);
                    tagName.add(name);
                    tagDescription.add(description);
                    tagImageId.add(R.drawable.ic_menu_camera);
                } while (c.moveToNext());

                ListTagModel adapter = new ListTagModel(getActivity(),tagImageId, tagName, tagDescription);
                list = (ListView) getView().findViewById(R.id.lvTagList);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentTagId = tagId.get(position);
                        tagDialog = getTagDialog();
                        tagDialog.show();
                        txtTagName.setText(tagName.get(position).toString());
                        txtTagDescription.setText(tagDescription.get(position).toString());
                    }
                });
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        configureTagList();
        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private boolean validate(View view) {
        if (this.txtTagName.getText().toString().isEmpty()) {
            this.txtTagName.setError("Tag name does not be blank");
            return false;
        }
        return true;
    }

    public AlertDialog getTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_tag_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        txtTagName = (EditText) v.findViewById(R.id.txtTagName);
        txtTagDescription = (EditText) v.findViewById(R.id.txtTagDescription);

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagDialog.dismiss();
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validate(v)) {
                            return;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("icon", "");
                        contentValues.put("name", txtTagName.getText().toString());
                        contentValues.put("description", txtTagDescription.getText().toString());
                        try {
                            if (currentTagId == 0) {
                                MainActivity.abacoDataBase.insertOrThrow("main.tag", null, contentValues);
                            } else {
                                MainActivity.abacoDataBase.update("main.tag", contentValues, "id=" + currentTagId, null);
                            }
                            configureTagList();
                            tagDialog.dismiss();
                        } catch (Exception ex) {
                            // TODO: Cambiar a mensaje de alerta
                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }
        );
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void cancel(MainActivity activity) {
        Log.d("", "Cancelando!!!");
        activity.replaceFragments(MainFragment.class);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
