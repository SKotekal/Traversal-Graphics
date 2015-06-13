import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;
public class Traversal_Graphics 
{
	private static BufferedImage c;
	private static BufferedImage master;
	private static JFrame frame = new JFrame();
	private static Fr fr;
	private static Graphics g;
	
	public static class Fr extends JPanel
	{
		private static ArrayList<Integer> x = new ArrayList<Integer>();
		private static ArrayList<Integer> y = new ArrayList<Integer>();
		private static ArrayList<Integer> w = new ArrayList<Integer>();
		private static ArrayList<Integer> h = new ArrayList<Integer>();
		private static ArrayList<Color> col = new ArrayList<Color>();
		private static ArrayList<Integer> m = new ArrayList<Integer>();
		public static BufferedImage render = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		public Fr()
		{
			frame.add(new JLabel(new ImageIcon(render)));
		}
		public void paintComponent(Graphics gr)
		{
			super.paintComponent(gr);
			gr.clearRect(0, 0, render.getWidth(), render.getHeight());
			gr.drawImage(render, 0, 0, null);
		}
		public void fillCell(int xi, int yi, int width, int height, Color color, int mark)
		{
			x.add(xi);
			y.add(yi);
			w.add(width);
			h.add(height);
			col.add(color);
			m.add(mark);
		}
		public void apply(Boolean oval)
		{
			Graphics gr = render.getGraphics();
			for(int i = 0; i < x.size(); i++)
			{
				gr.clearRect(x.get(i), y.get(i), w.get(i), h.get(i));
				
				
				gr.setColor(Color.BLACK);
				if(oval)
					gr.drawOval(x.get(i), y.get(i), w.get(i), h.get(i));
				else
					gr.drawRect(x.get(i), y.get(i), w.get(i), h.get(i));
				
				gr.setColor(col.get(i));				
				if(oval)
					gr.fillOval(x.get(i), y.get(i), w.get(i), h.get(i));
				else
					gr.fillRect(x.get(i), y.get(i), w.get(i), h.get(i));


			}
			x.clear();
			y.clear();
			w.clear();
			h.clear();
			col.clear();
		}
	}
	public static void main(String[] args) throws IOException
	{
		System.out.println("Subhodh Kotekal 2015\nImage Compression\nInput pathname for image file or DONE\n");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		
		while(!path.toUpperCase().equals("DONE"))
		{
			BufferedImage img = ImageIO.read(new File(path));
			c = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			fr = new Fr();
			g = frame.getGraphics();
			frame.setSize(img.getWidth(), img.getHeight());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(fr);
			frame.setResizable(true);
			frame.setVisible(true);
			master = img;		
			
			//compress(img, 0, 0);
			ArrayList<BufferedImage> imgList = new ArrayList<BufferedImage>();
			ArrayList<Integer> x = new ArrayList<Integer>();
			ArrayList<Integer> y = new ArrayList<Integer>();
			ArrayList<Color> colorList = new ArrayList<Color>();
			ArrayList<Integer> level = new ArrayList<Integer>();
			
			imgList.add(img);
			x.add(0);
			y.add(0);
			level.add(0);
			colorList.add(new Color(avg(img)));
			
			fr.fillCell(0, 0, img.getWidth(), img.getHeight(), colorList.get(0), 0);
			fr.paintComponent(fr.getGraphics());
			
			
			int counter = 0;
			//Random rng = new Random();
			while(imgList.size() > 0)
			{
					counter += 3;
					BufferedImage node = imgList.remove(0);
					int nodeX = x.remove(0);
					int nodeY = y.remove(0);
					Color nodeC = colorList.remove(0);
					int l = level.remove(0);
					
					/*int randIndex = rng.nextInt(imgList.size());
					BufferedImage node = imgList.remove(randIndex);
					int nodeX = x.remove(randIndex);
					int nodeY = y.remove(randIndex);
					Color nodeC = colorList.remove(randIndex);
					int l = level.remove(randIndex);*/
					
					int addX = node.getWidth()/2;
					int addY = node.getHeight()/2;
					if(node.getWidth() <= 5 && node.getHeight() <= 2)
					{
						fr.fillCell(nodeX, nodeY, node.getWidth()/2, node.getHeight()/2, new Color(node.getRGB(0, 0)), l+1);
					}
					else
					{
						double[] v = var(node, avg(img.getSubimage(nodeX, nodeY, node.getWidth(), node.getHeight())));
						//double err = Math.max(Math.pow(v[0], 0.5), Math.max(Math.pow(v[1], 0.5), Math.pow(v[2], 0.5)))/(Math.min(Math.pow(v[0], 0.5), Math.min(Math.pow(v[1], 0.5), Math.pow(v[2], 0.5))));
						//System.out.println(v[0] + " " + v[1] + " " + v[2]);// + " " + err);
						if(!(v[0] < 300 && v[1] < 300 && v[2] < 300 && node.getWidth() < c.getWidth()))
						//if(!(err < 1.1 && node.getWidth() < c.getWidth() ))
						{			
							BufferedImage sub = node.getSubimage(0, 0, node.getWidth()/2, node.getHeight()/2); //TL Quadrant
							imgList.add(sub);
							int color = avg(sub); 
							colorList.add(new Color(color));
							x.add(nodeX);
							y.add(nodeY);
							level.add(l+1);
							fr.fillCell(nodeX, nodeY, node.getWidth()/2, node.getHeight()/2, new Color(color), l+1);
							
							sub = node.getSubimage(node.getWidth()/2, 0, node.getWidth()/2, node.getHeight()/2); //TR Quadrant
							imgList.add(sub);
							color = avg(sub); 
							colorList.add(new Color(color));
							x.add(nodeX+addX);
							y.add(nodeY);
							level.add(l+1);
							fr.fillCell(nodeX+addX, nodeY, node.getWidth()/2, node.getHeight()/2, new Color(color), l+1);
							
							sub = node.getSubimage(0, node.getHeight()/2, node.getWidth()/2, node.getHeight()/2); //BL Quadrant
							imgList.add(sub);
							color = avg(sub);
							colorList.add(new Color(color));
							x.add(nodeX);
							y.add(nodeY + addY);
							level.add(l+1);
							fr.fillCell(nodeX, nodeY+addY, node.getWidth()/2, node.getHeight()/2, new Color(color), l+1);
							
							sub = node.getSubimage(node.getWidth()/2, node.getHeight()/2, node.getWidth()/2, node.getHeight()/2); //BR Quadrant
							imgList.add(sub);
							color = avg(sub);
							colorList.add(new Color(color));
							x.add(nodeX+addX);
							y.add(nodeY+addY);
							level.add(l+1);
							fr.fillCell(nodeX+addX, nodeY+addY, node.getWidth()/2, node.getHeight()/2, new Color(color), l+1);
						}
					}
					//if(Math.pow(counter, 0.25) == Math.floor(Math.pow(counter, 0.25)))
					if(!level.contains(l))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//fr.paint(fr.getGraphics());
						fr.apply(false);
						fr.paintComponent(fr.getGraphics());
						counter = ((int) Math.pow(counter, 0.25));
					}
			}
			File outputfile = new File("C:\\Users\\Subhodh\\Desktop\\Export.jpg");
			ImageIO.write(fr.render, "jpg", outputfile);
			System.out.println("COMPLETE\n");
			path = scan.nextLine();
		}
		System.exit(0);
	}
	/*public static void compress(BufferedImage image, int x, int y)//(X, Y) : (Width, Height) is starting point
	{
		//fr.repaint();
		//Thread.sleep(100);
		int[] v = var(image, avg(master.getSubimage(x, y, image.getWidth(), image.getHeight())));
		
		if(v[0] < 100 && v[1] < 100 && v[2] < 100 && image.getWidth() < c.getWidth())
		{		
			return;
		}
		if(image.getWidth() <= 5 && image.getHeight() <= 2)
		{
			//c.setRGB(x, y, image.getRGB(0, 0));
			fr.fillCell(x, y, image.getWidth()/2, image.getHeight()/2, new Color(image.getRGB(0, 0)));
			return;
		}

		
		int halfW = image.getWidth()/2;
		int halfH = image.getHeight()/2;
		int tempX = 0;
		int tempY = 0;
		
		BufferedImage sub = image.getSubimage(0, 0, image.getWidth()/2, image.getHeight()/2); //TL Quadrant
		int color = avg(sub); 
		fr.fillCell(x, y, image.getWidth()/2, image.getHeight()/2, new Color(color));
		compress(sub, x, y);
		
		sub = image.getSubimage(image.getWidth()/2, 0, image.getWidth()/2, image.getHeight()/2); //TR Quadrant
		color = avg(sub); 
		tempX = x;
		x += halfW;
		fr.fillCell(x, y, image.getWidth()/2, image.getHeight()/2, new Color(color));
		compress(sub, x, y);
		
		sub = image.getSubimage(0, image.getHeight()/2, image.getWidth()/2, image.getHeight()/2); //BL Quadrant
		color = avg(sub); 
		x = tempX;
		tempY = y;
		y += halfH;
		fr.fillCell(x, y, image.getWidth()/2, image.getHeight()/2, new Color(color));
		compress(sub, x, y);
		
		sub = image.getSubimage(image.getWidth()/2, image.getHeight()/2, image.getWidth()/2, image.getHeight()/2); //BR Quadrant
		color = avg(sub); 
		tempX = x;
		y = tempY;
		tempY = y;
		x += halfW;
		y += halfH;
		fr.fillCell(x, y, image.getWidth()/2, image.getHeight()/2, new Color(color));
		compress(sub, x, y);
		
	}*/
	public static int avg(BufferedImage image)
	{
		double r = 0;
		double g = 0;
		double b = 0;
		for(int i = 0; i < image.getHeight(); i++)
		{
			for(int j = 0; j < image.getWidth(); j++)
			{
				Color crayola = new Color(image.getRGB(j, i));
				r += (((double)crayola.getRed())/((double) (image.getHeight() * image.getWidth())));
				g += ((double) (crayola.getGreen())/((double) (image.getHeight() * image.getWidth())));
				b += ((double) crayola.getBlue())/((double) (image.getHeight() * image.getWidth()));
				
			}
		}
		Color ret = new Color(((int) Math.round(r)), ((int) Math.round(g)), ((int) Math.round(b)));
		return ret.getRGB();
	}
	public static double[] var(BufferedImage image, int mean)
	{
		double[] ret = new double[3];
		Color mcolor = new Color(mean);
		int mred = mcolor.getRed();
		int mgreen = mcolor.getGreen();
		int mblue = mcolor.getBlue();
		
		double r = 0;
		double g = 0;
		double b = 0;
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				Color crayola = new Color(image.getRGB(i, j));
				r += (((double) ((crayola.getRed()-mred) * (crayola.getRed()-mred)))/((double) (image.getHeight() * image.getWidth())));
				g += ((((double) (crayola.getGreen()-mgreen) * (crayola.getGreen()-mgreen)))/((double) (image.getHeight() * image.getWidth())));
				b += ((((double) (crayola.getBlue()-mblue) * (crayola.getBlue()-mblue)))/((double) (image.getHeight() * image.getWidth())));
			}
		}
	
		ret[0] = r;
		ret[1] = g;
		ret[2] = b;
		
		return ret;
	}
}
