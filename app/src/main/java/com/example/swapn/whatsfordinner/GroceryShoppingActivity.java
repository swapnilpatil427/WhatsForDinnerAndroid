package com.example.swapn.whatsfordinner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroceryShoppingActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private RecyclerView rv;
    HashMap<String, String> list;
    List<Ingredients> ingredients = new ArrayList<Ingredients>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_shopping);
        db = new DatabaseHelper(getApplicationContext());
        list =  db.getIngredientCount();
        initialize();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(ingredients);
        rv.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SwipeHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);
    }


    private void initialize() {
        int index = 0;
        for (String key : list.keySet()) {
            Ingredients ingredient = new Ingredients(key , list.get(key));
            ingredients.add(ingredient);
        }
    }

    public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

        RVAdapter adapter;
        public SwipeHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        public SwipeHelper(RVAdapter adapter) {
            super(ItemTouchHelper.LEFT , ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.remove(viewHolder.getAdapterPosition());
        }

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.IngredientViewHolder> {
        List<Ingredients> ingredients;
        RVAdapter(List<Ingredients> list) {
            this.ingredients = list;
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
          //  holder.ingredientQuantity.setText(Integer.toString(ingredients.get(i).getIngredient_unit()));
            String[] str = ingredients.get(i).getIngredient_unit().split(":");
            holder.ingredientUnit.setText(str[1]);
            holder.ingredientQuantity.setText(str[0]);
            //holder.personPhoto.setImageResource(persons.get(i).photoId);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        public void remove(int pos) {
            ingredients.remove(pos);
            this.notifyItemRemoved(pos);
        }


        public class IngredientViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView ingredientName;
            TextView ingredientUnit;
            TextView ingredientQuantity;
            ImageButton add;
            ImageButton remove;



            IngredientViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                ingredientName = (TextView) itemView.findViewById(R.id.ingredientName);
                ingredientUnit = (TextView) itemView.findViewById(R.id.unit);
                ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredientQuantity);

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
                            if(count-1 == 0)
                                ingredientName.setPaintFlags(ingredientName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                    }
                });
            }
        }
    }
}
