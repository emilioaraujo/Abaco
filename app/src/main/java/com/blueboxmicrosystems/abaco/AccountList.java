package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueboxmicrosystems.abaco.model.ListAccountModel;
import com.blueboxmicrosystems.abaco.model.ListCategoryModel;
import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Map;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link AccountList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountList extends Fragment {
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
    ListView list;
    Button btnCancel;
    Button btnSave;
    private Integer currentAccountId;
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
    AlertDialog accountDialog;
    View view;

    private String[] days = new String[]{"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30"};

    private String[] months = new String[]{"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    private ArrayList<String> currencyCodes = new ArrayList<>();
    private ArrayList<String> currencyCountry = new ArrayList<>();

    public AccountList() {
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
    public static AccountList newInstance(String param1, String param2) {
        AccountList fragment = new AccountList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setTitle("Account List");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_list, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddAccount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "Boton add account presionado");
                currentAccountId = 0;
                accountDialog = getAccountDialog();
                accountDialog.show();

            }
        });

        //Find the +1 button
        // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);

        return view;
    }

    public void configureAccountList() {
        if (MainActivity.abacoDataBase != null) {

            Cursor c = MainActivity.abacoDataBase.rawQuery("select id,name,description,initial_balance from account order by id desc", null);

            final ArrayList<Integer> accountId = new ArrayList<Integer>();
            final ArrayList<Integer> accountImageId = new ArrayList<Integer>();
            final ArrayList<String> accountName = new ArrayList<String>();
            final ArrayList<String> accountDescription = new ArrayList<String>();

            if (c.moveToFirst()) {
                do {
                    Integer id = c.getInt(0);
                    String name = c.getString(1);
                    String description = c.getString(2);
                    Double initBalance = c.getDouble(3);

                    accountId.add(id);
                    accountName.add(name);
                    accountDescription.add(description);
                    accountImageId.add(R.drawable.ic_menu_camera);
                } while (c.moveToNext());

                ListAccountModel adapter = new ListAccountModel(getActivity(), accountImageId, accountName, accountDescription);
                list = (ListView) getView().findViewById(R.id.lvwAccountList);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        currentAccountId = accountId.get(position);
                        accountDialog = getAccountDialog();
                        accountDialog.show();
                        if (MainActivity.abacoDataBase != null) {
                            Cursor c = MainActivity.abacoDataBase.rawQuery("" +
                                    "select " +
                                    "     id," +
                                    "     name," +
                                    "     description," +
                                    "     initial_balance," +
                                    "     coalesce(account_type_id,0) account_type_id," +
                                    "     pay_day, " +
                                    "     expire_month," +
                                    "     expire_year," +
                                    "     amount_limit," +
                                    "     currency_country " +
                                    " from account " +
                                    "where id =" + currentAccountId, null);
                            if (c.moveToFirst()) {
                                do {
                                    spAccountType.setSelection(c.getInt(4));
                                    txtAccountName.setText(c.getString(1).toString());
                                    txtAccountDescription.setText(c.getString(2).toString());
                                    txtInitialBalance.setText(c.getString(3).toString());
                                    txtAmountLimit.setText(c.getString(8));
                                    spPayDay.setSelection(c.getInt(5));
                                    spMonthExpire.setSelection(c.getInt(6));

                                    for (int i = 0; i < spYearExpire.getCount(); i++) {
                                        if (spYearExpire.getItemAtPosition(i).toString().equals(c.getString(7))) {
                                            spYearExpire.setSelection(i);
                                            break;
                                        }
                                    }
                                    String value = "";
                                    for (int i = 0; i < spAccountCurrency.getCount(); i++) {
                                        value = spAccountCurrency.getItemAtPosition(i).toString();
                                        // value = value.substring(0, value.indexOf(" ("));
                                        if (value.contains(c.getString(9))) {
                                            spAccountCurrency.setSelection(i);
                                            break;
                                        }
                                    }
                                } while (c.moveToNext());
                            }
                        }
                    }
                });
            }
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

    public AlertDialog getAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_account_add, null);
        builder.setView(v);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        //labels
        lblPayDay = (TextView) v.findViewById(R.id.lblPayDay);
        lblExpireEnd = (TextView) v.findViewById(R.id.lblExpireEnd);
        lblAmountLimit = (TextView) v.findViewById(R.id.lblAmountLimit);

        //inputs
        this.txtAccountName = (EditText) v.findViewById(R.id.txtAccountName);
        this.txtAccountDescription = (EditText) v.findViewById(R.id.txtAccountDescription);
        this.txtInitialBalance = (EditText) v.findViewById(R.id.txtInitialBalance);
        this.txtAmountLimit = (EditText) v.findViewById(R.id.txtAmountLimit);
        this.spAccountType = (Spinner) v.findViewById(R.id.spAccountType);
        this.spPayDay = (Spinner) v.findViewById(R.id.spPayDay);
        this.spMonthExpire = (Spinner) v.findViewById(R.id.spMonthExpire);
        this.spYearExpire = (Spinner) v.findViewById(R.id.spYearExpire);
        this.spAccountCurrency = (Spinner) v.findViewById(R.id.spAccountCurrency);
        //this.showOrHideControls();

        ArrayAdapter<CharSequence> adapterAccountType = ArrayAdapter.createFromResource(view.getContext(), R.array.account_type, android.R.layout.simple_spinner_item);
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spAccountType.setAdapter(adapterAccountType);

        ArrayAdapter<String> spinnerPayDayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, days);
        spinnerPayDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spPayDay.setAdapter(spinnerPayDayAdapter);

        //month spinner
        ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, months);
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

        ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, years);
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

        ArrayAdapter<String> spinnerCurrencyAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, currencyCodes);
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


        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accountDialog.dismiss();
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
                        String initialBalance = "0.00";
                        String amountLimit = "0.00";
                        Integer payDay = 0;
                        Integer expireMonth = 0;
                        Integer expireYear = 0;

                        if (!txtInitialBalance.getText().toString().equals("")) {
                            initialBalance = txtInitialBalance.getText().toString();
                        }
                        if (!txtAmountLimit.getText().toString().equals("")) {
                            amountLimit = txtAmountLimit.getText().toString();
                        }
                        if (spPayDay.getSelectedItemPosition() > 0) {
                            payDay = spPayDay.getSelectedItemPosition();
                        }
                        if (spMonthExpire.getSelectedItemPosition() > 0) {
                            expireMonth = spMonthExpire.getSelectedItemPosition();
                        }
                        if (spYearExpire.getSelectedItemPosition() > 0) {
                            expireYear = Integer.parseInt(spYearExpire.getSelectedItem().toString());
                        }

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("icon", "");
                        contentValues.put("color", "");
                        contentValues.put("account_type_id", Double.parseDouble(((Integer) spAccountType.getSelectedItemPosition()).toString()));
                        contentValues.put("name", txtAccountName.getText().toString());
                        contentValues.put("description", txtAccountDescription.getText().toString());
                        contentValues.put("initial_balance", Double.parseDouble(initialBalance));
                        contentValues.put("amount_limit", Double.parseDouble(amountLimit));
                        contentValues.put("pay_day", payDay);
                        contentValues.put("expire_month", expireMonth);
                        contentValues.put("expire_year", expireYear);
                        contentValues.put("currency_country", currencyCountry.get(spAccountCurrency.getSelectedItemPosition()));

                        try {
                            if (currentAccountId == 0) {
                                MainActivity.abacoDataBase.insertOrThrow("account", null, contentValues);
                            } else {
                                MainActivity.abacoDataBase.update("account", contentValues, "id=" + currentAccountId, null);
                            }
                            configureAccountList();
                            accountDialog.dismiss();
                        } catch (Exception ex) {
                            // TODO: Cambiar a mensaje de alerta
                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_LONG);
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
