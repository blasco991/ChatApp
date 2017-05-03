package com.blasco991.chatapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.ChatApp.MessageHolder;
import com.blasco991.chatapp.MVC;
import com.blasco991.chatapp.R;

public class MainActivity extends AppCompatActivity implements com.blasco991.chatapp.view.View {

    private static final String TAG = MainActivity.class.getName();

    private MVC mvc;
    private ArrayAdapter stringArrayAdapter;
    private ImageButton sendButton;
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
            mvc.controller.receiveMessages();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    @UiThread
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvc = ((ChatApp) getApplication()).getMVC();

        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        message = (EditText) findViewById(R.id.message);
        messages = (ListView) findViewById(R.id.messages);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(e -> {
            mvc.controller.sendMessage(username.getText().toString(), message.getText().toString());
        });
        stringArrayAdapter = new ArrayAdapter<ChatApp.Message>(this, R.layout.list_item, R.id.body, mvc.model.getMessages()) {

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

                ChatApp.Message message = getItem(position);

                holder.author.setText(message.author);
                holder.body.setText(message.body);
                return convertView;
            }
        };
        messages.setAdapter(stringArrayAdapter);

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
    }

    @Override
    public Context getContext() {
        return getContext();
    }

}
