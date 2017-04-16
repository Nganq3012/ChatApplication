package com.wruniversity.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.wruniversity.chatapplication.Chat;
import com.wruniversity.chatapplication.Model.MessageInfo;
import com.wruniversity.chatapplication.R;
import com.wruniversity.chatapplication.UserActivity;
import com.wruniversity.chatapplication.ui.widget.CircleImageView;
import java.util.List;

/**
 * * Created by laptop88 on 2/25/2017.
 */
public class MessageAdapter extends ArrayAdapter<MessageInfo> {
    public MessageAdapter(Context context, int resource, List<MessageInfo> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageInfo message = getItem(position);

        if (convertView == null) {
/*
            if (message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message_send, parent, false);
            else*/
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message_receive, parent, false);
        }
        CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.Avatar);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.txtmesage);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.Sender);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);

        TextView author2TextView = (TextView) convertView.findViewById(R.id.Sender2);
        TextView date2TextView = (TextView) convertView.findViewById(R.id.date2);
        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.txtImage);
        CircleImageView avatar2 = (CircleImageView) convertView.findViewById(R.id.Avatar2);

        TextView author3TextView = (TextView) convertView.findViewById(R.id.Sender3);
        TextView date3TextView = (TextView) convertView.findViewById(R.id.date3);
        EmojiconTextView emojiTextView=(EmojiconTextView) convertView.findViewById(R.id.emojiconTextView);
        CircleImageView avatar3 = (CircleImageView) convertView.findViewById(R.id.Avatar3);
        boolean isAddress=message.getAddress()!=null;
        boolean isUrlYoutube=message.getURLYYB()!=null;
        boolean isEmoji=message.getConvEmoji()!=null;
        boolean isPhoto=message.getPhotoUrl()!=null;
        boolean isMes = message.getConv() != null;
        if(isMes || isUrlYoutube ||isAddress) {
            avatar.setVisibility(View.VISIBLE);
            dateTextView.setVisibility(View.VISIBLE);
            authorTextView.setVisibility(View.VISIBLE);
            messageTextView.setVisibility(View.VISIBLE);

            avatar2.setVisibility(View.GONE);
            author2TextView.setVisibility(View.GONE);
            date2TextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.GONE);
            emojiTextView.setVisibility(View.GONE);
            date3TextView.setVisibility(View.GONE);
            author3TextView.setVisibility(View.GONE);
            avatar3.setVisibility(View.GONE);

            if(message.getGender().equals("Male"))
                avatar.setImageResource(R.drawable.user_chat1);
            else
                avatar.setImageResource(R.drawable.user_chat2);
            if(isMes)
                messageTextView.setText(message.getConv());
            else if(isUrlYoutube)
                messageTextView.setText(message.getURLYYB());
            else {
                messageTextView.setTextSize(10);
                messageTextView.setText(message.getAddress());
            }
            dateTextView.setText(message.getDate());
            authorTextView.setText(message.getUserSend());
        }
       else if(isPhoto){
            avatar.setVisibility(View.GONE);
            authorTextView.setVisibility(View.GONE);
            dateTextView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.GONE);

            emojiTextView.setVisibility(View.GONE);
            date3TextView.setVisibility(View.GONE);
            author3TextView.setVisibility(View.GONE);
            avatar3.setVisibility(View.GONE);

            avatar2.setVisibility(View.VISIBLE);
            date2TextView.setVisibility(View.VISIBLE);
            author2TextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.VISIBLE);
            if(message.getGender().equals("Male"))
                avatar2.setImageResource(R.drawable.user_chat1);
            else
                avatar2.setImageResource(R.drawable.user_chat2);

            date2TextView.setText(message.getDate());
            author2TextView.setText(message.getUserSend());
            Glide.with(photoImageView.getContext()).load(message.getPhotoUrl()).into(photoImageView);
        }


        else if(isEmoji){

             avatar2.setVisibility(View.GONE);
            author2TextView.setVisibility(View.GONE);
            date2TextView.setVisibility(View.GONE);
            photoImageView.setImageResource(android.R.color.transparent);
            photoImageView.setVisibility(View.GONE);

            messageTextView.setVisibility(View.GONE);
            avatar.setVisibility(View.GONE);
            authorTextView.setVisibility(View.GONE);
            dateTextView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.GONE);

             avatar3.setVisibility(View.VISIBLE);
             date3TextView.setVisibility(View.VISIBLE);
             author3TextView.setVisibility(View.VISIBLE);
             emojiTextView.setVisibility(View.VISIBLE);

             if(message.getGender().equals("Male"))
                 avatar3.setImageResource(R.drawable.user_chat1);
             else
                 avatar3.setImageResource(R.drawable.user_chat2);
             emojiTextView.setText(message.getConvEmoji());
             date3TextView.setText(message.getDate());
             author3TextView.setText(message.getUserSend());
        }

        return convertView;
    }

}