package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean mToggleValue;
    private ImageView mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("mainac", "main acivity works");
        setTitle("Lock me");

        //Initalize
        mToggleValue = false;
        mToggle = (ImageView)findViewById(R.id.imageView);
        mToggle.setImageResource(R.drawable.unlocked);

        //start or stop AntiTheftService
        mToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToggleValue = !mToggleValue;


                if(mToggleValue){
                    startService(new Intent(MainActivity.this, AntiTheftService.class));
                    mToggle.setImageResource(R.drawable.locked);
                }else{
                    stopService(new Intent(MainActivity.this, AntiTheftService.class));
                    mToggle.setImageResource(R.drawable.unlocked);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(this,SettingsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
