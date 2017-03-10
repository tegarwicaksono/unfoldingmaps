package module6;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import java.util.ArrayList;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;

import java.util.HashMap;


import de.fhpotsdam.unfolding.marker.Marker;


/**
 * Visualizes life expectancy in different countries. 
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
 */
public class WorldBankMetrics extends PApplet {
	private static final long serialVersionUID = 1L;

	private UnfoldingMap map;
	private String cityFile = "world_capitals_.json";
	
    private List<HashMap<String , Float>> indicatorList  = new ArrayList<HashMap<String,Float>>();
    private HashMap<String, Float> indicatorMap;
    private HashMap<String, String> countryName;
    
    private HashMap<Marker, Marker> country_city_map;
    
	private List<Feature> countries;
	private List<Marker> countryMarkers;
	private List<Marker> cityMarkers;

	int chosenIndicator = 0;
	
	int xbase = 25;
	int ybase = 50;
	
	int metricLE_x1 = xbase+25;
	int metricLE_x2 = metricLE_x1+100;
	
	int metricLE_y1 = ybase+50;
	int metricLE_y2 = metricLE_y1+50;
	
	int metricGG_x1 = xbase+25;
	int metricGG_x2 = metricGG_x1+100;
	
	int metricGG_y1 = ybase+110;
	int metricGG_y2 = metricGG_y1+50;	
	
	int metricGP_x1 = xbase+25;
	int metricGP_x2 = metricGP_x1+100;
	
	int metricGP_y1 = ybase+170;
	int metricGP_y2 = metricGP_y1+50;	

	int metricUE_x1 = xbase+25;
	int metricUE_x2 = metricUE_x1+100;
	
	int metricUE_y1 = ybase+230;
	int metricUE_y2 = metricUE_y1+50;	
	
	private CountryMarker lastSelected;
	private CountryMarker lastClicked;

	public void setup() {
		size(900, 700, OPENGL);
		map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//Load indicators data
		indicatorList.add(ParseFeed.loadCountryEmpty(this, "CountryName_CountryID.csv"));
		indicatorList.add(ParseFeed.loadLifeExpectancyFromCSV(this,"LifeExpectancyWorldBank.csv"));
		indicatorList.add(ParseFeed.loadGDPGrowthFromCSV(this,"GDP_growth.csv"));
		indicatorList.add(ParseFeed.loadGDPCapitaFromCSV(this,"GDP_per_capita.csv"));
		
		countryName = ParseFeed.loadCountryName(this, "CountryName_CountryID.csv");
		indicatorMap = indicatorList.get(chosenIndicator);
		
		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);

		country_city_map = new HashMap<Marker, Marker>();

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();

		for(Feature city : cities) {
			cityMarkers.add(new CountryMarker(city));	  
		}
			
