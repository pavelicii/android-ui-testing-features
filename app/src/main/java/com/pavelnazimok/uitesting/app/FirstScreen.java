package com.pavelnazimok.uitesting.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pavelnazimok.uitesting.R;

public class FirstScreen extends Fragment {

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.screen_first, container, false);

        final int defaultCoffeePrice = 5;

        final TextView coffeePrice = root.findViewById(R.id.label_coffee_price);
        final TextView totalPrice = root.findViewById(R.id.label_total_price);
        final TextView coffeeCount = root.findViewById(R.id.label_coffee_count);
        final Button incrementButton = root.findViewById(R.id.button_coffee_increment);
        final Button decrementButton = root.findViewById(R.id.button_coffee_decrement);
        final CoffeeOrder coffeeOrder = new CoffeeOrder(defaultCoffeePrice);

        coffeePrice.setText(String.valueOf(defaultCoffeePrice));
        totalPrice.setText("0");

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                coffeeOrder.incrementCoffeeCount();
                coffeeCount.setText(String.valueOf(coffeeOrder.getCoffeeCount()));
                totalPrice.setText(String.valueOf(coffeeOrder.getTotalPrice()));
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                coffeeOrder.decrementCoffeeCount();
                coffeeCount.setText(String.valueOf(coffeeOrder.getCoffeeCount()));
                totalPrice.setText(String.valueOf(coffeeOrder.getTotalPrice()));
            }
        });

        return root;
    }
}
