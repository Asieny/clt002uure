package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.VenueDetailsInfo;
import com.javis.mytools.DropBean;
import com.javis.mytools.DropdownButton;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/5 13:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CollectionMoreActivity extends BaseActivity implements View.OnClickListener {

    private ImageView      mIv_back;
    private List<DropBean> times;
    private List<DropBean> types;
    private DropdownButton dropdownButton1;
    private DropdownButton dropdownButton2;
    private RecyclerView   mRecyclerView_goods;
    private       String                                            mURLHeadString = NetConfig.IMG;
    private       String                                            url_address    = NetConfig.ROOT;
    public static ArrayList<VenueDetailsInfo.ArrBean.ListGoodsBean> mListGoodsBean = new ArrayList<>();//场馆收藏品信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_more);
        Intent intent = getIntent();
        url_address = intent.getStringExtra("url_address");
        mURLHeadString = intent.getStringExtra("showImgUrl");

        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        dropdownButton1 = (DropdownButton) findViewById(R.id.time1);
        dropdownButton2 = (DropdownButton) findViewById(R.id.time2);
        mRecyclerView_goods = (RecyclerView) findViewById(R.id.recyclerView_goods);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        initSomeData();
        dropdownButton1.setData(types);
        dropdownButton2.setData(times);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView_goods.setLayoutManager(gridLayoutManager);
        MyRecAdapter myRecCollectionAdapter = new MyRecAdapter(mListGoodsBean);
        mRecyclerView_goods.setAdapter(myRecCollectionAdapter);
    }

    private void initSomeData() {
        types = new ArrayList<DropBean>();
        times = new ArrayList<DropBean>();

        types.add(new DropBean("全部类别"));
        types.add(new DropBean("字画"));
        types.add(new DropBean("文物"));

        times.add(new DropBean("全部朝代"));
        times.add(new DropBean("当代"));
        times.add(new DropBean("近代"));
        times.add(new DropBean("清朝"));
        times.add(new DropBean("明朝"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public class MyRecAdapter extends RecyclerView.Adapter<MyRecAdapter.ViewHolder> {

        private List mData;

        public MyRecAdapter(List data) {
            this.mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            // 实例化展示的view
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.museum_collection_more_item, parent, false);

            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            final VenueDetailsInfo.ArrBean.ListGoodsBean mListGoodsBean = (VenueDetailsInfo.ArrBean.ListGoodsBean) mData.get(position);
            String newGoods_file = mListGoodsBean.getNewGoods_file();
            ImageLoaderUtil.displayImageIcon(mURLHeadString + newGoods_file, holder.img_goods);
            String goods_name = mListGoodsBean.getGoods_name();
            holder.tv_goods_name.setText(goods_name);
            holder.linear_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:点击显示详细藏品信息
                    Intent intent = new Intent(getBaseContext(), CollectionDetailsActivity.class);
                    intent.putExtra("tb_name", mListGoodsBean.getTb_name());
                    intent.putExtra("business_id", mListGoodsBean.getBusiness_id());
                    intent.putExtra("which", position);
                    intent.putExtra("url_address", url_address);
                    intent.putExtra("showImgUrl", mURLHeadString);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linear_item;//整个条目
            ImageView    img_goods;//藏品图片
            TextView     tv_goods_name;//藏品名称

            public ViewHolder(View itemView) {
                super(itemView);
                linear_item = (LinearLayout) itemView.findViewById(R.id.linear_item);
                img_goods = (ImageView) itemView.findViewById(R.id.img_goods);
                tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
