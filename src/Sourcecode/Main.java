package Sourcecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;


public class Main implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Main() {
		// TODO Auto-generated constructor stub
	}

/**
 * Classe main. Viene ripristinato la versione già salvata/serializzata del file
 * nel caso non la trovi crea una nuova istanza dell'album. 
 * @param args args
 */
	public static void main(String[] args) {
		final String filename = "SerialFile.dat";
		Album alb=null;
		File f = new File(filename);
		ImageIcon img=null;
		if(f.exists() && !f.isDirectory()) { 
		    FileInputStream fis = null;
	        ObjectInputStream in = null;
	        try {
	            fis = new FileInputStream(filename);
	            in = new ObjectInputStream(fis);
	            alb = (Album) in.readObject();
	           for (int i = 0; i < alb.Size(); i++) {
					for (int j = 0; j < alb.getCategoria(i).getNumFoto(); j++) {
						img = alb.getCategoria(i).pathToImage(alb.getCategoria(i).getLink(j));
						alb.getCategoria(i).aggiungiFoto(img);
					}
				} 
	            new FrameCat(alb,filename); 
	            in.close();
	        } catch (Exception ex) {
	        	f.delete();
	        	InitAlbum init = new InitAlbum(alb,filename);
	        }
		    
		}
		else
		{
			InitAlbum init = new InitAlbum(alb,filename);
		}

		
		
		
		

	}

	
	

}
