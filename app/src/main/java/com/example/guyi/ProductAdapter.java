package com.example.guyi;

/**
 * Created by 陈 on 2020/7/9.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private static final String TAG = "ProductAdapter";

    private Context mContext;

    private List<Product> mProductList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ProductImage;
        TextView ProductName;
        TextView ProductPrice;
        TextView ProductLocation;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            ProductImage = (ImageView) view.findViewById(R.id.product_image);
            ProductName = (TextView) view.findViewById(R.id.product_name);
            ProductPrice = (TextView) view.findViewById(R.id.product_price);
            ProductLocation = (TextView) view.findViewById(R.id.product_location);
        }
    }

    public ProductAdapter(List<Product> ProductList) {
        mProductList = ProductList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Product Product = mProductList.get(position);
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra(ProductActivity.Product_NAME, Product.getName());
                intent.putExtra(ProductActivity.Product_IMAGE_ID, Product.getImageId());
                intent.putExtra(ProductActivity.Product_LOCATION, Product.getLocation());
                intent.putExtra(ProductActivity.Product_PRICE, myDoubleToString(Product.getPrice()));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product Product = mProductList.get(position);
        holder.ProductName.setText(Product.getName());
        holder.ProductLocation.setText(Product.getLocation());
        holder.ProductPrice.setText(myDoubleToString(Product.getPrice()));
        Glide.with(mContext).load(Product.getImageId()).into(holder.ProductImage);
    }

    public String myDoubleToString(double num){
        // 小数保留两位，并转为字符串
        BigDecimal bd = new BigDecimal(num);
        Double handle_num= bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return "￥" + handle_num.toString();
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}

