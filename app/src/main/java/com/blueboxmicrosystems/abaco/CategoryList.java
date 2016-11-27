package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.model.ListCategoryModel;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CategoryList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryList#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CategoryList extends Fragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
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
    TabLayout tabs;
    ListView list;
    View view;
    NestedScrollView nestedScrollView;
    Integer transactionTypeId = 2;
    Button btnCancel;
    Button btnSave;
    private EditText txtCategoryName;
    private EditText txtCategoryDescription;
    private OnFragmentInteractionListener mListener;
    private Integer currentCategoryId;
    AlertDialog categoryDialog;
    AppBarLayout appBarLayout;

    public CategoryList() {
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
    public static CategoryList newInstance(String param1, String param2) {
        CategoryList fragment = new CategoryList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Category List");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "Boton add category presionado");
                currentCategoryId = 0;
                categoryDialog = getCategoryDialog();
                categoryDialog.show();
            }
        });
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        transactionTypeId = tab.getPosition()+2;
                        configureCategoryList();
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        //Not implemented
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        //Not implemented
                    }
                }
        );
        list = (ListView) view.findViewById(R.id.lvCategory);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        configureCategoryList();
        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        return view;
    }

    public void configureCategoryList() {
        if (MainActivity.abacoDataBase != null) {
            if(transactionTypeId==2){
                appBarLayout.setBackgroundColor(Color.GREEN);
            }else{
                appBarLayout.setBackgroundColor(Color.RED);
            }
            Cursor c = MainActivity.abacoDataBase.rawQuery("" +
                    "select id,transaction_type_id, name, description " +
                    "from transaction_category " +
                    "where transaction_type_id=" + transactionTypeId + " " +
                    "order by id desc", null);

            final ArrayList<Integer> categoryId = new ArrayList<Integer>();
            final ArrayList<Integer> categoryTransactionTypeId = new ArrayList<Integer>();
            final ArrayList<String> categoryName = new ArrayList<String>();
            final ArrayList<String> categoryDescription = new ArrayList<String>();
            final ArrayList<Integer> categoryImageId = new ArrayList<Integer>();

            ListCategoryModel adapter = new ListCategoryModel(getActivity(), categoryImageId, categoryName, categoryDescription);
            list.setAdapter(adapter);
            if (c.moveToFirst()) {
                do {
                    categoryImageId.add(R.drawable.ic_menu_camera);
                    categoryId.add(c.getInt(0));
                    categoryTransactionTypeId.add(c.getInt(1));
                    categoryName.add(c.getString(2));
                    categoryDescription.add(c.getString(3));
                } while (c.moveToNext());

                 adapter = new ListCategoryModel(getActivity(), categoryImageId, categoryName, categoryDescription);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentCategoryId = categoryId.get(position);
                        categoryDialog = getCategoryDialog();
                        categoryDialog.show();
                        txtCategoryName.setText(categoryName.get(position).toString());
                        txtCategoryDescription.setText(categoryDescription.get(position).toString());
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private boolean validate(View view) {
        if (this.txtCategoryName.getText().toString().isEmpty()) {
            this.txtCategoryName.setError("Account name does not be blank");
            return false;
        }
        return true;
    }

    public AlertDialog getCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_category_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        txtCategoryName = (EditText) v.findViewById(R.id.txtCategoryName);
        txtCategoryDescription = (EditText) v.findViewById(R.id.txtCategoryDescription);

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryDialog.dismiss();
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
                        contentValues.put("transaction_type_id", transactionTypeId.toString());
                        contentValues.put("name", txtCategoryName.getText().toString());
                        contentValues.put("description", txtCategoryDescription.getText().toString());
                        try {
                            if (currentCategoryId == 0) {
                                MainActivity.abacoDataBase.insertOrThrow("transaction_category", null, contentValues);
                            } else {
                                MainActivity.abacoDataBase.update("transaction_category", contentValues, "id=" + currentCategoryId, null);
                            }
                            configureCategoryList();
                            categoryDialog.dismiss();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Toast.makeText(getActivity(),"Fecha: " + year + "-" + month + "-" + dayOfMonth,Toast.LENGTH_LONG).show();
    }

    //@Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

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
