package com.japyijiwenfa.adapter;


import java.util.List;

import com.japyijiwenfa.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CatalogAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<String> mChapterNames;
	public CatalogAdapter(Context context,List<String> chapterNames){
		mContext = context;
		mChapterNames = chapterNames;
	}
	@Override
	public int getCount() {
		return mChapterNames.size();
	}

	@Override
	public Object getItem(int position) {
		return mChapterNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.catalog_list_item, null);
			holder.image = (ImageView)convertView.findViewById(R.id.catalog_chapter_img);
			holder.chapterName = (TextView)convertView.findViewById(R.id.catalog_chapter_name);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		String name = mChapterNames.get(position);
		/*if(position % 2 == 0){
			holder.image.setBackgroundResource(R.drawable.about_icon);
		}else{
			holder.image.setBackgroundResource(R.drawable.accountmanage_icon);
		}*/
		holder.chapterName.setText(name);
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView chapterName;
	}
}
