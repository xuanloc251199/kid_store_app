package com.example.kid_toy_store.view.fragment;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.adapter.TagAdapter;
import com.example.kid_toy_store.adapter.TicketAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.FragmentSearchBinding;
import com.example.kid_toy_store.helper.RandomTextGenerator;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tags;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.response.SearchResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    FragmentSearchBinding mBinding;

    private EditText edtSearchBox;
    private RecyclerView rvProducts, rvTickets;
    private ProductAdapter productAdapter;
    private TicketAdapter ticketAdapter;

    private ApiService apiService;

    private Handler handler = new Handler(Looper.getMainLooper()); // Để xử lý debounce
    private Runnable searchRunnable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSearchBinding.inflate(inflater, container, false);

        setButtonOnClickListener(mBinding);

//        setupTagRv(mBinding);

        // Thiết lập RecyclerView cho sản phẩm và vé
        setupRv();

        // Khởi tạo API Service
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Thiết lập tìm kiếm
        setupSearchBox();

        return mBinding.getRoot();
    }

    private void setButtonOnClickListener(FragmentSearchBinding binding) {

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupRv() {

        // Setup RecyclerView for Products
        rvProducts = mBinding.rvProduct;
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(new ArrayList<>(), getContext());
        rvProducts.setAdapter(productAdapter);
        rvProducts.setNestedScrollingEnabled(false);

        // Setup RecyclerView for Tickets
        rvTickets = mBinding.rvTicket;
        rvTickets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ticketAdapter = new TicketAdapter(new ArrayList<>(), getContext());
        rvTickets.setAdapter(ticketAdapter);
        rvTickets.setNestedScrollingEnabled(false);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSearchBox() {
        edtSearchBox = mBinding.edtSearchBox;

        // Đặt drawableEnd là null khi chưa có văn bản
        edtSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);

        // Lắng nghe sự kiện khi người dùng xóa văn bản trong ô tìm kiếm
        edtSearchBox.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = edtSearchBox.getCompoundDrawablesRelative()[2];
                if (drawableEnd != null && event.getRawX() >= (edtSearchBox.getRight() - drawableEnd.getBounds().width())) {
                    edtSearchBox.setText(""); // Xóa văn bản
                    return true;
                }
            }
            return false;
        });

        // Xử lý khi người dùng nhập văn bản vào ô tìm kiếm
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // Hiển thị drawableEnd (ic_clear) khi có văn bản
                    edtSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_clear, 0);
                } else {
                    // Ẩn drawableEnd nếu không có văn bản
                    edtSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
                }

                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable); // Hủy bỏ tìm kiếm cũ (debounce)
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    // Đợi 300ms trước khi gọi API tìm kiếm (debounce)
                    searchRunnable = () -> search(query);
                    handler.postDelayed(searchRunnable, 300);
                } else {
                    // Nếu không có từ khóa tìm kiếm, xóa kết quả
                    productAdapter.setProducts(null);
                    ticketAdapter.setTickets(null);
                }
            }
        });

        // Sử dụng OnFocusChangeListener để hiển thị hoặc ẩn drawableEnd khi EditText có hoặc mất focus
        edtSearchBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && edtSearchBox.getText().length() > 0) {
                // Hiển thị drawableEnd khi EditText có focus và có văn bản
                edtSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_clear, 0);
            } else {
                // Ẩn drawableEnd khi EditText mất focus hoặc không có văn bản
                edtSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
            }
        });

        // Xử lý sự kiện nhấn vào drawableEnd (xóa văn bản)
        edtSearchBox.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = edtSearchBox.getCompoundDrawables()[2];
                if (drawableEnd != null && event.getRawX() >= (edtSearchBox.getRight() - drawableEnd.getBounds().width())) {
                    edtSearchBox.setText(""); // Xóa văn bản khi nhấn vào drawableEnd
                    return true;
                }
            }
            return false;
        });
    }

    // Hàm tìm kiếm
    private void search(String query) {
        Log.d("SearchQuery", "Từ khóa tìm kiếm: " + query);
        apiService.search(query).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchResponse searchResponse = response.body();
                    List<Products> products = searchResponse.getProducts();
                    List<Tickets> tickets = searchResponse.getTickets();

                    displaySearchResults(products, tickets);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                    mBinding.tvResultCount.setText("Tìm thấy 0 kết quả");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("SearchError", t.getMessage());
            }
        });
    }

    // Hiển thị kết quả tìm kiếm cho cả sản phẩm và vé
    @SuppressLint("SetTextI18n")
    private void displaySearchResults(List<Products> products, List<Tickets> tickets) {
        int productCount = (products != null) ? products.size() : 0;
        int ticketCount = (tickets != null) ? tickets.size() : 0;
        int totalResults = productCount + ticketCount;

        if (totalResults > 0) {
            // Hiển thị số lượng kết quả khi có kết quả tìm kiếm
            mBinding.tvResultCount.setText("Tìm thấy " + totalResults + " kết quả");
            mBinding.tvResultCount.setVisibility(View.VISIBLE);
            mBinding.scrSearchResult.setVisibility(View.VISIBLE);// Hiển thị TextView
        } else {
            // Ẩn TextView nếu không có kết quả
            mBinding.tvResultCount.setVisibility(View.GONE);
        }

        if (products != null && !products.isEmpty()) {
            productAdapter.setProducts(products);
        } else {
            productAdapter.setProducts(null);
        }

        if (tickets != null && !tickets.isEmpty()) {
            ticketAdapter.setTickets(tickets);
        } else {
            ticketAdapter.setTickets(null);
        }
    }

}