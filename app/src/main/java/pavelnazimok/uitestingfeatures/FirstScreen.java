package pavelnazimok.uitestingfeatures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstScreen extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            public void onClick(View v) {
                coffeeOrder.incrementCoffeeCount();
                coffeeCount.setText(String.valueOf(coffeeOrder.getCoffeeCount()));
                totalPrice.setText(String.valueOf(coffeeOrder.getTotalPrice()));
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coffeeOrder.decrementCoffeeCount();
                coffeeCount.setText(String.valueOf(coffeeOrder.getCoffeeCount()));
                totalPrice.setText(String.valueOf(coffeeOrder.getTotalPrice()));
            }
        });

        return root;
    }
}
