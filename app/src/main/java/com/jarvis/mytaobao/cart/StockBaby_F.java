package com.jarvis.mytaobao.cart;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jarvis.mytaobao.Data.Book;
import com.jarvis.mytaobao.home.BabyActivity;
import com.guangxi.culturecloud.R;
import com.javis.Adapter.Adapter_ListView_book;


/**
 * 购物车界面中的库存界面
 * @author http://yecaoly.taobao.com
 *
 */
public class StockBaby_F extends Fragment {

	private ListView listView_tao;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.cart_stock_f, null);
		initView(view);
		return view;
	}
	
	private void initView(View view){
		Book book = new Book(R.drawable.book1,"最美广西图书馆","地点：广西南宁国贸大厦909星座","时间：2017.11.12 19:20");
		List<Book> data = new ArrayList<Book>();
		for (int i = 0; i < 10; i++) {
			data.add(book);
		}
		
		
		listView_tao = (ListView) view.findViewById(R.id.listView_tao);
		listView_tao.setAdapter(new Adapter_ListView_book(getActivity(),data));
		listView_tao.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), BabyActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
