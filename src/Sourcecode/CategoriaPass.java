package Sourcecode;

import java.io.Serializable;

/**
 * Questa classe estende la classe categoria ed � pensata per aggiungere la possibilit� di 
 * protegere la categoria da una password.
 * @author Magradze
 *
 */
public class CategoriaPass extends Categoria implements Serializable{
	private String password;
	
	public CategoriaPass(String nome,String Descrizione,String pass) {
		super(nome,Descrizione);
		setPassword(pass);
		setDefImage("util"+ System.getProperty("file.separator")+"lock.png");
	}

	
	
	/**
	 * Questo metodo definito nella classe padre d� la possibilit� di
	 * restituire la password della categoria, la password infatti � un'informazione 
	 * in pi� della categoria.
	 */
	@Override 
	public String GetExtraInfo()
	{
		return getPassword();
	}
	

	/**
	 *  Viene ridefinito il metodo modifica, questa volta con 3 parametri
	 * @param nome nome categoria
	 * @param descr descrizione categoria
	 * @param pass password categoria
	 */
	public void modifica(String nome,String descr,String pass)
	{
	    setNome(nome);
	    setDescrizione(descr);
	    setPassword(pass);
	}
	

	/**
	 * 
	 * @return la password della categoria
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter della password
	 * @param password Password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	
	
}
