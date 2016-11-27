package com.blueboxmicrosystems.abaco;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountAdd.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountAdd extends DialogFragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Integer accountId;
    private EditText txtAccountName;
    private EditText txtAccountDescription;
    private EditText txtInitialBalance;
    private EditText txtAmountLimit;
    private TextView lblPayDay;
    private TextView lblExpireEnd;
    private TextView lblAmountLimit;
    private Spinner spAccountType;
    private Spinner spPayDay;
    private Spinner spYearExpire;
    private Spinner spMonthExpire;
    private Spinner spAccountCurrency;

    private String[] days = new String[]{"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30"};

    private String[] months = new String[]{"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    private ArrayList<String> currencyCodes = new ArrayList<>();
    private ArrayList<String> currencyCountry = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AccountAdd() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountAdd newInstance(String param1, String param2) {
        AccountAdd fragment = new AccountAdd();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.getActivity().setTitle("Account Detail");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            //Habilitar el Up Button

            //actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            //actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_account_add, container, false);
        View view = inflater.inflate(R.layout.dialog_account_add, container, false);

        // Obtener instancia de la action bar
        //ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // if (actionBar != null) {
        // Habilitar el Up Button
        //actionBar.setDisplayHomeAsUpEnabled(true);
        // Cambiar icono del Up Button
        //actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        // }

        //labels
        this.lblPayDay = (TextView) view.findViewById(R.id.lblPayDay);
        this.lblExpireEnd = (TextView) view.findViewById(R.id.lblExpireEnd);
        this.lblAmountLimit = (TextView) view.findViewById(R.id.lblAmountLimit);

        //inputs
        this.txtAccountName = (EditText) view.findViewById(R.id.txtAccountName);
        this.txtAccountDescription = (EditText) view.findViewById(R.id.txtAccountDescription);
        this.txtInitialBalance = (EditText) view.findViewById(R.id.txtInitialBalance);
        this.txtAmountLimit = (EditText) view.findViewById(R.id.txtAmountLimit);
        this.spAccountType = (Spinner) view.findViewById(R.id.spAccountType);
        this.spPayDay = (Spinner) view.findViewById(R.id.spPayDay);
        this.spMonthExpire = (Spinner) view.findViewById(R.id.spMonthExpire);
        this.spYearExpire = (Spinner) view.findViewById(R.id.spYearExpire);
        this.spAccountCurrency = (Spinner) view.findViewById(R.id.spAccountCurrency);
        this.showOrHideControls();

        ArrayAdapter<CharSequence> adapterAccountType = ArrayAdapter.createFromResource(getActivity(), R.array.account_type, android.R.layout.simple_spinner_item);
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccountType.setAdapter(adapterAccountType);

        ArrayAdapter<String> spinnerPayDayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, days);
        spinnerPayDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spPayDay.setAdapter(spinnerPayDayAdapter);

        //month spinner
        ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, months);
        spinnerMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spMonthExpire.setAdapter(spinnerMonthAdapter);
        //--

        //year spinner
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        String[] years = new String[20];

        years[0] = "Select";
        for (int i = 1; i < 20; i++) {
            years[i] = currentYear.toString();
            currentYear = currentYear + 1;
        }

        ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, years);
        spinnerYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spYearExpire.setAdapter(spinnerYearAdapter);
        //--

        //curency spinner
        currencyCodes.removeAll(currencyCodes);
        currencyCountry.removeAll(currencyCountry);

        currencyCodes.add("Select");
        currencyCountry.add("");
        String currencyCode;
        Map currencies = MainActivity.getAvailableCurrencies();
        for (Object country : currencies.keySet()) {
            currencyCountry.add(country.toString());
            if (currencies.get(country).toString().equals(Currency.getInstance(currencies.get(country).toString()).getSymbol())) {
                currencyCodes.add(country.toString() + " (" + currencies.get(country).toString() + ")");
            } else {
                currencyCodes.add(country.toString() + " (" + currencies.get(country).toString() + Currency.getInstance(currencies.get(country).toString()).getSymbol() + ")");
            }


        }

        ArrayAdapter<String> spinnerCurrencyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencyCodes);
        spinnerCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spAccountCurrency.setAdapter(spinnerCurrencyAdapter);
        //--


        //listeners
        spAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showOrHideControls();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showOrHideControls();
            }
        });
        //--

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void showOrHideControls() {
        if (this.spAccountType.getSelectedItemPosition() == 3 || this.spAccountType.getSelectedItemPosition() == 4) {
            this.lblPayDay.setVisibility(View.VISIBLE);
            this.spPayDay.setVisibility(View.VISIBLE);
            this.lblExpireEnd.setVisibility(View.VISIBLE);
            this.spMonthExpire.setVisibility(View.VISIBLE);
            this.spYearExpire.setVisibility(View.VISIBLE);
            this.lblAmountLimit.setVisibility(View.VISIBLE);
            this.txtAmountLimit.setVisibility(View.VISIBLE);
        } else {
            this.lblPayDay.setVisibility(View.GONE);
            this.spPayDay.setVisibility(View.GONE);
            this.lblExpireEnd.setVisibility(View.GONE);
            this.spMonthExpire.setVisibility(View.GONE);
            this.spYearExpire.setVisibility(View.GONE);
            this.lblAmountLimit.setVisibility(View.GONE);
            this.txtAmountLimit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validate() {
        // TODO: Organizar de acuerdo al orden en el form
        if (this.spAccountType.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) this.spAccountType.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Mandatory");
            return false;
        }
        if (this.spAccountCurrency.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) this.spAccountCurrency.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Mandatory");
            return false;
        }
        if (this.txtAccountName.getText().toString().isEmpty()) {
            this.txtAccountName.setError("Mandatory");
            return false;
        }

        if (this.spAccountType.getSelectedItemPosition() == 3 || this.spAccountType.getSelectedItemPosition() == 4) {
            if (this.spPayDay.getSelectedItemPosition() == 0) {
                TextView errorText = (TextView) this.spPayDay.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Mandatory");
                return false;
            }
            if (this.spMonthExpire.getSelectedItemPosition() == 0) {
                TextView errorText = (TextView) this.spMonthExpire.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Mandatory");
                return false;
            }
            if (this.spYearExpire.getSelectedItemPosition() == 0) {
                TextView errorText = (TextView) this.spYearExpire.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Mandatory");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fullscreen_dialog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_cancel) {
            this.dismiss();
            getFragmentManager().beginTransaction().replace(R.id.flContent,((Fragment)new AccountList())).commit();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            // TODO: Completar boton save de AccountAdd

            if (!this.validate()) {
                Toast.makeText(getActivity(), "Solve the problems marked in red color", Toast.LENGTH_LONG).show();
                return false;
            }
            String initialBalance = "0.00";
            String amountLimit = "0.00";
            Integer payDay = 0;
            Integer expireMonth = 0;
            Integer expireYear = 0;

            if (!this.txtInitialBalance.getText().toString().equals("")) {
                initialBalance = this.txtInitialBalance.getText().toString();
            }
            if (!this.txtAmountLimit.getText().toString().equals("")) {
                amountLimit = this.txtAmountLimit.getText().toString();
            }
            if (this.spPayDay.getSelectedItemPosition() > 0) {
                payDay = this.spPayDay.getSelectedItemPosition();
            }
            if (this.spMonthExpire.getSelectedItemPosition() > 0) {
                expireMonth = this.spMonthExpire.getSelectedItemPosition();
            }
            if (this.spYearExpire.getSelectedItemPosition() > 0) {
                expireYear = Integer.parseInt(this.spYearExpire.getSelectedItem().toString());
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("icon", "");
            contentValues.put("color", "");
            contentValues.put("account_type_id", Double.parseDouble(((Integer) this.spAccountType.getSelectedItemPosition()).toString()));
            contentValues.put("name", this.txtAccountName.getText().toString());
            contentValues.put("description", this.txtAccountDescription.getText().toString());
            contentValues.put("initial_balance", Double.parseDouble(initialBalance));
            contentValues.put("amount_limit", Double.parseDouble(amountLimit));
            contentValues.put("pay_day", payDay);
            contentValues.put("expire_month", expireMonth);
            contentValues.put("expire_year", expireYear);
            contentValues.put("currency_country", currencyCountry.get(spAccountCurrency.getSelectedItemPosition()));

            try {
                if (accountId == null) {
                    MainActivity.abacoDataBase.insertOrThrow("account", null, contentValues);
                } else {
                    MainActivity.abacoDataBase.update("account", contentValues, "id=" + accountId, null);
                }
                this.dismiss();
                getFragmentManager().beginTransaction().replace(R.id.flContent,((Fragment)new AccountList())).commit();
                return true;
            } catch (Exception ex) {
                // TODO: Cambiar a mensaje de alerta
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG);
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month,int dayOfMonth){
     Toast.makeText(getActivity(),"Fecha: " + year + "-" + month + "-" + dayOfMonth,Toast.LENGTH_LONG).show();
    }

    //@Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    private void getDatePicker(){
        // Obtener fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Retornar en nueva instancia del dialogo selector de fecha
        DatePickerDialog test = new DatePickerDialog(getActivity(), this, year, month, day);
        test.show();
    }

    public void save(MainActivity activity) {
        Log.d("", "Salvando!!!");
        System.out.println("salvando" + this.txtAccountName.getText().toString());
    }


    public void cancel(MainActivity activity) {
        MainActivity a;
        if (getContext() instanceof MainActivity) {
            a = (MainActivity) getContext();
        }
        Log.d("", "Cancelando!!!");
        activity.replaceFragments(AccountList.class);

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
