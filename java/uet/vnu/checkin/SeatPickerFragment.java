package uet.vnu.checkin;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SeatPickerFragment extends Fragment implements FirebaseCallback
{
    /* use this flag to make sure setupUiElements() runs only 1 time */
    private boolean UI_SETUP = false;
    /* user must select a seat before click Confirm button.
    */
    private int selectedSeat = -1;

    private Button button;
    private Button[] buttons;
    private LinearLayout.LayoutParams params1, params2;
    private LinearLayout buttonRow;
    private LinearLayout layout;
    private View fragmentView;
    private ProgressBar spinner;
    private AlertDialog alertDialog;

    private Firebase firebase;
    private SeatManager seatManager;

    private Bundle extras;
    private long participantIndex;
    private long checkInNumber;

    public SeatPickerFragment()
    { }

    private int getScreenWidth()
    {
        Display display = getActivity().getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (size.x);
    }

    private void showDialog()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = (inflater.inflate(R.layout.dialog_seat_picker, null));
        builder.setView(dialogView);

        Button buttonOk = dialogView.findViewById(R.id.btn_seat_ok);
        Button buttonCancel = dialogView.findViewById(R.id.btn_seat_cancel);

        TextView textView = dialogView.findViewById(R.id.tv_seat_number);
        textView.setText(Integer.toString(selectedSeat + 1));

        alertDialog = builder.create();

        buttonOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firebase.sendSeatData(seatManager.getAllSeatsStatus());
                alertDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void setupUiElements(final View fragmentView)
    {
        /* we setup UI elements only 1 time */
        if(UI_SETUP == false)
        {
            button = fragmentView.findViewById(R.id.button_seat_picker_confirm);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(seatManager.aSeatIsSelected())
                    {
                        showDialog();
                        selectedSeat = v.getId();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Bạn phải chọn ghế trước khi nhấn XÁC NHẬN", Toast.LENGTH_SHORT).show();
                        selectedSeat = -1;
                    }
                }
            });

            layout = (LinearLayout)fragmentView.findViewById(R.id.layout);
            layout.setOrientation(LinearLayout.VERTICAL);

            params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.width = getScreenWidth() / 10 - 30;
            params1.height -= 3;
            params1.topMargin = 20;
            params1.leftMargin = 8;
            params1.rightMargin = 8;

            params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);

            buttons = new Button[10];

            buttonRow = new LinearLayout(getActivity());

            int id = 0;
            for(int i = 0; i < 15; i++)
            {
                buttonRow = new LinearLayout(getContext());
                buttonRow.setOrientation(LinearLayout.HORIZONTAL);
                buttonRow.setLayoutParams(params2);
                for(int j = 0; j < 10; j++)
                {
                    buttons[j] = new Button(getActivity());
                    buttons[j].setId(id);
                    buttons[j].setLayoutParams(params1);
                    buttons[j].setText(Integer.toString(id + 1));
                    buttons[j].setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            /* get row and column of a button from its ID */
                            int row = (v.getId()) / 10;
                            int col = (v.getId()) - (row * 10);
                            selectedSeat = v.getId();
                            seatManager.selectSeat(row, col, v);
                        }
                    });
                    buttonRow.addView(buttons[j]);
                    id ++;
                }
                layout.addView(buttonRow);
            }
        }
        UI_SETUP = true;
    }

    /*
        if the seat is booked then set color Red
        else set color White
    */
    private void showSeatsAvailability()
    {
        Button button;
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                button = layout.findViewById(i * 10 + j);
                if(seatManager.getCurrentSeatStatus(i, j) == '1')
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorTaken));
                }
                else
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_seat_picker, container, false);
        spinner = fragmentView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        firebase = new Firebase(this);
        seatManager = new SeatManager(getActivity());
        extras = getActivity().getIntent().getExtras();
        participantIndex = extras.getLong("PARTICIPANT_INDEX");
        checkInNumber = extras.getLong("CHECK_IN_NUMBER");

        return fragmentView;
    }

    @Override
    public void onSeatDataReceived(String data)
    {
        this.seatManager.setSeatStatus(data);
        setupUiElements(fragmentView);
        showSeatsAvailability();
        /* when complete loading UI, the loading spinner disappears */
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onSeatDataSent()
    {
        /* number of checked-in increases by 1 and set checkIn = true */
        firebase.confirmCheckIn(participantIndex, checkInNumber);
        Intent intent = new Intent(getActivity(), FinishActivity.class);
        startActivity(intent);
        /* end curent activity */
        getActivity().finish();
    }
}

