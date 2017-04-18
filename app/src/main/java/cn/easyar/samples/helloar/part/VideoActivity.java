package cn.easyar.samples.helloar.part;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import cn.easyar.samples.helloar.R;

/**
 * Created by Pants on 2017/4/18.
 */

public class VideoActivity extends Activity {

    public static final String ARG_VIDEO_PATH = "arg_video_path";

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_video);

        String videoUrl = getIntent().getStringExtra(ARG_VIDEO_PATH);
        Uri uri = Uri.parse(videoUrl);

        videoView = (VideoView) this.findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public static Intent getIntent(Context context, String videoPath) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(ARG_VIDEO_PATH, videoPath);
        return intent;
    }
}
