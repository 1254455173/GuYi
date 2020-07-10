package com.example.guyi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity {
    public static int MAIN_ACTIVITY_TAB_ID = 0;


    /* 产品卡片成员 */
    private Product[] products = {new Product("test1", R.drawable.test_1,"永川", 99), new Product("test2", R.drawable.test_2, "南川", 102),
            new Product("test3", R.drawable.test_3, "万盛", 67), new Product("test4", R.drawable.test_4, "万州", 345),
            new Product("test5", R.drawable.test_5, "城口", 87), new Product("test6", R.drawable.test_6, "云阳", 22 ),
            new Product("test7", R.drawable.test_7, "巫山", 887), new Product("test8", R.drawable.test_8, "秀山", 76),
            new Product("test9", R.drawable.test_9, "丰都", 43)};

    private List<Product> productList = new ArrayList<>();

    private ProductAdapter adapter;

    /* 下拉刷新成员 */
    private SwipeRefreshLayout swipeRefresh;


    /* 个人信息滑动菜单成员 */
    private DrawerLayout mDrawerLayout;

    /* 轮播图成员 */
    private Banner mbanner;
    private MyImageLoader myImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);


        /* 初始化轮播图 */
        initData();
        initView();


        // 状态栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 个人信息滑动菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // 个人头像
        }
        navView.setCheckedItem(R.id.nav_call);

        // 个人信息滑动菜单，导航按钮事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_call:
                        try {
                            Toast.makeText(MainActivity.this, "默认拨打 10086",Toast.LENGTH_SHORT).show();
                            try{ // 等一下
                                Thread.sleep(1000);
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:10086"));  // 暂时默认打 10086
                            startActivity(intent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.nav_location:
                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "You click the button",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });


        // 产品卡片
        initProducts();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        // 产品卡片下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProducts();
            }
        });

    }


    /**
     * 产品卡片成员
     */
    private void initProducts() {
        productList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(products.length);
            productList.add(products[index]);
        }
    }

    /**
     * 产品卡片下拉刷新
     */
    private void refreshProducts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initProducts();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    /**
     * 实现个人信息滑动菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                showUserPhoto();  /////////////////// aaaaaaaaaaaaaaaaaaaaa这里好烦
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


    /**
     * 轮播图实现
     */
    private void initData(){  // 初始化数据
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.lunbo1);
        imagePath.add(R.drawable.lunbo2);
        imagePath.add(R.drawable.lunbo4);
        imageTitle.add("第一张图");
        imageTitle.add("第二场图");
        imageTitle.add("第三张图");
    }

    private void initView(){
        myImageLoader = new MyImageLoader();
        mbanner = (Banner) findViewById(R.id.banner);
        // 设置 banner 样式
        mbanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置图片加载器
        mbanner.setImageLoader(myImageLoader);
        // 设置轮播的动画效果
        mbanner.setBannerAnimation(Transformer.CubeIn);
        // 设置图片的文字
        mbanner.setBannerTitles(imageTitle);
        // 设置轮播的时间间隔
        mbanner.setDelayTime(5000);
        // 设置是否为自动轮播
        mbanner.isAutoPlay(true);
        // 设置指示器的位置，小点点，居中显示
        mbanner.setIndicatorGravity(BannerConfig.CENTER);
        // 设置图片加载地址  // 轮播图点击事件的监听
        mbanner.setImages(imagePath)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        switch(position){
                            case 0:
                                //textView.setText("汽车");

                                break;
                            case 1:
                                //textView.setText("大巴");
                                break;
                            case 2:
                                //textView.setText("飞机");
                                break;
                        }
                    }
                }).start();
    }

    public class MyImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path,
                                 ImageView imageView){
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }



    /**
     * 这里有问题，更改用户头像，应该预先保存下来
     */
    public void showUserPhoto(){  // 动态展示头像
        String imagePath;
        ImageView picture = (ImageView) findViewById(R.id.userPhoto);
        Log.d("MainActivity ImagePath", RegisterActivity.IMAGE_PATH+"");
        if(RegisterActivity.IMAGE_PATH != null && picture != null){
            imagePath = RegisterActivity.IMAGE_PATH;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }

    }


    /**
     * 打电话功能
     */
    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
