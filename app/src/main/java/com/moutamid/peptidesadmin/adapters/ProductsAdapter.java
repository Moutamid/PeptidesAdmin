package com.moutamid.peptidesadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.peptidesadmin.AddProductActivity;
import com.moutamid.peptidesadmin.Constants;
import com.moutamid.peptidesadmin.R;
import com.moutamid.peptidesadmin.models.ProductModel;

import java.util.ArrayList;
import java.util.Collection;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsVH> implements Filterable {

    Context context;
    ArrayList<ProductModel> list;
    ArrayList<ProductModel> listAll;

    public ProductsAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ProductsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsVH(LayoutInflater.from(context).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsVH holder, int position) {
        ProductModel model = list.get(holder.getAdapterPosition());
        holder.desc.setText(model.getShortDesc());
        holder.name.setText(model.getName());
        holder.catBody.setText(model.getCategory() + "\t\t" + context.getResources().getString(R.string.dot) + "\t\t" + model.getBodyType());
        Glide.with(context).load(model.getImage()).into(holder.imageView);

        holder.edit.setOnClickListener(v -> {
            Stash.put(Constants.PASS, model);
            context.startActivity(new Intent(context, AddProductActivity.class));
        });

        holder.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you really want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        Constants.databaseReference().child(Constants.PRODUCTS).child(model.getID()).removeValue();
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ProductModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(listAll);
            } else {
                for (ProductModel listModel : listAll) {
                    if (listModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends ProductModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ProductsVH extends RecyclerView.ViewHolder {
        MaterialButton edit, delete;
        TextView desc, catBody, name;
        ImageView imageView;

        public ProductsVH(@NonNull View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            desc = itemView.findViewById(R.id.shortDesc);
            name = itemView.findViewById(R.id.name);
            catBody = itemView.findViewById(R.id.cat);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
