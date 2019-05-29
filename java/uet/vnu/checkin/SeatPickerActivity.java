package uet.vnu.checkin;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SeatPickerActivity extends AppCompatActivity
{
    private SeatPickerFragment fragment = new SeatPickerFragment();

    private void runInFullscreen()
    {
        /* fullscreen mode */
        View  decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_picker);
        runInFullscreen();

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.layout_seat, fragment)
                                   .commit();
    }
}
