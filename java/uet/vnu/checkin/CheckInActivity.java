package uet.vnu.checkin;

import android.app.Activity;
import android.content.Intent;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckInActivity extends AppCompatActivity implements View.OnTouchListener
{
    private FirebaseDatabase database;
    private ParticipantManager participantManager;
    private ConstraintLayout layout;
    private TextView textView;
    private ProgressBar progressBar;
    private Runnable runnable;
    private Handler handler;

    private long participantIndex;
    private boolean textColorState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        getSupportActionBar().hide();
        runInFullscreen();

        textView = findViewById(R.id.tv_touch_to_start);
        layout = (ConstraintLayout)findViewById(R.id.screen);
        progressBar = findViewById(R.id.progress_check_in);
        layout.setOnTouchListener(this);

        /* text blinking */
        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                textBlinkTask();
            }
        };
        handler.postDelayed(runnable, 750);

        database = FirebaseDatabase.getInstance();
        database.getReference().addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                participantManager = ParticipantManager.getInstance(CheckInActivity.this);
                participantManager.readFromDatabase(dataSnapshot);
                participantManager.showCheckInProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String id = data.getStringExtra("DATA");
                participantIndex = participantManager.searchForId(id);
                if(participantIndex != -1)
                {
                    Participant participant = participantManager.getParticipant(participantIndex);
                    /* if the participant has already checked-in before */
                    if(participant.getCheckedIn() == true)
                    {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        View dialogView = (inflater.inflate(R.layout.dialog_checkin_noti, null));
                        builder.setView(dialogView);
                        builder.create().show();
                    }
                    else
                    {
                        showSuccessDialog(participant);
                    }

                }
                else
                {
                    showFailedDialog();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        startActivityForResult(new Intent(this, QrScannerActivity.class), 1);
        return false;
    }

    private void textBlinkTask()
    {
        if(textColorState == true)
        {
            textView.setAlpha(1.0f);
            /* reverse the color state */
            textColorState = false;
        }
        else
        {
            textView.setAlpha(0.6f);
            /* reverse the color state */
            textColorState = true;
        }
        handler.postDelayed(runnable, 750);
    }

    private void runInFullscreen()
    {
        /* fullscreen mode */
        View  decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void showSuccessDialog(Participant participant)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = (inflater.inflate(R.layout.dialog_checkin_success, null));
        builder.setView(dialogView);

        TextView titleTextView = (TextView)dialogView.findViewById(R.id.tv_dialog_title);
        TextView idTextView    = (TextView)dialogView.findViewById(R.id.tv_dialog_id);
        TextView dobTextView   = (TextView)dialogView.findViewById(R.id.tv_dialog_dob);
        TextView typeTextView  = (TextView)dialogView.findViewById(R.id.tv_dialog_type);
        Button button          = (Button)dialogView.findViewById(R.id.btn_dialog_ok);

        titleTextView.setText(participant.getName());
        idTextView.setText(participant.getId());
        dobTextView.setText(participant.getDateOfBirth());
        typeTextView.setText(participant.getType());
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CheckInActivity.this, SeatPickerActivity.class);
                intent.putExtra("CHECK_IN_NUMBER", participantManager.getCheckedInNumber());
                intent.putExtra("PARTICIPANT_INDEX", participantIndex);
                startActivity(intent);
                /* end current activity */
                finish();
            }
        });

        builder.create().show();
    }

    private void showFailedDialog()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = (inflater.inflate(R.layout.dialog_checkin_failed, null));
        builder.setView(dialogView);
        builder.create().show();
    }
}
