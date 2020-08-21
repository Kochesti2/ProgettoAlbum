package Sourcecode;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Categoria implements Serializable{
	/**
	 * Classe Categoria viene serializzata
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private String Descrizione;
	private int numFoto;
	private Vector<String> images = new Vector<>();
	private Vector<ImageIcon> imageVec= new Vector<>(); ;
	private String DefImage;
	private String ErrorImage;
	private boolean isOpened=false;
	
	/**
	 * Il costruttore della classe Categoria. Prende il nome della categoria e lo assegna, 
	 * prende la descrizione della categoria e la assegna. 
	 * Quando la categoria viene creata viene carivata di default picture.png per questo viene settato 
	 * come defaultImage picture.png
	 * Se la foto non si riesce a trovo nel indirizzo serializzato, se è stato spostata o cancellata,
	 * si carica la foto error.png
	 * @param nome
	 * @param descrizione
	 */
	public Categoria(String nome,String descrizione) {
		setDefImage("util"+ System.getProperty("file.separator")+"picture.png");
		setErrorImage("util"+ System.getProperty("file.separator")+"error.png");
		this.numFoto=0;
		setNome(nome);
		setDescrizione(descrizione);
	
	}
	

	/**
	 * Modifica il nome e la descrizione della categoria
	 * @param nome nome categoria
	 * @param descr descrizione della categoria
	 */
	public void modifica(String nome,String descr)
	{
	    setNome(nome);
	    setDescrizione(descr);
	}
	

	/**
	 * 
	 * @return Qualsiasi altra informazione sulla categoria
	 */
	public String GetExtraInfo()
	{
		return "";
	}
	
	/**
	 * Un metodo che è equivalente di RemoveAll dal Vector
	 */
	public void svuotaVector()
	{
		imageVec.removeAllElements();
	}
	

	/**
	 * Aggiunge la Foto al vettore degli immagini 
	 * Aggiunge il link(path) della foto al vettore dove sono salvate path
	 * incrementa il numero delle foto presenti nella categoria
	 * @param imgLink link dell'immagine
	 * @param img immagine
	 */
	public void addFoto(String imgLink,ImageIcon img)
	{
		imageVec.addElement(img);
		images.addElement(imgLink);
		this.numFoto++;
	}
	

	/**
	 *  Aggiunge la foto al vettore delle immagini 
	 * @param img immagine
	 */
	public void aggiungiFoto(ImageIcon img)
	{
		imageVec.addElement(img);
	}
	
	
	/**
	 * Dato un indice rimuove la foto dal vettore degli immagini
	 * di counseguenza rimuove anche path dal vettore dei link
	 * e diminuisce il numero delle foto 
	 * @param i indice della foto
	 */
	public void eliminaFoto(int i)
	{
		if(numFoto<=0 || (i+1)>numFoto)  return;
 		
		images.remove(i);
		imageVec.remove(i);		
		this.numFoto--;
			
	}
	
	public String getLink(int i)
	{
		return images.get(i);
	}
	
	
	public ImageIcon getFoto(int i)
	{
		return imageVec.get(i);
	}
	
	/**
	 * Prende il path della foto prova a leggere e restituisce ImageIcon corrispondente
	 * @param path percorso della foto
	 * @return ImageIcon corrispondente alla path
	 */
	public ImageIcon pathToImage(String path)
	{
		ImageIcon imageIcon=null;
		try {
			imageIcon = new ImageIcon(ImageIO.read(new File(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				imageIcon = new ImageIcon(ImageIO.read(new File(getErrorImage())));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//File not found
		} 
		// load the image to a imageIcon
	    imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH)); 
	    return imageIcon;
	}
	
	
	
	public int getNumFoto()
	{
		return numFoto;
	}
	
	
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDescrizione() {
		return Descrizione;
	}


	public void setDescrizione(String descrizione) {
		Descrizione = descrizione;
	}


	public String getDefImage() {
		return DefImage;
	}


	public void setDefImage(String defImage) {
		DefImage = defImage;
	}


	public boolean isOpened() {
		return isOpened;
	}


	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}


	public String getErrorImage() {
		return ErrorImage;
	}


	public void setErrorImage(String errorImage) {
		ErrorImage = errorImage;
	}

}

