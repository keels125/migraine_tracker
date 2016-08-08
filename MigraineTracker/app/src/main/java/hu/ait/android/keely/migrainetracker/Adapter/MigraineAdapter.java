package hu.ait.android.keely.migrainetracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.keely.migrainetracker.Data.Migraine;
import hu.ait.android.keely.migrainetracker.R;

/**
 * Created by Keely on 5/11/15.
 */
public class MigraineAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return migrainesList.size();
    }

    @Override
    public Migraine getItem(int position) {
        return migrainesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private Context context;
    private List<Migraine> migrainesList;

    public MigraineAdapter(Context context, List<Migraine> migrainesList){
        this.context=context;
        this.migrainesList=migrainesList;
    }

    public void addMigraine(Migraine migraine){
        migrainesList.add(migraine);
    }

    public void updateMigraine(int index, Migraine migraine){
        migrainesList.set(index, migraine);
    }

    public void removeItem(int index){
        migrainesList.remove(index);
    }

    static class ViewHolder{
        TextView tvDesc;
        TextView tvDur;
        TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater=LayoutInflater.from(context);
            v=inflater.inflate(R.layout.row_migraines, null);
            ViewHolder holder=new ViewHolder();
            holder.tvDesc= (TextView) v.findViewById(R.id.tvDesc);
            holder.tvDur= (TextView) v.findViewById(R.id.tvDur);
            holder.tvDate= (TextView) v.findViewById(R.id.tvDate);
            v.setTag(holder);

        }

        final Migraine migraine=migrainesList.get(position);
        if (migraine!=null){
            ViewHolder holder= (ViewHolder) v.getTag();
            holder.tvDesc.setText(migraine.getDesc());
            holder.tvDur.setText(migraine.getDur());
            holder.tvDate.setText(migraine.getDate().toString());

        }

        return v;
    }


}
