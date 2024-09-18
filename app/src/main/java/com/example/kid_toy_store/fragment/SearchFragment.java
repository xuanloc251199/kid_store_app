package com.example.kid_toy_store.fragment;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.adapter.TagAdapter;
import com.example.kid_toy_store.databinding.FragmentSearchBinding;
import com.example.kid_toy_store.helper.RandomTextGenerator;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tags;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    FragmentSearchBinding mBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSearchBinding.inflate(inflater, container, false);

        setButtonOnClickListener(mBinding);

        setupTagRv(mBinding);

        setupProductRv();

        return mBinding.getRoot();
    }

    private void setButtonOnClickListener(FragmentSearchBinding binding) {

    }

    private void setupProductRv() {

        ArrayList<Products> productsList = getFakeData(Products.class, 10);
        ProductAdapter productsAdapter = new ProductAdapter(productsList, getContext());
        RecyclerView recyclerView = mBinding.rvProduct;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutFrozen(true);
        recyclerView.setAdapter(productsAdapter);
    }

    private void setupTagRv(FragmentSearchBinding binding) {

        List<Tags> tagsList = new ArrayList<>();
        tagsList.add(new Tags(RandomTextGenerator.generateRandomText(10 + 1), true));
        tagsList.add(new Tags(RandomTextGenerator.generateRandomText(10 + 1), true));
        tagsList.add(new Tags(RandomTextGenerator.generateRandomText(10 + 1), true));
        tagsList.add(new Tags(RandomTextGenerator.generateRandomText(10 + 1), true));
        tagsList.add(new Tags(RandomTextGenerator.generateRandomText(10 + 1), true));

        // Thiết lập FlexboxLayoutManager cho RecyclerView
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        TagAdapter tagAdapter = new TagAdapter(tagsList);
        binding.rvTag.setLayoutManager(flexboxLayoutManager);
        binding.rvTag.setAdapter(tagAdapter);
    }
}