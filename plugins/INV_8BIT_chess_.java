/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins;

import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;



/**
 *
 * @author Daniel Inversini
 */
public class INV_8BIT_chess_ implements PlugInFilter {
    
    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
        
    }
    
    public void run(ImageProcessor ip) {
       // byte[] pixels = (byte[])ip.getPixels();
        int w = ip.getWidth();
        int h = ip.getHeight();
        double wOffset = w/8;
        double hOffset = h/8;
       
        
        //top to bottom
        for(int i = 0; i<w;i++)
        {
            
            for(int j = 0;j<h;j++){
                int k=(int)(i/wOffset);
                int l=(int)(j/hOffset);
                if(((k==0||k==2||k==4||k==6) && ((l==0||l==2||l==4||l==6) ))
                        || ((k==1||k==3||k==5||k==7) && ((l==1||l==3||l==5||l==7) )))
                {
                    ip.set(i,j,(byte)(255-ip.get(i, j)));
                }
                else{
                ip.set(i,j,(byte)ip.get(i,j));        
                }
                
            }
        }
        
        
        /*
        for (int i = 0; i< pixels.length; i++)
        {
            if(i%2 == 0)
            {
                pixels[i] = (byte)(255-pixels[i]);
            }
            }
        }
        */
        
    }
}

    
    
