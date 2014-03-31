package com.oym.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent; //import android.os.Bundle;
import android.view.MotionEvent;
//import static com.oym.android.Position.MERCATOR_DEGREE;
import com.oym.android.*;
//import com.oym.android.R;
import com.oym.android.api.*;
import com.oym.android.overlay.*;

public class TestActivity extends Activity {

	public GeoKit gk;
	private android.os.Handler mHandler = new android.os.Handler();
	public android.graphics.Bitmap poiAnnotationBitmap;

	protected void onResume() {
		super.onResume();
		stop = false;
		System.err.println("Activity onResume()");
	}

	protected void onPause() {
		gk.pause();
		super.onPause();
		stop = true;
		System.err.println("Activity onPause()");
	}

	protected void onStop() {
		super.onStop();
		gk.stop();
		stop = true;
		System.err.println("Activity onStop()");
	}

	protected void onDestroy() {
		super.onDestroy();
		stop = true;
		System.err.println("Activity onDestroy()");
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
				android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.test);

		gk = new com.oym.android.GeoKit(this, mHandler);

		// poiAnnotationBitmap =
		// android.graphics.BitmapFactory.decodeResource(getResources(),
		// R.drawable.pin);

	}

	protected void onStart() {
		super.onStart();
		gk.attachToView((GeoKitView) findViewById(R.id.view_map));
		gk.addEventListener(new TestEventListener());
		final double x = -0.127125f;
		final double y = 60.289085f;
		final int z = 13;
		gk.setCenterAndZoomLevel(new Position(Position.MERCATOR_DEGREE, x, y), z, false);

		RouteRequest routeRequest = new RouteRequest();
		
		routeRequest.setStart(new Position(Position.MERCATOR_DEGREE, -2.102680, 69.977950));
		routeRequest.setEnd(new Position(Position.MERCATOR_DEGREE, -9.150193, 42.063264));

		
		routeRequest.setTransportMode(RouteRequest.TM_FASTEST_CAR);
		gk.route(routeRequest, new TestResponseListener());

		
		startTests(600);
	}

	private boolean stop = false;

	private void startTests(int seconds) {

		final long end = System.currentTimeMillis() + seconds * 1000;

		Thread mThread = new Thread() {
			
			public void run() {

				while (!stop) {
					if (gk.getGeoKitContext().isMapReady) {
						try {
							if (System.currentTimeMillis() > end)
								break;
							nextAction();
							Thread.sleep(1000);

						} catch (Exception ex) {
						}
					} else {
						System.err.println("waiting for GeoKit to be ready...");
						try {
							Thread.sleep(1000);
						} catch (Exception ex) {
						}

					}
				}

			}
		};
		mThread.setDaemon(true);
		mThread.setPriority(Thread.MIN_PRIORITY);
		mThread.start();
	}

	private final int ACTION_PAN = 0;
	private final int ACTION_DOUBLETAP = 1;
	private final int ACTION_MAPTYPE = 2;
	private final int ACTION_ROTATE = 3;
	private final int ACTION_SETCENTER = 4;
	private final int ACTION_SETZOOM = 5;
	private final int ACTION_ADD_OVERLAYS = 6;
	private final int ACTION_CLEAR_OVERLAYS = 7;

	private final Position centerPos = new Position(Position.MERCATOR_DEGREE, 0, 0);
	private final java.util.List<Overlay> pinOverlays = new java.util.ArrayList<Overlay>();

	private void nextAction() {
		final int action = (int) (Math.random() * 8f);
		switch (action) {
		case ACTION_PAN:
			final int fromX = (int) (Math.random() * 1f * gk.getGeoKitContext().width);
			final int fromY = (int) (Math.random() * 1f * gk.getGeoKitContext().height);
			final int toX = (int) (Math.random() * 1f * gk.getGeoKitContext().width);
			final int toY = (int) (Math.random() * 1f * gk.getGeoKitContext().height);
			final int stepCount = (int) (Math.random() * 100f);
			System.err.println("action pan: fromX=" + fromX + " fromY=" + fromY + " toX=" + toX + " toY=" + toY + " stepCount=" + stepCount);

			long downTime = android.os.SystemClock.uptimeMillis();
			long eventTime = android.os.SystemClock.uptimeMillis();
			float y = fromY;
			float x = fromX;
			float yStep = (toY - fromY) / stepCount;
			float xStep = (toX - fromX) / stepCount;
			android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, fromX, y, 0);
			dispatchTouchEvent(event);

			for (int i = 0; i < stepCount; ++i) {
				y += yStep;
				x += xStep;
				eventTime = android.os.SystemClock.uptimeMillis();
				event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
				dispatchTouchEvent(event);
			}
			eventTime = android.os.SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, fromX, y, 0);
			dispatchTouchEvent(event);
			break;

		case ACTION_DOUBLETAP:
			final int dt_fromX = (int) (Math.random() * 1f * gk.getGeoKitContext().width);
			final int dt_fromY = (int) (Math.random() * 1f * gk.getGeoKitContext().height);
			downTime = android.os.SystemClock.uptimeMillis();
			eventTime = downTime;
			event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, dt_fromX, dt_fromY, 0);
			dispatchTouchEvent(event);
			eventTime = downTime;
			event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, dt_fromX, dt_fromY, 0);
			dispatchTouchEvent(event);
			
			downTime += 2;
			event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, dt_fromX, dt_fromY, 0);
			dispatchTouchEvent(event);
			eventTime = downTime + 2;
			event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, dt_fromX, dt_fromY, 0);
			dispatchTouchEvent(event);
			
			System.err.println("action doubletap: x=" + dt_fromX + " y=" + dt_fromY);
			break;
			
		case ACTION_MAPTYPE:
			final int mapType = (int) (Math.random() * 3f);
			System.err.println("action change map type: maptype=" + mapType);
			gk.setMapType(mapType);

			break;
		case ACTION_ROTATE:
			try {
				final float angle = (float) (Math.random() * 360f);
				gk.rotate(angle, true);
				Thread.sleep(400);
				// restore
				gk.rotate(0f, false);
				System.err.println("action rotate: angle=" + angle);
				
			} catch (Exception ex) {
			}
			break;

		case ACTION_SETCENTER:
			final double cx = Math.random() * 3f * (Math.random() > .5f ? 1 : -1); // +/-
			final double cy = 60f + Math.random() * 3f * (Math.random() > .5f ? 1 : -1);
			final int zoom = gk.getZoomLevel();
			centerPos.update(Position.MERCATOR_DEGREE, cx, cy);
			System.err.println("action set center: x=" + cx + ", y=" + cy + ", z=" + zoom);
			gk.setCenterAndZoomLevel(centerPos, zoom, false);
			break;

		case ACTION_SETZOOM:
			final int z = (int) (Math.random() * 16f);
			System.err.println("action setzoom: z=" + z);
			gk.setZoomLevel(z, true);
			break;
			
			
		case ACTION_ADD_OVERLAYS:
			final Box box = gk.getBounds();
			final int pinCount = (int) (Math.random() * 100f);
			for (int i = 0; i < pinCount; i++) {
				final double pinX = box.getSouthWest().getMercatorDegreeX() + Math.random() * (box.getNorthEast().getMercatorDegreeX() - box.getSouthWest().getMercatorDegreeX());
				final double pinY = box.getSouthWest().getMercatorDegreeY() + Math.random() * (box.getNorthEast().getMercatorDegreeY() - box.getSouthWest().getMercatorDegreeY());
				
				final Annotation overlay = new com.oym.android.overlay.Annotation(
						new Position(Position.MERCATOR_DEGREE, pinX, pinY),
						new com.oym.android.Point(0,17), 
						false, 
						"annotation_" + i, 
						"",
						android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.flag), getApplicationContext()
				);
				gk.addOverlay(overlay, i);
				pinOverlays.add(overlay);
			}
			System.err.println("action add overlays: count=" + pinCount);
			break;

		case ACTION_CLEAR_OVERLAYS:
			for (Overlay ov : pinOverlays) {
				gk.removeOverlay(ov);
			}
			pinOverlays.clear();
			//gk.clearOverlays();				
			System.err.println("action clear overlays:");

		default:
			break;
		}

		// - Simulation d'un double-touch (zoom in jusqu'au niveau de zoom
		// suivant)
		// - "Positionnement": alternance entre les 2 points suivants
	}

	
	private class TestEventListener implements com.oym.android.EventListener {

		public void onMapMoved(Position oldPosition, Position newPosition) {
		}

		public void onMapZoomed(double oldZoomLevel, double newZoomLevel) {
		}

		public void onMapDoubleTap(Position position, Overlay overlay, double distance) {
			gk.zoomIn(gk.toPixels(position), true);
		}

		public void onMapLongPress(Position position, Overlay overlay, double distance) {
		}

		public void onMapSingleTap(Position position, Overlay overlay, double distance) {
		}
	}
	
	private class TestResponseListener implements ResponseListener {

		public void onGeocodeResponse(GeocodeResponse geocodeResponse) {
		}  

		public void onRouteResponse(RouteResponse routeResponse) {
			if (routeResponse != null) {
				final java.util.Properties props = new java.util.Properties();
				props.put(Polyline.STROKE_COLOR, new Integer(android.graphics.Color.argb(100, 0, 0, 255)));
				props.put(Polyline.STROKE_WIDTH, new Integer(6));
				
				gk.addOverlay(new Polyline(routeResponse.getRoute(), props), 1);
				
				// start marker
				final RouteInstruction startInstruction = routeResponse.getRoute().getInstructions().get(0);
				final com.oym.android.overlay.Annotation startOverlay = new com.oym.android.overlay.Annotation(
						startInstruction.getPosition(),
						new com.oym.android.Point(0,17), 
						false, 
						"annotation_start", 
						"",
						android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.route_start), getApplicationContext()
				);
				gk.addOverlay(startOverlay, 1);
				 
				// end marker
				final RouteInstruction endInstruction = routeResponse.getRoute().getInstructions().get(routeResponse.getRoute().getInstructions().size() - 1);
				final com.oym.android.overlay.Annotation endOverlay = new com.oym.android.overlay.Annotation(
						endInstruction.getPosition(),
						new com.oym.android.Point(0,17), 
						false, 
						"annotation_end", 
						"",
						android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.route_end), getApplicationContext()
				);
				gk.addOverlay(endOverlay, 1);

				// center map on route
				final Box routeBounds = routeResponse.getRoute().getBounds();
				final int routeOverviewZL = gk.getBoundsZoomLevel(routeBounds);
				final Position routeCenter = new Position(Position.WGS84, 
						routeBounds.getSouthWest().getWGS84Longitude() + (routeBounds.getNorthEast().getWGS84Longitude() - routeBounds.getSouthWest().getWGS84Longitude()) / 2f,  
						routeBounds.getSouthWest().getWGS84Latitude() + (routeBounds.getNorthEast().getWGS84Latitude() - routeBounds.getSouthWest().getWGS84Latitude()) / 2f
				);
				
				//gk.setCenterAndZoomLevel(routeCenter, routeOverviewZL);
				
				// add a pulsing circle at start location
				final java.util.Properties circleProps = new java.util.Properties();
				circleProps.put(Circle.STROKE_COLOR, new Integer(android.graphics.Color.argb(180, 255, 0, 255)));
				circleProps.put(Circle.FILL_COLOR, new Integer(android.graphics.Color.argb(80, 255, 0, 255)));
				circleProps.put(Circle.PULSE_DURATION, new Integer(20000));
				
				gk.addOverlay(new Circle(startInstruction.getPosition(), 1000f , false, circleProps), 2);
				gk.refresh();
			}
		}
 
		@Override
		public void onReverseGeocodeResponse(ReverseGeocodeResponse response) {
		}

		@Override
		public void onAutoCompleteResponse(AutoCompleteResponse response) {
		}

		@Override
		public void onPOIFullDataResponse(POIFullDataResponse response) {
		}

		@Override
		public void onPOIPartialDataResponse(POIPartialDataResponse response) {
		}

		@Override
		public void onPOISearchResponse(POISearchResponse response) {
			if (response.getErrorCode() == null) { 
				int zIndex = 10;
				for (POI poi : response.getPois()) {
					final Position pos = poi.getPosition();
					final String businessName = poi.getAttributes().get(0).getValue();
					final Annotation poiAnnotation = new Annotation(pos, 
						new com.oym.android.Point(0,17), 
						false, 
						businessName, 
						"",
						poiAnnotationBitmap, 
						getApplicationContext()
					);

					gk.addOverlay(poiAnnotation, zIndex++);
				};
			}
			gk.refresh();
		}
	}
}
