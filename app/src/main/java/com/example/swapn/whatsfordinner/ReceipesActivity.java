package com.example.swapn.whatsfordinner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReceipesActivity extends AppCompatActivity {
    private List<Receipe> receipes= new ArrayList<Receipe>();
    private DatabaseHelper db;
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_DIRECTION = "receipe_direction";
    private static final String RECEIPE_PIC = "receipe_pic";
    private static  String mealDate = "";
    private static  String mealTime= "";
    public static final String PREFS_NAME = "MyMealsPref";
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipes);
        Intent intent = getIntent();
        mealDate = intent.getStringExtra("mealdate");
        mealTime = intent.getStringExtra("time");
        db = new DatabaseHelper(getApplicationContext());
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String[] meal_receipes = settings.getString("meal_receipes", "").split(":");
        String[] meal_receipes_dir = settings.getString("meal_receipes_dir", "").split(":");
        String[] meal_receipes_pic = settings.getString("meal_receipes_pic", "").split(":");

        for(int i=0; i < meal_receipes.length; i++) {
            if(!meal_receipes[i].equals("")) {
                receipes.add(new Receipe(meal_receipes[i],meal_receipes_dir[i],meal_receipes_pic[i]));
            }
        }

       // receipes = db.getAllReceipes();
        contextOfApplication = getApplicationContext();
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(receipes);
        rv.setAdapter(adapter);


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
            holder.receipePic.setImageResource(R.drawable.a);
         /*   if(receipes.get(i).getReceipe_pic().equals("default")) {
                holder.receipePic.setImageResource(R.drawable.a);
            } else {
                try {
                    Uri imageUri = Uri.parse(receipes.get(i).getReceipe_pic());
                    final InputStream imageStream = getContextOfApplication().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    holder.receipePic.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    Log.e("error",e.getMessage());
                }
            } */
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

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String meal_receipes="";
                        String meal_receipes_dir = "";
                        String meal_receipes_pic = "";
                        boolean flag = true;
                        for(Receipe r : receipes) {
                            if(r.getReceipe_name().equals(receipeName.getText().toString()) && flag) {
                                flag = false;
                            } else {
                                meal_receipes += ":" + r.getReceipe_name();
                                meal_receipes_dir += ":" + r.getReceipe_direction();
                                meal_receipes_pic += ":" + r.getReceipe_pic();
                                //flag=false;
                            }
                        }

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("meal_receipes", meal_receipes);
                        editor.putString("meal_receipes_dir", meal_receipes_dir);
                        editor.putString("meal_receipes_pic", meal_receipes_pic);
                        editor.commit();


                        Context context = v.getContext();
                        Intent intent = new Intent(context, MealsActivity.class);
                        intent.putExtra("mealdate", mealDate);
                        intent.putExtra("mealtime", mealTime);
                        intent.putExtra(RECEIPE_NAME, receipeName.getText().toString());
                        context.startActivity(intent);
                    }
                });

            }
        }
    }


}