	    map.addMarkers(cityMarkers);
		hideMarkers();
		shadeCountriesDefault();
	}

	public void draw() {
		// Draw map tiles and country markers
		shadeCountries();
		map.draw();
		drawButtons();
	}
	
	private void drawButtons() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);	
		rect(xbase, ybase, 150, 400);
		
		fill(0);
		textAlign(CENTER, CENTER);
		textSize(12);
		text("World Bank Indicators", xbase+75, ybase+25);
		
		
		fill(200,55,100);
		rect(metricLE_x1,metricLE_y1,metricLE_x2-metricLE_x1,metricLE_y2-metricLE_y1);
		
		fill(0);
		textAlign(CENTER, CENTER);
		textSize(12);
		text("Life Expectancy", xbase+75, metricLE_y1 + 10);		
		text("(years)", xbase+75, metricLE_y1+30);				
		
		fill(100,180,75);
		rect(metricGG_x1,metricGG_y1,metricGG_x2-metricGG_x1,metricGG_y2-metricGG_y1);
		
		fill(0);
		textAlign(CENTER, CENTER);
		textSize(12);
		text("GDP Growth", xbase+75, metricGG_y1+10);		
		text("(%)", xbase+75, metricGG_y1+30);		

		fill(240,100,15);
		rect(metricGP_x1,metricGP_y1,metricGP_x2-metricGP_x1,metricGP_y2-metricGP_y1);
		
		fill(0);
		textAlign(CENTER, CENTER);
		textSize(12);
		text("GDP per capita", xbase+75, metricGP_y1+10);		
		text("(US$)", xbase+75, metricGP_y1+30);		
		
		fill(15,100,240);
		rect(metricUE_x1,metricUE_y1,metricUE_x2-metricUE_x1,metricUE_y2-metricUE_y1);
		
		fill(0);
		textAlign(CENTER, CENTER);
		textSize(12);
		text("Unemployment", xbase+75, metricUE_y1+10);		
		text("Rate (%)", xbase+75, metricUE_y1+30);	
	}

	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;	
		}
		selectMarkerIfHover();
	}
	
	private void hideMarkers() {
		for(Marker marker : cityMarkers) {
			marker.setHidden(true);
		}
	}	
	// If there is a marker selected 
	private void selectMarkerIfHover()
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : countryMarkers) {
			if (m.isInside(map,  mouseX, mouseY)) {
				for (Marker capital : cityMarkers) {
					CountryMarker cc = (CountryMarker) capital;
					if (capital.getProperty("countryID").equals(m.getId())) {
						lastSelected = cc;
						capital.setSelected(true);
						//capital.setHidden(false);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void mouseReleased() {
		if (mouseX > metricLE_x1 && mouseX < metricLE_x2 && mouseY > metricLE_y1 && mouseY < metricLE_y2) {
			chosenIndicator = 1;
		} else if ( mouseX > metricGG_x1 && mouseX < metricGG_x2 && mouseY > metricGG_y1 && mouseY < metricGG_y2) {
			chosenIndicator = 2;
		} else if ( mouseX > metricGP_x1 && mouseX < metricGP_x2 && mouseY > metricGP_y1 && mouseY < metricGP_y2) {
			chosenIndicator = 3;
		} else {
			chosenIndicator = 0;
		}
		indicatorMap = indicatorList.get(chosenIndicator);			
	}

	
	
	private void shadeCountries() {
		
		if (chosenIndicator == 1) {
			shadeCountriesLE();
		} else if (chosenIndicator == 2) {
			shadeCountriesGG();
		} else if (chosenIndicator == 3) {
			shadeCountriesGP();
		} else {
			shadeCountriesDefault();
		}
	}
	//Helper method to color each country based on life expectancy
	//Red-orange indicates low (near 40)
	//Blue indicates high (near 100)
	
	private void shadeCountriesDefault() {
		for (Marker marker : countryMarkers) {
			marker.setColor(color(255,255,255));
			for (Marker capital : cityMarkers) {
				if (capital.getProperty("countryID").equals(marker.getId())) {
					capital.setProperty("IndicatorName", " ");
					capital.setProperty("Indicator", " ");
					capital.setProperty("IndicatorUnit", " ");
				}
			}		
		}		
	}
	
	private void shadeCountriesLE() {		
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			if (indicatorMap.containsKey(countryId)) {
				float indicatorVal = indicatorMap.get(countryId);
				int colorLevel = (int) map(indicatorVal, 40, 90, 0, 255);
				marker.setColor(color(colorLevel, 255-colorLevel, 100));
				
				for (Marker capital : cityMarkers) {
					if (capital.getProperty("countryID").equals(marker.getId())) {
						capital.setProperty("IndicatorName", "Life Expectancy: ");
						capital.setProperty("Indicator", Float.toString(indicatorVal));
						capital.setProperty("IndicatorUnit", "years");
						break;
					}
				}		
			}
			else {
				marker.setColor(color(255,255,255));
			}
		}
	}
	
	private void shadeCountriesGG() {		
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			if (indicatorMap.containsKey(countryId)) {
				float indicatorVal = indicatorMap.get(countryId);
				// Encode value as brightness (values range: 40-90)
				int colorLevel = (int) map(indicatorVal, -60, 40, 0, 255);
				marker.setColor(color(100, colorLevel, 255-colorLevel));
				for (Marker capital : cityMarkers) {
					if (capital.getProperty("countryID").equals(marker.getId())) {
						capital.setProperty("IndicatorName", "GDP Growth: ");
						capital.setProperty("Indicator", Float.toString(indicatorVal));
						capital.setProperty("IndicatorUnit", "%");
						break;
					}
				}
			}
			else {
				marker.setColor(color(255,255,255));
			}
		}
	}

	private void shadeCountriesGP() {
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			if (indicatorMap.containsKey(countryId)) {
				float indicatorVal = indicatorMap.get(countryId);
				// Encode value as brightness (values range: 40-90)
				int colorLevel = (int) map(indicatorVal, 250, 100000, 0, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
				for (Marker capital : cityMarkers) {
					if (capital.getProperty("countryID").equals(marker.getId())) {
						capital.setProperty("IndicatorName", "GDP Per Capita: ");
						capital.setProperty("Indicator", Float.toString(indicatorVal));
						capital.setProperty("IndicatorUnit", "US$");
						break;
					}
				}	
			}
			else {
				marker.setColor(color(255,255,255));
			}
		}
	}
}
