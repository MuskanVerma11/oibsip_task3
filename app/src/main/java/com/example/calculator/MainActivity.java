package com.example.calculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    TextView num;
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().getDecorView().setSystemUiVisibility(0);

        num=findViewById(R.id.num);
        Button zero=findViewById(R.id.zero);
        Button one = findViewById(R.id.one);
        Button two=findViewById(R.id.two);
        Button three=findViewById(R.id.three);
        Button four =findViewById(R.id.four);
        Button five =findViewById(R.id.five);
        Button six=findViewById(R.id.six);
        Button seven=findViewById(R.id.seven);
        Button eight=findViewById(R.id.eight);
        Button nine=findViewById(R.id.nine);
        Button add=findViewById(R.id.add);
        Button sub=findViewById(R.id.sub);
        Button mul=findViewById(R.id.multiply);
        Button div=findViewById(R.id.divide);
        Button equal=findViewById(R.id.equal);
        ImageButton erase=findViewById(R.id.cut);

        zero.setOnClickListener(v -> updateText("0"));

        one.setOnClickListener(v -> updateText("1"));

        two.setOnClickListener(v -> updateText("2"));

        three.setOnClickListener(v -> updateText("3"));

        four.setOnClickListener(v -> updateText("4"));

        five.setOnClickListener(v -> updateText("5"));

        six.setOnClickListener(v -> updateText("6"));

        seven.setOnClickListener(v -> updateText("7"));

        eight.setOnClickListener(v -> updateText("8"));

        nine.setOnClickListener(v -> updateText("9"));

        add.setOnClickListener(v -> {
            if(num.getText().toString().isEmpty() || num.getText().toString()=="0"){
                showAlert("Invalid Input");
            }else {
                count++;
                if(count==2){
                    handleCalculate();
                    count--;
                }

                updateText("+");
            }

        });

        sub.setOnClickListener(v -> {
            count++;
            if(count==2){
                handleCalculate();
                count--;
            }
            if(num.getText().toString().isEmpty() || num.getText().toString()=="0"){
                showAlert("Invalid Input");
            }else updateText("-");
        });

        mul.setOnClickListener(v -> {
            count++;
            if(count==2){
                handleCalculate();
                count--;
            }
            if(num.getText().toString().isEmpty() || num.getText().toString()=="0"){
                showAlert("Invalid Input");
            }else updateText("*");
        });

        div.setOnClickListener(v -> {
            count++;
            if(count==2){
                handleCalculate();
                count--;
            }
            if(num.getText().toString().isEmpty() || num.getText().toString()=="0"){
                showAlert("Invalid Input");
            }else updateText("/");
        });

        erase.setOnClickListener(v -> handleErase());

        equal.setOnClickListener(v -> handleCalculate());

        erase.setOnLongClickListener(v -> {

            num.setText("0");

            return true;
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));

    }

    public void showAlert(String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        TextView customTitle = new TextView(this);
        customTitle.setText("Warning !!!");
        customTitle.setPadding(60,60,60,10); // You can customize padding here
        customTitle.setTextSize(20);
        customTitle.setTextColor(ContextCompat.getColor(this, R.color.black));

        builder.setCustomTitle(customTitle)
                .setMessage(status)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handleErase();
                        if (status.equals("Arithmetic Exception : Division by zero.")) {
                            num.setText("0");
                        }
                    }
                });

        
        AlertDialog alert = builder.create();
        alert.show();

        TextView titleView = alert.findViewById(android.R.id.title);
        if (titleView != null) {
            titleView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        TextView messageView = alert.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary));

    }


    public void updateText(String number) {
        String currentText=num.getText().toString();

        if(currentText.equals("0")){
            num.setText(number);
        }else {
            num.setText(num.getText() + number);
        }
    }

    public void handleErase(){
        String text = num.getText().toString();

        if (!text.isEmpty()) {
            // Remove the last character using substring
            text = text.substring(0, text.length() - 1);

            // Update the TextView with the new text
            num.setText(text);

            if(num.getText().toString().isEmpty()){
                num.setText("0");
            }
        }
    }

    public void handleCalculate(){
        String text=num.getText().toString();

        if (text.equals("0") || "+-/*".contains(String.valueOf(text.charAt(text.length() - 1)))) {
            showAlert("Invalid expression.");
//            count--;
            return;
        }

        double cal=evaluteQuestion(text);

        String formattedResult;
        if (cal == (long) cal) {
            formattedResult = String.format("%d", (long) cal);
        } else {
            formattedResult = String.format("%s", cal);
        }

        if (cal == 0) {
            num.setText("0");
        } else {
            num.setText(formattedResult);
        }

    }

    public double evaluteQuestion(String question) {
        Scanner sc = new Scanner(question);

        // get the next number from the scanner
        double firstValue = Double.parseDouble(sc.findInLine("[0-9]+(\\.[0-9]+)?"));

        // get everything which follows and is not a number (might contain white spaces)
        String operator = sc.findInLine("[^0-9]*").trim();
        double secondValue = Double.parseDouble(sc.findInLine("[0-9]+(\\.[0-9]+)?"));

        switch (operator){
            case "+":
                return firstValue + secondValue;
            case "-":
                if(firstValue-secondValue==0){
                    num.setText("0"+num.getText().toString());
                    return 0;
                }else return firstValue - secondValue;
            case "/":
                if (secondValue == 0) {
                    showAlert("Arithmetic Exception : Division by zero.");
                }
                return firstValue / secondValue;
            case "*":
                return firstValue * secondValue;

            default:
                return 0;
        }
    }

}