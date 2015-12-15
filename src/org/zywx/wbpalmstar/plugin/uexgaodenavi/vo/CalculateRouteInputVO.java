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

package org.zywx.wbpalmstar.plugin.uexgaodenavi.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ylt on 15/12/8.
 */
public class CalculateRouteInputVO implements Serializable {

    public List<String[]> startPoints;
    public String[] startPoint;

    public List<String[]> endPoints;
    public String[] endPoint;

    public List<String[]> wayPoints;

    /**
     * 0.速度优先 1.花费最少 2.距离最短 3.不走高速 4.时间最短且躲避拥堵 5.不走收费道路且躲避拥堵
     */
    public int driveMode;

}
