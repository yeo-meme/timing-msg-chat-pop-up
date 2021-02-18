package com.nalazoocare.messagechatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.internal.$Gson$Preconditions;
import com.nalazoocare.messagechatt.databinding.ActivityMainBinding;
import com.nalazoocare.messagechatt.databinding.ChatItemEmptyBinding;
import com.nalazoocare.messagechatt.databinding.ChatItemImageBinding;
import com.nalazoocare.messagechatt.databinding.ChatItemListBinding;
import com.nalazoocare.messagechatt.databinding.ChatItemTalkUserBinding;
import com.nalazoocare.messagechatt.databinding.ChatItemText2Binding;
import com.nalazoocare.messagechatt.databinding.ChatItemTextBinding;
import com.nalazoocare.messagechatt.databinding.SubBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ChatItemTalkUserBinding talkUserBinding;
    ChatItemTextBinding chatItemTextBinding;
    ChatItemEmptyBinding emptyBinding;
    FirebaseFirestore mFireStore;
    ChatItemImageBinding imageBinding;
    ChatItemText2Binding text2Binding;

    ChatItemListBinding listBinding;

    private ListAdapter listAdapter;

    /**
     * 리스트뷰 어댑터
     */
    private Adapter adapter;
    private Handler mHandler;
    private int increaseCnt = 0;

    /**
     * DB 저장된 데이터 get 변수
     */
    private List<Object> adminTalkList;
    private List<Object> userTalkList;

    private List<String> userTalk;

    //파이어스토어 스토리지
    private FirebaseStorage mFireStorage;
    private StorageReference storageRef;
    private OnEndCallBack onEndCallBack;
    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFireStore = FirebaseFirestore.getInstance();
        mFireStorage = FirebaseStorage.getInstance();
        adapter = new Adapter(this);
        mHandler = new Handler();

        /**
         * initData
         */
        initGetData();
        layoutInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        onEndCallBack = new OnEndCallBack() {
            @Override
            public void end() {
                Log.d("user add View","user add View");
                increaseCnt = 0;
                mHandler.postDelayed(() -> oneTalkView(), 1000);
            }
        };
    }

    /**
     * 초기 데이터 get
     */
    private void initGetData() {
        adminTalk();
        userTalk();
    }

    private void userTalk() {
        final DocumentReference userRef = mFireStore.collection("message").document("userTalk1");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                getUserMessage(task.getResult().getData());
            }
        });
    }

    private void getUserMessage(Map<String, Object> data) {
        userTalkList = (List<Object>) data.get("answer");
        for (int i =0; i< userTalkList.size(); i++) {
            Log.d("userLIst","userLIst:"+ userTalkList.get(i));
        }
    }

    private void adminTalk() {
        final DocumentReference messageRef = mFireStore.collection("message").document("adminTalk1");
        messageRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                getAdminMessage(task.getResult().getData());
            }
        });
    }

    private void showAddView(String msg) {
        Log.d("increaseCnt", "increaseCnt:" + increaseCnt);
        chatItemTextBinding = ChatItemTextBinding.inflate(layoutInflater, binding.viewContainer, false);
        emptyBinding = ChatItemEmptyBinding.inflate(layoutInflater, binding.viewContainer, false);
        binding.viewContainer.addView(chatItemTextBinding.getRoot());
        binding.viewContainer.addView(emptyBinding.getRoot());
        chatItemTextBinding.tvText.setText(msg);
        increaseCnt++;
        addViewController();
    }

    /**
     * 파이어스토리지 저장된 이미지 겟
     */
    private void getImage() {
        storageRef = mFireStorage.getReferenceFromUrl("gs://message-42fe3.appspot.com").child("message/test_img.jpeg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                makeProfile(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failed", "failed :" + e);
            }
        });
    }

    private void addData() {
//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CollectionReference collectionReference = mFireStore.collection("message");
//                msgList = new ArrayList<>();
//                Map<String, List<String>> msgMap = new HashMap<>();
//                msgList.add("안녕 난 현신이라고 해");
//                msgList.add("만나서 반가워요:)");
//                msgList.add("날라주 케어에 대해 말해주고 싶은게 정말 많은데");
//                msgList.add("더 듣고 싶은 이야기를 선택해주세요:)");
//                msgMap.put("question", msgList);
//                collectionReference.add(msgMap);
//            }
//        });

    }

    private void makeProfile(Uri uri) {
        imageBinding = ChatItemImageBinding.inflate(layoutInflater,binding.viewContainer,false);
        binding.viewContainer.addView(imageBinding.getRoot());
        imageBinding.ivImg.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(uri)
                .into(imageBinding.ivImg);
        scrollDown();

        mHandler.postDelayed(() -> adminTalk2() , 1000 );
    }

    private void adminTalk2() {
        final DocumentReference adminTalk2Ref = mFireStore.collection("message").document("adminTalk2");
        adminTalk2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        untieData(task.getResult().getData());
            }
        });
    }

    private void untieData(Map<String, Object> data) {
        adminTalkList.clear();
        adminTalkList = (List<Object>) data.get("question");
        mHandler.postDelayed(() -> adminTalk2AddView(),1000 );
        increaseCnt = 0;
    }

    private void scrollDown() {
        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void adminTalk2AddView() {
        if (adminTalkList.size() > increaseCnt) {
            String msg = (String) adminTalkList.get(increaseCnt);
            text2Binding = ChatItemText2Binding.inflate(layoutInflater,binding.viewContainer, false);
            text2Binding.tvText2.setText(msg);
            increaseCnt++;
            binding.viewContainer.addView(text2Binding.getRoot());
            mHandler.postDelayed(()-> adminTalk2AddView(), 1000);
            scrollDown();
        } else {
            onEndCallBack.end();

        }
    }

    /**
     * 맵 vlaue 오브젝트 리스트로 변환
     *
     * @param data 데이터 map
     */
    private void getAdminMessage(Map<String, Object> data) {
        adminTalkList = (List<Object>) data.get("question");
        for (int i = 0; i < adminTalkList.size(); i++) {
            Log.d("meme", "meme hh:" + adminTalkList.get(i));
        }
        mHandler.postDelayed(() -> delay(), 1000);
//        addViewController();
    }

    private void addViewController() {
        if (adminTalkList.size() > increaseCnt) {
            String msg = (String) adminTalkList.get(increaseCnt);
            mHandler.postDelayed(() -> showAddView(msg), 1000);
            Log.d("increase","increase :" + increaseCnt);
        }
        else {
            onEndCallBack.end();
        }
    }

    private void showImage() {
        Log.d("halo", "halo");
        ChatItemImageBinding imageBinding;
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        imageBinding = ChatItemImageBinding.inflate(inflater, binding.viewContainer, false);
        imageBinding.ivImg.setImageResource(R.drawable.test_img);
        binding.viewContainer.addView(imageBinding.getRoot());
    }

    /**
     * 사용자 딜레이
     */
    private void delay() {
        increaseCnt = 0;
        mHandler.postDelayed(() -> addViewController(), 1000);
    }

    private void oneTalkView() {
//        talkUserBinding = ChatItemTalkUserBinding.inflate(layoutInflater,binding.viewContainer,false);
        emptyBinding = ChatItemEmptyBinding.inflate(layoutInflater, binding.viewContainer, false);
        listBinding = ChatItemListBinding.inflate(layoutInflater,binding.viewContainer,false);
//        if (userTalkList != null) {
//            for (int i=0; i< userTalkList.size(); i++) {
//                if (i == 0) {
//                    talkUserBinding.tvText1.setText(userTalkList.get(i)+"");
//                }
//                if (i == 1) {
//                    talkUserBinding.tvText2.setText(userTalkList.get(i)+"");
//                }
//            }
//            binding.viewContainer.addView(talkUserBinding.getRoot());
//            binding.viewContainer.addView(emptyBinding.getRoot());
//            scrollDown();
//        }
        if (userTalkList != null) {
            Log.d("talk","talk:" + userTalkList);
//            listAdapter.addItem(String.valueOf(userTalkList));
            listAdapter = new ListAdapter(this);

            for (Object data : userTalkList) {
                String msg = (String) data;
                listAdapter.addItem(msg);
            }
            listBinding.listView.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
            binding.viewContainer.addView(listBinding.getRoot());
            binding.viewContainer.addView(emptyBinding.getRoot());
        }
//        initTalkUserListener();
    }

    private void initTalkUserListener() {
//        talkUserBinding.tvText1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                imageBinding = ChatItemImageBinding.inflate(layoutInflater,binding.viewContainer,false);
////                binding.viewContainer.addView(imageBinding.getRoot());
//                getImage();
//                talkUserBinding.tvText2.setVisibility(View.GONE);
//            }
//        });
//        talkUserBinding.tvText2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"톡2",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void userOneTalkView() {
        if (userTalkList.size() > increaseCnt) {
            String msg = String.valueOf(userTalkList.get(increaseCnt));
            Log.d("talk user" , "talk user :" + msg +",increase :" + increaseCnt);
            talkUserBinding = ChatItemTalkUserBinding.inflate(layoutInflater,binding.viewContainer,false);
            emptyBinding = ChatItemEmptyBinding.inflate(layoutInflater, binding.viewContainer, false);
            if (increaseCnt == 0) {
                talkUserBinding.tvText1.setText(""+userTalkList.get(increaseCnt));
            } else if (increaseCnt == 1) {
                talkUserBinding.tvText2.setText(""+userTalkList.get(increaseCnt));
            }
            increaseCnt++;
            userOneTalkView();
        } else {
            binding.viewContainer.addView(talkUserBinding.getRoot());
        }
    }

    /**
     * 사용자 선택 챗
     */
    private void userAddView() {
        if (userTalkList.size() > increaseCnt) {
            String msg = String.valueOf(userTalkList.get(increaseCnt));
            Log.d("increase"," increase user :" + msg);
            talkUserBinding = ChatItemTalkUserBinding.inflate(layoutInflater, binding.viewContainer, false);
            emptyBinding = ChatItemEmptyBinding.inflate(layoutInflater, binding.viewContainer, false);
            binding.viewContainer.addView(talkUserBinding.getRoot());
            binding.viewContainer.addView(emptyBinding.getRoot());
            increaseCnt++;
            delay();
        } else {
            getImage();
        }
    }
}
