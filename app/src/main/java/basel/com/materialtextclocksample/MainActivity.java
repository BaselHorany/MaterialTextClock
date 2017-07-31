package basel.com.materialtextclocksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.basel.materialtextclock.MaterialTextClock;


public class MainActivity extends AppCompatActivity {

    MaterialTextClock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);

        clock = (MaterialTextClock) findViewById(R.id.clock);
        clock.setColor("#704a29");// set color for am/pm
        clock.setLang("en");//or "ar"
        clock.setSize(0.6f);// <1 or >1, represent am/pm size to clock default text size

    }


}
