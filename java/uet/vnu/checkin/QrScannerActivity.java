package uet.vnu.checkin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        getSupportActionBar().hide();

        zXingScannerView = new ZXingScannerView(this);
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        contentFrame.addView(zXingScannerView);
    }

    @Override
    public void handleResult(Result rawResult)
    {
        Intent intent = new Intent();
        intent.putExtra("DATA", rawResult.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera(0);
        zXingScannerView.setAutoFocus(true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        zXingScannerView.stopCamera();
    }
}
