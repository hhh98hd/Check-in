package uet.vnu.checkin;

import android.animation.Animator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity
{
    private ImageView imageView;
    private TextView textView;
    private Intent intent;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        runInFullscreen();

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                textView.animate().alphaBy(1f).setDuration(3450);
            }
        };

        intent = new Intent(this, CheckInActivity.class);
        textView = findViewById(R.id.tv_intro);
        textView.setAlpha(0f);
        imageView = findViewById(R.id.iv_icon);
        imageView.setAlpha(0f);
        imageView.animate().setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                handler.post(runnable);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                startActivity(intent);
                /* end current activity */
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            { }
        }).alphaBy(1f).setDuration(3500);
    }

    private void runInFullscreen()
    {
        /* fullscreen mode */
        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
