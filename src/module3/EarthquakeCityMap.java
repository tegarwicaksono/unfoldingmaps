package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	//private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	private String earthquakesURL = "test1.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			//map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.AerialProvider());

			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println("Properties of f" + f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    //TODO: Add code here as appropriate
	    
	    //adding Chile's earthquake as Location
	    //Location valLoc = new Location(-38.14f, -73.03f);	    
	    //Describe properties of Location
	    //PointFeature valEq = new PointFeature(valLoc);
	    //valEq.addProperty("title", "Valdivia, Chile");
	    //valEq.addProperty("magnitude", "9.5");
	    //valEq.addProperty("date", "May 22, 1960");
	    //valEq.addProperty("year", "1960");	    
	    //Marker valMk = new SimplePointMarker(valLoc);
	    //valMk.setColor(color(255,0,0));	    
	    //map.addMarker(valMk);
	    
	    int gray   = color(150,150,150);
	    int red	   = color(255,0,0);
	    int blue   = color(0,0,255);
	    List<Marker> markers = new ArrayList<Marker>();	    	    
	    
	    for (PointFeature eq: earthquakes) {
	    	Marker mk = new SimplePointMarker(eq.getLocation(), eq.getProperties());
	    	float mag = (float) mk.getProperty("magnitude");
	    	if ( mag >= THRESHOLD_MODERATE ) {
	    		mk.setColor(red);
	    		mk.setStrokeColor(red);
	    		mk.setStrokeWeight(10);
	    	}
	    	
	    	else if ( mag >= THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) {
	    		mk.setColor(yellow);
	    		mk.setStrokeColor(yellow);
	    		mk.setStrokeWeight(3);
	    	}
	    	
	    	else {
	    		mk.setColor(blue);
	    		mk.setStrokeColor(blue);
	    		mk.setStrokeWeight(0);
	    	}
	    	markers.add(mk);
	    }
	    
	    
	    map.addMarkers(markers); 
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
	    background(230);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		fill(255);
		rect(20, 50, 160, 400, 5);
		
		fill(0, 102, 153);
		textSize(16);
		text("Earthquake Key", 40, 80); 
		
		fill(255,0,0);
		ellipse(40, 120,15,15);
		
		fill(255,255,0);
		ellipse(40, 150,12,12);
		
		fill(0,0,255);
		ellipse(40, 180,8,8);		

		fill(0, 0, 0);
		textSize(12);
		text(">5.0 Magnitude", 60, 125); 
		
		fill(0, 0, 0);
		textSize(12);
		text("4.0-5.0 Magnitude", 60, 155);
		
		fill(0, 0, 0);
		textSize(12);
		text("<4.0 Magnitude", 60, 185); 
		//fill(0, 102, 153);
		// Remember you can use Processing's graphics methods here
	
	}
}
