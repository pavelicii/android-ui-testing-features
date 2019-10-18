package pavelnazimok.uitestingfeatures;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThirdScreen extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.screen_third, container, false);

        final Button listViewButton = root.findViewById(R.id.button_listview);

        List<String> randomNumbers = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            randomNumbers.add(String.valueOf(new Random().nextInt(10000)));
        }

        final CharSequence[] menuItems = randomNumbers.toArray(new CharSequence[0]);

        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setItems(menuItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                TextView label = root.findViewById(R.id.label_item_selected);
                                label.setText(menuItems[item]);
                            }
                        })
                        .setTitle("ListView")
                        .create();

                dialog.show();
            }
        });

        return root;
    }
}
