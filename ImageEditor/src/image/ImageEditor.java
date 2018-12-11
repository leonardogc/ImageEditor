package image;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class ImageEditor {

	public static void main(String[] args) throws IOException {
		BufferedImage img = ImageEditor.loadImage("C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\panda.jpg");
		
		BufferedImage newImg = ImageEditor.reduceSize(img,img.getWidth()/200);

		/*for(int i = 0; i < 256; i++) { 
			BufferedImage newImg2 = ImageEditor.contrast(newImg, i);
			ImageEditor.saveImage(newImg2, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\contrast\\img"+i+".png");
		}*/
		
		newImg = ImageEditor.contrast(newImg, 40);
		ImageEditor.toText(newImg, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\img.txt");
		
	}
	
	public static void toText(BufferedImage image, String path) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(path);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        
        int w = image.getWidth();
		int h = image.getHeight();
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				int pixel = image.getRGB(width, height);
				
			    int red = (pixel >> 16) & 0xff;
			    int green = (pixel >> 8) & 0xff;
			    int blue = (pixel) & 0xff;
			    
			    int avg = (red+green+blue)/3;
			    
			    if(avg >= 127) {
			    	bufferedWriter.write("@");
			    }
			    else {
			    	bufferedWriter.write(" ");
			    }
			    
			    bufferedWriter.write(" ");
			}
			bufferedWriter.write("\n");
		}
		
		bufferedWriter.close();
	}
	
	public static BufferedImage reduceSize(BufferedImage image, int factor) {
		int w = image.getWidth()/factor;
		int h = image.getHeight()/factor;
		
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				
				int alpha = 0;
			    int red = 0;
			    int green = 0;
			    int blue = 0;
				
				for(int fh = 0; fh < factor; fh++) {
					for(int fw = 0; fw < factor; fw++) {
						int pixel = image.getRGB(width*factor+fw, height*factor+fh);
						
						alpha += (pixel >> 24) & 0xff;
					    red += (pixel >> 16) & 0xff;
					    green += (pixel >> 8) & 0xff;
					    blue += (pixel) & 0xff;
					}
				}
				
				alpha /= factor*factor;
				red /= factor*factor;
				green /= factor*factor;
				blue /= factor*factor;
			    
			    int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
			    
			    newImage.setRGB(width, height, newPixel);
			}
		}
		
		return newImage;
	}
	
	public static BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}
	public static void saveImage(BufferedImage image, String path) throws IOException {
		ImageIO.write(image, "png", new File(path));
	}
	
	public static BufferedImage toBlackAndWhite(BufferedImage image) throws IOException {
		int w = image.getWidth();
		int h = image.getHeight();
		
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				int pixel = image.getRGB(width, height);
				
				int alpha = (pixel >> 24) & 0xff;
			    int red = (pixel >> 16) & 0xff;
			    int green = (pixel >> 8) & 0xff;
			    int blue = (pixel) & 0xff;
			    
			    int avg = (red+green+blue)/3;
			    
			    int newPixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
			    
			    newImage.setRGB(width, height, newPixel);
			}
		}
		
		return newImage;
	}
	
	public static BufferedImage filter(BufferedImage image, int rmin, int rmax, int gmin, int gmax, int bmin, int bmax, int rgMin, int rgMax, int gbMin, int gbMax, int brMin, int brMax) throws IOException {
		int w = image.getWidth();
		int h = image.getHeight();
		
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				int pixel = image.getRGB(width, height);
				
				int alpha = (pixel >> 24) & 0xff;
			    int red = (pixel >> 16) & 0xff;
			    int green = (pixel >> 8) & 0xff;
			    int blue = (pixel) & 0xff;
			    
			    int rg = red-green;
				int gb = green-blue;
				int br = blue-red;
			    
			    int newPixel = 0;
			    
			    if(rmin <= red && red <= rmax && 
		    		gmin <= green && green <= gmax && 
		    		bmin <= blue && blue <= bmax && 
		    		rgMin <= rg && rg <= rgMax &&
		    		gbMin <= gb && gb <= gbMax &&
		    		brMin <= br && br <= brMax) {
			    	newPixel = pixel;
			    }
			    else {
			    	int avg = (red+green+blue)/3;
			    	newPixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
			    }
			   
			    newImage.setRGB(width, height, newPixel);
			}
		}
		
		return newImage;
	}
	
	public static BufferedImage contrast(BufferedImage image, int value) throws IOException {
		int w = image.getWidth();
		int h = image.getHeight();
		
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				int pixel = image.getRGB(width, height);
				
				int alpha = (pixel >> 24) & 0xff;
			    int red = (pixel >> 16) & 0xff;
			    int green = (pixel >> 8) & 0xff;
			    int blue = (pixel) & 0xff;
			    
			    int avg = (red+green+blue)/3;
			    
			    if(avg >= value) {
			    	avg = 255;
			    }
			    else {
			    	avg = 0;
			    }
			    
			    int newPixel = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
			    
			    newImage.setRGB(width, height, newPixel);
			}
		}
		
		return newImage;
	}

}
