package com.blueboxmicrosystems.abaco;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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

import com.blueboxmicrosystems.abaco.dialog.CategoryAddDialog;
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
    private Integer currentRecordId;
    CategoryAddDialog createUpdateDialog;
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
                currentRecordId = 0;
                showCreateUpdateDialog(currentRecordId);
            }
        });
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        transactionTypeId = tab.getPosition()+2;

                        if(tab.getPosition()==0) {
                            //toolbar color
                            MainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                            //navbar color
                            //getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorIncome));
                        }else{
                            //toolbar color
                            MainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                            //navbar color
                            //getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorExpense));
                        }
                        configureList();
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
        configureList();
        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        return view;
    }

    public void configureList() {
        if (MainActivity.abacoDataBase != null) {

            if(transactionTypeId==2){
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                MainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorIncome));
            } else {
                appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                MainActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorExpense));
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
                        currentRecordId = categoryId.get(position);
                        showCreateUpdateDialog(currentRecordId);
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

    public void showCreateUpdateDialog(Integer id){
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("transactionTypeId",transactionTypeId);

        createUpdateDialog = new CategoryAddDialog();
        createUpdateDialog.setArguments(args);
        createUpdateDialog.show(getActivity().getSupportFragmentManager(), "Category");
        createUpdateDialog.setActionListener(new CategoryAddDialog.ActionListener() {
            @Override
            public void onSave(Integer id) {
                configureList();
            }

            @Override
            public void onCancel() {
                //nada por hacer aqui
            }
        });
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
