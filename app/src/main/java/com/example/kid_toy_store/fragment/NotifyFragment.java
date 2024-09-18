package com.example.kid_toy_store.fragment;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kid_toy_store.adapter.NotifyAdapter;
import com.example.kid_toy_store.databinding.FragmentNotifyBinding;
import com.example.kid_toy_store.model.Notifications;

import java.util.ArrayList;

public class NotifyFragment extends Fragment {

    FragmentNotifyBinding mBinding;
    View mView;

    NotifyAdapter notifyAdapter;
    ArrayList<Notifications> notifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentNotifyBinding.inflate(inflater);
        mView = mBinding.getRoot();

        setupNotifyRv(mBinding);

        setOnClickListener(mBinding);

        return mView;
    }

    private void setOnClickListener(FragmentNotifyBinding binding) {

    }

    private void setupNotifyRv(FragmentNotifyBinding binding) {

        notifications = getFakeData(Notifications.class, 13);
        notifyAdapter = new NotifyAdapter(notifications, getContext());
        RecyclerView recyclerView = binding.rvNotify;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(notifyAdapter);
    }
}