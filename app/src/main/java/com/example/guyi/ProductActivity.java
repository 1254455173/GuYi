package com.example.guyi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guyi.db.City;
import com.example.guyi.db.County;
import com.example.guyi.db.Province;
import com.example.guyi.util.Utility;
import com.github.clans.fab.FloatingActionMenu;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductActivity extends BaseActivity {
    private ProgressDialog progressDialog;

    /**
     * 产品信息成员
     */

    public static final String Product_NAME = "Product_name";

    public static final String Product_IMAGE_ID = "Product_image_id";

    public static final String Product_PRICE = "Product_price";

    public static final String Product_LOCATION = "Product_location";

    private String ProductName;
    private int ProductImageId;
    private String ProductPrice;
    private String ProductLocation;


    /**
     * 天气信息成员
     */
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 选中的区县
     */
    private County selectedCounty;

    /**
     * 选中的级别
     */
    private int currentLevel;

    /**
     * 默认重庆
     */
    private final int CQ_PROVINCE_POSITION = 3;
    private final int CQ_CITY_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // MainActivity 传来的 product 信息
        Intent intent = getIntent();
        ProductName = intent.getStringExtra(Product_NAME);
        ProductImageId = intent.getIntExtra(Product_IMAGE_ID, 0);
        ProductPrice = intent.getStringExtra(Product_PRICE);
        ProductLocation = intent.getStringExtra(Product_LOCATION);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView ProductImageView = (ImageView) findViewById(R.id.product_image_view);
        TextView ProductContentText = (TextView) findViewById(R.id.product_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(ProductName);
        Glide.with(this).load(ProductImageId).into(ProductImageView);
        String ProductContent = generateProductContent(ProductName);
        ProductContentText.setText(ProductContent);


        // 浮动展开按钮
        final FloatingActionMenu fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);


        floatClick();


    }

    /**
     * 天气信息查询操作
     */
    private void queryProvinces(){
        provinceList = DataSupport.findAll(Province.class);

        if(provinceList.size()>0){
            dataList.clear();
            for(Province province: provinceList){
                dataList.add(province.getProvinceName());
            }
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到，再到服务器上查询
     */
    private void queryCities(){
        cityList = DataSupport.where("provinceid= ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city: cityList){
                dataList.add(city.getCityName());
            }
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            Log.d("test cities address:", address);
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中室内所有的县，优先从数据库查询，如果没有查询到再去服务器查询
     */
    private void queryCounties(){

        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county: countyList){
                dataList.add(county.getCountyName());
            }
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/"
                    + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(final String address, final String type){
         Thread httpThread = new Thread(new Runnable(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(address)
                        .get().build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String responseText = response.body().string();
                    boolean result = false;
                    if("province".equals(type)){
                        result = Utility.handleProvinResponse(responseText);
                        Log.d("test responseTest", responseText);

                    }else if("city".equals(type)){
                        result = Utility.handleCityResponse(responseText,
                                selectedProvince.getId());

                    } else if ("county".equals(type)){
                        result = Utility.handleCountyResponse(responseText,
                                selectedCity.getId());
                    }
                    if(result) {
                        if ("province".equals(type)) {
                            queryProvinces();
                            Log.d("the secondProvince", provinceList.size() + "");
                        } else if ("city".equals(type)) {
                            queryCities();
                        } else if ("county".equals(type)) {
                            queryCounties();
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        httpThread.start();
        try{
            httpThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }




        /*HttpUtil.sendOkHttpRequest(address, new Callback() {  // 异步容易出现 bug
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinResponse(responseText);
                    Log.d("test responseTest", responseText);

                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,
                            selectedProvince.getId());

                } else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,
                            selectedCity.getId());
                }
               if(result){  // 记得切回主线程
                    closeProgressDialog();
                    ProductActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("province".equals(type)){
                                queryProvinces();
                                Log.d("the secondProvince", provinceList.size()+"");
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                ProductActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ProductActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }


    /**
     * 浮动按钮响应
     */
    private void floatClick(){
        com.github.clans.fab.FloatingActionButton fab_travel = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_travel);
        fab_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 根据当前产品产地，匹配区县在列表中的数据，以便获得天气 id
                int position = 0;
                for(County county: countyList){
                    if(county.getCountyName().equals(ProductLocation)){
                        break;
                    }else{
                        position++;
                    }
                }
                Log.d("test myCountyPosition", position+"");
                String weatherId = countyList.get(position).getWeatherId();

                // 打开 WeatherActivity，并传送 weatherID 数据
                Intent intent = new Intent(ProductActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", weatherId);
                startActivity(intent);
                ProductActivity.this.finish();
            }
        });

        queryProvinces();
        //myThreadWait(provinceList);

        Log.d("test provinceList", provinceList.size()+"");
        selectedProvince = provinceList.get(CQ_PROVINCE_POSITION); // 重庆市
        queryCities();

        //myThreadWait(cityList);

        Log.d("test cityList", cityList.size()+"");
        selectedCity = cityList.get(CQ_CITY_POSITION);  // 重庆市

        queryCounties();
        //myThreadWait(countyList);

        Log.d("test countyList", countyList.size()+"");
    }

    /**
     * 异步写法容易出现 bug 啊！！！！
     */
    private void myThreadWait(List<?> list){ // 初次加载阻塞，以成功获取数据
        if(list.size() == 0){
            try{
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String generateProductContent(String ProductName) {
        StringBuilder ProductContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            ProductContent.append(ProductName);
        }
        return ProductContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(ProductActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private  void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}

