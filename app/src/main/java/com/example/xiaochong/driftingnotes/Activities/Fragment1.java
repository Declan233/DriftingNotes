package com.example.xiaochong.driftingnotes.Activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.xiaochong.driftingnotes.R;

import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment1 extends Fragment implements LocationSource, AMapLocationListener, View.OnClickListener {


    @Bind(R.id.upload_title)
    EditText uploadTitle;
    @Bind(R.id.upload_context)
    EditText uploadContext;
    @Bind(R.id.upload_image_show)
    ImageView uploadImageShow;
    @Bind(R.id.upload_image)
    Button uploadImage;
    @Bind(R.id.bt_putout)
    Button btPutout;

    @Bind(R.id.map1)
    MapView map1;
    @Bind(R.id.et_search)
    LinearLayout etSearch;


    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap aMap;
    private Dialog mCameraDialog;

    private static final String TAG = "Fragment1";
    private static final int ALBUM_OK = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        ButterKnife.bind(this, view);
        initViews(savedInstanceState);

        return view;
    }

    private void initViews(Bundle savedInstanceState) {
        map1.onCreate(savedInstanceState);
        aMap = map1.getMap();
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            clientOption = new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            clientOption.setInterval(15000);
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
            Log.d(TAG, "activate: ");
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                Log.d(TAG, "onLocationChanged: 定位失败" + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        map1.onResume();
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        map1.onDestroy();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        map1.onPause();
        super.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        map1.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        super.onDestroy();
    }


    @OnClick({R.id.upload_image, R.id.bt_putout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_image:
                setDialog();
                break;
            case R.id.bt_putout:
                break;
        }
    }

    private void setDialog() {
        mCameraDialog = new Dialog(this.getContext(), R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(
                R.layout.popue_windows, null);
        //初始化视图
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }


    @OnClick(R.id.et_search)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), PoiActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_img:
                //选择照片按钮
                gallery(v);
//                Toast.makeText(this.getContext(), "请选择照片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_open_camera:
                //拍照按钮
                startActivity(new Intent(getContext(), TakePhotoActivity.class));
//                Toast.makeText(this.getContext(), "即将打开相机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                mCameraDialog.dismiss();
                break;

        }
    }


    public void gallery(View view) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, ALBUM_OK);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ALBUM_OK == requestCode) {
            Toast.makeText(this.getContext(), "OK now", Toast.LENGTH_SHORT).show();
            Bitmap bitmap;
            Log.e(TAG, " onActivityResult ");
            ContentResolver cr = this.getActivity().getContentResolver();
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Log.e(TAG, " onActivityResult " + data.getData().toString());//此处用Log.e，仅是为了查看红色Log方便
//                getImagePath(uri);//这是用来读取图片的exif
                uploadImageShow.setImageBitmap(bitmap);
                mCameraDialog.dismiss();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
//
//    private String getImagePath(Uri uri) {
//        if (null == uri) {
//            Log.e("getImagePath", "uri return null");
//            return null;
//        }
//
//        Log.e("getImagePath", uri.toString());
//        String path = null;
//        final String scheme = uri.getScheme();
//        if (null == scheme) {
//            path = uri.getPath();
//        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
//            path = uri.getPath();
//        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(uri, proj, null, null,
//                    null);
//            int nPhotoColumn = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            if (null != cursor) {
//                cursor.moveToFirst();
//                path = cursor.getString(nPhotoColumn);
//            }
//            cursor.close();
//        }
//
//        return path;
//    }


}
