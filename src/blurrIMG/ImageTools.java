package blurrIMG;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ImageTools {

	static BufferedImage loadImage(String path){
		BufferedImage outImg = null;
		
		try {
			outImg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outImg;
	}
	
	static void saveImage(BufferedImage img, String format, String path){
		
		try {
			ImageIO.write(img, format, new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void showImage(BufferedImage img, String info){
		
		PicturePanel picPanel = new PicturePanel();
		picPanel.setPicture(img);
		picPanel.setSize(img.getWidth(),img.getHeight());
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle(info);
		window.setContentPane(picPanel);
		
		window.pack();
		window.setVisible(true);
	}
	static int clamp(int val, int min, int max){
		if(val<min)
			return min;
		else
			if(val>max)
				return max;
			else
				return val;
	}
	static int clamp(int val){
		return  clamp(val,0,255);
	}
	
	static float[][] Gauss(double sigma,int w)
	{ 
		double x0,y0;
		double sigma2;
		float[][]g=new float[w][w];
		x0=Math.floor(w/2)+1;
		y0=x0;
		sigma2=2*sigma*sigma;
		for(int i=0;i<w;i++)
			for(int j=0;j<w;j++)
			{
				g[i][j]=(float) (1/(Math.PI*sigma2)*Math.pow(Math.E,(-((i-x0)*(i-x0)+(j-y0)*(j-y0))/sigma2) ));
			}
		return g;
	}
	
	static BufferedImage convolveSimple(BufferedImage inImg, Kernel kernel){
		BufferedImage outImg = new BufferedImage(inImg.getTileWidth(),inImg.getHeight(),inImg.getType());
		
		// kernel patratic impar !!!
		int kWidth = kernel.getWidth();
		int kRadius = kWidth/2;
		float[] kData = kernel.getKernelData(null);
		int kCount = 0;
		
		for(int band = 0; band<inImg.getRaster().getNumBands(); band++){
			
			for(int y=0;y<inImg.getHeight(); ++y)
				for(int x=0;x<inImg.getWidth(); ++x){
					float gray = 0;
					kCount = 0;
					
					for(int j=-kRadius; j<=kRadius; ++j)
						for(int i=-kRadius; i<=kRadius; ++i){
							if((x+i)<0 || ((x+i)>inImg.getWidth()-1) || (y+j)<0 || ((y+j)>inImg.getHeight()-1)){
								gray+= inImg.getRaster().getSample(x, y, band) * kData[kCount];
								kCount++;
							}
							else{
								gray+= inImg.getRaster().getSample(x+i, y+j, band) * kData[kCount];
								kCount++;
							}
						}
					outImg.getRaster().setSample(x, y, band, clamp((int)Math.round(gray)));
				}
		}
		return outImg;
	}
	
}
