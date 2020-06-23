package com.pavelnazimok.uitesting.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.pavelnazimok.uitesting.R;

public class SecondScreen extends Fragment {

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.screen_second, container, false);

        final Button alertButton = root.findViewById(R.id.button_alert_dialog);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(R.string.button_alert_dialog);
                alertDialog.setMessage(getString(R.string.message_alert_dialog));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_alert_dialog_positive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_alert_dialog_negative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        return root;
    }
}
