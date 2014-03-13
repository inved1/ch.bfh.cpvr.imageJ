/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins;

import java.awt.Color;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.PNG_Writer;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
/**
 *
 * @author Daniel Inversini
 */
public class INV_DEBAYER_ implements PlugInFilter {
    
    public int setup(String arg,ImagePlus imp){
        return DOES_8G;
    }

    public void run(ImageProcessor ip1)
    { 
        int w1 = ip1.getWidth();
        int h1 = ip1.getHeight();
        byte[] pix1 = (byte[]) ip1.getPixels();
        ImagePlus imgGray = NewImage. createByteImage("GrayDeBayered", w1/2, h1/2,1, NewImage. FILL_BLACK);

        ImageProcessor ipGray = imgGray.getProcessor();

        byte[] pixGray = (byte[]) ipGray.getPixels();
        int w2 = ipGray.getWidth();
        int h2 = ipGray.getHeight();
        
        ImagePlus imgRGB = NewImage. createRGBImage("RGBDeBayered", w1/2, h1/2,1, NewImage. FILL_BLACK);
        ImageProcessor ipRGB = imgRGB.getProcessor();
        
        int[] pixRGB = (int[]) ipRGB.getPixels();
        long msStart = System.currentTimeMillis();
        ImagePlus imgHue = NewImage. createByteImage("Hue", w1/2, h1/2,1, NewImage. FILL_BLACK);
        ImageProcessor ipHue = imgHue.getProcessor();
        
        byte[] pixHue = (byte[]) ipHue.getPixels();
        int i1 = 0, i2 = 0;

        
        
        /*
        red1   | green 2
        ----------------
        green3 | blue 4
        
        so red = x;y
        green = (x+1);y + (y+1);x
        blue = x+1;y+1
        
        */
        int r =0, g=0, b=0;
        
        for(int x=0;x<w1;x=x+2)
        {
            for(int y=0;y<h1;y=y+2)
            {
                
                r = ip1.get(x, y+1);
                g = (ip1.get(x,y)+ip1.get(x+1,y+1))/2;
                b = ip1.get(x+1,y);
               
                //float[] hsb = Color.RGBtoHSB(r, g, b, null);
                
                ipRGB.set(i1, i2, (int) ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff));
                i2 += 1;
            }
            i2 = 0;
            i1 += 1;
        }
        
        ImagePlus ip = new ImagePlus("",ipRGB);
                
        ImageConverter con = new ImageConverter(ip);
        
        
        con.convertToHSB();
       
        ip.show();
    
    
        long ms = System.currentTimeMillis() - msStart;
    
        System.out.println(ms);
    
        PNG_Writer png = new PNG_Writer();
    
        try
        { 
            
            png.writeImage(imgRGB , "Billard1024x544x3.png" , 0);
            png.writeImage(imgHue, "Billard1024x544x1H.png" , 0);
            png.writeImage(imgGray, "Billard1024x544x1B.png" , 0);
        } 
        catch (Exception e)
        { 
            e.printStackTrace();
        }
    
        imgGray.show();
        imgGray.updateAndDraw();
        imgRGB.show(); 
        imgRGB.updateAndDraw();
        imgHue.show(); 
        imgHue.updateAndDraw();
}    
    
    public static void main(String[] args){
        INV_DEBAYER_ plugin = new INV_DEBAYER_();
        ImagePlus im = new ImagePlus("../../Images/Billard2048x1088x1.png" );
        im.show();
        plugin.setup("", im);
        plugin.run(im.getProcessor());
    }

  
    
}
