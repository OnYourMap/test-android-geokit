package com.oym.android.demo;

import com.oym.android.api.*;

import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RoutePlannerActivity extends android.app.Activity {

	private android.widget.LinearLayout main;
	private android.widget.ScrollView scrollView;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		scrollView = new android.widget.ScrollView(this);
		scrollView.setBackgroundColor(android.graphics.Color.BLACK);
		scrollView.setFillViewport(true);
		
		main = new android.widget.LinearLayout(this);
		main.setOrientation(android.widget.LinearLayout.VERTICAL);
		main.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		scrollView.addView(main);
		setContentView(scrollView);
		
		
		System.err.println("route planner panel");
	}
	
	public void onStart() {
		super.onStart();
	/*	if (DemoActivity.aRouteResponse != null) {
			int i = 1;
			for (RouteInstruction rInstruction : DemoActivity.aRouteResponse.getRoute().getInstructions()) {
				final String renderedInstruction = RouteUtility.render(rInstruction, getResources());
				final TextView textView = new android.widget.TextView(this);
				textView.setText(android.text.Html.fromHtml((i++) + ". " + renderedInstruction));
				textView.setTextSize(16f);
				//textView.setLayoutParams(new LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				main.addView(textView);
				
			}
		}*/
	}
}
