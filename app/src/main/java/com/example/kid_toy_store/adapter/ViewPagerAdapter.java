package com.example.kid_toy_store.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    // Danh sách các fragment
    private final List<Fragment> fragmentList;

    // Constructor nhận FragmentActivity và danh sách fragment
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
        super(fragmentActivity);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Trả về fragment dựa trên vị trí
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng fragment trong danh sách
        return fragmentList.size();
    }
}
