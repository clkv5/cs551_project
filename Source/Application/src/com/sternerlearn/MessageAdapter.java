package com.sternerlearn;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Message> mMessages;
	
    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.mContext = context;
        this.mMessages = messages;
    }
	
    
    @Override
    public int getCount() {
        return mMessages != null ? mMessages.size() : 0;
    }
    
    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
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
        
		Message m = mMessages.get(position);
		
		//TODO:  change IDs
        TextView msgField = (TextView)v.findViewById(R.id.name);
        TextView senderField = (TextView)v.findViewById(R.id.points);
        TextView dateField = (TextView)v.findViewById(R.id.date);
        TextView descField = (TextView)v.findViewById(R.id.desc);
        
        msgField.setText( m.getMessage() );
        senderField.setText(m.getSender() );
        dateField.setText( m.getDate() );
        descField.setText("");
		
        return v;
	}
	
    public void setMessages( ArrayList<Message> messages )
    {
    	mMessages = messages;
    }
}
