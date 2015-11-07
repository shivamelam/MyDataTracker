package com.melam.shiva.datatracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class BarGraph {

    public Intent getIntent(Context context, ArrayList<String> stats){


        //int y[] = {10, 20, 30, 40, 10, 30};

        CategorySeries series = new CategorySeries("Bar Series");

        for(int i=0; i<stats.size();i++)
        {
            series.add("Bar Series"+i, Double.parseDouble(stats.get(i)));
        }


        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series.toXYSeries());

        XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesSpacing((float) 1.0);

        mrenderer.addSeriesRenderer(renderer);
        mrenderer.setAxisTitleTextSize(30);
        mrenderer.setLabelsTextSize(30);
        mrenderer.setXTitle("Days");
        mrenderer.setYTitle("Data Used!");
        mrenderer.setGridColor(Color.LTGRAY);

        return ChartFactory.getBarChartIntent(context,dataset ,mrenderer, BarChart.Type.DEFAULT);
    }
}
