package com.sternerlearn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfractionsAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Infraction> mInfractions;

    public InfractionsAdapter(Context context, ArrayList<Infraction> is) {
        this.mContext = context;
        this.mInfractions = is;
    }

    @Override
    public int getCount() {
    	
        return mInfractions != null ? mInfractions.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mInfractions.get(position);
    }
    
    // loverly hacks
    public void setData( ArrayList<Infraction> is )
    {
    	mInfractions = is;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

        if (v == null) 
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate( R.layout.infraction_list_item, null);
        } 
        
        Infraction a = mInfractions.get(position);
        
        TextView nameField = (TextView)v.findViewById(R.id.name);
        TextView descField = (TextView)v.findViewById(R.id.desc);
        TextView dateField = (TextView)v.findViewById(R.id.date);

        nameField.setText(a.mString);
        descField.setText(a.mDescription);
        
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d", Locale.US);
        
        dateField.setText(formatter.format(a.mTime).toString());

        return v;
    }
}
