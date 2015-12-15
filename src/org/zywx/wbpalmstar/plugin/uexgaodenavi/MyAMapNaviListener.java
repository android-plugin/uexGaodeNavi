/*
 * Copyright (c) 2015.  The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.zywx.wbpalmstar.plugin.uexgaodenavi;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.InitOutputVO;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.OnGetNavigationTextVO;
import org.zywx.wbpalmstar.plugin.uexgaodenavi.vo.OnStartNaviVO;

/**
 * Created by ylt on 15/12/8.
 */
public class MyAMapNaviListener implements AMapNaviListener {

    private EUExGaodeNavi mEUExGaodeNavi;

    public MyAMapNaviListener(EUExGaodeNavi euExBase){
        this.mEUExGaodeNavi =euExBase;
    }

    @Override
    public void onInitNaviFailure() {
        callbackInit(false);
    }

    @Override
    public void onInitNaviSuccess() {
        callbackInit(true);
    }

    private void callbackInit(boolean result){
        InitOutputVO outputVO=new InitOutputVO();
        outputVO.result=result;
        this.mEUExGaodeNavi.callBackPluginJs(JsConst.CALLBACK_INIT, DataHelper.gson.toJson(outputVO));
    }

    @Override
    public void onStartNavi(int i) {
        OnStartNaviVO startNaviVO=new OnStartNaviVO();
        startNaviVO.type=i;
        mEUExGaodeNavi.callBackPluginJs(JsConst.ON_START_NAVI,DataHelper.gson.toJson(startNaviVO));
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {
        OnGetNavigationTextVO output=new OnGetNavigationTextVO();
        output.text=s;
        output.type=i;
        mEUExGaodeNavi.callBackPluginJs(JsConst.ON_GET_NAVIGATION_TEXT, DataHelper.gson.toJson(output));
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {
        mEUExGaodeNavi.removeMapView();
        mEUExGaodeNavi.callBackPluginJs(JsConst.ON_ARRIVE_DESTINATION,"");
    }

    @Override
    public void onCalculateRouteSuccess() {
        mEUExGaodeNavi.callbackCalculateRoute(true);

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        mEUExGaodeNavi.callbackCalculateRoute(false);
    }

    @Override
    public void onReCalculateRouteForYaw() {
        mEUExGaodeNavi.callBackPluginJs(JsConst.ON_RE_CALCULATE_ROUTE_FOR_YAW,"");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        mEUExGaodeNavi.callBackPluginJs(JsConst.ON_RE_CALCULATE_ROUTE_FOR_TRAFFIC_JAM,"");
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }
}
