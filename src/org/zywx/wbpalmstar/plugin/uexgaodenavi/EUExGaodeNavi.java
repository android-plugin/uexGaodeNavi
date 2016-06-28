package org.zywx.wbpalmstar.plugin.uexgaodenavi;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.NaviLatLng;

import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.CalculateRouteInputVO;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.CalculateRouteOutputVO;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.StartNaviInputVO;

import java.util.ArrayList;
import java.util.List;


public class EUExGaodeNavi extends EUExBase implements AMapNaviViewListener {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_INIT = 1;
    private static final int MSG_CALCULATE_ROUTE = 2;
    private int CALCULATEERROR = 0;

    private List<NaviLatLng> mStartList;
    private List<NaviLatLng> mEndList;
    private List<NaviLatLng> mWayPointList;

    public AMapNavi mAMapNavi = null;

    public NaviMapFragment mMapFragment;

    public static final String FRAGMENT_ID = "gaode_map_navi_id";

    public int mNaviType = AMapNavi.GPSNaviMode;

    public EUExGaodeNavi(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }

    @Override
    protected boolean clean() {
        return false;
    }


    public void init(String[] params) {
        String json = params[0];
        if (mAMapNavi == null) {
            mAMapNavi = AMapNavi.getInstance(mContext.getApplicationContext());
            mAMapNavi.setAMapNaviListener(new MyAMapNaviListener(this));
        }
        mAMapNavi.setEmulatorNaviSpeed(150);
        if (mMapFragment == null) {
            mMapFragment = new NaviMapFragment();
            mMapFragment.setEUExGaodeNavi(this);
        }
    }

    public void showMapView() {
        if (mMapFragment==null){
            return;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addFragmentToCurrentWindow(mMapFragment, layoutParams, FRAGMENT_ID);
    }

    public void removeMapView() {
        if (mMapFragment != null) {
            mMapFragment.onDestroy();
            removeFragmentFromWindow(mMapFragment);
        }
    }

    public void calculateWalkRoute(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        CalculateRouteInputVO inputVO = DataHelper.gson.fromJson(json, CalculateRouteInputVO.class);

        calculateWalkRoute(inputVO);
    }

    public void calculateDriveRoute(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        CalculateRouteInputVO inputVO = DataHelper.gson.fromJson(json, CalculateRouteInputVO.class);
        calculateDriveRoute(inputVO);
    }

    public void startNavi(String[] params) {
        String json = params[0];
        if (!TextUtils.isEmpty(json)) {
            StartNaviInputVO inputVO = DataHelper.gson.fromJson(json, StartNaviInputVO.class);
            if (inputVO.type == 1) {
                //模拟导航
                mNaviType = AMapNavi.EmulatorNaviMode;
            }
        }
        showMapView();
        mAMapNavi.startNavi(mNaviType);
    }


    private void calculateWalkRoute(CalculateRouteInputVO inputVO) {
        try {
            NaviLatLng endNaviLatLng = convertToNaviLatLng(inputVO.endPoint);
            if (inputVO.startPoint == null) {
                mAMapNavi.calculateWalkRoute(endNaviLatLng);
            } else {
                NaviLatLng startNaviLatLng = convertToNaviLatLng(inputVO.startPoint);
                mAMapNavi.calculateWalkRoute(startNaviLatLng, endNaviLatLng);
            }
        } catch (Exception e) {
            if (BDebug.DEBUG){
                e.printStackTrace();
            }
            callbackCalculateRoute(false);
        }
    }

    private void calculateDriveRoute(CalculateRouteInputVO inputVO) {
        try {
            getStartList(inputVO);
            getEndList(inputVO);
            getWayPointList(inputVO);
            if (mStartList == null) {
                mAMapNavi.calculateDriveRoute(mEndList, mWayPointList, inputVO.driveMode);
            } else {
                mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList, inputVO.driveMode);
            }
        } catch (Exception e) {
            if (BDebug.DEBUG){
                e.printStackTrace();
            }
            callbackCalculateRoute(false);
        }

    }


    public void callbackCalculateRoute(boolean result) {
        CalculateRouteOutputVO outputVO = new CalculateRouteOutputVO();
        outputVO.result = result;
        callBackPluginJs(JsConst.CALLBACK_CALCULATE_ROUTE, DataHelper.gson.toJson(outputVO));
    }


    private void stopNavi(String[] params) {
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
        }
        removeMapView();
    }

    @Override
    public void onHandleMessage(Message message) {
        if (message == null) {
            return;
        }
        Bundle bundle = message.getData();
        switch (message.what) {
            default:
                super.onHandleMessage(message);
        }
    }

    public void callBackPluginJs(String methodName, String jsonData) {
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }


    public List<NaviLatLng> getStartList(CalculateRouteInputVO inputVO) {
        if (inputVO.startPoints != null) {
            mStartList = convertPoints(inputVO.startPoints);
        } else if (inputVO.startPoint != null) {
            mStartList = new ArrayList<NaviLatLng>();
            if (inputVO.startPoint.length > 1) {
                mStartList.add(convertToNaviLatLng(inputVO.startPoint));
            }
        } else {
            mStartList = null;
        }
        return mStartList;
    }

    private List<NaviLatLng> convertPoints(List<String[]> pointsList) {
        List<NaviLatLng> naviLatLngs = new ArrayList<NaviLatLng>();
        for (String[] nums : pointsList) {
            if (nums != null && nums.length > 1) {
                naviLatLngs.add(convertToNaviLatLng(nums));
            } else {
                BDebug.e("params error.");
            }
        }
        return naviLatLngs;
    }

    private NaviLatLng convertToNaviLatLng(String[] params) {
        return new NaviLatLng(Double.valueOf(params[0]), Double.valueOf(params[1]));
    }

    public List<NaviLatLng> getEndList(CalculateRouteInputVO inputVO) {
        if (inputVO.endPoints != null) {
            mEndList = convertPoints(inputVO.endPoints);
        } else if (inputVO.endPoint != null) {
            mEndList = new ArrayList<NaviLatLng>();
            if (inputVO.endPoint.length > 1) {
                mEndList.add(convertToNaviLatLng(inputVO.endPoint));
            }
        } else {
            mEndList = null;
        }
        return mEndList;
    }

    public List<NaviLatLng> getWayPointList(CalculateRouteInputVO inputVO) {
        if (inputVO.wayPoints != null) {
            mWayPointList = convertPoints(inputVO.wayPoints);
        } else {
            mWayPointList = null;
        }
        return mWayPointList;
    }


    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        removeMapView();
        callBackPluginJs(JsConst.ON_NAVI_CANCEL, "");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

}
