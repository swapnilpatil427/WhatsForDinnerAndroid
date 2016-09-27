package com.example.swapn.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GroceryEditActivity extends AppCompatActivity {

    private List<IngredientClass> ingredients = new ArrayList<IngredientClass>();
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_INGREDIENTS = "receipe_ingredients";
    private static final String RECEIPE_IMAGE = "receipe_image";
    private static final String RECEIPE_DIRECTION = "receipe_DIRECTION";
    private String receipe_name;
    private String receipe_image;
    private String receipe_direction;
    private String receipe_nutrition;
    private String edit="";
    private List<View> itemViews;
    private RecyclerView rv;
    DatabaseHelper db;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_edit);
        Intent intent = getIntent();
        receipe_name = intent.getStringExtra(RECEIPE_NAME);
        String receipe_ingredients = intent.getStringExtra(RECEIPE_INGREDIENTS);
        receipe_image = intent.getStringExtra(RECEIPE_IMAGE);
        receipe_direction = intent.getStringExtra(RECEIPE_DIRECTION);
        receipe_nutrition = intent.getStringExtra("nutrition");
        edit = intent.getStringExtra("edit");
        String[] ingredient_list = receipe_ingredients.split(";");
        db = new DatabaseHelper(getApplicationContext());
        // Log.d("test",id + name);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        HashMap<String, Integer> unique = new  HashMap<String, Integer>();
        for(int i = 0;i< ingredient_list.length; i++) {
            System.out.println("length"+ ingredient_list.length);
            if(unique.containsKey(ingredient_list[i])) {
                unique.put(ingredient_list[i], unique.get(ingredient_list[i]) + 1);
            } else {
                unique.put(ingredient_list[i], 1);
            }
        }

        for (String key : unique.keySet()) {
            try {
                Ingredients ingredient = db.getIngredient(key);
                IngredientClass temp = new IngredientClass(key, ingredient.getIngredient_unit(), unique.get(key));
                ingredients.add(temp);
            } catch (Exception e) {
                Log.d("error", "here");
            }
        }


       // initializeData(ingredients_list);
        RVAdapter adapter = new RVAdapter(ingredients);
        rv.setAdapter(adapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void SubmitClick(View view) {
            Receipe receipe = new Receipe(receipe_name, receipe_direction, receipe_image);
            List<ReceipeIngredients> receipeIngredients = new ArrayList<ReceipeIngredients>();
            if (validateIngredients()) {
                for (int i = 0; i < rv.getChildCount(); i++) {
                    View child = rv.getChildAt(i);
                    RecyclerView.ViewHolder childViewHolder = rv.getChildViewHolder(child);
                    TextView name = (TextView) child.findViewById(R.id.ingredientName);
                    TextView quantity = (TextView) child.findViewById(R.id.ingredientQuantity);
                    TextView unit = (TextView) child.findViewById(R.id.unit);
                    ReceipeIngredients receipe_ingredient = new ReceipeIngredients(receipe_name, name.getText().toString(),
                            quantity.getText().toString() + ":" + unit.getText().toString());
                    receipeIngredients.add(receipe_ingredient);
                }

                if(edit == null) {
                    db.createReceipe(receipe, receipeIngredients, receipe_nutrition);
                } else {
                    db.updateReceipeData(receipe, receipeIngredients);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), "Receipe Updated.", duration);
                    toast.show();
                }
                Intent intent = new Intent(this, ReceipeActivity.class);
                startActivity(intent);
            }

    }

    public boolean validateIngredients() {
        return true;
    }


    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.







    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroceryEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.swapn.whatsfordinner/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroceryEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.swapn.whatsfordinner/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public static class RVAdapter extends RecyclerView.Adapter<RVAdapter.IngredientViewHolder> {

        List<IngredientClass> ingredients;

        RVAdapter(List<IngredientClass> ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grocery_edit_card, viewGroup, false);
            IngredientViewHolder pvh = new IngredientViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(IngredientViewHolder holder, int i) {
            holder.ingredientName.setText(ingredients.get(i).getIngredient_name());
            holder.ingredientQuantity.setText(Integer.toString(ingredients.get(i).getIngredient_quantity()));
            holder.ingredientUnit.setText(ingredients.get(i).getIngredient_unit());
            //  holder.personPhoto.setImageResource(persons.get(i).photoId);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }


        public static class IngredientViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView ingredientName;
            TextView ingredientQuantity;
            TextView ingredientUnit;
            ImageButton add;
            ImageButton remove;

            IngredientViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                ingredientName = (TextView) itemView.findViewById(R.id.ingredientName);
                ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredientQuantity);
                ingredientUnit = (TextView) itemView.findViewById(R.id.unit);
                //setUnit(ingredientUnit, ingredientName);
                add = (ImageButton) itemView.findViewById(R.id.add_quantity);
                remove = (ImageButton) itemView.findViewById(R.id.remove_quantity);
                //itemViews.add(itemView);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ingredientName.setPaintFlags( ingredientName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        Context context = v.getContext();
                        int count = Integer.parseInt(ingredientQuantity.getText().toString());
                        ingredientQuantity.setText(Integer.toString(count + 1));
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        int count = Integer.parseInt(ingredientQuantity.getText().toString());
                        if (count != 0) {
                            ingredientQuantity.setText(Integer.toString(count - 1));
                            ingredientQuantity.setText(Integer.toString(count - 1));
                            if(count-1 == 0)
                                ingredientName.setPaintFlags(ingredientName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                    }
                });
            }
        }
    }
}
