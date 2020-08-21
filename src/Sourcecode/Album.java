package Sourcecode;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class Album implements Serializable{
	/**
	 * Questa classe viene serializzata
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private String dataCreazione;
	private Vector<Categoria> categorie= new Vector<>();
	
	/**
	 * Costruttore 
	 * @param nome è il nome dell'album
	 */
	public Album(String nome) {
		
		setNome(nome);
		setDataCreazione();
		
		}

	/**
	 * Aggiunge un nuovo elemento senza password alle catogorie.
	 * @param nome nome categoria
	 * @param Descrizione descrizione categoria
	 */
	public void aggiungiCat(String nome,String Descrizione)
	{
		categorie.addElement(new Categoria(nome,Descrizione));
	}

	

	/**
	 * Aggiunge un nuovo elemento con password alle categorie.
	 * @param nome nome categoria
	 * @param Descrizione descrizione categoria
	 * @param pass password
	 */
	public void aggiungiCatPass(String nome,String Descrizione,String pass)
	{
		categorie.addElement(new CategoriaPass(nome, Descrizione, pass));
	}

	

	/**
	 * Elimina categoria dall'album
	 * @param i indice
	 */
	public void eliminaCat(int i)
	{	
		categorie.removeElementAt(i);	
	}
	
	

	/**
	 * Sposta una foto in un'altra categoria
	 * @param i indice
	 * @param s sorgente
	 * @param d destinazione
	 */
	public void spostaFoto(int i, Categoria s, Categoria d)
	{
		d.addFoto(s.getLink(i),s.getFoto(i));
		s.eliminaFoto(i);
	}
	

	/**
	 * Cerca e restituisce l'indice della categoria nell'album prendendo in input il nome della categoria.
	 * @param cat categoria
	 * @return indice oppure 0
	 */
	public int searchByName(String cat)
	{
		int i=0;
		while(!getCategoria(i).getNome().equals(cat))
		{
			i++;
		}
		return i;
	}
	

	

	/**
	 * Getter della categoria
	 * @param i indice 
	 * @return categoria i-esima
	 */
	public Categoria getCategoria(int i)
	{	
		return categorie.get(i);
	}
	

	/**
	 * 
	 * @return il numero delle categorie presenti nell'album 
	 */
	public int Size()
	{
		return categorie.size();
	}
	

	/**
	 * Getter della data di creazione
	 * @return data di creazione
	 */
	public String getDataCreazione() {
		
		return dataCreazione;
	}

	/**
	 * Imposta la data della creazione alla data attuale
	 */
	public void setDataCreazione() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		this.dataCreazione = dateString;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

}

