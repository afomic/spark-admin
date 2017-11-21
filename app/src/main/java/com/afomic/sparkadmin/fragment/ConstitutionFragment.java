package com.afomic.sparkadmin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afomic.sparkadmin.CsvParserIntentService;
import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.adapter.ConstitutionAdapter;
import com.afomic.sparkadmin.model.Constitution;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.afomic.sparkadmin.data.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by afomic on 11/14/17.
 *
 */

public class ConstitutionFragment extends Fragment {
    @BindView(R.id.rv_constitution_list)
    RecyclerView constitutionListView;

    @BindView(R.id.empty_view_layout)
    LinearLayout emptyViewLayout;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    DatabaseReference constitutionRef;
    ConstitutionAdapter mAdapter;

    ArrayList<Constitution> mConstitutions;

    CourseBroadcastReceiver mReceiver;

    private static final int REQUEST_CODE_GET_CSV=101;

    public static ConstitutionFragment newInstance(){
        return new ConstitutionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter(CsvParserIntentService.ACTION_GET_CONSTITUTION);
        mReceiver=new CourseBroadcastReceiver();
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_constitution_layout,container,false);
        ButterKnife.bind(this,v);

        mConstitutions=new ArrayList<>();

        constitutionRef= FirebaseDatabase.getInstance().getReference("constitution/sample");

        constitutionListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter=new ConstitutionAdapter(getActivity(),mConstitutions);

        constitutionListView.setAdapter(mAdapter);

        constitutionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count =0;
                mConstitutions.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Constitution constitution=snapshot.getValue(Constitution.class);
                    mConstitutions.add(constitution);
                    count++;
                }
                if(count>0){
                    hideErrorMessage();
                    mAdapter.notifyDataSetChanged();

                }else {
                    showErrorMessage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }
    @OnClick(R.id.btn_upload_constitution)
    public void selectCourseCsv(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE_GET_CSV);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_GET_CSV&&resultCode==RESULT_OK){
            Uri fileUri=data.getData();
            if(fileUri!=null){
                CsvParserIntentService.startGetConstitution(getContext(),fileUri.getPath());
                showProgressBar();
                hideErrorMessage();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class CourseBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String error=intent.getStringExtra(Constant.EXTRA_ERROR);
            hideProgressBar();
            if(error!=null){
                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                showErrorMessage();
            }else{
                ArrayList<Constitution> constitutionList=
                        intent.getParcelableArrayListExtra(Constant.EXTRA_CONSTITUTION_LIST);
                if(constitutionList!=null){

                    for (Constitution constitution:constitutionList){
                        mConstitutions.add(constitution);
                        //TODO upload list to firebase
                    }
                    mAdapter.notifyDataSetChanged();
                    hideErrorMessage();

                }

            }


        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
    }
    private void showErrorMessage(){
        emptyViewLayout.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage(){
        emptyViewLayout.setVisibility(View.GONE);
    }

}
