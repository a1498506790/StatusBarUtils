package com.github.statusbarutils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @data 2018-08-13
 * @desc
 */

public class MyFragment extends Fragment {

    private TextView mTv;
    private BannerLayout mBannerLayout;

    public static MyFragment newInstance(String item) {
        Bundle args = new Bundle();
        args.putString("item", item);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        mBannerLayout = view.findViewById(R.id.bannerLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            List<String> covers = new ArrayList<>();
            covers.add("https://yanxuan.nosdn.127.net/bde1b2692eb5c31ff9f1478f1d7933d2.jpg?quality=95&thumbnail=1920x420&imageView");
            covers.add("https://yanxuan.nosdn.127.net/6d96579938fdc40c9e772c12c70cdac8.jpg?quality=95&thumbnail=1920x420&imageView");
            covers.add("https://yanxuan.nosdn.127.net/54f3ec9768eccf9c3e294e74e6896038.jpg?quality=95&thumbnail=1920x420&imageView");
            covers.add("https://yanxuan.nosdn.127.net/0b2e48b2de1c481c00c0a78f0b3b4333.jpg?quality=95&thumbnail=1920x420&imageView");
            covers.add("https://yanxuan.nosdn.127.net/8fc21828b31a4b0a5e5b9f10c6d1ba8d.jpg?quality=95&thumbnail=1920x420&imageView");
            mBannerLayout.setViewUrls(covers);
        }
    }
}
