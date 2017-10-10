package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragment implements SeekBar.OnSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SeekBar sb1;
    private SeekBar sb2;
    private TextView txtv1;
    private TextView txtv2;
    private View mRootView;

    private int progress1;
    private int progress2;

    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seekBarInit();
    }

    //Initialize SeekBars and TextViews
    public void seekBarInit(){
        sb1=(SeekBar) getView().findViewById(R.id.seekBar2);
        sb2=(SeekBar) getView().findViewById(R.id.seekBar3);

        txtv1=(TextView) getView().findViewById(R.id.textView3);
        txtv2=(TextView) getView().findViewById(R.id.textView4);

        //Get last values of SeekBars
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue1 = 5;
        int defaultValue2 = 10;
        progress1 = sharedPref.getInt(getString(R.string.progress1), defaultValue1);
        progress2 = sharedPref.getInt(getString(R.string.progress2), defaultValue2);

        sb1.setProgress(progress1);
        sb2.setProgress(progress2);

        sb1.setOnSeekBarChangeListener(this);
        sb2.setOnSeekBarChangeListener(this);

        txtv1.setText(getString(R.string.delay) + ": " + sb1.getProgress() + "/" + sb2.getMax());
        txtv2.setText(getString(R.string.sensitivity) + ": " + sb2.getProgress() + "/" + sb2.getMax());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
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

   @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId())
        {
            case R.id.seekBar2:
                sb1.setProgress(progress);
                txtv1.setText(getString(R.string.delay) + ": " + progress + "/" + sb2.getMax());

                break;
            case R.id.seekBar3:
                sb2.setProgress(progress);
                txtv2.setText(getString(R.string.sensitivity) + ": " + sb2.getProgress() + "/" + sb2.getMax());
                break;
        }
    }



    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (seekBar.getId())
        {
            case R.id.seekBar2:
                editor.putInt(getString(R.string.progress1), sb1.getProgress());
                editor.commit();
                break;
            case R.id.seekBar3:
                editor.putInt(getString(R.string.progress2), sb2.getProgress());
                editor.commit();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
