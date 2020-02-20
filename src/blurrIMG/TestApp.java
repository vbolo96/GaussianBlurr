package blurrIMG;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestApp extends JFrame{
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable(){
	        public void run() {
	           DialogBox yea=new DialogBox();
	            yea.Interface();
	            //apelarea metodei principale
	        }
	});
	}

}

