package dzehavi.com.rssreader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass, that shows author's name, current date and time, and title of selected feed.
 */
public class InfoFragment extends Fragment {

    // triggers a clock update every second
    private Timer timer;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// listen for new RSS feed selection, the title of the feed will be passed from MainActivity
        ((MainActivity)getActivity()).setFeedTitleListener(
                new MainActivity.OnFeedTitleListener() {
            @Override
            public void onFeedTitle(String feedDescription) {
				// update feed title text view
                TextView viewById = getView().findViewById(R.id.selectedFeedTextView);
                if(viewById != null)
                    viewById.setText(feedDescription);

            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // refresh every second
        startTimer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // cleanup
        stopTimer();
    }

    private void startTimer() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDateAndTime();
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
    }

	/**
	* format current date and time and show inside date&time text view
	*/
    private void updateDateAndTime() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                TextView clockView = getView().findViewById(R.id.timeTextView);
                if (clockView != null) {
                    clockView.setText(formattedDate);
                }
            }
        });

    }
}
