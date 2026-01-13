package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcard.database.AppDatabase;
import com.example.flashcard.database.entities.Category;
import java.util.concurrent.Executors;

public class SetSelectionActivity extends AppCompatActivity {

    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_selection);

        RecyclerView recyclerView = findViewById(R.id.rv_sets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        recyclerView.setAdapter(adapter);

        // Header Navigation
        findViewById(R.id.btn_back_menu).setOnClickListener(v -> finish());

        loadCategories();

        adapter.setOnCategoryClickListener(category -> {
            adapter.toggleSelection(category.id);
        });

        findViewById(R.id.btn_start_game).setOnClickListener(v -> {
            java.util.List<Integer> selectedIds = adapter.getSelectedCategoryIds();
            if (selectedIds.isEmpty()) {
                android.widget.Toast
                        .makeText(this, "Wybierz przynajmniej jeden zestaw!", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert to array
            int[] ids = new int[selectedIds.size()];
            for (int i = 0; i < selectedIds.size(); i++)
                ids[i] = selectedIds.get(i);

            Intent intent = new Intent(SetSelectionActivity.this, CountdownActivity.class);
            intent.putExtra("MODE", "CATEGORY");
            intent.putExtra("CATEGORY_IDS", ids);
            startActivity(intent);
        });
    }

    private void loadCategories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Fetch categories
            final java.util.List<Category> categories = AppDatabase.getInstance(this).quizDao().getAllCategories();
            runOnUiThread(() -> adapter.setCategories(categories));
        });
    }
}
