package com.example.swapn.whatsfordinner.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.whatsfordinner.DatabaseHelper;
import com.example.swapn.whatsfordinner.R;
import com.example.swapn.whatsfordinner.ReceipeIngredients;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReceipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReceipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceipeDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_DIRECTION = "receipe_direction";
    private static final String RECEIPE_PIC = "receipe_pic";
    private DatabaseHelper db;
    private List<ReceipeIngredients> receipeIngredientList;
    private TextView receipe_name;
    private TextView receipe_directions;
    private TextView receipe_ingredients;
    private ImageView receipe_image;
    String receipeName;
    String receipeDirection;
    String receipePic;
    private TextView  nutrition_calories;
    private TextView  nutrition_carbohydrates;
    private TextView  nutrition_minerals;
    private TextView  nutrition_vitamins;
    private TextView  nutrition_sugar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReceipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceipeDetailFragment newInstance(String param1, String param2) {
        ReceipeDetailFragment fragment = new ReceipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_receipe_detail, container, false);
        //if(getArguments(). != 0) {
           //
        if (getArguments() != null) {
            receipeName = getArguments().getString(RECEIPE_NAME);
            receipeDirection = getArguments().getString(RECEIPE_DIRECTION);
            receipePic = getArguments().getString(RECEIPE_PIC);
            initialize(view);
        }

        //}

        return view;
    }

    private void initialize (View view) {
        db = new DatabaseHelper(getActivity().getApplicationContext());
        receipeIngredientList = db.getReceipeIngredient(receipeName);
        String[] nutritions = db.getNutritions(receipeName).split(":");
        receipe_name = (TextView) view.findViewById(R.id.receipe_name);
        receipe_directions = (TextView) view.findViewById(R.id.receipe_direction);
        receipe_ingredients = (TextView) view.findViewById(R.id.receipe_ingredient);
        receipe_image = (ImageView) view.findViewById(R.id.receipe_pic);
        nutrition_calories = (TextView) view.findViewById(R.id.caloriesQuantity);
        nutrition_carbohydrates = (TextView) view.findViewById(R.id.carbohydratesQuantity);
        nutrition_minerals = (TextView) view.findViewById(R.id.mineralsQuantity);
        nutrition_vitamins = (TextView) view.findViewById(R.id.vitaminsQuantity);
        nutrition_sugar = (TextView) view.findViewById(R.id.sugarQuantity);
        receipe_image.setImageBitmap(BitmapFactory.decodeFile(receipePic));
        receipe_name.setText(receipeName);
        receipe_directions.setText(receipeDirection);
        nutrition_calories.setText(nutritions[0] + " cal");
        nutrition_carbohydrates.setText(nutritions[1] + " unit");
        nutrition_minerals.setText(nutritions[2] + " unit");
        nutrition_vitamins.setText(nutritions[3] + " unit");
        nutrition_sugar.setText(nutritions[4] + " unit");
        String ingredientText = "";
        for(ReceipeIngredients receipeIngredient : receipeIngredientList) {
            String[] temp = receipeIngredient.getIngredient_quantity().split(":");
            ingredientText += "* "
                    + receipeIngredient.getIngredient_name()
                    + " (" + temp[0] + " " + temp[1] + ") "
                    + System.getProperty ("line.separator")
                    + System.getProperty ("line.separator");
        }
        receipe_ingredients.setText(ingredientText);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
       // void onFragmentInteraction(Uri uri);
    }
}
