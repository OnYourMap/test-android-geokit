package com.oym.android.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

public class TaskMarker extends View {

	public TaskMarker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint rectanglePaint = new Paint();
		rectanglePaint.setStyle(Paint.Style.FILL);
		rectanglePaint.setARGB(160, 255, 74, 18);
		rectanglePaint.setAntiAlias(true);
		RectF rect = new RectF(0, 0, 80, 40);
		canvas.drawRoundRect(rect, 10, 10, rectanglePaint);
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(20);
		textPaint.setAntiAlias(true);
		canvas.drawText("1014", 10, 25, textPaint);
		
		Paint paintOuter = new Paint();
		paintOuter.setStyle(Paint.Style.FILL);
		paintOuter.setColor(Color.WHITE);
		paintOuter.setAntiAlias(true);
		canvas.drawCircle(80, 20, 20, paintOuter);
		Paint paintInner = new Paint();
		paintInner.setStyle(Paint.Style.FILL);
		paintInner.setARGB(255, 255, 74, 18);
		paintInner.setAntiAlias(true);
		canvas.drawCircle(80, 20, 17, paintInner);
		
		
		Paint p = new Paint();
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_corner_marker);
        p.setColor(Color.RED);
        canvas.drawBitmap(b, 47, 35, p);
	}

}
