package com.github.statusbarutils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import crossoverone.statuslib.StatusUtil;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    String[] items = {"推荐", "手机", "家具123", "家具2", "家具4", "家具1", "家具", "家具1", "家具", "家具", "家具"};
    List<Fragment> mFragments;
    LinearLayout mLlt;
    TextView mTvAllTab;
    ImageView mIvExpandable;
    boolean mIsExpandable = false;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏 ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 第二个参数是状态栏色值。
        // 第三个参数是兼容5.0到6.0之间的状态栏颜色字体只能是白色，如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体（可以找ui给一个合适颜色值）。
        // 如果您的项目是6.0以上机型或者某些界面不适用沉浸, 推荐使用两个参数的setUseStatusBarColor。
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#33000000"));
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
        initTab();
        findViewById(R.id.rl_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MsgActivity.class));
            }
        });
    }

    private void initTab() {
        mFragments = new ArrayList<>();
        for (String item : items) {
            mFragments.add(MyFragment.newInstance(item));
        }
        mViewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        reflex(mTabLayout);
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTvAllTab = findViewById(R.id.tv_all_tab);
        mIvExpandable = findViewById(R.id.iv_expandable);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLlt = findViewById(R.id.llt);
        findViewById(R.id.llt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsExpandable = !mIsExpandable;
                startAnim();
            }
        });
        mTvAllTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        mLlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsExpandable) {
                    mIsExpandable = !mIsExpandable;
                    startAnim();
                }
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(new MyAdapter());
    }

    private void startAnim(){
        mLlt.setVisibility(View.VISIBLE);
        ObjectAnimator animator;
        ValueAnimator valueAnimator;
        int i = items.length % 5;
        int i1 = items.length / 5;
        Log.e("test", "i : " + i);
        int recyclerViewH = 0;
        if (i > 0) {
            recyclerViewH = (int) ((int) ((i1+ 1) * (dip2px(25) + dip2px(10))) + dip2px(10));
        }else{
            recyclerViewH = (int) (i1 * (dip2px(25) + dip2px(10)) + dip2px(10));
        }
        if (mIsExpandable) {
            animator = ObjectAnimator.ofFloat(mIvExpandable, "rotation", 0, 180);
            valueAnimator = ValueAnimator.ofFloat(0, recyclerViewH);
            mTvAllTab.setVisibility(View.VISIBLE);
        }else{
            animator = ObjectAnimator.ofFloat(mIvExpandable, "rotation", 180, 0);
            valueAnimator = ValueAnimator.ofFloat(recyclerViewH, 0);
            mTvAllTab.setVisibility(View.GONE);
        }
        animator.setDuration(200);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mIsExpandable) {
                    mLlt.setVisibility(View.VISIBLE);
                }else{
                    mLlt.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
                layoutParams.height = (int) animatedValue;
                mRecyclerView.setLayoutParams(layoutParams);
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(items[position]);
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == position) {
                holder.tv.setBackgroundResource(R.drawable.bg_line2);
                holder.tv.setTextColor(Color.parseColor("#cf8309"));
            }else{
                holder.tv.setBackgroundResource(R.drawable.bg_line);
                holder.tv.setTextColor(Color.parseColor("#8f8d8d"));
            }
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(position);
                    mIsExpandable = !mIsExpandable;
                    startAnim();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.text);
            }
        }
    }

    class MyViewPageAdapter extends FragmentPagerAdapter{

        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items[position];
        }
    }

    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = (int) dip2px(10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public float dip2px(float dipValue){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
