package com.kc.activity;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.daShen.custom.timeAxis.TimeAxis;
import com.kc.base.BaseActivity;
import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    TimeAxis timeAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        timeAxis = getViewById(R.id.timeAxis);
//        timeAxis.setOnValueChangeListener(new TimeAxis.OnValueChangeListener() {
//            @Override
//            public void onValueChange(TimeAlgorithm _value) {
//
//            }
//
//            @Override
//            public void onStartValueChange(TimeAlgorithm _value) {
//
//            }
//
//            @Override
//            public void onStopValueChange(TimeAlgorithm _value) {
//
//            }
//        });

//        final CstTextView textView = new CstTextView(this);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        textView.setTextSize(60);
//        textView.setLayoutParams(lp);
//        textView.setText("Scroller");
//        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
//        setContentView(textView);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.handleY(200);
//            }
//        },2000);

//        for (int i = 0; i < mCityCodeArr.length; i++) {
//            Log.e(TAG, "开始搜索城市："+ mCityArr[i]);
//            searchPOI(mCityCodeArr[i]);
//        }
        mSearchStartTime = System.currentTimeMillis();
        searchPOI(mCityCodeArr[mCurSearchCityIndx], mCurSearchPage);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }

    private final String[] mCityArr = {"广州", "东莞", "佛山", "深圳"};
    private final String[] mCityCodeArr = {"020", "0769", "0757", "0755"};

    private int mCurSearchCityIndx = 0;
    private int mCurSearchPage = 0;

    private long mSearchStartTime;

    private void searchPOI(String cityName, int page) {
        PoiSearch.Query query = new PoiSearch.Query("地铁站", null, cityName);//"150500"
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(200);// 设置每页最多返回多少条poiitem
        query.setPageNum(page);//设置查询页码
        //
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        Log.e(TAG, ">>>>>>>>>>>>>>>>>searchPOI: 开始异步搜索城市地铁：" + cityName + ", page = " + page);
        //
        poiSearch.searchPOIAsyn();
    }

    private List<PoiItem> mAllPois = new ArrayList<>();

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if (code == 1000) {
            ArrayList<PoiItem> pois = poiResult.getPois();
            int size = pois.size();
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>onPoiSearched: poi搜索成功！！size = " + size);
            PoiItem poi;
            for (int i = 0; i < size; i++) {
                poi = pois.get(i);
                Log.e(TAG, "poi city:" + poi.getCityName() + ",站名:" + poi.getTitle() + ",纬经度:" + poi.getLatLonPoint().toString());
            }
            if (size > 0) {
                mAllPois.addAll(pois);
            }
            if (size == 30) {
                searchPOI(mCityCodeArr[mCurSearchCityIndx], mCurSearchPage++);
            } else {
                mCurSearchCityIndx++;
                if (mCurSearchCityIndx > mCityCodeArr.length - 1) {
                    long time = (System.currentTimeMillis() - mSearchStartTime) / 1000;
                    Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>onPoiSearched: 搜索结束！！总条数：" + mAllPois.size() + ",用时(s):" + time);
                    calculateDis();
                    return;
                }
                mCurSearchPage = 0;
                searchPOI(mCityCodeArr[mCurSearchCityIndx], mCurSearchPage);
            }
        } else {
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>onPoiSearched: !!!!!!!错误码：" + code);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private double[] mDataLatitudes = new double[]{
            22.566142254795,
            22.6862052598878
    };
    private double[] mDataLongtitudes = new double[]{
            113.980836777953,
            113.940525211308
    };

    private void calculateDis() {
        LatLng latLng;
        LatLng latLng1;
        PoiItem poi;
        float minMeterDis = 0;
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>calculateDis: 开始计算最小距离");
        mSearchStartTime = System.currentTimeMillis();
        for (int i = 0; i < mDataLatitudes.length; i++) {
            latLng = new LatLng(mDataLatitudes[i], mDataLongtitudes[i]);

            int size = mAllPois.size();

            for (int k = 0; k < size; k++) {
                poi = mAllPois.get(k);
                latLng1 = new LatLng(poi.getLatLonPoint().getLatitude(), poi.getLatLonPoint().getLongitude());
                float meterDis = AMapUtils.calculateLineDistance(latLng, latLng1);
                if (k == 0) {
                    minMeterDis = meterDis;
                } else {
                    if (meterDis < minMeterDis) {
                        minMeterDis = meterDis;
                        Log.e(TAG, ">>>calculateDis: 新最短距离:" + i + "->" + minMeterDis);
                    }
                }
            }
            Log.e(TAG, ">>>>>>>>>>calculateDis: 数据" + i + "到最近地铁距离" + minMeterDis + "米");
        }
        long time = (System.currentTimeMillis() - mSearchStartTime) / 1000;
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>calculateDis: 完成计算最小距离,耗时(s):" + time);
    }

}
