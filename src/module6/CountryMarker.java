package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public class CountryMarker extends SimplePointMarker {

	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	protected boolean hidden = true;
	
	public CountryMarker(Location location) {
		super(location);
	}
	
	public CountryMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	public CountryMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	// Common piece of drawing method for markers; 
	// YOU WILL IMPLEMENT. 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	public void draw(PGraphics pg, float x, float y) {
		// For starter code just drawMaker(...)
		if (selected) {
			showTitle(pg, x, y);			
		}
	}

	public String getIndicatorName() {
		return (String)getProperty("IndicatorName");	
	}	
	
	public String getIndicatorValue() {
		return (String)getProperty("Indicator");	
	}

	public String getIndicatorUnit() {
		return (String)getProperty("IndicatorUnit");	
	}
	
	public String getCountryName() {
		return (String)getProperty("country");
	}
	
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCountryName();
		String ind = getIndicatorName() + getIndicatorValue() + " " + getIndicatorUnit();
		pg.pushStyle();
		
		pg.fill(240, 240, 240);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-5-39, Math.max(pg.textWidth(name), pg.textWidth(ind)) + 6, 39);
		//pg.rect(250, 450-5-39, Math.max(pg.textWidth(name), pg.textWidth(ind)) + 6, 39);

		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		//pg.text(name, 250+3, 450-5-33);
		//pg.text(ind, 250+3,450 - 5 -18);
		pg.text(name, x+3, y-5-33);
		pg.text(ind, x+3,y - 5 -18);		
		pg.popStyle();
	}
}
