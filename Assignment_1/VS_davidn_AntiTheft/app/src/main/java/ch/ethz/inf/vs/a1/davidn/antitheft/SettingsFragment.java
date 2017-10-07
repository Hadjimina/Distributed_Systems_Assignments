package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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

    public void seekBarInit(){
        sb1=(SeekBar) getActivity().findViewById(R.id.seekBar2);
        sb2=(SeekBar) getActivity().findViewById(R.id.seekBar3);

        txtv1=(TextView) getActivity().findViewById(R.id.textView3);
        txtv2=(TextView) getActivity().findViewById(R.id.textView4);

        sb1.setMax(10);
        sb2.setMax(20);

        sb1.setOnSeekBarChangeListener(this);
        sb2.setOnSeekBarChangeListener(this);

        txtv1.setText(R.string.settings);
        txtv2.setText(sb2.getProgress() + " / " + sb2.getMax());
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
                txtv1.setText(progress + " / " + sb1.getMax());
                break;
            case R.id.seekBar3:
                sb2.setProgress(progress);
                txtv2.setText(progress + " / " + sb2.getMax());
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
