package image;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Listeners extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private BufferedImage img;
	private BufferedImage img_changed;
	
	private int rmin;
	private int rmax;
	private int gmin;
	private int gmax;
	private int bmin;
	private int bmax;
	
	private int rgMax;
	private int rgMin;
	private int gbMax;
	private int gbMin;
	private int brMax;
	private int brMin;
	
	
	public Listeners() throws IOException {
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		
		String path = "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\tiger.jpg";
		
		this.img = ImageEditor.reduceSize(ImageIO.read(new File(path)), 3);
		this.img_changed = ImageEditor.reduceSize(ImageIO.read(new File(path)), 3);
		
		this.rmin = 256;
		this.rmax = -1;
		this.gmin = 256;
		this.gmax = -1;
		this.bmin = 256;
		this.bmax = -1;
		
		this.rgMax = -256;
		this.gbMax = -256;
		this.brMax = -256;
		this.rgMin = 256;
		this.gbMin = 256;
		this.brMin = 256;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img_changed, 0, 0, null);
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		int pixel = this.img.getRGB(e.getX(), e.getY());

		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;

		int rg = red-green;
		int gb = green-blue;
		int br = blue-red;

		if(rg < this.rgMin) {
			this.rgMin = rg;
		}

		if(gb < this.gbMin) {
			this.gbMin = gb;
		}
		
		if(br < this.brMin) {
			this.brMin = br;
		}
		
		if(rg > this.rgMax) {
			this.rgMax = rg;
		}
		
		if(gb > this.gbMax) {
			this.gbMax = gb;
		}
		
		if(br > this.brMax) {
			this.brMax = br;
		}
		
		if(red < this.rmin) {
			this.rmin = red;
		}
		if(green < this.gmin) {
			this.gmin = green;
		}
		if(blue < this.bmin) {
			this.bmin = blue;
		}
		
		if(red > this.rmax) {
			this.rmax = red;
		}
		if(green > this.gmax) {
			this.gmax = green;
		}
		if(blue > this.bmax) {
			this.bmax = blue;
		}

		/*System.out.println("Rmin:" + this.rmin + " Rmax:" + this.rmax + 
							" Gmin:" + this.gmin + " Gmax:" + this.gmax +
							" Bmin:" + this.bmin + " Bmax:" + this.bmax +
							" rg:" + this.rg + " gb:" + this.gb + " br:" + this.br);*/
		
		/*System.out.println(this.rmin + ", " + this.rmax + ", " + 
							this.gmin + ", " + this.gmax + ", " + 
							this.bmin + ", " + this.bmax + ", " + 
							this.rgMin + ", " + this.rgMax + ", " +
							this.gbMin + ", " + this.gbMax + ", " +
							this.brMin + ", " + this.brMax);*/
		try {
			this.img_changed = ImageEditor.filter(this.img, this.rmin, this.rmax, 
															this.gmin, this.gmax, 
															this.bmin, this.bmax, 
															this.rgMin, this.rgMax, 
															this.gbMin, this.gbMax, 
															this.brMin, this.brMax);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S) {
			try {
				ImageEditor.saveImage(this.img_changed, "C:\\Users\\Leonardo Capozzi\\Pictures\\ImageTest\\img.png");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
