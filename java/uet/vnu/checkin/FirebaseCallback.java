package uet.vnu.checkin;

public interface FirebaseCallback
{
    void onSeatDataReceived(String data);

    void onSeatDataSent();
}
