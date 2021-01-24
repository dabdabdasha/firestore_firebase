package com.example.dashaaaaaaaaa;  // поменять название пакета на свой

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //поле ввода нового сообщения
    private EditText messageContent;
    private RecyclerView messagesRecyclerView;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();


        db.collection("Messages").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                //измененный список всех документов в коллекции

                    List<DocumentSnapshot> documents = value.getDocuments();


                    //пройтись по
                    for (DocumentSnapshot doc : documents) {
                        //получаем данные из документа
                        String str = doc.getString("text");
                        messageContent.setText(str) ;
                        String str1 = doc.getString("date");
                        messageList.add(new Message(str,str1));

                    }
                messageAdapter.notifyDataSetChanged();
                }
                hfasdjkfhadshfkjhsdahfkjadshkfhkadsjf
        });
    }


    public void sendMessage(View view) {
        Map<String, Object> data = new HashMap<>();
        Message mess = new Message(messageContent.getText().toString());
        data.put("text",messageContent.getText().toString());
        data.put("date",mess.getDate());
        String name;
        messageContent.setText(" ");
        //messageList.add(mess);

        db.collection("Journal").document(name = mess.getDate()+" "+mess.getContent()).set(data);
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //привязка по id
        messagesRecyclerView = findViewById(R.id.messageList);
        messageContent = findViewById(R.id.editTextMessage);

        //создание адаптера и подключение его к recycleView
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //добавление itemTouchHelper для удаления элемента списка свайпом вправо
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Message m = messageList.get(viewHolder.getAdapterPosition());
                String name = m.getDate()+" "+m.getContent();

                db.collection("Messages").document(name).delete();
                messageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(messagesRecyclerView);
    }


}