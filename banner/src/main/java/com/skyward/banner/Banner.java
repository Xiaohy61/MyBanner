package com.skyward.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skyward.banner.adapter.BannerAdapter;
import com.skyward.banner.utils.BannerScroller;
import com.skyward.banner.utils.BannerTransformer;
import com.skyward.banner.utils.BannerType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author skyward
 * date: 2019/5/28
 * desc:
 */
public class Banner extends FrameLayout {

    private List<?> mImageList = new ArrayList<>();
    private static final String TAG = "skyward";
    private ViewPager mBannerViewPager;
    private boolean autoPlay;
    private static Handler mHandler = new Handler();
    private int delayTime = 3000;
    private LinearLayout llDot;
    private OnItemClickListener mItemClickListener;
    private OnPageChangeListener mOnPageChangeListener;
    private int mBannerLayout = R.layout.banner_layout;
    private ViewPager.PageTransformer mTransformer = new BannerTransformer();
    private BannerType mBannerType = BannerType.NORMAL;
    private int dotUnSelected = R.drawable.oval_banner_indicator_unselected;
    private int dotSelected = R.drawable.oval_banner_indicator_selected;
    private View mView;
    private ImageLoader mImageLoader;
    private CustomImageLoader mCustomImageLoader;
    private int pageMargin = -20;
    private int offscreenPageLimit =0;
    private int indicatorHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
    private int indicatorMargin = 5;
    private int indicatorWidth = LinearLayout.LayoutParams.WRAP_CONTENT;


    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        init();
    }

    private void init() {

        mView = LayoutInflater.from(getContext()).inflate(mBannerLayout, this, true);
        if (mView != null) {
            mBannerViewPager = findViewById(R.id.banner_view_pager);
            llDot = findViewById(R.id.ll_dot);
        }
        initViewpagerScroll();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        mBannerLayout = typedArray.getResourceId(R.styleable.Banner_banner_layout, mBannerLayout);
        dotUnSelected = typedArray.getResourceId(R.styleable.Banner_dot_un_selected, dotUnSelected);
        dotSelected = typedArray.getResourceId(R.styleable.Banner_dot_selected, dotSelected);
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorHeight);
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, indicatorMargin);
        indicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorWidth);
        typedArray.recycle();
    }

    public Banner setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
        return this;
    }

    public Banner setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }

    public Banner setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
        return this;
    }

    public Banner setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
        return this;
    }

    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public Banner setData(final List<?> mImageList) {
        this.mImageList = mImageList;
        return this;
    }


    public Banner setPageTransformer(ViewPager.PageTransformer transformer) {
        this.mTransformer = transformer;
        return this;
    }

    public Banner setBannerType(BannerType type) {
        this.mBannerType = type;
        return this;
    }

    public Banner setDefaultImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        return this;
    }

    public Banner setCustomImageLoader(CustomImageLoader customImageLoader) {
        this.mCustomImageLoader = customImageLoader;
        return this;
    }

    public Banner setNormalBannerOffscreenPageLimit(int offscreenPageLimit){
        this.offscreenPageLimit = offscreenPageLimit;
        return this;
    }



    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            //开始轮播
            if (autoPlay) {
                startAutoPlay();
            }
        } else if (visibility == INVISIBLE || visibility == GONE) {
            //停止轮播
            if (autoPlay) {
                stopAutoPlay();
            }
        }
    }

    public void start() {

        initDot(mImageList);
        BannerAdapter adapter = new BannerAdapter(mImageList, getContext(), mBannerType);
        adapter.setBannerItemClickListener(new BannerAdapter.BannerItemClick() {
            @Override
            public void onBannerItemClickListener(int position) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position, mView);
                }
            }
        });

        adapter.setAdapterImageLoader(new BannerAdapter.AdapterImageLoader() {
            @Override
            public void imageLoader(Object imagePath, ImageView imageView) {
                if (mImageLoader != null) {
                    mImageLoader.imageLoader(getContext(), imagePath, imageView);
                }
            }
        });

        adapter.setAdapterCustomImageLoader(new BannerAdapter.AdapterCustomImageLoader() {
            @Override
            public View customImageLoader(ViewGroup container, Object imagePath) {
                if (mCustomImageLoader != null) {
                    return mCustomImageLoader.customImage(container, getContext(), imagePath);
                }
                return null;
            }
        });

        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position %= mImageList.size();
                updateDotStatus(position);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position, mView);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = mBannerViewPager.getCurrentItem();
                switch (state) {
                    // 空闲状态，没有任何滚动正在进行（表明完成滚动）
                    case ViewPager.SCROLL_STATE_IDLE:
                        setImageCurrentPosition(position);
                        break;
                    // 正在拖动page状态
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        setImageCurrentPosition(position);
                        break;
                    // 手指已离开屏幕，自动完成剩余的动画效果
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    default:
                        break;
                }
            }
        });
        mBannerViewPager.setAdapter(adapter);
        startAutoPlay();
        showBannerType();
    }

    private void showBannerType() {
        if (mBannerType == BannerType.NORMAL) {
            mBannerViewPager.setCurrentItem(0);
            mBannerViewPager.setOffscreenPageLimit(offscreenPageLimit);
        } else if (mBannerType == BannerType.OFFSET) {
            mBannerViewPager.setCurrentItem(1);
            mBannerViewPager.setOffscreenPageLimit(mImageList.size() + 4);
            mBannerViewPager.setPageTransformer(true, mTransformer);
            mBannerViewPager.setPageMargin(pageMargin);
        }else if(mBannerType == BannerType.GUIDE_PAGES){
            mBannerViewPager.setCurrentItem(0);
            mBannerViewPager.setOffscreenPageLimit(mImageList.size());
        }
    }

    /**
     * 无限循环轮播处理
     *
     * @param position 轮播图当前位置
     */
    private void setImageCurrentPosition(int position) {
        if (mBannerType == BannerType.NORMAL) {
            if (position == 0) {
                position = mImageList.size();
                mBannerViewPager.setCurrentItem(position, false);
            } else if (position == (mImageList.size() + 2) - 1) {
                position = 1;
                mBannerViewPager.setCurrentItem(position, false);
            }

        } else if (mBannerType == BannerType.OFFSET) {
            if (position == 1) {
                position = mImageList.size() + 1;
                mBannerViewPager.setCurrentItem(position, false);
            } else if (position == (mImageList.size() + 4) - 2) {
                position = 2;
                mBannerViewPager.setCurrentItem(position, false);
            }
        }
    }

    private void initViewpagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller scroller = new BannerScroller(mBannerViewPager.getContext());
            int scrollTime = 800;
            scroller.setDuration(scrollTime);
            mField.set(mBannerViewPager, scroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private void startAutoPlay() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, delayTime);
    }

    private void stopAutoPlay() {
        mHandler.removeCallbacks(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mImageList.size() > 1 && autoPlay) {
                //这里是以防在fragment中使用，当fragment不可见时viewpager addOnPageChangeListener会失效，从而造成无限轮播失效
                int position = mBannerViewPager.getCurrentItem();
                setImageCurrentPosition(position);
                //+1 开始自动轮播功能
                int nextPosition = mBannerViewPager.getCurrentItem()+1;
                mBannerViewPager.setCurrentItem(nextPosition, true);

                mHandler.postDelayed(mRunnable, delayTime);
            }
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化轮播图指示器
     *
     * @param mImageList 图片集合
     */
    private void initDot(List<?> mImageList) {
        if (llDot == null) {
            return;
        }
        for (int i = 0; i < mImageList.size(); i++) {

            ImageView dotView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            params.leftMargin = indicatorMargin;
            params.rightMargin = indicatorMargin;
            dotView.setLayoutParams(params);

            if (i == 0) {
                dotView.setImageResource(dotSelected);
            } else {
                dotView.setImageResource(dotUnSelected);
            }

            llDot.addView(dotView);

        }

    }

    /**
     * 更新轮播图指示器位置
     *
     * @param pos 当前录播图位置
     */
    private void updateDotStatus(int pos) {
        if (llDot == null) {
            return;
        }
        for (int i = 0; i < llDot.getChildCount(); i++) {
            if (pos == i) {
                ImageView dotView = (ImageView) llDot.getChildAt(i);
                dotView.setImageResource(dotSelected);
            } else {
                ImageView dotView = (ImageView) llDot.getChildAt(i);
                dotView.setImageResource(dotUnSelected);
            }
        }
    }


    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public interface OnItemClickListener {
        /**
         * 轮播图点击回调
         *
         * @param position 当前位置
         * @param view     承载轮播图的父布局
         */
        void onItemClick(int position, View view);
    }

    public interface OnPageChangeListener {
        /**
         * 轮播页面切换回调
         *
         * @param position 当前位置
         * @param view     承载轮播图的父布局
         */
        void onPageSelected(int position, View view);
    }

    public interface ImageLoader {
        /**
         * 图片加载处理
         *
         * @param context   context
         * @param imagePath 图片路径
         * @param imageView imageView
         */
        void imageLoader(Context context, Object imagePath, ImageView imageView);

    }

    public interface CustomImageLoader {
        View customImage(ViewGroup container, Context context, Object imagePath);
    }
}
