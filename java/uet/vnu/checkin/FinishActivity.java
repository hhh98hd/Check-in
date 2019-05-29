package uet.vnu.checkin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity
{
    private CountDownTimer timer;
    private TextView textView;
    private Button button;

    private void runInFullscreen()
    {
        /* fullscreen mode */
        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        runInFullscreen();
        textView = findViewById(R.id.tv_count_down);
        button = findViewById(R.id.btn_finish_ok);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(FinishActivity.this, CheckInActivity.class);
                startActivity(intent);
            }
        });

        /* 5s count down */
        timer = new CountDownTimer(5000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                textView.setText(Long.toString(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish()
            {
                Intent intent = new Intent(FinishActivity.this, CheckInActivity.class);
                startActivity(intent);
                /* end current activity */
                finish();
            }
        }.start();
    }
}
