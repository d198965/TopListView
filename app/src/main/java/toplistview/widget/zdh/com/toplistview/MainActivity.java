package toplistview.widget.zdh.com.toplistview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setClickListener();
    }

    private void setClickListener(){
        findViewById(R.id.tab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("widget://tabtopview"));
                startActivity(tabIntent);
            }
        });

        findViewById(R.id.sticky_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("widget://stickytopview"));
                startActivity(tabIntent);
            }
        });
    }

}
