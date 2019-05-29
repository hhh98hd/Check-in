package uet.vnu.checkin;

import android.app.Activity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

public class ParticipantManager
{
    private static ParticipantManager instance = null;

    private static DataSnapshot eventInfo;
    private static ProgressBar progressBar;
    private static TextView textView;
    private static long participantNumber;
    private static long checkedInNumber;
    private static Activity activity;

    /* we only want 1 ParticipantManager object */
    public static ParticipantManager getInstance(Activity _activity)
    {
        if(instance == null)
        {
            instance = new ParticipantManager();
        }
        activity = _activity;
        progressBar = activity.findViewById(R.id.progress_check_in);
        textView = activity.findViewById(R.id.tv_checkin_progress);
        return instance;
    }

    public Participant getParticipant(long index)
    {
        return  eventInfo.child("list")
                         .child(Long.toString(index))
                         .getValue(Participant.class);
    }

    /* if the ID is valid then return index of the participant, else return -1 */
    public long searchForId(String id)
    {
        for(long i = 0; i < this.participantNumber; i++)
        {
            Participant participant = eventInfo.child("list")
                                               .child(Long.toString(i))
                                               .getValue(Participant.class);
            if(participant.getId().equals(id))
            {
                return i;
            }
        }
        return -1;
    }

    /* get DataSnapshot */
    public void readFromDatabase(DataSnapshot _eventInfo)
    {
        this.eventInfo = _eventInfo;
        this.participantNumber = eventInfo.child("list").getChildrenCount();
        this.checkedInNumber = eventInfo.child("event").child("check_in").getValue(Integer.class);
    }

    public long getCheckedInNumber()
    {
        return this.checkedInNumber;
    }

    /* show check-in percentage and ProgressBar */
    public void showCheckInProgress()
    {
        float percentage = 100 * ((float)checkedInNumber / (float)participantNumber);
        progressBar.setIndeterminate(false);
        progressBar.setMax((int)participantNumber);
        progressBar.setProgress((int)checkedInNumber);
        textView.setText(Integer.toString(Math.round(percentage)) + "%");
    }
}
