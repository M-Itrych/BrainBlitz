package com.example.flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcard.database.entities.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private java.util.List<Category> categories = new java.util.ArrayList<>();
    private java.util.HashSet<Integer> selectedIds = new java.util.HashSet<>();
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void toggleSelection(int categoryId) {
        if (selectedIds.contains(categoryId)) {
            selectedIds.remove(categoryId);
        } else {
            selectedIds.add(categoryId);
        }
        notifyDataSetChanged();
    }

    public java.util.List<Integer> getSelectedCategoryIds() {
        return new ArrayList<>(selectedIds);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        boolean isSelected = selectedIds.contains(category.id);
        holder.bind(category, isSelected, listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }

        public void bind(final Category category, boolean isSelected, final OnCategoryClickListener listener) {
            tvName.setText(category.name);

            if (isSelected) {
                tvName.setBackgroundColor(android.graphics.Color.parseColor("#46178F")); // Kahoot Purple
                tvName.setTextColor(android.graphics.Color.WHITE);
            } else {
                tvName.setBackgroundColor(android.graphics.Color.parseColor("#EEEEEE"));
                tvName.setTextColor(android.graphics.Color.BLACK);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
