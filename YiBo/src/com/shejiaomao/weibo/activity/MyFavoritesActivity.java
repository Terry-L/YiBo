package com.shejiaomao.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shejiaomao.weibo.BaseActivity;
import com.shejiaomao.maobo.R;
import com.shejiaomao.weibo.SheJiaoMaoApplication;
import com.shejiaomao.weibo.common.theme.ThemeUtil;
import com.shejiaomao.weibo.service.adapter.MyFavoriteListAdapter;
import com.shejiaomao.weibo.service.listener.GoBackClickListener;
import com.shejiaomao.weibo.service.listener.GoHomeClickListener;
import com.shejiaomao.weibo.service.listener.MicroBlogContextMenuListener;
import com.shejiaomao.weibo.service.listener.MicroBlogItemClickListener;
import com.shejiaomao.weibo.service.listener.StatusRecyclerListener;
import com.shejiaomao.weibo.service.listener.StatusScrollListener;
import com.shejiaomao.weibo.service.task.MyFavoritesTask;

public class MyFavoritesActivity extends BaseActivity {
	private MyFavoriteListAdapter adapter = null;
	private SheJiaoMaoApplication sheJiaoMao;
	private ListView lvMicroBlog = null;
	private View listFooter = null;

	private StatusRecyclerListener recyclerListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_timeline);

		sheJiaoMao = (SheJiaoMaoApplication) getApplication();
		adapter = new MyFavoriteListAdapter(this, sheJiaoMao.getCurrentAccount());
		initComponents();
		bindEvent();

		executeTask();
	}

	private void initComponents() {
		LinearLayout llHeaderBase = (LinearLayout)findViewById(R.id.llHeaderBase);
		lvMicroBlog = (ListView) this.findViewById(R.id.lvMicroBlog);
		ThemeUtil.setSecondaryHeader(llHeaderBase);
		ThemeUtil.setContentBackground(lvMicroBlog);
		ThemeUtil.setListViewStyle(lvMicroBlog);
		
		TextView tvTitle = ((TextView) this.findViewById(R.id.tvTitle));
		tvTitle.setText(R.string.title_favorites);

		lvMicroBlog.setFastScrollEnabled(sheJiaoMao.isSliderEnabled());
		showMoreFooter();
		lvMicroBlog.setAdapter(adapter);
		setBack2Top(lvMicroBlog);
		
		recyclerListener = new StatusRecyclerListener();
		lvMicroBlog.setRecyclerListener(recyclerListener);
	}

	private void bindEvent() {
		Button btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new GoBackClickListener());

		Button btnOperate = (Button) this.findViewById(R.id.btnOperate);
		btnOperate.setVisibility(View.VISIBLE);
		btnOperate.setText(R.string.btn_home);
		btnOperate.setOnClickListener(new GoHomeClickListener());

		MicroBlogItemClickListener itemClickListener = new MicroBlogItemClickListener(this);
		lvMicroBlog.setOnItemClickListener(itemClickListener);
		MicroBlogContextMenuListener contextMenuListener =
			new MicroBlogContextMenuListener(lvMicroBlog);
		lvMicroBlog.setOnCreateContextMenuListener(contextMenuListener);
		lvMicroBlog.setOnScrollListener(new StatusScrollListener());
	}

	private void executeTask() {
		new MyFavoritesTask(this, adapter, sheJiaoMao.getCurrentAccount()).execute();
	}

	public void showLoadingFooter() {
		if (listFooter != null) {
			lvMicroBlog.removeFooterView(listFooter);
		}
		listFooter = getLayoutInflater().inflate(R.layout.list_item_loading, null);
		ThemeUtil.setListViewLoading(listFooter);

		lvMicroBlog.addFooterView(listFooter);
	}

	public void showMoreFooter() {
		if (listFooter != null) {
			lvMicroBlog.removeFooterView(listFooter);
		}

		listFooter = getLayoutInflater().inflate(R.layout.list_item_more, null);
		ThemeUtil.setListViewMore(listFooter);
		listFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				executeTask();
			}
		});
		lvMicroBlog.addFooterView(listFooter);
	}

	public void showNoMoreFooter() {
		if (listFooter != null) {
			lvMicroBlog.removeFooterView(listFooter);
		}
		listFooter = getLayoutInflater().inflate(R.layout.list_item_more, null);
		ThemeUtil.setListViewMore(listFooter);
		
		TextView tvFooter = (TextView) listFooter.findViewById(R.id.tvFooter);
		tvFooter.setText(R.string.label_no_more);
		lvMicroBlog.addFooterView(listFooter);
	}

}
