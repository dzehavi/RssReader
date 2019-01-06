package dzehavi.com.rssreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * A view that displays the description of the selected feed in RssFragment
 */
public class RssDescriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_description);
        // the description is passed in the launching Intent
		Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        ((TextView) findViewById(R.id.description_text_view)).setText(description);
    }
}
