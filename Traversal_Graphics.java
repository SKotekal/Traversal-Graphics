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
		//Declare arrayLists for x position, y position, width, height, color
		//and marker for each subimage
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
			//Create subimage with given parameters
			x.add(xi);
			y.add(yi);
			w.add(width);
			h.add(height);
			col.add(color);
			m.add(mark);
		}
		public void apply(Boolean oval)
		{
			//Paint the given image
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
		//Accept user image given by pathname
		System.out.println("Subhodh Kotekal 2015\nImage Compression\nInput pathname for image file or DONE\n");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		
		//Prompt for circle or rectangle
		System.out.println("Enter T or F for circle breakdown rather than rectangle: ");
		String circle = scan.nextLine();
		boolean circ = false;
		if(circle.equals("T"))
		{
			circ = true;
		}
		
		while(!path.toUpperCase().equals("DONE"))
		{
			//Initialize frame which contains the image
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
			//Contains list of subimages and x, y, color, and level (number of divisions) for the subimages
			ArrayList<BufferedImage> imgList = new ArrayList<BufferedImage>();
			ArrayList<Integer> x = new ArrayList<Integer>();
			ArrayList<Integer> y = new ArrayList<Integer>();
			ArrayList<Color> colorList = new ArrayList<Color>();
			ArrayList<Integer> level = new ArrayList<Integer>();
			
			//First image is simply the average color
			imgList.add(img);
			x.add(0);
			y.add(0);
			level.add(0);
			colorList.add(new Color(avg(img)));
			
			fr.fillCell(0, 0, img.getWidth(), img.getHeight(), colorList.get(0), 0);
			fr.paintComponent(fr.getGraphics());
			
			//Queue structure that holds the subimages such 
			//that the subimages can be traversed in Breadth First manner.
			//This means all of the subimages across the image will be divided
			//to the desired error threshold rather than dividing by quadrant all
			//the way down to the error threshold.
			int counter = 0;
			while(imgList.size() > 0)
			{
					//Pop first subimage
					counter += 3;
					BufferedImage node = imgList.remove(0);
					int nodeX = x.remove(0);
					int nodeY = y.remove(0);
					Color nodeC = colorList.remove(0);
					int l = level.remove(0);
					
					
					int addX = node.getWidth()/2;
					int addY = node.getHeight()/2;
					if(node.getWidth() <= 5 && node.getHeight() <= 2)
					{
						fr.fillCell(nodeX, nodeY, node.getWidth()/2, node.getHeight()/2, new Color(node.getRGB(0, 0)), l+1);
					}
					else
					{
						//Calculate variance of R, G, and B values of color between original image and subimage
						double[] v = var(node, avg(img.getSubimage(nodeX, nodeY, node.getWidth(), node.getHeight())));
						if(!(v[0] < 700 && v[1] < 700 && v[2] < 700 && node.getWidth() < c.getWidth()) && node.getWidth() > 1 && node.getHeight()>1)
						{	
							//Divide image into four subimages
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
					
					//Paint all of the divisions across the image at once
					//This creates the simultaneous division aesthetic
					if(!level.contains(l))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						fr.apply(circ);
						fr.paintComponent(fr.getGraphics());
						counter = ((int) Math.pow(counter, 0.25));
					}
			}
			
			//Output final image
			path = path.substring(0, path.lastIndexOf("\\"));
			File outputfile = new File(path + "\\Export.jpg");
			ImageIO.write(fr.render, "jpg", outputfile);
			System.out.println("COMPLETE\n");
			path = scan.nextLine();
		}
		System.exit(0);
	}
	public static int avg(BufferedImage image)
	{
		//Find average red, green, and blue color values.
		//Mix these together to find average RGB value
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
		//Calculate variance in color by calculating
		//variance in R, G, and B values
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
