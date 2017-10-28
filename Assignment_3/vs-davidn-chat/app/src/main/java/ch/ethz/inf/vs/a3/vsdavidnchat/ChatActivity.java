package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ch.ethz.inf.vs.a3.message.MessageComparator;
import ch.ethz.inf.vs.a3.queue.PriorityQueue;

public class ChatActivity extends AppCompatActivity {

    TextView msgView;
    PriorityQueue<Message> queue;
    //TODO CHANGE CLASS OF MESSAGE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgView = (TextView)findViewById(R.id.messageView);

        MessageComparator comparator = new MessageComparator();
        queue = new PriorityQueue<>(comparator);

        getMessagesIntoQueue();
        setViewContent();
    }



    public void setViewContent(){
        while(!queue.isEmpty()){
            Message current = queue.poll();
            msgView.append(current.toString() + "\n");
        }
    }

    public void getMessagesIntoQueue(){
        //TODO Get messages from server
    }

}
