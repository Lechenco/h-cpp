package com.dji.gsdemo.gmapsteste.utils.samples;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;
import boustrophedon.provider.primitives.Subarea;

public class Sample {
    public static class DataItem {
        public double latitude;
        public double longitude;

        public IPoint toPoint() {
            return new Point(latitude, longitude);
        }
    }
    String type;
    List<DataItem> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public ISubarea generateSubArea() {
        ArrayList<IPoint> points = this.data.stream().map(DataItem::toPoint).collect(Collectors.toCollection(ArrayList::new));

        IPolygon polygon = new Polygon(points);
        ISubarea subarea = new Subarea(polygon);

        switch (type) {
            case "special":
                subarea.setSubareaType(SubareaTypes.SPECIAL);
                break;
            case "normal":
            default:
                subarea.setSubareaType(SubareaTypes.NORMAL);
                break;
        }
        return subarea;
    }
}
