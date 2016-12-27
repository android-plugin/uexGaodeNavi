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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.navi.AMapNaviView;

import com.amap.api.navi.AMapNaviViewListener;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.base.view.BaseFragment;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

/**
 * Created by ylt on 15/12/15.
 */
public class NaviMapFragment extends BaseFragment {

    private EUExGaodeNavi mEUExGaodeNavi;

    AMapNaviView mNaviView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(EUExUtil.getResLayoutID
                ("plugin_uex_gaodenavi_mapview_fragment"), null, false);
        mNaviView = (AMapNaviView) rootView.findViewById(EUExUtil.getResIdID("navi_view"));
        if (mEUExGaodeNavi!=null){
            mNaviView.setAMapNaviViewListener(mEUExGaodeNavi);
        }else{
            BDebug.e("AMapNaviViewListener is null...");
        }
        mNaviView.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroy() {
        clean();
        super.onDestroy();
    }

    public void clean(){
        if (mNaviView!=null) {
            mNaviView.onDestroy();
            mNaviView=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNaviView!=null) {
            mNaviView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNaviView!=null){
            mNaviView.onPause();
        }
     }


    public void setEUExGaodeNavi(EUExGaodeNavi EUExGaodeNavi) {
        mEUExGaodeNavi = EUExGaodeNavi;
    }


}
