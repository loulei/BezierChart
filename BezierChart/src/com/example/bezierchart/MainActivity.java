package com.example.bezierchart;

import java.util.ArrayList;
import java.util.List;

import com.example.bezierchart.BezierChart.Point;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private BezierChart bezierchart;
	private EditText et_a, et_b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		et_a = (EditText) findViewById(R.id.et_a);
		et_b = (EditText) findViewById(R.id.et_b);
		bezierchart = (BezierChart) findViewById(R.id.bezierchart);
		List<Point> points = new ArrayList<BezierChart.Point>();
		String[] labels = new String[10];
		for(int i=0; i<10; i++){
			labels[i] = i+"";
			points.add(new Point(i, (float)	 (Math.random()*10)));
		}
		bezierchart.init(points, labels);
	}
	
	public void refreshChart(View view){
		float a = Float.valueOf(et_a.getText().toString());
		float b = Float.valueOf(et_b.getText().toString());
		
		bezierchart.reloadParameter(a, b);
	}
}


















