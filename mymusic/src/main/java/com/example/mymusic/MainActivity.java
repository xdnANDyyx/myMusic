package com.example.mymusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView next,pre,play,pic,playStyle;
    TextView singer,song;
    RecyclerView mrc;
    Animation animation;
    private SeekBar seekBar;
    private ObjectAnimator mCircleAnimator;
    int totalTime;
    // 用于判断当前的播放顺序，0->单曲循环,1->顺序播放,2->随机播放
    private int play_style = 0;
    //数据源
    List<LocalMusicBean> mDatas;
    private  LocalMusicAdapter adapter;
    //记录当前播放音乐的位置
    int currentPosition = -1;
    //记录暂停时音乐进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_main);

        initView();
        mediaPlayer = new MediaPlayer();
        mDatas = new ArrayList<>();
        adapter =  new LocalMusicAdapter(this,mDatas);
        mrc.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrc.setLayoutManager(layoutManager);
        //*****************************
        pic = findViewById(R.id.lmb_pic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircleAnimator = ObjectAnimator.ofFloat(pic, "rotationY", 0, 180,0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircleAnimator.setDuration(2000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircleAnimator.setInterpolator(new LinearInterpolator());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircleAnimator.setRepeatCount(-1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircleAnimator.setRepeatMode(ObjectAnimator.RESTART);
        }
        //设置播放方式
        playStyle = findViewById(R.id.play_style);
        playStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_style++;
                if (play_style > 2) {
                    play_style = 0;
                }

                switch (play_style) {
                    case 0:
                        playStyle.setImageResource(R.mipmap.icon_dan);
                        Toast.makeText(MainActivity.this, "单曲循环",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        playStyle.setImageResource(R.mipmap.icon_xun);
                        Toast.makeText(MainActivity.this, "顺序播放",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        playStyle.setImageResource(R.mipmap.icon_sui);
                        Toast.makeText(MainActivity.this, "随机播放",
                                Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

            //加载本地数据源
        loadLocalMusicData();


        //设置每一项点击事件
        setEventListener();
        //播放完毕后自动播放下一曲的方式************************************************************
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                switch (play_style){
                    case 0:
                        //播放当前播放的音乐
                        LocalMusicBean Bean1 = mDatas.get(currentPosition);
                        playMusicInPostion(Bean1);
                        break;
                    case 1:
                        //自动播放下一首音乐
                        LocalMusicBean Bean2 = mDatas.get(currentPosition+1);
                        playMusicInPostion(Bean2);
                        break;
                    case 2:
                        //随机播放下一首音乐
                        Random ran0=new Random();
                        int i=ran0.nextInt(5);
                        LocalMusicBean Bean3 = mDatas.get(i);
                        playMusicInPostion(Bean3);
                        break;
                    default:
                        break;
                }
            }
        });



    }
    //***********************************Handler原位置**************************************************

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            seekBar.setProgress(currentPosition);
        }
    };

    private void setEventListener() {
        adapter.setOnItemClickListner(new LocalMusicAdapter.OnItemClickListner() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void OnItemClick(View view, int position) {
                currentPosition = position;
                LocalMusicBean musivBean = mDatas.get(position);
                playMusicInPostion(musivBean);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playMusicInPostion(LocalMusicBean musivBean) {
        singer.setText(musivBean.getSinger());
        song.setText(musivBean.getSong());
        seekBar = findViewById(R.id.positionBar2);
        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        seekBar.setProgress(0);
        //设置新的播放路径
        try {
            mediaPlayer.setDataSource(musivBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //***********
        totalTime=mediaPlayer.getDuration();
        seekBar.setMax(totalTime);

        //歌曲进程条变换
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mediaPlayer.seekTo(progress);
                            seekBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer !=null){
                    try {
                        Message msg=new Message();
                        msg.what=mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void playMusic() {
        //播放音乐的函数
        if(mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            if (currentPausePositionInSong == 0){
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mCircleAnimator.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
                mCircleAnimator.resume();
            }

            play.setImageResource(R.mipmap.t);
        }
    }
    @SuppressLint("NewApi")
    private void pauseMusic() {
        //暂停音乐的函数
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
          currentPausePositionInSong = mediaPlayer.getCurrentPosition();
          mediaPlayer.pause();
            mCircleAnimator.pause();
          play.setImageResource(R.mipmap.w);
        }
    }

    @SuppressLint("NewApi")
    private void stopMusic() {
        //停止音乐的函数
        if(mediaPlayer!=null){
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            mCircleAnimator.end();
            play.setImageResource(R.mipmap.w);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void loadLocalMusicData() {
        //加载本地存储中的MP3文件
        /*
        1.获取
         */
        ContentResolver contentResolver = getContentResolver();
        //2.获取本地音乐URL地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //3.开始查询地址
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        //4.遍历cursor
        int id = 0;
        while (cursor.moveToNext()){
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String formatT = sdf.format(new Date(time));
            //将数据封装到对象当中
            LocalMusicBean localMusicBean = new LocalMusicBean(sid, song, singer, album, formatT, path);
            mDatas.add(localMusicBean);


        }
        //数据变化，通知适配器更新
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        //初始化控件函数
        next = findViewById(R.id.lmb_next);
        pre = findViewById(R.id.lmb_pre);
        play = findViewById(R.id.lmb_play);
        singer = findViewById(R.id.lmb_singer);
        song = findViewById(R.id.lmb_song);
        mrc = findViewById(R.id.rc);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lmb_next:
                if (currentPosition == mDatas.size() - 1){
                    Toast.makeText(this,"已经是最一首歌了，没有下一首了",Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPosition = currentPosition + 1;
                LocalMusicBean nextBean = mDatas.get(currentPosition);
                playMusicInPostion(nextBean);

                break;
            case R.id.lmb_pre:
                if (currentPosition == 0){
                    Toast.makeText(this,"已经是第一首歌了，没有上一首了",Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPosition = currentPosition - 1;
                LocalMusicBean lastBean = mDatas.get(currentPosition);
                playMusicInPostion(lastBean);
                break;
            case R.id.lmb_play:
                if (currentPosition == -1){
                    //并没有选中要播放的音乐
                    Toast.makeText(this,"请选择要播放的音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()){
                    //此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else {
                    //此时没有播放音乐，点击播放音乐
                    playMusic();
                }
                break;
        }
    }


}
