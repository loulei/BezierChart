package com.example.bezierchart;

import java.util.ArrayList;
import java.util.List;

import com.example.bezierchart.BezierChart.Point;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	private BezierChart bezierchart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bezierchart = (BezierChart) findViewById(R.id.bezierchart);
		List<Point> points = new ArrayList<BezierChart.Point>();
		String[] labels = new String[10];
		for(int i=0; i<10; i++){
			labels[i] = i+"";
			points.add(new Point(i, (float) (Math.random()*10)));
		}
		bezierchart.init(points, labels);
	}
}


















