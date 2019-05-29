package uet.vnu.checkin;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class SeatManager
{
    /* user is only allowed to select 1 seat / ID */
    private boolean seatSelected;

    private char[][] seats;
    private Context context;
    private  String seatStatus;

    public SeatManager(Context _context)
    {
        this.seatSelected = false;
        this.seatStatus = new String();
        this.context = _context;
        seats = new char[15][10];
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                this.seats[i][j] = '0';
            }
        }
    }

    public void setSeatStatus(String str)
    {
        this.seatStatus = str;
        int j = 0, curRow = 0, n = seatStatus.length();
        for(int i = 0; i < n; i++)
        {
            if(seatStatus.charAt(i) != '/')
            {
                seats[curRow][j] = str.charAt(i);
                j++;
            }
            else
            {
                curRow += 1;
                j = 0;
            }
        }
    }

    public Character getCurrentSeatStatus(int row, int col)
    {
        return seats[row][col];
    }

    public char[][] getAllSeatsStatus()
    {
        return  seats;
    }

    public void selectSeat(int row, int col, View view)
    {
        if(this.seats[row][col] != '1')
        {
            if(this.seats[row][col] == '0' && seatSelected == false)
            {
                this.seats[row][col] = '*';
                this.seatSelected = true;
                view.setBackgroundColor(this.context.getResources().getColor(R.color.colorSelected));
            }
            else if(this.seats[row][col] == '0' && seatSelected == true)
            {
                Toast.makeText(context, "Bạn chỉ được chọn 1 chỗ duy nhất.", Toast.LENGTH_SHORT).show();
            }
            else if(this.seats[row][col] == '*' && seatSelected == true)
            {
                this.seats[row][col] = '0';
                this.seatSelected = false;
                view.setBackgroundColor(this.context.getResources().getColor(R.color.colorNormal));
            }
        }
    }

    public boolean aSeatIsSelected()
    {
        if(seatSelected == false)
        {
            return false;
        }
        else
        {
            return  true;
        }
    }
}
