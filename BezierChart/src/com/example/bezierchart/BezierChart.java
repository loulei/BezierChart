package com.example.bezierchart;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BezierChart extends View {

	public BezierChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BezierChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BezierChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	private Rect chartRect = new Rect();
	private Paint chartBgPaint = new Paint();
	private Paint gridPaint = new Paint();
	private Paint labelPaint = new Paint();
	private Paint bezierPaint = new Paint();
	private Paint straightPaint = new Paint();
	private List<Point> mPoints;
	private String[] mLabels;
	private Point[] adjustedPoints;
	private Path path;
	private float a = 1.0F/4;
	private float b = 1.0F/4;
	
	{
		chartBgPaint.setColor(Color.CYAN);
		labelPaint.setColor(Color.DKGRAY);
		labelPaint.setAntiAlias(true);
		labelPaint.setTextSize(24f);
		labelPaint.setTextAlign(Align.LEFT);
		gridPaint.setColor(Color.GRAY);
		gridPaint.setStyle(Style.STROKE);
		gridPaint.setStrokeWidth(1);
		gridPaint.setAntiAlias(true);
		bezierPaint.setColor(Color.RED);
		bezierPaint.setStyle(Style.STROKE);
		bezierPaint.setStrokeWidth(1);
		bezierPaint.setAntiAlias(true);
		straightPaint.setStyle(Style.STROKE);
		straightPaint.setColor(Color.BLUE);
		straightPaint.setAntiAlias(true);
		straightPaint.setStrokeWidth(1);
		path = new Path();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		getDrawingRect(chartRect);
		canvas.drawRect(chartRect, chartBgPaint);
		if(mPoints != null){
			drawLabels(canvas);
			int chartHeight = chartRect.bottom - chartRect.top;
			int chartWidth = chartRect.right - chartRect.left;
			adjustPoints(chartWidth, chartHeight);
			drawGrid(canvas, chartWidth);
			drawLine(canvas);
		}
		
		super.onDraw(canvas);
	}
	
	public void init(List<Point> points, String[] labels){
		this.mPoints = points;
		this.mLabels = labels;
		adjustedPoints = new Point[mPoints.size()];
		invalidate();
	}
	
	private void drawLabels(Canvas canvas){
		int width = chartRect.right - chartRect.left;
		float labelY = chartRect.bottom;
		float part = width*1.0F / (mLabels.length-1);
		
		for(int i=0; i<mLabels.length; i++){
			String s = mLabels[i];
			float centerX = chartRect.left + part * i;
			float labelWidth = labelPaint.measureText(s);
			float labelX;
			if(i == 0){
				labelX = chartRect.left;
			}else if (i == mLabels.length-1){
				labelX = chartRect.right - labelWidth;
			}else {
				labelX = centerX - labelWidth/2;
			}
			canvas.drawText(s, labelX, labelY, labelPaint);
		}
		FontMetrics fontMetrics = labelPaint.getFontMetrics();
		float textHeight = (float) Math.floor(fontMetrics.descent - fontMetrics.ascent);
		chartRect.bottom = (int) (chartRect.bottom - textHeight);
	}
	
	private void drawGrid(Canvas canvas, int width){
		canvas.drawRect(chartRect, gridPaint);
		int gridCount = mLabels.length-1;
		float part = width*1.0F / gridCount;
		for(int i=1; i<=gridCount; i++){
			float x = chartRect.left + i*part;
			canvas.drawLine(x, chartRect.top, x, chartRect.bottom, gridPaint);
		}
	}

	private void drawLine(Canvas canvas){
		path.reset();
		path.moveTo(adjustedPoints[0].x, adjustedPoints[0].y);
		for(int i=1; i<adjustedPoints.length; i++){
			path.lineTo(adjustedPoints[i].x, adjustedPoints[i].y);
		}
		canvas.drawPath(path, straightPaint);
		path.reset();
		path.moveTo(adjustedPoints[0].x, adjustedPoints[0].y);
		for(int i=0; i<adjustedPoints.length-1; i++){
			float ctrlX1;
			float ctrlY1;
			float ctrlX2;
			float ctrlY2;
			
			if(i == 0)
			{
				ctrlX1 = adjustedPoints[i].x + a*(adjustedPoints[i+1].x - adjustedPoints[i].x);
				ctrlY1 = adjustedPoints[i].y + a*(adjustedPoints[i+1].y - adjustedPoints[i].y);
				ctrlX2 = adjustedPoints[i+1].x - b*(adjustedPoints[i+2].x - adjustedPoints[i].x);
				ctrlY2 = adjustedPoints[i+1].y - b*(adjustedPoints[i+2].y - adjustedPoints[i].y);
			}else if(i == (adjustedPoints.length - 2)){
				ctrlX1 = adjustedPoints[i].x + a*(adjustedPoints[i+1].x - adjustedPoints[i-1].x);
				ctrlY1 = adjustedPoints[i].y + a*(adjustedPoints[i+1].y - adjustedPoints[i-1].y);
				ctrlX2 = adjustedPoints[i+1].x - b*(adjustedPoints[i+1].x - adjustedPoints[i].x);
				ctrlY2 = adjustedPoints[i+1].y - b*(adjustedPoints[i+1].y - adjustedPoints[i].y);
			}else{
				ctrlX1 = adjustedPoints[i].x + a*(adjustedPoints[i+1].x - adjustedPoints[i-1].x);
				ctrlY1 = adjustedPoints[i].y + a*(adjustedPoints[i+1].y - adjustedPoints[i-1].y);
				ctrlX2 = adjustedPoints[i+1].x - b*(adjustedPoints[i+2].x - adjustedPoints[i].x);
				ctrlY2 = adjustedPoints[i+1].y - b*(adjustedPoints[i+2].y - adjustedPoints[i].y);
			}
			path.cubicTo(ctrlX1, ctrlY1, ctrlX2, ctrlY2, adjustedPoints[i+1].x, adjustedPoints[i+1].y);
		}
		canvas.drawPath(path, bezierPaint);
	}
	
	private void adjustPoints(int chartWidth, int chartHeight){
		float maxY = 0;
		for(Point point : mPoints){
			maxY = point.y > maxY ? point.y : maxY;
		}
		
		float scaleY = chartHeight*1.0F / (maxY);
		float part = chartWidth / (mPoints.size()-1);
		for(int i=0; i<mPoints.size(); i++){
			Point point = new Point();
			point.x = chartRect.left + i*part;
			point.y = mPoints.get(i).y * scaleY;
			adjustedPoints[i] = point;
		}
	}
	
	
	public static class Point{
		public float x;
		public float y;
		
		public Point(float x, float y){
			this.x = x;
			this.y = y;
		}
		
		public Point(){}
	}
	
	public void reloadParameter(float a, float b){
		this.a = a;
		this.b = b;
		invalidate();
	}
}


















