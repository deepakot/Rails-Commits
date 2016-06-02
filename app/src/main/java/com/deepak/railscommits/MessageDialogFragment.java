package com.deepak.railscommits;



import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sharma on 6/2/2016.
 */
public class MessageDialogFragment extends DialogFragment {
    String message;
    static MessageDialogFragment newInstance(String message) {
        MessageDialogFragment frag = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.Message, message);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getArguments().getString(AppConstants.Message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample_dialog, container, false);
        getDialog().setTitle(AppConstants.Message);
        Button button = (Button)rootView.findViewById(R.id.dismiss);
        TextView messageView= (TextView) rootView.findViewById(R.id.message);
        messageView.setText(message);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                dismiss();
            }
        });
        return rootView;
    }

}
