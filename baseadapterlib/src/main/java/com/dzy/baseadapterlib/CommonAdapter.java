package com.dzy.baseadapterlib;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/5/29 0029.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseHolder>
{

    List<T> mDatas;
    int mLayoutId;

    int mLoadMoreLayoutId;
    int mErrorLayoutId;
    int mEmptyLayoutId;


    boolean isEmpty = false;
    boolean enableLoadMore = false;
    boolean isError = false;



    public static final int NORMALTYPE = 1;
    public static final int EMPTYTYPE = 2;
    public static final int ERRPRTYPE = 3;
    public static final int LOADMORETYPE = 4;


    private LoadMoreListener mLoadMoreListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    public CommonAdapter(List<T> datas, @LayoutRes int id)
    {
        mDatas = datas;
        mLayoutId = id;
    }

    public abstract void bindView (ContentHolder holder,int position,T item);


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        if (viewType == NORMALTYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
            ContentHolder holder = new ContentHolder(v);
            holder.setOnItemClickListener(mOnItemClickListener);
            Log.d("tag","item height "+v.getHeight());
            return holder;
        }
        if (viewType==EMPTYTYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(mEmptyLayoutId, parent, false);
            return new BaseHolder(v);
        }
        if (viewType==ERRPRTYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(mErrorLayoutId, parent, false);
            return new BaseHolder(v);
        }
        if (viewType == LOADMORETYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(mLoadMoreLayoutId, parent, false);
            BaseHolder holder = new BaseHolder(v);
            holder.tag = "loadmore";
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position)
    {
         if (holder instanceof  ContentHolder)
             bindView((ContentHolder) holder,position,mDatas.get(position));
         if (holder.tag!=null)
         {
             Log.d("tag","load more");
             if (mLoadMoreListener!=null)
                 mLoadMoreListener.loadMore();
         }
    }

    @Override
    public int getItemCount()
    {
        if (isEmpty||isError)
            return 1;
        if (enableLoadMore)
            return mDatas.size() + 1;

        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isEmpty)
            return EMPTYTYPE;

        if (isError)
            return ERRPRTYPE;

        if (enableLoadMore&&position==mDatas.size())
            return LOADMORETYPE;

        return NORMALTYPE;
    }

    public void showEmptyView()
    {
        isError = false;
        isEmpty = true;
        notifyDataSetChanged();
    }

    public void showErrorView()
    {
        isError = true;
        isEmpty = false;
        notifyDataSetChanged();
    }

    public void showDataContent()
    {
        isError = false;
        isEmpty = false;
        notifyDataSetChanged();
    }

    public void setLoadMore(boolean b)
    {
        if (enableLoadMore!=b)
        {
            enableLoadMore = b;
            notifyDataSetChanged();
        }
    }

    public void setLoadMoreLayoutId(@LayoutRes int loadMoreLayoutId)
    {
        mLoadMoreLayoutId = loadMoreLayoutId;
        enableLoadMore = true;
    }

    public void setErrorLayoutId(@LayoutRes int errorLayoutId)
    {
        mErrorLayoutId = errorLayoutId;
    }

    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId)
    {
        mEmptyLayoutId = emptyLayoutId;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener)
    {
        mLoadMoreListener = loadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener)
    {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public interface LoadMoreListener
    {
        void loadMore();
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener
    {
        boolean onItemLongClick(View v, int position);
    }
}
