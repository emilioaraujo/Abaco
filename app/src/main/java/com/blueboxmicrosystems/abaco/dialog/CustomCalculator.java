package com.blueboxmicrosystems.abaco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blueboxmicrosystems.abaco.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by MARAUJO on 11/28/2016.
 */

public class CustomCalculator extends DialogFragment {

    private OnCalculatorResultListener onCalculatorResultListener;
    String lastKey;
    String lastValue;

    TextView txtOne;
    TextView txtTwo;
    TextView txtThree;
    TextView txtFour;
    TextView txtFive;
    TextView txtSix;
    TextView txtSeven;
    TextView txtEight;
    TextView txtNine;
    TextView txtCero;
    TextView txtDot;
    TextView txtMinus;
    TextView txtMult;
    TextView txtPlus;
    TextView txtDivision;
    TextView txtEqual;
    EditText txtDisplayValue;
    Button btnCancel;
    Button btnAccept;

    ArrayList<String> operation = new ArrayList<>();

    public CustomCalculator() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public interface OnCalculatorResultListener {
        public abstract void onResult(Double result);
    }

    public void setOnCalculatorResultListener(OnCalculatorResultListener listener) {
        this.onCalculatorResultListener = listener;
    }

    private void addToOperation(String value, String sign) {
        if (operation.size() == 0) {
            if (value.equals("") && (lastKey.equals("+") || lastKey.equals("-") || lastKey.equals("*") || lastKey.equals("/"))) {
                return;
            }
        }
        if (operation.size() > 0) {
            String lastEntry = operation.get(operation.size() - 1);
            if (lastValue.equals("")) {
                if (lastKey.equals("+") || lastKey.equals("-") || lastKey.equals("*") || lastKey.equals("/")) {
                    operation.remove(operation.size() - 1);
                }
            }
        }
        if (!lastValue.equals("")) {
            operation.add(value);
        }
        operation.add(lastKey);

        String listString = "";
        for (String s : operation) {
            listString += s + " ";
        }

        System.out.println(listString);
        calcular();
    }

    private void calcular() {
        lastValue = "";
        ArrayList<String> calc = operation;
        Double res = 0D;
        String lv = "";
        int it = 0;
        for (String s : calc) {
            it++;
            if (it == calc.size()) {
                break;
            }
            if (lv.equals("")) {
                res = Double.parseDouble(s);
            } else {
                if (lv.equals("+")) {
                    res = res + Double.parseDouble(s);
                }
                if (lv.equals("-")) {
                    res = res - Double.parseDouble(s);
                }
                if (lv.equals("*")) {
                    res = res * Double.parseDouble(s);
                }
                if (lv.equals("/")) {
                    res = res / Double.parseDouble(s);
                }
            }
            lv = s;
        }
        if ((res % 1) > 0) {
            txtDisplayValue.setText(res.toString());
        } else {
            txtDisplayValue.setText(((Integer) res.intValue()).toString());
        }
    }

    /**
     * Crea un diálogo de alerta sencillo
     *
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.calculator, null);
        builder.setView(v);

        lastValue = "";
        lastKey = "";

        txtDisplayValue = (EditText) v.findViewById(R.id.resultado);
        txtDisplayValue.setText("0");

        txtOne = (TextView) v.findViewById(R.id.numero_1);
        txtTwo = (TextView) v.findViewById(R.id.numero_2);
        txtThree = (TextView) v.findViewById(R.id.numero_3);
        txtFour = (TextView) v.findViewById(R.id.numero_4);
        txtFive = (TextView) v.findViewById(R.id.numero_5);
        txtSix = (TextView) v.findViewById(R.id.numero_6);
        txtSeven = (TextView) v.findViewById(R.id.numero_7);
        txtEight = (TextView) v.findViewById(R.id.numero_8);
        txtNine = (TextView) v.findViewById(R.id.numero_9);
        txtCero = (TextView) v.findViewById(R.id.cero);
        txtDot = (TextView) v.findViewById(R.id.punto);
        txtMinus = (TextView) v.findViewById(R.id.signo_menos);
        txtMult = (TextView) v.findViewById(R.id.signo_por);
        txtPlus = (TextView) v.findViewById(R.id.signo_mas);
        txtDivision = (TextView) v.findViewById(R.id.signo_division);
        txtEqual = (TextView) v.findViewById(R.id.signo_igual);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnAccept = (Button) v.findViewById(R.id.btnAccept);

        txtOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "1";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });

        txtTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "2";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "3";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "4";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "5";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "6";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "7";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "8";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "8";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtCero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "0";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = ".";
                if (lastValue.equals("")) {
                    txtDisplayValue.setText("");
                }
                lastValue = lastValue + lastKey;
                txtDisplayValue.setText(lastValue);
            }
        });


        txtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "-";
                addToOperation(lastValue, "-");

            }
        });


        txtMult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "*";
                addToOperation(lastValue, "*");
            }
        });


        txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "+";
                addToOperation(lastValue, "+");

            }
        });


        txtDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "/";
                addToOperation(lastValue, "/");
            }
        });


        txtEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastKey = "=";
                addToOperation(lastValue, "=");
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double res = 0D;
                if (!txtDisplayValue.getText().equals("")) {
                    res = Double.parseDouble(txtDisplayValue.getText().toString());
                    res = Math.round(res * 100.0) / 100.0;
                }
                onCalculatorResultListener.onResult(res);
                dismiss();
            }
        });

        return builder.create();
    }


}