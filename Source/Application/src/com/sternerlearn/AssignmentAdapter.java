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

public class AssignmentAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Assignment> mAssignments;

    public AssignmentAdapter(Context context, ArrayList<Assignment> assigns) {
        this.mContext = context;
        this.mAssignments = assigns;
    }

    @Override
    public int getCount() {
    	
        return mAssignments != null ? mAssignments.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mAssignments.get(position);
    }
    
    // loverly hacks
    public void setGrades( ArrayList<Assignment> assigns )
    {
    	mAssignments = assigns;
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
            v = inflater.inflate( R.layout.assignment_list_item, null);
        } 
        
        Assignment a = mAssignments.get(position);
        
        TextView nameField = (TextView)v.findViewById(R.id.name);
        TextView descField = (TextView)v.findViewById(R.id.desc);
        TextView pointsField = (TextView)v.findViewById(R.id.points);
        TextView dateField = (TextView)v.findViewById(R.id.date);

        nameField.setText(a.mName);
        descField.setText(a.mDesc);
        pointsField.setText("Worth: " + Double.toString(a.mPoints));
        
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, 'at' h:mm a", Locale.US);
        
        dateField.setText(formatter.format(a.mDueDate).toString());

        return v;
    }
}
