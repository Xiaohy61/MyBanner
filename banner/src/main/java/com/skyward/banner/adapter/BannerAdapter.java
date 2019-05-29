package com.skyward.banner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skyward.banner.utils.BannerType;

import java.util.List;

/**
 * @author skyward
 * date: 2019/5/28
 * desc:
 */
public class BannerAdapter extends PagerAdapter {

    private List<?> mImageList;
    private Context mContext;
    private BannerType mBannerType;
    private BannerItemClick mBannerItemClick;
    private AdapterImageLoader mAdapterImageLoader;
    private AdapterCustomImageLoader mAdapterCustomImageLoader;


    public BannerAdapter(List<?> mImageList, Context context, BannerType bannerType) {
        this.mImageList = mImageList;
        this.mContext = context.getApplicationContext();
        this.mBannerType = bannerType;
    }

    public void setBannerItemClickListener(BannerItemClick clickListener) {
        this.mBannerItemClick = clickListener;
    }

    public void setAdapterImageLoader(AdapterImageLoader imageLoader) {
        this.mAdapterImageLoader = imageLoader;
    }

    public void setAdapterCustomImageLoader(AdapterCustomImageLoader customImageLoader) {
        this.mAdapterCustomImageLoader = customImageLoader;
    }

    @Override
    public int getCount() {

        if (mBannerType == BannerType.NORMAL) {
            return mImageList.size() + 2;
        } else if (mBannerType == BannerType.OFFSET) {
            return mImageList.size() + 4;
        } else {
            return mImageList.size();
        }
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        position %= mImageList.size();


        View view = mAdapterCustomImageLoader.customImageLoader(container,mImageList.get(position));

        if (view != null) {
            itemClick(view, position);
            container.addView(view);
            return view;
        } else {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            if (mAdapterImageLoader != null) {
                mAdapterImageLoader.imageLoader(mImageList.get(position), imageView);
            }

            itemClick(imageView, position);
            container.addView(imageView);
            return imageView;
        }


    }

    private void itemClick(View layout, final int position) {
        if (layout == null) {
            return;
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerItemClick != null) {
                    mBannerItemClick.onBannerItemClickListener(position);
                }
            }
        });
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface BannerItemClick {
        /**
         * 图片点击回调
         * @param position 当前图片位置
         */
        void onBannerItemClickListener(int position);
    }

    public interface AdapterImageLoader {
        /**
         * 图片显示处理
         *
         * @param imagePath 图片路径
         * @param imageView imageView
         */
        void imageLoader(Object imagePath, ImageView imageView);
    }

    public interface AdapterCustomImageLoader {
        /**
         * 自定义图片布局
         * @param container  container容器
         * @param imagePath 图片路径
         * @return view
         */
        View customImageLoader(ViewGroup container,Object imagePath);
    }
}
