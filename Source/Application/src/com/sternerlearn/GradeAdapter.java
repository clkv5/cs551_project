package com.sternerlearn;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

@SuppressWarnings("deprecation")
public class GradeAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Grade> mGrades;

    public GradeAdapter(Context context, ArrayList<Grade> grades) {
        this.mContext = context;
        this.mGrades = grades;
    }

    @Override
    public int getCount() {
        return mGrades.size();
    }

    @Override
    public Object getItem(int position) {
        return mGrades.get(position);
    }
    
    // loverly hacks
    public void setGrades( ArrayList<Grade> grades )
    {
    	mGrades = grades;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		TwoLineListItem twoLineListItem;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        text1.setText(mGrades.get(position).getAssignmentName());
        text2.setText(mGrades.get(position).getPointsString());

        return twoLineListItem;
    }
}
