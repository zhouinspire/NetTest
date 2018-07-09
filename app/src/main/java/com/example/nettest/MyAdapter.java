package com.example.nettest;


import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	public static final String TAG = "MyAdapter";

	private Context mContext;
	private List<String> mImageList;
	private List<String> mTitleList, mDateList, mAuthorList;
	private Main2Activity main;

	public MyAdapter(Context context, List<String> imageList,
			List<String> titleList, List<String> dateList,
			List<String> authorList) {
		mContext = context;
		mImageList = imageList;
		mTitleList = titleList;
		mDateList = dateList;
		mAuthorList = authorList;

	}

	@Override
	public int getCount() {
		return mImageList.size();
	}

	@Override
	public Object getItem(int position) {
		return mImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		Log.d(TAG, "position---->" + position);
		Log.d(TAG, "converView--->" + converView);
		Log.d(TAG, "parent---->" + parent);
		Log.d(TAG, "---------------------");

		View linearLayout = converView;
		ImageView image;
		TextView title, date, author;
		ViewHolder holder;

		if (converView == null) {

			LayoutInflater inflater = LayoutInflater.from(mContext);

			linearLayout = inflater.inflate(R.layout.item_layout, null);

			image = (ImageView) linearLayout.findViewById(R.id.item_image);
			title = (TextView) linearLayout.findViewById(R.id.item_title);
			date = (TextView) linearLayout.findViewById(R.id.item_date);
			author = (TextView) linearLayout.findViewById(R.id.item_author);


			holder = new ViewHolder(image, title, date, author);
			linearLayout.setTag(holder);

		} else {

			holder = (ViewHolder) linearLayout.getTag();


			image = holder.mImage;
			title = holder.mTitle;
			date = holder.mDate;
			author = holder.mCategory;

		}

		String imageUrl = mImageList.get(position);

		main.getInputStreamFromNet(imageUrl, image);
		// image.setImageResource(mImageList.get(position));
		title.setText(mTitleList.get(position));
		date.setText(mDateList.get(position));
		author.setText(mAuthorList.get(position));
		return linearLayout;
	}

	
	

	class ViewHolder {
		private ImageView mImage;
		private TextView mTitle, mDate, mCategory;

		public ViewHolder(ImageView img, TextView text1, TextView text2,
				TextView text3) {
			mImage = img;
			mTitle = text1;
			mDate = text2;
			mCategory = text3;
		}
	}


}
