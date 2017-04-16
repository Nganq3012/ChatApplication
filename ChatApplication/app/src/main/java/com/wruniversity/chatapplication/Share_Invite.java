package com.wruniversity.chatapplication;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;

public class Share_Invite extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_share__invite, container, false);

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(getActivity());
        mAlertBuilder.setMessage("Are you want invite more friends").setIcon(R.drawable.speech_bubble).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = "Chat with new friends in android";
                String imageThumnal = "https://androidcoban.com/wp-content/uploads/2016/07/hoc_lap_trinh_android.png";
                String linkShare = "https://www.facebook.com/ngatriples";
                shareLinkFB(title, linkShare, imageThumnal);

            }
        }).setPositiveButton("Invite your friends", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InviteFriend("https://fb.me/157785308080000", "http://2.bp.blogspot.com/-99shOruuadw/VQsG2T233sI/AAAAAAAAEi0/noFTxUBh_rg/s1600/appscripts.png");
            }
        }).setNeutralButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    return v;
    }
    public void shareLinkFB(String title, String linkShare, String imgThumnal) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setImageUrl(Uri.parse(imgThumnal))
                .setContentUrl(Uri.parse(linkShare))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#AndroidCoBan.Com")
                        .build())
                .build();
        ShareDialog.show(Share_Invite.this, content);
    }
    public void InviteFriend(String appLinkUrl, String previewImageUrl) {

        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(appLinkUrl)
                .setPreviewImageUrl(previewImageUrl)
                .build();
        AppInviteDialog.show(Share_Invite.this, content);
    }

}
