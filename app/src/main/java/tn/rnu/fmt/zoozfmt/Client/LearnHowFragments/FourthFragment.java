package tn.rnu.fmt.zoozfmt.Client.LearnHowFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tn.rnu.fmt.zoozfmt.R;

/*
*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FourthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
*/
public class FourthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView =  inflater.inflate(R.layout.fragment_fourth, container, false);
        TextView mText = mRootView.findViewById(R.id.description_txt);
        String textSet = getString(R.string.description_title)+"\n" +getString(R.string.first_tip)+"\n"
                +getString(R.string.second_tip)+"\n"
                +getString(R.string.third_tip)
                +"\n"+getString(R.string.conclusion)+"\n";
        //mText.setText(textSet);
        return mRootView;
    }
}
