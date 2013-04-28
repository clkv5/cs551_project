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
            v = inflater.inflate( R.layout.message_list_item, null);
        } 
        
		Message m = mMessages.get(position);

        TextView msgField = (TextView)v.findViewById(R.id.message);
        TextView senderField = (TextView)v.findViewById(R.id.sender);
        TextView dateField = (TextView)v.findViewById(R.id.date);
        
        msgField.setText( m.getMessage() );
        senderField.setText(m.getSender() );
        dateField.setText( m.getDate() );;
		
        return v;
	}
	
    public void setMessages( ArrayList<Message> messages )
    {
    	mMessages = messages;
    }
}
