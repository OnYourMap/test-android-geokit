package com.oym.android.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.speech.RecognizerIntent;
//import android.os.Bundle;
//import static com.oym.android.Position.MERCATOR_DEGREE;
import com.oym.android.*;
//import com.oym.android.R;
import com.oym.android.api.*;
import com.oym.android.overlay.*;

public class DemoActivity extends Activity {
	
	private final ResponseListener responseListener = new TestResponseListener();
	private final EventListener eventListener = new TestEventListener();
	public GeoKit gk;
	private android.os.Handler mHandler = new android.os.Handler();
	private MappingState leaveState;
	
//	public static RouteResponse aRouteResponse;
	private com.oym.android.overlay.Annotation locOverlay;
//	private com.oym.android.overlay.Annotation locOverlay2;
   
	private MappingState testMapState = null;
	
	private String ID = "" + System.currentTimeMillis();
	
//	protected void onNewIntent(android.content.Intent intent) {
//		String mapStateString = (String) intent.getExtras().get("map_state");
//		String[] tokens = mapStateString.split(";");
//		System.err.println("restoring state");
//		gk.setCenterAndZoomLevel(new Position(Position.WGS84, Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3])), Integer.parseInt(tokens[1]));
//	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		System.err.println("Activity " + ID + " onCreate() [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onCreate(savedInstanceState);
        
		this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		
		final android.widget.ImageButton zoominButton = (android.widget.ImageButton) findViewById(R.id.imagebutton_zoomin);
		zoominButton.bringToFront();
		zoominButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
            	gk.zoomIn(null, true);
//	            	state = new com.oym.android.MappingState();
//	            	state.setBounds(new Box(new Position(Position.MERCATOR_DEGREE, -5, 58), new Position(Position.MERCATOR_DEGREE, 5, 62)));
//	            	gk.setState(state);
//	            	gk.moveDirection(100, 0, true);
//	            	gk.moveTo(new Position(Position.MERCATOR_DEGREE, -5, 58), true);
            } 
        });
		final android.widget.ImageButton zoomoutButton = (android.widget.ImageButton) findViewById(R.id.imagebutton_zoomout);
		zoomoutButton.bringToFront();
		zoomoutButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
            	gk.zoomOut(null, true);
