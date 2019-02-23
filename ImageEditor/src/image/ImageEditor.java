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
		BufferedImage img = ImageEditor.loadImage("C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\tiger.jpg");
		
		BufferedImage newImg = ImageEditor.reduceSize(img,img.getWidth()/200);

		/*for(int i = 0; i < 256; i++) { 
			BufferedImage newImg2 = ImageEditor.contrast(newImg, i);
			ImageEditor.saveImage(newImg2, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\contrast\\img"+i+".png");
		}*/
		
		newImg = ImageEditor.contrast(newImg, 40);
		ImageEditor.toText(newImg, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\img.txt");
		
		/*BufferedImage img = ImageEditor.loadImage("C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\tiger.jpg");
		img = ImageEditor.toBlackAndWhite(img);
		img = ImageEditor.reduceSize(img,img.getWidth()/200);
		img = ImageEditor.floydSteinberg(img, 2);
		
		ImageEditor.toText(img, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\img.txt");
		ImageEditor.saveImage(img, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\tiger2.png");*/
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
	
	public static BufferedImage floydSteinberg(BufferedImage image, int n) {
		if(n < 2 || n > 256) {
			return null;
		}
		
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				newImage.setRGB(width, height, image.getRGB(width, height));
			}
		}
		
		for(int height = 0; height < h; height++) {
			for(int width = 0; width < w; width++) {
				int pixel = newImage.getRGB(width, height);
				int newPixel = newColor(pixel, n);
				
				newImage.setRGB(width, height, newPixel);
				
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				int newAlpha = (newPixel >> 24) & 0xff;
				int newRed = (newPixel >> 16) & 0xff;
				int newGreen = (newPixel >> 8) & 0xff;
				int newBlue = (newPixel) & 0xff;
				
				int errAlpha = alpha-newAlpha;
				int errRed = red-newRed;
				int errGreen = green-newGreen;
				int errBlue = blue-newBlue;
				
				int otherPixel;
				int otherAlpha;
				int otherRed;
				int otherGreen;
				int otherBlue;

				//x+1 y
				if(width+1 < w) {
					otherPixel = newImage.getRGB(width+1, height);

					otherAlpha = (otherPixel >> 24) & 0xff;
					otherRed = (otherPixel >> 16) & 0xff;
					otherGreen = (otherPixel >> 8) & 0xff;
					otherBlue = (otherPixel) & 0xff;

					otherAlpha += errAlpha * 7 / 16;
					otherRed += errRed * 7 / 16;
					otherGreen += errGreen * 7 / 16;
					otherBlue += errBlue * 7 / 16;

					newImage.setRGB(width+1, height, toPixel(otherAlpha, otherRed, otherGreen, otherBlue));
				}

				//x-1 y+1
				if(width-1 >= 0 && height+1 < h) {
					otherPixel = newImage.getRGB(width-1, height+1);

					otherAlpha = (otherPixel >> 24) & 0xff;
					otherRed = (otherPixel >> 16) & 0xff;
					otherGreen = (otherPixel >> 8) & 0xff;
					otherBlue = (otherPixel) & 0xff;

					otherAlpha += errAlpha * 3 / 16;
					otherRed += errRed * 3 / 16;
					otherGreen += errGreen * 3 / 16;
					otherBlue += errBlue * 3 / 16;

					newImage.setRGB(width-1, height+1, toPixel(otherAlpha, otherRed, otherGreen, otherBlue));
				}
				
				//x y+1
				if(height+1 < h) {
					otherPixel = newImage.getRGB(width, height+1);

					otherAlpha = (otherPixel >> 24) & 0xff;
					otherRed = (otherPixel >> 16) & 0xff;
					otherGreen = (otherPixel >> 8) & 0xff;
					otherBlue = (otherPixel) & 0xff;

					otherAlpha += errAlpha * 5 / 16;
					otherRed += errRed * 5 / 16;
					otherGreen += errGreen * 5 / 16;
					otherBlue += errBlue * 5 / 16;

					newImage.setRGB(width, height+1, toPixel(otherAlpha, otherRed, otherGreen, otherBlue));
				}

				//x+1 y+1
				if(width+1 < w && height+1 < h) {
					otherPixel = newImage.getRGB(width+1, height+1);

					otherAlpha = (otherPixel >> 24) & 0xff;
					otherRed = (otherPixel >> 16) & 0xff;
					otherGreen = (otherPixel >> 8) & 0xff;
					otherBlue = (otherPixel) & 0xff;

					otherAlpha += errAlpha * 1 / 16;
					otherRed += errRed * 1 / 16;
					otherGreen += errGreen * 1 / 16;
					otherBlue += errBlue * 1 / 16;

					newImage.setRGB(width+1, height+1, toPixel(otherAlpha, otherRed, otherGreen, otherBlue));
				}
			}
		}

		return newImage;
	}
	
	private static int toPixel(int alpha, int red, int green, int blue) {
		if(alpha < 0) {
			alpha = 0;
		}
		
		if(alpha > 255) {
			alpha = 255;
		}
		
		if(red < 0) {
			red = 0;
		}
		
		if(red > 255) {
			red = 255;
		}
		
		if(green < 0) {
			green = 0;
		}
		
		if(green > 255) {
			green = 255;
		}
		
		if(blue < 0) {
			blue = 0;
		}
		
		if(blue > 255) {
			blue = 255;
		}
		
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	
	private static int newColor(int pixel, int nColors) {
		nColors--;
		
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		
		
		int newAlpha = (int)((Math.round((nColors*alpha)/255.0)*255.0)/nColors);
		int newRed = (int)((Math.round((nColors*red)/255.0)*255.0)/nColors);
		int newGreen = (int)((Math.round((nColors*green)/255.0)*255.0)/nColors);
		int newBlue = (int)((Math.round((nColors*blue)/255.0)*255.0)/nColors);
		
		return toPixel(newAlpha, newRed, newGreen, newBlue);
	}

}
