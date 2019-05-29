package uet.vnu.checkin;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase
{
    private FirebaseDatabase database;
    private FirebaseCallback callback;

    public Firebase(FirebaseCallback _callback)
    {
        this.callback = _callback;
        database = FirebaseDatabase.getInstance();
        database.getReference() .child("event")
                                .child("seat")
                                .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                callback.onSeatDataReceived(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    public void sendSeatData(char seats[][])
    {
        String seatStatus = "";
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(seats[i][j] == '*' || seats[i][j] == '1')
                {
                    seatStatus += "1";
                }
                else
                {
                    seatStatus += "0";
                }
            }
            /* end of row */
            seatStatus += "/";
        }
        /* remove old data */
        database.getReference().child("event").child("seat").setValue("");
        database.getReference().child("event").child("seat").setValue(seatStatus).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                callback.onSeatDataSent();
            }
        });
    }

    public void confirmCheckIn(long index, long checkInNumber)
    {
        database.getReference().child("event").child("check_in").setValue(checkInNumber + 1);
        database.getReference().child("list").child(Long.toString(index)).child("checkedIn").setValue(true);
    }

}
