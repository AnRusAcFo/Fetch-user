package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.Fragment.ViewerFragment;
import com.example.appbanhang.model.MeetingModel;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.VideoSDK;
import live.videosdk.rtc.android.listeners.MeetingEventListener;

public class MeetingActivity extends AppCompatActivity {
    private Meeting meeting;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        getMeetingIdFromServer();
    }

    private void getMeetingIdFromServer() {
        compositeDisposable.add(apiBanHang.getMeeting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meetingModel -> {
                            if (meetingModel.isSuccess()){
                                String meetingId = meetingModel.getResult().get(0).getMeetingid();
                                String token = meetingModel.getResult().get(0).getToken();
                                String mode = "VIEWER";
                                boolean streamEnable = mode.equals("CONFERENCE");
                                // initialize VideoSDK
                                VideoSDK.initialize(getApplicationContext());
                                // Configuration VideoSDK with Token
                                VideoSDK.config(token);
                                // Initialize VideoSDK Meeting
                                meeting = VideoSDK.initMeeting(
                                        MeetingActivity.this, meetingId, "FETCH",
                                        streamEnable, streamEnable, null, mode, false,null);
                                // join Meeting
                                meeting.join();
//                                Toast.makeText(getApplicationContext(), meetingId, Toast.LENGTH_SHORT).show();
                                // if mode is CONFERENCE than replace mainLayout with SpeakerFragment otherwise with ViewerFragment
                                meeting.addEventListener(new MeetingEventListener() {
                                    @Override
                                    public void onMeetingJoined() {
                                        if (meeting != null) {
                                            if (mode.equals("VIEWER")) {
                                                getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.mainLayout, new ViewerFragment(), "viewerFragment")
                                                        .commit();
                                            }
                                        }
                                    }
                                });
                            }
                        },throwable -> {
                            Log.d("logggggg", throwable.getMessage());
                        }
                ));
    }
    public Meeting getMeeting() {
        return meeting;
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}