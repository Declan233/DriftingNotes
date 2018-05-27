package com.example.xiaochong.driftingnotes.Activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.Utils.getAbsolutePathUtil;
import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

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
    @Bind(R.id.loc_disp)
    TextView locDisp;


    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap aMap;
    private Dialog mCameraDialog;

    private static final String TAG = "Fragment1";
    private static final int ALBUM_OK = 2;

    private LocationBean mylb;//定位信息
    private String imageurl;//选择图片的路径
    private String imageuploadurl="";//上传的图片的返回路径

    private String uuid;//用户id
    private String token;//用户登陆凭证

    private String postid;//发布的帖子的ID


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        ButterKnife.bind(this, view);
        initViews(savedInstanceState);

        return view;
    }

    /**
     * 初始地图样式，获取用户信息
     * @param savedInstanceState
     */
    private void initViews(Bundle savedInstanceState) {
        map1.onCreate(savedInstanceState);
        aMap = map1.getMap();
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        aMap.getUiSettings().setMyLocationButtonEnabled(true);

        uuid = SharedPreferencesUtil.getString(getContext(),"UUID","");
        token = SharedPreferencesUtil.getString(getContext(),"TOKEN","");

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
            clientOption.setOnceLocation(true);
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

    /**
     * 持续定位
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                mylb = new LocationBean(aMapLocation.getLongitude(), aMapLocation.getLatitude(), aMapLocation.getAddress(), aMapLocation.getDescription());
                makepoint();
                locDisp.setText(mylb.getTitle());
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
        String loc = SharedPreferencesUtil.getString(getContext(),"TITLE","");
        if (!loc.equals("")){
            mylb = new LocationBean(SharedPreferencesUtil.getFloat(getContext(),"LONTITUDE",0),
                    SharedPreferencesUtil.getFloat(getContext(),"LATITUDE",0),
                    SharedPreferencesUtil.getString(getContext(),"TITLE",""),
                    SharedPreferencesUtil.getString(getContext(),"SNIPPET",""));
            makepoint();
            locDisp.setText(loc);
        }
        else if (mylb!=null){
            locDisp.setText(mylb.getTitle());
        }
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
//        map1.onSaveInstanceState(outState);
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


    /**
     * 发布和选择图片两个按钮的监听
     * @param view
     */
    @OnClick({R.id.upload_image, R.id.bt_putout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_image:
                setDialog();
                break;
            case R.id.bt_putout:
                Postm();
                break;
        }
    }

    /**
     * 发布判断
     */
    private void Postm() {
        String title = uploadTitle.getText().toString();
        String content = uploadContext.getText().toString();
        if(title.equals("")){
            Toast.makeText(this.getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageurl!=null){
            Log.d(TAG, "Postm: "+imageurl);
            uploadImage(imageurl);
        }
        if (imageuploadurl.equals("failed")){
            imageuploadurl = "";
            return;
        }
        StartPost(title,content);
    }

    /**
     * 发起上传帖子请求
     * @param title
     * @param content
     */
    private void StartPost(String title,String content) {
        if (mylb==null){
            Toast.makeText(this.getContext(), "地址为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.Table.Create");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");
        params.put("model_name","DN_Notes");

        JSONObject data = new JSONObject();
        try {
            data.put("title",title);
            data.put("content",content);
            data.put("longitude",mylb.getLon());
            data.put("latitude",mylb.getLat());
            data.put("url",imageuploadurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "StartPost: "+data.toString());

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .addParam("data",data.toString())
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "StartPost onSuccess:" + response);
                        int code = response.optInt("ret");
                        if (code==200){
                            postid = response.optJSONObject("data").optString("id");
                            Toast.makeText(Fragment1.this.getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                            uploadTitle.setText("");
                            uploadContext.setText("");
                            uploadImageShow.setImageBitmap(null);
                        }
                    }

                    @Override

                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "StartPost onFailure:" + error_msg);
                    }

                });
    }

    /**
     * 上传图片
     * @param imageurl
     */
    private void uploadImage(String imageurl) {
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.CDN.UploadImg");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");

        MyApplication.getmInstance().getMyOkHttp().upload()
                .url(apiUrl)
                .params(params)
                .addFile("file", new File(imageurl))        //上传已经存在的File
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doDelete onSuccess:" + response);
                        int ret = response.optInt("ret");
                        uploadImageResult(ret,response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Toast.makeText(Fragment1.this.getContext(), "图片上传失败，请重试", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "doUpload onFailure:" + error_msg);
                    }

                });
    }

    /**
     * 上传图片的结果
     * @param code
     * @param response
     */
    private void uploadImageResult(int code,JSONObject response) {
        if (code==200){
            imageuploadurl = response.optJSONObject("data").optString("url");
            Log.e(TAG, "uploadImageResult: "+imageuploadurl);
            if (postid!="")
                alterurl();
        }else{
            Toast.makeText(this.getContext(), "图片上传失败，请重试", Toast.LENGTH_SHORT).show();
            imageuploadurl = "failed";
        }

    }



    /**
     * 修改帖子中的url图片路径
     */
    private void alterurl() {
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.Table.FreeUpdate");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");
        params.put("model_name","DN_Notes");

        JSONArray where = new JSONArray();
        JSONArray cond1 = new JSONArray();
        cond1.put("id");
        cond1.put("=");
        cond1.put(postid);
        where.put(cond1);

        JSONObject data = new JSONObject();
        try {
            data.put("url",imageuploadurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .addParam("where",where.toString())
                .addParam("data",data.toString())
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "alterurl onSuccess:" + response);
                        int ret = response.optInt("ret");
                        postid="";
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "alterurl onFailure:" + error_msg);
                    }

                });
    }

    /**
     * 图片选择弹出框
     */
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


    /**
     * 进入定位查找界面
     */
    @OnClick(R.id.et_search)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), PoiActivity.class));
    }


    /**
     * 弹出框按钮的监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_img:
                //选择照片按钮
                gallery(v);
                break;
            case R.id.btn_open_camera:
                //拍照按钮
                startActivity(new Intent(getContext(), TakePhotoActivity.class));
                break;
            case R.id.btn_cancel:
                mCameraDialog.dismiss();
                break;

        }
    }


    /**
     * 打开相册 选择图片
     * @param view
     */
    public void gallery(View view) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, ALBUM_OK);
    }


    /**
     * 选择的图片显示
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ALBUM_OK == requestCode) {
            Toast.makeText(this.getContext(), "OK now", Toast.LENGTH_SHORT).show();
            Bitmap bitmap;
            Log.e(TAG, " onActivityResult ");
            ContentResolver cr = this.getActivity().getContentResolver();
            Uri url = data.getData();
            imageurl = getAbsolutePathUtil.getRealPathFromUri(getContext(),url);
            try {
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(url));
                Log.e(TAG, " onActivityResult " + data.getData().toString());//此处用Log.e，仅是为了查看红色Log方便
                uploadImageShow.setImageBitmap(bitmap);
                mCameraDialog.dismiss();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据地址绘制需要显示的点
     */
    public void makepoint(){
        LatLng latLng=new LatLng(mylb.getLat(),mylb.getLon());

        Log.e(TAG, "makepoint: " );
        aMap.clear();
        //使用默认点标记
        Marker maker=aMap.addMarker(
                new MarkerOptions().position(latLng).title(mylb.getTitle()).snippet(mylb.getSnippet()).visible(true));

        //改变可视区域为指定位置
        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
        CameraUpdate cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,14,0,30));
        aMap.moveCamera(cameraUpdate);//地图移向指定区域

    }


}
