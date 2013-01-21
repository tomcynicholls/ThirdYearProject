package com.thirdyearproject.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
	
	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        
     // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(AppActivity.EXTRA_MESSAGE);

        // Create the text view
        ((TextView)findViewById(R.id.show_message)).setText(message);

        // Set the text view as the activity layout
       // setContentView(textView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    	Intent intent = new Intent(this, ViewXmlWriter.class);
    	String message = ((TextView)findViewById(R.id.show_message)).getText().toString();
    	intent.putExtra(AppActivity.EXTRA_MESSAGE, message);
    	startActivity(intent);
        
    }
    
}
