package com.jarvis.mytaobao.tao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.ShowsDetailActivityNew;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.jarvis.mytaobao.Data.RecommendBean;
import com.javis.Adapter.Adapter_ListView_tao;
import com.javis.mytools.DropBean;
import com.javis.mytools.DropdownButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 微淘主界面
 * 
 * @author http://yecaoly.taobao.com
 * 
 */
public class Tao_F extends Fragment {

	private DropdownButton dropdownButton1;
	private DropdownButton dropdownButton2;
	private DropdownButton dropdownButton3;
	private List<DropBean> times;
	private List<DropBean> types;
	private List<DropBean> names;

	private ListView listView_tao;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = LayoutInflater.from(getActivity()).inflate(R.layout.tao_f,
				null);
		initView(mView);
		return mView;
	}

	private void initView(View view) {
		listView_tao = (ListView) view.findViewById(R.id.listView_tao);

	}

	@Override
	public void onResume() {
		super.onResume();
		//访问网络获取列表数据
		getDataFromIntnet();

		//		listView_tao.setAdapter(new Adapter_ListView_tao(getActivity()));
		//		listView_tao.setOnItemClickListener(new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		//					long arg3) {
		//				Intent intent = new Intent(getActivity(), BabyActivity.class);
		//				startActivity(intent);
		//			}
		//		});

		dropdownButton1 = (DropdownButton)mView.findViewById(R.id.time1);
		dropdownButton2 = (DropdownButton)mView.findViewById(R.id.time2);
		dropdownButton3 = (DropdownButton)mView.findViewById(R.id.time3);

		initSomeData();
		dropdownButton1.setData(times);
		dropdownButton2.setData(types);
		dropdownButton3.setData(names);

		listView_tao.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Intent intent = new Intent(getActivity(), ShowsDetailActivityNew.class);
				Adapter_ListView_tao adapter_listView_tao = (Adapter_ListView_tao) listView_tao.getAdapter();
				intent.putExtra("filmRoom",adapter_listView_tao.getItem(arg2).getId());
				intent.putExtra("activity_name",adapter_listView_tao.getItem(arg2).getTableName());
				intent.putExtra("type",adapter_listView_tao.getItem(arg2).getTb_type());
				intent.putExtra("modelId",0);
				startActivity(intent);
			}
		});
	}

	public void getDataFromIntnet() {
		HttpUtil.get(NetConfig.RECOMMND_LIST, new HttpUtil.JsonHttpResponseUtil() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
				JSONArray array = jsonObject.optJSONArray("arr");
				List<RecommendBean> recommendBeen = new ArrayList<RecommendBean>();
				try {
					for(int i=0;i<array.length();i++){
						RecommendBean bean = new RecommendBean();
						String address =  array.getJSONObject(i).getString("faddress");
						double price =  array.getJSONObject(i).getDouble("price");
						String id = array.getJSONObject(i).getString("id");
						String image =  array.getJSONObject(i).getString("newpic");
						String label1 =  array.getJSONObject(i).getString("label1");
						String label2 =  array.getJSONObject(i).getString("label2");
						String label3 =  array.getJSONObject(i).getString("label3");
						String name =  array.getJSONObject(i).getString("fname");
						String tableName = array.getJSONObject(i).getString("tb_name");
						String tb_type = array.getJSONObject(i).getString("tb_type");
						String fdate =array.getJSONObject(i).getString("fdate");
						bean.setFdate(fdate);
						bean.setTableName(tableName);
						bean.setAddress(address);
						bean.setId(id);
						bean.setImage(image);
						bean.setLabel1(label1);
						bean.setLabel2(label2);
						bean.setLabel3(label3);
						bean.setName(name);
						bean.setNumber(price);
						bean.setTableName(tableName);
						bean.setTb_type(tb_type);
						recommendBeen.add(bean);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listView_tao.setAdapter(new Adapter_ListView_tao(getActivity(),recommendBeen));
			}
		});
	}

	private void initSomeData() {
	        times = new ArrayList<DropBean>();  
	        types = new ArrayList<DropBean>();  
	        names = new ArrayList<DropBean>();  
	        times.add(new DropBean("全部商圈"));  
	        times.add(new DropBean("商圈A"));
	        times.add(new DropBean("商圈B"));
	  
	        types.add(new DropBean("智能排序"));  
	        types.add(new DropBean("顺序"));  
	        types.add(new DropBean("降序"));  
	        
	        
	        names.add(new DropBean("筛选"));  
	        names.add(new DropBean("可预订"));  
	        names.add(new DropBean("全部"));  
	    }  

}
