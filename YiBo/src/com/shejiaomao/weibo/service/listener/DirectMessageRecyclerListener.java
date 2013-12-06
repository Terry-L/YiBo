package com.shejiaomao.weibo.service.listener;

import android.view.View;
import android.widget.AbsListView.RecyclerListener;

import com.shejiaomao.weibo.service.adapter.DirectMessageHolder;

public class DirectMessageRecyclerListener implements RecyclerListener {

	@Override
	public void onMovedToScrapHeap(View view) {
		if (view == null) {
			return;
		}

		Object obj = view.getTag();
		if (obj == null ||
			!(obj instanceof DirectMessageHolder)
		) {
		   return;	
		}
		
		DirectMessageHolder holder = (DirectMessageHolder)obj;
		holder.recycle();
	}

}
