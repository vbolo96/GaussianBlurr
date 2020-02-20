package blurrIMG;


import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PicturePanel extends JPanel{
	
	private BufferedImage picture = null;
	private boolean fitToScreen = false;
	private double scaleValue = 1.0;
	private boolean aspectRatio = true;
	private String fileName = null;
	
	public PicturePanel(){
		
	}

	@Override
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		
		Graphics2D g = (Graphics2D)g1;
		
		if(picture == null)
			return;
		
		if(fitToScreen)
			if(aspectRatio){
				double hScale = 1.0*getWidth()/picture.getWidth(null);
				double vScale = 1.0*getHeight()/picture.getHeight(null);
				
				scaleValue = Math.min(hScale, vScale);
				
				int width = (int)(scaleValue * picture.getWidth(null));
				int height = (int)(scaleValue * picture.getHeight(null));
				
				g.drawImage(picture, 0, 0, width, height, null);
			}
			else{
				g.drawImage(picture, 0, 0, getWidth(), getHeight(), null);
			}
		else{
			g.drawImage(picture, 0, 0, null);
		}
		
		if(fileName != null){
			g.setColor(Color.WHITE);
			g.drawString(fileName, 10, 20);
		}
	}

	public BufferedImage getPicture() {
		return picture;
	}

	public void setPicture(BufferedImage picture) {
		this.picture = picture;
		repaint();
	}

	public boolean isFitToScreen() {
		return fitToScreen;
	}

	public void setFitToScreen(boolean fitToScreen) {
		this.fitToScreen = fitToScreen;
	}

	public double getScaleValue() {
		return scaleValue;
	}

	public void setScaleValue(double scaleValue) {
		this.scaleValue = scaleValue;
	}

	public boolean isAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(boolean aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		if(picture == null)
			return new Dimension(200,200);
		
		if(fitToScreen)
			return new Dimension(0,0);
		
		int width = (int)(scaleValue * picture.getWidth(null));
		int height = (int)(scaleValue * picture.getHeight(null));
		
		return new Dimension(width,height);
	}

	@Override
	public Dimension getMaximumSize() {
		
		if(picture == null)
			return new Dimension(200,200);
		
		return new Dimension(picture.getWidth(null),picture.getHeight(null));
	}

	@Override
	public Dimension getMinimumSize() {
		
		return new Dimension(200,200);
	}

}
