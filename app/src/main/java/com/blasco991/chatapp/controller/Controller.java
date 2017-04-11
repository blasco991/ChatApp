package com.blasco991.chatapp.controller;

import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.blasco991.chatapp.ChatApp;
import com.blasco991.chatapp.ChatApp.Message;
import com.blasco991.chatapp.MVC;
import com.blasco991.chatapp.view.View;

import net.jcip.annotations.ThreadSafe;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by blasco991 on 11/04/17.
 */
@ThreadSafe
public class Controller {
    private MVC mvc;

    public void setMVC(MVC mvc) {
        this.mvc = mvc;
    }

    @UiThread
    public void sendMessage(String username, String message) {
        new Sender().execute(username, message);
    }

    @UiThread
    public void receiveMessages() {
        new Receiver().execute();
    }

    private class Sender extends AsyncTask<String, Void, Boolean> {

        @Override
        @WorkerThread
        protected Boolean doInBackground(String... args) {
            boolean status = false;
            try {
                URL url = new URL("http://dev.blasco991.com/AddMessage?author=" + args[0] +
                        "&message=" + args[1]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                if (urlConnection.getResponseCode() == 200)
                    status = true;

                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        @UiThread
        protected void onPostExecute(Boolean success) {
            if (success) {
                mvc.controller.receiveMessages();
                mvc.forEachView(View::onModelChanged);
            } else mvc.forEachView(v -> v.makeToast("Failed"));
        }

    }

    private class Receiver extends AsyncTask<Void, Void, List<Message>> {

        @Override
        @WorkerThread
        protected List<Message> doInBackground(Void... voids) {
            List<Message> messagesList = new LinkedList<>();
            try {
                URL url = new URL("http://dev.blasco991.com/ListMessages");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                if (urlConnection.getResponseCode() == 200)

                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(urlConnection.getInputStream());
                        NodeList messages = doc.getElementsByTagName("message");
                        for (int i = 0; i < messages.getLength(); i++) {
                            Node node = messages.item(i);
                            Message messageData = new Message();
                            messageData.position = Integer.parseInt(node.getFirstChild().getTextContent());
                            messageData.author = node.getChildNodes().item(1).getTextContent();
                            messageData.body = node.getLastChild().getTextContent();
                            messagesList.add(messageData);
                        }

                    } catch (IOException | ParserConfigurationException | SAXException e) {
                        e.printStackTrace();
                    }

                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return messagesList;
        }

        @Override
        @UiThread
        protected void onPostExecute(List<Message> messages) {
            mvc.model.setMessages(messages);
            mvc.forEachView(View::onModelChanged);
        }

    }
}
