package hu.ait.android.keely.migrainetracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.keely.migrainetracker.Data.Food;

import hu.ait.android.keely.migrainetracker.R;

/**
 * Created by Keely on 5/13/15.
 */
public class FoodAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Food getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private Context context;
    private List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList){
        this.context=context;
        this.foodList=foodList;
    }

    public void addFood(Food food){
        foodList.add(food);
    }

    public void updateFood(int index, Food food){
        foodList.set(index, food);
    }

    public void removeItem(int index){
        foodList.remove(index);
    }

    static class ViewHolder{

        TextView tvDesc;
        TextView tvDate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater=LayoutInflater.from(context);
            v=inflater.inflate(R.layout.row_foods, null);
            ViewHolder holder=new ViewHolder();
            holder.tvDesc= (TextView) v.findViewById(R.id.tvDesc);
            holder.tvDate= (TextView) v.findViewById(R.id.tvDate);
            v.setTag(holder);

        }

        final Food food=foodList.get(position);
        if (food!=null){
            ViewHolder holder= (ViewHolder) v.getTag();
            holder.tvDesc.setText(food.getDesc());
            holder.tvDate.setText(food.getDate().toString());

        }

        return v;
    }


}
