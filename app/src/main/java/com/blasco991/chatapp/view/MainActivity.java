package com.blasco991.chatapp.view;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.MVC;
import com.blasco991.chatapp.R;
import com.blasco991.chatapp.controller.JobService;
import com.blasco991.chatapp.model.Model;

import static android.app.job.JobInfo.NETWORK_TYPE_ANY;
import static com.blasco991.chatapp.ChatApp.ONESHOT_JOB_TAG;

public class MainActivity extends AppCompatActivity implements com.blasco991.chatapp.view.View {

    private static final String TAG = MainActivity.class.getName();

    private MVC mvc;
    private ArrayAdapter stringArrayAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText username;
    private EditText message;
    private ListView messages;

    @Override
    @UiThread
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true; // show the menu
    }

    @Override
    @UiThread
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_refresh) {
            refreshCommand();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void refreshCommand() {
        int scheduleResult = ((JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE)).schedule(
                new JobInfo.Builder(ONESHOT_JOB_TAG, new ComponentName(getPackageName(), JobService.class.getName()))
                        .setMinimumLatency(0).setRequiredNetworkType(NETWORK_TYPE_ANY).build());
        Log.d(TAG, "OneShot job schedule result: " + scheduleResult);
    }

    @Override
    @UiThread
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvc = ((ChatApp) getApplication()).getMVC();

        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        username = (EditText) findViewById(R.id.username);
        message = (EditText) findViewById(R.id.message);
        messages = (ListView) findViewById(R.id.messages);
        final AppCompatImageButton sendButton = (AppCompatImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(e -> {
            mvc.controller.sendMessage(username.getText().toString(), message.getText().toString());
            message.getText().clear();
        });
        stringArrayAdapter = new ArrayAdapter<Model.Message>(this, R.layout.list_item, R.id.body, mvc.model.getMessages()) {

            @NonNull
            @Override
            public android.view.View getView(int position, View convertView, @NonNull ViewGroup parent) {

                MessageHolder holder;

                if (convertView == null) {
                    convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.list_item, parent, false);
                    holder = new MessageHolder();
                    holder.author = (TextView) convertView.findViewById(R.id.author);
                    holder.body = (TextView) convertView.findViewById(R.id.body);

                    convertView.setTag(holder);
                } else {
                    holder = (MessageHolder) convertView.getTag();
                }

                Model.Message message = getItem(position);
                holder.author.setText(message.author);
                holder.body.setText(message.body);
                return convertView;
            }
        };
        messages.setAdapter(stringArrayAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::refreshCommand);
    }

    public static class MessageHolder {
        //int position;
        TextView author;
        TextView body;
    }

    @Override
    @UiThread
    protected void onStart() {
        super.onStart();
        mvc.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvc.controller.receiveMessages();
    }

    @Override
    @UiThread
    protected void onStop() {
        mvc.unregister(this); // this allows dead activities to be garbage collected
        super.onStop();
    }

    @Override
    @UiThread
    public void onModelChanged() {
        stringArrayAdapter.notifyDataSetChanged();
        messages.setSelection(message.length());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Context getContext() {
        return this;
    }

}
