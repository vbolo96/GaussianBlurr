package blurrIMG;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DialogBox extends JFrame{

	BufferedImage img;
	BufferedImage BlurredImg;
	String deviation;
	String dimension;
	float[][]g;
	int value2;
	public void Interface()
	{
		JFrame myFrame=new JFrame();
		myFrame.setSize(500, 400);
		JPanel contentPanel=new JPanel();
		JLabel label1=new JLabel("Deviatie(sigma)");
		label1.setBounds(20, 15, 150, 20);
		JTextField field1 = new JTextField(20);
		field1.setBounds(20, 45, 100, 30 );
		JLabel label2=new JLabel("Dimensiunea nucleului");
		label2.setBounds(20, 75, 150, 20);
		JTextField field2 = new JTextField(20);
		field2.setBounds(20, 105, 100, 30 );
		JTextArea textField = new JTextArea();
		JScrollPane sp = new JScrollPane(textField);
		sp.setBounds(200,10,250,300);
		//textField.setBounds(200,10,250,300 );
		JButton openImg=new JButton("Open Image");
		openImg.setBounds(20,155, 140,30);
		JButton BlurrImg= new JButton("Blurr");
		BlurrImg.setBounds(20,195, 140,30);
		JButton downloadBtn= new JButton("Download Blurred Picture");
		downloadBtn.setBounds(20,235, 140,30);
		JButton saveKernel=new JButton("Save Kernel");
		saveKernel.setBounds(20,275, 140,30);
		contentPanel.add(label1);
		contentPanel.add(field1);
		contentPanel.add(label2);
		contentPanel.add(field2);
		//contentPanel.add(textField);
		contentPanel.add(openImg);
		contentPanel.add(BlurrImg);
		contentPanel.add(downloadBtn);
		contentPanel.add(saveKernel);
		contentPanel.add(sp);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		myFrame.add(contentPanel);
		myFrame.setLocationRelativeTo(null);
		//myFrame.pack();
        myFrame.setVisible(true);
		
        
        openImg.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser file=new JFileChooser();
                file.setFileSelectionMode(JFileChooser.FILES_ONLY);
                file.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
                file.setAcceptAllFileFilterUsed(true);
                int result = file.showOpenDialog(DialogBox.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = file.getSelectedFile();
                    img=  ImageTools.loadImage(selectedFile.getAbsolutePath());
                    ImageTools.showImage(img, "original image");
                }
            }
        });
        
        BlurrImg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(img==null)
				{
					JFrame frame=new JFrame();
					JOptionPane.showMessageDialog(frame,
						    "Choose an image first!",
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					if(field1.getText().equals("")||field2.getText().equals(""))
					{
						JFrame frame=new JFrame();
						JOptionPane.showMessageDialog(frame,
						    "Fill in the textfields with appropiate values!",
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						deviation=field1.getText().toString();
						dimension=field2.getText().toString();
						double value1 = Double.parseDouble(deviation);
						value2 = Integer.parseInt(dimension);
						
						g=ImageTools.Gauss(value1,value2);
						int p=0;
						float[] blurAvg=new float[value2*value2*value2*value2];
						for(int i=0;i<value2;i++)
							for(int j=0;j<value2;j++)
							{
								blurAvg[p++]=g[i][j];
							}
						
						Kernel k = new Kernel(value2*value2,value2*value2,blurAvg);
						ImageTools.showImage(ImageTools.convolveSimple(img, k), "BlurAvg");
						BlurredImg=ImageTools.convolveSimple(img, k);
						
						String val="";
						for(int i=0;i<value2;i++)
						{
							for(int j=0;j<value2;j++)
							{
								val=val+" "+g[i][j];
							}
							val=val+"\n";
						}
						textField.setText("S-a folosit un filtru de netezire(blurring)\n "
								+ "bazat pe un nucleu de distributie Gauss cu o \n"
								+ "deviatie standard sigma egala cu " +value1+
								" \nsi un nucleu de dimensiunea "+value2+" X "+value2+
								" \n avand urmatoarele valori:\n " + val);
					}
				}
			}});
        
        downloadBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(BlurredImg==null)
				{
					JFrame frame=new JFrame();
					JOptionPane.showMessageDialog(frame,
						    "Blurr the image first!",
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					JFileChooser saveFile=new JFileChooser();
				int returnval=saveFile.showSaveDialog(DialogBox.this);
				if(returnval==JFileChooser.APPROVE_OPTION)
				{
					try{
						ImageIO.write(BlurredImg,"bmp",saveFile.getSelectedFile());
						System.out.println("Blurred image saved succesfully");
					}
					catch(IOException ioe){
						System.out.println("Failed to save the image");
					}
				}
				else{
					System.out.println("No file chosen");
				}
				}
				
				
			}});
        
        saveKernel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(BlurredImg==null)
				{
					JFrame frame=new JFrame();
					JOptionPane.showMessageDialog(frame,
						    "Blurr the image first!",
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					try{
					File kernFile=new File("kernels.txt");
					PrintStream writer=new PrintStream(kernFile);
					for(int i=0;i<value2;i++)
					{
						for(int j=0;j<value2;j++)
						{
							writer.print(g[i][j]+"  ");
						}
						writer.println("\n");
					}
					writer.close();
				}
				catch(FileNotFoundException fnf)
				{
					System.out.println("THE FILE WAS NOT FOUND!");
				}
				}
				
			}});

}
	}
