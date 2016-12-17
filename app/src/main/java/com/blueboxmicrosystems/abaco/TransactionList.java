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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.dialog.CustomCalculator;
import com.blueboxmicrosystems.abaco.model.ListAccountModel;
import com.google.android.gms.plus.PlusOneButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link TransactionList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionList extends Fragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

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

    private OnFragmentInteractionListener mListener;

    EditText txtDateTime;
    EditText txtAmount;
    ListView list;
    Button btnCancel;
    Button btnSave;
    private Integer currentTransactionId;
    Calendar transactionCalendar;

    AlertDialog transactionDialog;
    View view;

    private ArrayList<String> currencyCodes = new ArrayList<>();
    private ArrayList<String> currencyCountry = new ArrayList<>();

    public TransactionList() {
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
    public static TransactionList newInstance(String param1, String param2) {
        TransactionList fragment = new TransactionList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Transaction List");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddTransaction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "Boton add transaction presionado");
                currentTransactionId = 0;
                transactionDialog = getTransactionDialog();
                transactionDialog.show();

            }
        });
        transactionCalendar = Calendar.getInstance();
        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);

        return view;
    }

    public void configureAccountList() {
        // TODO: Completar configuracion de lista
        if (MainActivity.abacoDataBase != null) {


        }
    }

    private void showOrHideControls() {
        // TODO: esconder y mostrar controles
    }

    @Override
    public void onResume() {
        super.onResume();
        configureAccountList();

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
        // TODO: Organizar de acuerdo al orden en el form
        return true;
    }

    public void onRadioButtonClicked(View view) {
        // TODO: Completar radio button listener
        /*
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_ninjas:
                if (checked)
                    // Ninjas rule
                    break;
        }
        */
    }

    public AlertDialog getTransactionDialog() {
        // TODO: COmpletar dialogo de transaccion
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_transaction_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        //labels


        //inputs
        txtAmount = (EditText) v.findViewById(R.id.txtAmount);
        txtDateTime = (EditText) v.findViewById(R.id.txtDateTime);


        txtAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    CustomCalculator calc = new CustomCalculator();
                    calc.show(getActivity().getSupportFragmentManager(), "Calculator");
                    calc.setOnCalculatorResultListener(new CustomCalculator.OnCalculatorResultListener() {
                        @Override
                        public void onResult(Double result) {
                            txtAmount.setText(result.toString());
                            txtDateTime.requestFocus();
                        }
                    });
                }
            }
        });

        txtDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDatePicker();
                }
            }
        });
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transactionDialog.dismiss();
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getDatePicker();
                        //new CustomCalculator().show(getActivity().getSupportFragmentManager(), "Calculator");


                    }
                }
        );
        return builder.create();
    }

    public void getCalculator() {
        new CustomCalculator().show(getActivity().getSupportFragmentManager(), "SimpleDialog");
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

    public void cancel(MainActivity activity) {
        Log.d("", "Cancelando!!!");
        activity.replaceFragments(MainFragment.class);

    }

    @Override
    public void onDateSet(DatePicker mView, int year, int month, int dayOfMonth) {
        transactionCalendar.set(year, month, dayOfMonth, 0, 0);
        getTimePicker();
    }

    //@Override
    public void onTimeSet(TimePicker mView, int hour, int minute) {
        transactionCalendar.set(Calendar.HOUR, hour);
        transactionCalendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String dateTimeString = format.format(transactionCalendar.getTime());
        txtDateTime.setText(dateTimeString);
    }

    private void getDatePicker() {
        // Obtener fecha actual
        transactionCalendar = Calendar.getInstance();
        int year = transactionCalendar.get(Calendar.YEAR);
        int month = transactionCalendar.get(Calendar.MONTH);
        int day = transactionCalendar.get(Calendar.DAY_OF_MONTH);

        // Retornar en nueva instancia del dialogo selector de fecha
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        mDatePicker.show();
    }

    private void getTimePicker() {
        // Obtener fecha actual
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);

        // Retornar en nueva instancia del dialogo selector de fecha
        TimePickerDialog mTimerPicker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        mTimerPicker.show();
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
