package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText display;       // Display for input and results
    private String currentOperator; // Stores the current operator
    private double firstNumber;     // First number in the calculation
    private boolean isNewOperation; // Flag to start a new number input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the display
        display = findViewById(R.id.display);
        isNewOperation = true;
    }

    // Handles number button clicks
    public void onNumberClick(View view) {
        if (isNewOperation) {
            display.setText("");  // Clear display for a new number
            isNewOperation = false;
        }
        Button button = (Button) view;
        String number = button.getText().toString();
        display.append(number);  // Add number to display
    }

    // Handles operation button clicks (+, -, *, /)
    public void onOperationClick(View view) {
        Button button = (Button) view;
        currentOperator = button.getText().toString();  // Get the operator
        firstNumber = Double.parseDouble(display.getText().toString());  // Store first number
        isNewOperation = true;  // Set flag to start a new number input
    }

    // Handles equals button click
    public void onEqualsClick(View view) {
        double secondNumber = Double.parseDouble(display.getText().toString());  // Get the second number
        double result = 0;

        // Perform the appropriate calculation based on the operator
        switch (currentOperator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    isNewOperation = true;
                    return;
                }
                break;
        }
        // Display the result
        display.setText(String.valueOf(result));
        isNewOperation = true;  // Ready for next operation
    }

    // Handles clear button click
    public void onClearClick(View view) {
        display.setText("");  // Clear display
        firstNumber = 0;
        isNewOperation = true;
    }
}