//	            	state = gk.getState();
//	            	gk.moveDirection(-100, 0, true);
//	            	gk.moveTo(new Position(Position.MERCATOR_DEGREE, 0, 0), true);
            }
        });
	}

	protected void onStart() {
		System.err.println("Activity " + ID + " onStart() [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onStart();
		
		gk = new com.oym.android.GeoKit(this, mHandler);
		gk.addEventListener(eventListener);
		gk.attachToView((GeoKitView)findViewById(R.id.view_map));
		
		gk.clearOverlays();

		//double x = -0.127125f;
		//double y = 60.289085f;
		//int z = 2;
		double x = 6.54;
		double y = 46.55;
		int z = 10;
		if (leaveState != null) {
			// restore map state after onStop(). 
			// note overlays must be recreated here, theirs info should have been saved inside onStop() method. (not done in this demo app)
			gk.setState(leaveState);
			
		} else {
			//gk.setCenterAndZoomLevel(new Position(Position.MERCATOR_DEGREE, x, y), z, false);
			gk.setCenterAndZoomLevel(new Position(Position.WGS84, x, y), z, false);
		}
		
		/*locOverlay = new com.oym.android.overlay.Annotation(
				new Position(Position.WGS84, x, y),
				new com.oym.android.Point(0,17), 
				false, 
				"annotation_end", 
				"",
				android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.flag), getApplicationContext()
		);*/
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup)this.findViewById(R.id.view_map);
	    View popupView = layoutInflater.inflate(R.layout.task_marker, parent, false);
	    //parent.addView(popupView);
	    //View testView = new View(getApplicationContext());
	    // Create a LinearLayout element
	    LinearLayout ll = new LinearLayout(getApplicationContext());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    

	    // Add text
	    TextView tv = new TextView(getApplicationContext());
	    tv.setText("my text");
	    tv.setTextColor(Color.RED);
	    tv.setBackgroundColor(Color.WHITE);
	    ll.addView(tv);
	    
	    View v = (View)ll;
	    //v.setBackgroundColor(0xFFFFFF);
	    v.setLayoutParams(new ViewGroup.LayoutParams(
	            ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT));
	    
	    //View myView = new View(getApplicationContext());
	    View myView = (View)ll;
	    myView.setBackgroundColor(Color.RED);
	    myView.setLayoutParams(new LayoutParams(100, 50));
	    
	    View tmv = new TaskMarker(getApplicationContext());
	    
	    View theView = tmv;
	    parent.addView(theView);
	    
		locOverlay = new com.oym.android.overlay.Annotation(
				new Position(Position.WGS84, x, y),
				new com.oym.android.Point(0,17), 
				false, 
				"annotation_end", 
				"",
				theView, 100, 100, 
				getApplicationContext()
		);
		gk.addOverlay(locOverlay, 1);
		
		final RouteRequest routeRequest = new RouteRequest();
		routeRequest.setStart(new Position(Position.MERCATOR_DEGREE, -0.127125f, 60.289085f));
		routeRequest.setEnd(new Position(Position.MERCATOR_DEGREE, 0.086315f, 60.409785f));
		routeRequest.setTransportMode(RouteRequest.TM_FASTEST_CAR);
	/*	mHandler.post(new Runnable() {
			public void run() {
				try {
					gk.route(routeRequest, responseListener);
				} catch (Exception ex) {
				}
			}
		});*/
		
		// add a pulsing circle at start location
		final java.util.Properties circleProps = new java.util.Properties();
		circleProps.put(Circle.STROKE_COLOR, new Integer(android.graphics.Color.argb(180, 255, 0, 255)));
		circleProps.put(Circle.FILL_COLOR, new Integer(android.graphics.Color.argb(80, 255, 0, 255)));
		circleProps.put(Circle.PULSE_DURATION, new Integer(20000));
		
	//	gk.addOverlay(new Circle(new Position(Position.MERCATOR_DEGREE, x, y), 1000f , false, circleProps), 2);
		gk.refresh();
		
	}
	
	protected void onResume() {
		System.err.println("Activity " + ID + " onResume() [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onResume();
		gk.resume();
		// no need to restore map state, done internally by the sdk. 
		// note: resources MUST be still available, meaning no Bitmap.recycle() & co. on images/view added as overlays to the map.
	}
	
	protected void onPause() {
		System.err.println("Activity onPause() " + ID + " [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onPause();
		gk.pause();
		testMapState = gk.getState();
		// no need to save map state, done internally by the sdk
	}
	
	protected void onStop() {
		System.err.println("Activity onStop() " + ID + " [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onStop(); 
		// must save state of the map but also all info about overlays, for a possible restore when onStart() is called (not done in this demo app)
		leaveState = gk.getState();
		gk.stop();
		
	}
	
	protected void onDestroy() {
		System.err.println("Activity onDestroy() " + ID + " [native mem] total: " + Debug.getNativeHeapSize() + ", free: " + Debug.getNativeHeapFreeSize() + ", allocated: " + Debug.getNativeHeapAllocatedSize());
		super.onDestroy();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.demo_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
	    switch (item.getItemId()) {
	    
		    case R.id.map_type_plan:
	    		startActivity(new android.content.Intent(getApplicationContext(), DemoActivity.class));
//		        gk.setMapType(com.oym.android.MapType.PLAN);
		        return true;
		        
		    case R.id.map_type_sat:
		        //gk.pause();

		    	//gk.setMapType(com.oym.android.MapType.SATELLITE);
		    	final Position p = gk.getCenter();
		    	final double newX = p.getMercatorDegreeX() + (5 * Math.random()) * (Math.random() > .5 ? 1 : -1);
		    	final double newY = p.getMercatorDegreeY() + (5 * Math.random()) * (Math.random() > .5 ? 1 : -1);
		    	final int newZ = gk.getZoomLevel() + 1;
		    	gk.setCenterAndZoomLevel(new Position(Position.MERCATOR_DEGREE, newX, newY), newZ, true);
		    	
		        return true;

		    case R.id.map_type_hybrid:
		    	//gk.resume();
		       // gk.setMapType(com.oym.android.MapType.HYBRID);
		        return true;
 
		    case R.id.route_planner:
//		    	if (aRouteResponse != null) {
//		    		startActivity(new android.content.Intent(getApplicationContext(), DemoActivity.class));
//		    	}

		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	




	private class TestEventListener implements com.oym.android.EventListener {

		public void onMapMoved(Position oldPosition, Position newPosition) {
			//System.err.println("onMapMoved");
		}

		public void onMapZoomed(double oldZoomLevel, double newZoomLevel) {
			//System.err.println("onMapZoomed");
		}


		public void onMapDoubleTap(Position position, Overlay overlay, double distance) {
			gk.zoomIn(gk.toPixels(position), true);

			if (overlay instanceof Annotation) {
				System.err.println("double tap on overlay: " + ((Annotation) overlay).infoTitle() + " @ distance: " + distance);
			} else {
//				System.err.println("onMapDoubleTap");
			}
		}

		public void onMapLongPress(Position position, Overlay overlay, double distance) {
			if (overlay instanceof Annotation) {
				System.err.println("long press on overlay: " + ((Annotation) overlay).infoTitle() + " @ distance: " + distance);
			} else {
				System.err.println("onMapLongPress");
			}
		}

		public void onMapSingleTap(Position position, Overlay overlay, double distance) {
			if (overlay instanceof Annotation) {
				Point tapPoint = gk.toPixels(position);
				Point overlayPoint = gk.toPixels(overlay.getPosition());
				// center point of the overlay (add anchor distance)
				int pcx = overlayPoint.getX() + overlay.getAnchor().getX();
				int pcy = overlayPoint.getY() - overlay.getAnchor().getY();
				
				// tap point relative to the overlay
				int tapX = overlay.getView().getWidth() / 2 + tapPoint.getX() - pcx;
				int tapY = overlay.getView().getHeight() / 2 + tapPoint.getY() - pcy;
				System.err.println("single tap on overlay: " + ((Annotation) overlay).infoTitle() + " @ distance: " + distance);
				System.err.println("tap pixel coordinate relative to overlay: " + tapX + " " + tapY);
			} else {
				System.err.println("onMapSingleTap");
			}
		}

		
	}
	
	private class TestResponseListener implements ResponseListener {

		public void onGeocodeResponse(GeocodeResponse geocodeResponse) {
			System.err.println("onGeocodeResponse");
			if (geocodeResponse != null && !geocodeResponse.getGeocodedAddresses().isEmpty()) {
				// get first address
				Address addr = geocodeResponse.getGeocodedAddresses().get(0);
				gk.setCenterAndZoomLevel(addr.getPosition(), 13, false);
				locOverlay.getPosition().update(Position.MERCATOR_DEGREE, addr.getPosition().getMercatorDegreeX(), addr.getPosition().getMercatorDegreeY());
				gk.refresh();
			}
		}  

		public void onRouteResponse(final RouteResponse routeResponse) {
			try {
				if (routeResponse != null) {
				//	aRouteResponse = routeResponse;
					System.err.println("got route response...");
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
	
					for (int i = 1; i < routeResponse.getRoute().getInstructions().size() - 1; i++) {
						RouteInstruction ri = routeResponse.getRoute().getInstructions().get(i);
						final com.oym.android.overlay.Annotation maneuverOverlay = new com.oym.android.overlay.Annotation(
								ri.getPosition(),
								new com.oym.android.Point(0,17), 
								false, 
								"maneuver_" + i, 
								"",
								android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.milestone_icon), getApplicationContext()
						);
						gk.addOverlay(maneuverOverlay, (i+1));
	
					}
					
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
					
					// search for pois
					final POISearchRequest poiSearchRequest = new POISearchRequest();
					final java.util.List<String> attributeNames = new java.util.ArrayList<String>();
					attributeNames.add("BUSINESS_NAME");
					poiSearchRequest.setAttributeNames(attributeNames);
					poiSearchRequest.setPoint1(routeBounds.getNorthEast());
					poiSearchRequest.setPoint2(routeBounds.getSouthWest());
					poiSearchRequest.setSearchExpression("(BUSINESS_NAME_NORM==THEATRE)");
					poiSearchRequest.setBucketStart(0);
					poiSearchRequest.setBucketEnd(100);
	
					mHandler.post(new Runnable() {
						public void run() {
							try {
								gk.poiSearch(poiSearchRequest, responseListener);
							} catch (Exception ex) {
							}
						}
					});
				}
			} catch (Exception ex) {
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
			try {
				if (response.getErrorCode() == null) {
					
					android.graphics.Bitmap poiAnnotationBitmap = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.flag_gold);
	
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
					//poiAnnotationBitmap.recycle();
					//poiAnnotationBitmap = null;
				}
				gk.refresh();
			} catch (Exception ex) {
			}
		}
	}

}
