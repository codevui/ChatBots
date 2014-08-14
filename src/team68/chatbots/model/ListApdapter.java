package team68.chatbots.model;

import java.util.ArrayList;

import team68.chatbots.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;



public class ListApdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Message> mMessages;



	public ListApdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	@Override
	public Object getItem(int position) {		
		return mMessages.get(position);
	}
	
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_row, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.message_text);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.message.setText(message.getMessage());
		
		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		if(message.isStatusMessage())
		{
			holder.message.setBackgroundResource(0);
			lp.gravity = Gravity.LEFT;
			holder.message.setTextColor(R.color.textFieldColor);
		}
		else
		{		
			//Align user's message to the right
			if(message.isMine())
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_green);
				lp.gravity = Gravity.RIGHT;
			}
			//Align bot's message to the left
			else
			{
				holder.message.setBackgroundResource(R.drawable.speech_bubble_orange);
				lp.gravity = Gravity.LEFT;
			}
			holder.message.setLayoutParams(lp);
			holder.message.setTextColor(R.color.textColor);	
		}
		return convertView;
	}
	private static class ViewHolder
	{
		TextView message;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

}
