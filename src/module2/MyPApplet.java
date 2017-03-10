package module2;

import processing.core.*;

public class MyPApplet extends PApplet{
	private String URL = "http://cseweb.ucsd.edu/~minnes/palmTrees.jpg";
	private PImage backgroundImg;
	
	//setup() is only run once
	public void setup() {
		size(200,200);
		//color of the background (set to white)
		background(255, 255, 255);
		
		backgroundImg = loadImage(URL, "jpg");
		//backgroundImg.resize(50,  100);
		//above command didn't preserve aspect ratio
		
		backgroundImg.resize(0, height);
		//above is the command to adjust the img size
		//to have an aspect ratio that is equal to 
		//the original image
		
		image(backgroundImg,0,0);  //display at 0,0
		
	}
	
	//draw() is called multiple times
	public void draw() {
		//calculate color code for sun
		//second() is a built-in method using System.clock;
		//there's also minute(), or hour();
		int[] color = sunColorSec(second());
		
		fill(color[0],color[1],color[2]);
		
		//ellipse(a,b,c,d) where a,b are the center of ellipse
		//c is the width of ellipse, d is the height of ellipse

		//the variable width, height below refer to 
		//the height and width of the window
		ellipse(width/4, height/5, width/5, height/5);
	}
	
	public int[] sunColorSec(float seconds) {
		int[] rgb = new int[3];
		
		//scale the brightness of the yellow based on the seconds.
		//30 seconds is black. 0 seconds is bright value;
		
		float difffrom30 = Math.abs(30-seconds);
		
		float ratio = difffrom30 / 30;
		
		rgb[0] = (int)(255*ratio);
		rgb[1] = (int)(255*ratio);
		rgb[2] = 0;
		
		//System.out.println("R "+ rgb[0] + " G " + rgb[1] + " B " + rgb[2]);
		return rgb;
	}
	
}
