package com.example.seekm.studemts;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    EditText txtData;

    public DateDialog(View view){


        txtData=(EditText)view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c=Calendar.getInstance();

        c.set(1997,6,6);



        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);




        return new DatePickerDialog(getActivity(),this,year,month,day);

    }

    public void onDateSet(DatePicker view , int year , int month , int day){


        String date=day+"-"+(month+1)+"-"+year;
        txtData.setText(date);
    }

}
