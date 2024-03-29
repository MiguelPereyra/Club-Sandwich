package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;

        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView mainName = findViewById(R.id.name_value_tv);
        TextView alsoKnowAs = findViewById(R.id.also_known_as_value_tv);
        TextView placeOfOrigin = findViewById(R.id.place_of_origin_value_tv);
        TextView description = findViewById(R.id.description_value_tv);
        TextView ingredients = findViewById(R.id.ingredients_value_tv);

        mainName.setText(sandwich.getMainName());
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        String alsoKnowAsString = joinList(alsoKnownAsList);
        alsoKnowAs.setText(alsoKnowAsString);
        placeOfOrigin.setText(sandwich.getPlaceOfOrigin());
        description.setText(sandwich.getDescription());
        String ingredientsString = joinList(sandwich.getIngredients());
        ingredients.setText(ingredientsString);
    }

    private String joinList(List<String> list) {
        return TextUtils.join(", ", list);
    }
}
