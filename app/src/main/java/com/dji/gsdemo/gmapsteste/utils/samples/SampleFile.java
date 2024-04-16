package com.dji.gsdemo.gmapsteste.utils.samples;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.primitives.Area;

public class SampleFile {
    private ArrayList<SampleSubArea> subareas;
    private SampleSubArea.DataItem startPosition;

    public IPoint generateStartPosition() {
        return startPosition.toPoint();
    }
    public IArea generateArea() {
        IArea area = new Area();
        for (SampleSubArea s : subareas) {
            area.add(s.generateSubArea());
        }

        return area;
    }
}
