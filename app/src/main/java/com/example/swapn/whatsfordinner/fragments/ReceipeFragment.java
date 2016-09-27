package com.example.swapn.whatsfordinner.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.whatsfordinner.DatabaseHelper;
import com.example.swapn.whatsfordinner.NewDishctivity;
import com.example.swapn.whatsfordinner.R;
import com.example.swapn.whatsfordinner.Receipe;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReceipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReceipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Receipe> receipes;
    private DatabaseHelper db;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static Context contextOfApplication;
    public static FragmentManager fragmentManger;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    Activity activity;
    private List<ImageView> imgvws = new ArrayList<ImageView>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReceipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceipeFragment newInstance(String param1, String param2) {
        ReceipeFragment fragment = new ReceipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activity = this.getActivity();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipe, container, false);
        db = new DatabaseHelper(getActivity().getApplicationContext());
        receipes = db.getAllReceipes();
        fragmentManger = getFragmentManager();
        contextOfApplication = getActivity().getApplicationContext();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(receipes);
        rv.setAdapter(adapter);
      /*  if(receipes.size() != 0 ) {
            mListener.onDetailsReceipe(receipes.get(0).getReceipe_name(),
                    receipes.get(0).getReceipe_direction(),
                    receipes.get(0).getReceipe_pic()
            );
        } */
        return view;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetailsReceipe(String receipe_name, String receipe_direction, String receipe_pic);
    }

    protected static void setPicture(ImageView iv, String uriString) {
        try {
            Uri imageUri = Uri.parse(uriString);
            final InputStream imageStream =getContextOfApplication().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            iv.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            Log.e("errorReceipeDetail",e.getMessage());
        }
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ReceipeViewHolder>{

        List<Receipe> receipes;

        RVAdapter(List<Receipe> receipes){
            this.receipes = receipes;
        }
        @Override
        public ReceipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipe_card, viewGroup, false);
            ReceipeViewHolder pvh = new ReceipeViewHolder(v);
            return pvh;
        }



        @Override
        public void onBindViewHolder(ReceipeViewHolder holder, int i) {
            holder.receipeName.setText(receipes.get(i).getReceipe_name());
            String direction = receipes.get(i).getReceipe_direction();
            if(direction.length() < 50) {
                direction = direction.replace(System.getProperty ("line.separator"), " ");
            } else {
                direction = direction.substring(0,50).replace(System.getProperty ("line.separator"), " ") + " ... more";
            }
            holder.receipeDirection.setText(direction);
            holder.receipePic.setTag(receipes.get(i).getReceipe_pic());
            //holder.receipePic.setImageResource(R.drawable.a);




            if(receipes.get(i).getReceipe_pic().equals("default")) {
                holder.receipePic.setImageResource(R.drawable.a);
            } else {
                try {
// Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }



                   //  String path = getPathFromURI(Uri.parse(receipes.get(i).getReceipe_pic()));
                   // Uri imageUri = Uri.parse(receipes.get(i).getReceipe_pic());
                    holder.receipePic.setImageBitmap(BitmapFactory.decodeFile(receipes.get(i).getReceipe_pic()));
                   /// Log.i("Log", "Image Path : " + path);
                    // Set the image in ImageView
                   //holder.receipePic.setImageURI(imageUri);

             /*       Uri imageUri = Uri.parse(receipes.get(i).getReceipe_pic());
                    final InputStream imageStream = getContextOfApplication().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    holder.receipePic.setImageBitmap(selectedImage); */
                } catch (Exception e) {
                    Log.e("error",e.getMessage());
                }
            }
            // holder.receipePic.(receipes.get(i).getReceipe_pic());
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
        @Override
        public int getItemCount() {
            return receipes.size();
        }

        public class ReceipeViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView receipeName;
            TextView receipeDirection;
            ImageView receipePic;

            ReceipeViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                receipeName = (TextView)itemView.findViewById(R.id.receipe_name);
                receipeDirection = (TextView)itemView.findViewById(R.id.receipe_direction);
                receipePic = (ImageView)itemView.findViewById(R.id.receipe_pic);
                //imgvws.add

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mListener.onDetailsReceipe(receipeName.getText().toString(),
                                receipeDirection.getText().toString(),
                                receipePic.getTag().toString()
                        );


                        //Put the value

                      /*  Context context = v.getContext();
                        Intent intent = new Intent(context, ReceipeDetailActivity.class);
                        intent.putExtra(RECEIPE_NAME, receipeName.getText().toString());
                        intent.putExtra(RECEIPE_DIRECTION, receipeDirection.getText().toString());
                        intent.putExtra(RECEIPE_PIC, receipePic.getTag().toString());
                        context.startActivity(intent); */
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, NewDishctivity.class);
                        intent.putExtra("receipe_name", receipeName.getText().toString());
                        intent.putExtra("receipe_dir", receipeDirection.getText().toString());
                        intent.putExtra("receipe_pic", receipePic.getTag().toString());
                        context.startActivity(intent);
                        return true;
                    }
                });

            }
        }
    }
}
