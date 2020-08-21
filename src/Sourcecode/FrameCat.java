package Sourcecode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import java.awt.*;

public class FrameCat extends JFrame implements WindowListener,ActionListener{
		/**
	 * Questa classe estende JFrame e implementa WindowsListener e ActionListener.
	 * Disegna Frame principale all'interno della quale sono disegnati le categorie,
	 * man mano che si aggiungono le cattegorie esse vengono disegnate una di fianco all'altro.
	 * La classe gestisce la dimensione/posizione/comportamento della frame _frame e panel _panel.
	 */
	private static final long serialVersionUID = 1L;
		private JFrame _frame;
		private JPanel _panel;
		private Album alb;
		private FotoFrame ff=null;
		private int id;
		private String filename;
		private int selected;
		private int selectCount=0;
		private boolean SelectedMod=false;
		private int primoClick=0;
		
		
		/**
		 * Costruttore della FrameCat. FrameCat crea e disegna il frame dove sono contenute le cattegorie dell'album.
		 * 
		 * @param alb Album 
		 * @param filename  nome del file che verrà salvato sul disco
		 */
	public FrameCat(Album alb,String filename) {
		this.filename=filename;
		this.alb=alb;
		_frame = new JFrame("Gestione categorie");
		_panel = new JPanel();
		_panel.setBackground(Color.WHITE);
		JScrollPane pane = new JScrollPane(_panel);
		_frame.add(pane);

        

		
		CatMenu();		 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 _frame.setMinimumSize(new Dimension((int)screenSize.getWidth()/2, (int)screenSize.getHeight()/2));	 
		 _panel.removeAll();
		 _panel.setLayout(new GridBagLayout());
		
		 GridBagConstraints c = new GridBagConstraints();
		 c.fill = GridBagConstraints.NONE;
		 
		
		for (int i = 0; i < alb.Size(); i++) {
			draw(alb,i);
		}
		
		Center(_frame);
		
		_frame.setVisible(true);
		_frame.addWindowListener(this);
		_frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleClosing();
            }
        });
		_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
	}

	/**
	 * Centra il feame sullo schermo
	 * @param frame frame principale
	 */
	public void Center(JFrame frame)
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
		
		frame.setLocation(x, y);
		
	}
	
	
	/**
	 * Disegna la barra dei menù da dove è possibile eseguire delle azioni sulle categorie.
	 * 
	 */
	public void CatMenu()
	{
		id=0;  
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("Categorie");
		JMenu m2 = new JMenu("Info");
		JMenuItem mi1 = new JMenuItem(new AbstractAction("Aggiungi") {
				/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
				/**
				 * Controlla se la categoria è già stata aperta 
				 * Se è aperta non la apre finchè non viene chiusa
				 */
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(isSelectedMod())
						return;
					GuiAddCat();
				}
		});
		JMenuItem mi2 = new JMenuItem(new AbstractAction("Modifica") {
			/**
			 * Action listener:
			 * Gestisce la modifica del nome, descrizione e\o la password della categoria
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				if(isSelectedMod())
					return;
				id = getCategoriaFromDialog(alb);	
				if (id>=alb.Size() || id < 0) 
					return;
				String n = alb.getCategoria(id).getNome();			
				if(alb.getCategoria(id) instanceof CategoriaPass)
				{
					if(CheckPass(alb.getCategoria(id)))
					{
						GuiModCat(id);
						redraw(alb,_panel);
					}
					else
						return;
				}
				else
				{
					GuiModCat(id);
					redraw(alb,_panel);
				}
			}
	});
		JMenuItem mi3 = new JMenuItem(new AbstractAction("Elimina") {
			/**
			 * Action listener:
			 * Dà il comando di eliminazione della categoria
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(isSelectedMod())
					return;
				
				id = getCategoriaFromDialog(alb);
				
				if (id>=alb.Size() || id < 0)
					return;
				
				
				String n = alb.getCategoria(id).getNome();
				
				if(alb.getCategoria(id) instanceof CategoriaPass)
				{
					if(CheckPass(alb.getCategoria(id)))
					{
						alb.eliminaCat(id); //---
						JOptionPane.showMessageDialog(_frame, "Categoria " + n + " eliminata");
						redraw(alb,_panel);
					}
					else
						return;
				}
				else
				{
					alb.eliminaCat(id);
					JOptionPane.showMessageDialog(_frame, "Categoria " + n + " eliminata");
					redraw(alb,_panel);
				}
				
				if(alb.Size()==0)
				{
					_panel.removeAll();
				}
				
				
			}
	});
		JMenuItem mi4 = new JMenuItem(new AbstractAction("Info") {
			/**
			 * Restotuisce le informazioni sull'album
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(_frame, "Album: " + alb.getNome() + " | Creato il: " + alb.getDataCreazione());
			}
	});
		
		JMenuItem mi5 = new JMenuItem(new AbstractAction("Sposta tutto") {
			/**
			 * Action listener:
			 * Dopo aver controllato che:
			 * -ci siano almeno 2 categorie
			 * 
			 * Funzionamento della selected mode: 
			 * Quando selected mode è true, è attiva la possibilità di spostare delle foto da una categoria ad all'altra 
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if(alb.Size()==1)
				{
					JOptionPane.showMessageDialog(_frame, "Attenzione nell'album c'è solo una categoria", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(alb.Size()==0)
				{
					JOptionPane.showMessageDialog(_frame, "Album è vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isSelectedMod())
				{
					_frame.setTitle("Gestione categorie");
					setSelectedMod(false); 
					redraw(alb, _panel);
				}
				else
				{
					setSelectedMod(true);
					JOptionPane.showMessageDialog(_frame, "Primo Click: da dove, Secondo Click : dove", "Sposta tutte le immagini",1);
				}
			
				
				
				
			}
		});
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		
		m1.add(mi1);
		m1.add(mi2);
		m1.add(mi3);
		m1.add(mi5);
		m2.add(mi4);
		
		mb.add(m1);
		mb.add(m2);
		_frame.setJMenuBar(mb);
	}


	/**
	 *  Disegna JOptionPane, lo carica con le categorie e da la possibilità di scegliere 
	 *  una categoria da restituire
	 * @param alb l'album attuale
	 * @return la categoria selezionata in JOptionPane
	 */ 
	public int getCategoriaFromDialog(Album alb)
	{
		Object[] possibilities = new Object[alb.Size()];
		for(int i=0;i<alb.Size();i++)
		{
			possibilities[i]=alb.getCategoria(i).getNome();
		}
		String s = (String)JOptionPane.showInputDialog(_frame,"Categorie presenti","Scegli categoria",
                JOptionPane.PLAIN_MESSAGE,null, possibilities,null);
		if ((s != null) && (s.length() > 0)) {
		    return  alb.searchByName(s);
		}
		return -1;
	}
	
	/**
	 * Disegna JOptionPanel coi campi:
	 * -nome
	 * -descrizione
	 * -password
	 * 
	 * se password è vuota allora viene creata la categoria cenza password
	 */
	public void GuiAddCat()
	{
		JTextField nomeCategoria = new JTextField();
		JTextField descr = new JTextField(30);
		JPasswordField password = new JPasswordField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Nome Categoria"), nomeCategoria,
				new JLabel("Descrizione"), descr,
				new JLabel("Password"),password	
		};
		int result = JOptionPane.showConfirmDialog(null,inputs, "Crea categoria",JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if(CatCheckName(nomeCategoria.getText()))
			{
			String passText = new String(password.getPassword());
			if(passText.equals("")) //se password categoria è vuoto allora la password non c'è
			{
				alb.aggiungiCat(nomeCategoria.getText(), (descr.getText().equals("")?"":descr.getText()) );// se la descrizione è vuota passa la stringa vuota altrimenti passa la descrizione			
			}
			else
			{
				alb.aggiungiCatPass(nomeCategoria.getText(),(descr.getText().equals("")?"":descr.getText()),passText);
			}
			JOptionPane.showMessageDialog(_frame, "Categoria " + nomeCategoria.getText() + " creata");
			draw(alb,alb.Size()-1); 
		} 
	
		}
	}
	

	/**
	 * Estrae la categoria dall'album e la passa alla GuiModCategoria per modificare i campi necessari
	 * @param id l'id della categoria che voglio modificare
	 */
		public void GuiModCat(int id)
		{
			Categoria temp = alb.getCategoria(id);
            GuiModCategoria(temp);
		}
	
		/**
		 * Riceve la categoria temp
		 * modifica eventualmente il nome e la descrizione.
		 * @param temp categoria
		 */
	public void GuiModCategoria(Categoria temp)
	{
		JTextField nomeCategoria = new JTextField(temp.getNome());
		JTextField descr = new JTextField(temp.getDescrizione());
		final JComponent[] inputs = new JComponent[] {
		        new JLabel("Nome Categoria"),
		        nomeCategoria,
		        new JLabel("Descrizione"),
		        descr,
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, "Modifica Categoria", JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
			if(CatCheckNameVer1(nomeCategoria.getText()))
			{
				temp.setDescrizione(descr.getText().equals("")?"":descr.getText());
				temp.setNome(nomeCategoria.getText());
		} 
	
		}
	}
	

	/**
	 * Ogni volta che viene chiamata questa funzione, grazie a GridBagLayout viene disegnata una categoria dopo l'altra sul frame.
	 * @param alb  Album su cui si lavora
	 * @param i l'indeice della categoria da disegnare
	 */
    public void draw(Album alb,Integer i)
    {

		 
    		if(isSelectedMod())
    		{
    			_panel.setBackground(Color.ORANGE);
    		}
    		else
    		{
    			_panel.setBackground(Color.WHITE);
    			
    		}
    		GridBagConstraints c = new GridBagConstraints();
			JButton temp = null; 
			temp= new JButton(i.toString()); 
			temp.setToolTipText(alb.getCategoria(i).getNome());
			temp.setBackground(Color.GREEN);
			 if(alb.getCategoria(i) instanceof CategoriaPass)
			 {
				 temp.setIcon(alb.getCategoria(i).pathToImage(alb.getCategoria(i).getDefImage()));
			 }
				 else
				 if(alb.getCategoria(i).getNumFoto()==0)
				 {
					 temp.setIcon(alb.getCategoria(i).pathToImage(alb.getCategoria(i).getDefImage())); 
				 }	 
					 else
				 {
					 temp.setIcon(getRandomFoto(alb.getCategoria(i)));
				 }
					 c.fill = GridBagConstraints.NONE;
					 c.weightx = 0.0;
					 c.gridy = i/4;
					 c.gridx = i % 4;
					// labelvector.add(temp); 
					 temp.setText("");
					 _panel.add(temp, c);
					 //labelvector.get(i).setName(i.toString());
					 
					
					 
				 
			 

		 

			 
			 
					 temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(alb.getCategoria(i).isOpened())
						return;
					
					setSelected(i);
					
					if(!isSelectedMod())
					{
					if(alb.getCategoria(i) instanceof CategoriaPass)
					{
						if(CheckPass(alb.getCategoria(i)))	
						{
							ff = new FotoFrame(alb, alb.getCategoria(i));
							alb.getCategoria(i).setOpened(true);
						}
						else
							return;
					}
					else
					{
						ff = new FotoFrame(alb, alb.getCategoria(i));
						alb.getCategoria(i).setOpened(true);
					}
				}
					else
					{
						if(selectCount==0) 
							{
							primoClick=getSelected();
							if(alb.getCategoria(primoClick) instanceof CategoriaPass && !CheckPass(alb.getCategoria(primoClick)))
							{
								setSelectedMod(false);
								redraw(alb, _panel);
								return;
							}		
							
							
							selectCount++;
							_frame.setTitle("Selezionato: " + alb.getCategoria(selectCount).getNome());
							}
						else
						if(selectCount==1)
						{
							if(alb.getCategoria(getSelected()) instanceof CategoriaPass && !CheckPass(alb.getCategoria(getSelected())) || getSelected()==primoClick)
							{
								setSelectedMod(false);
								_frame.setTitle("Gestione categorie");
								redraw(alb, _panel);
								selectCount=0;
								return;
							}	
						SpostaTutto(alb, primoClick, getSelected());
						selectCount=0;
						setSelectedMod(false);
						alb.eliminaCat(primoClick);
						_frame.setTitle("Gestione categorie");
						redraw(alb, _panel);
						}
					}
						
				}
			});
		 

		
	 _panel.revalidate();
		 
		
		
		//_frame.pack();

    }
	


    /**
     * Sposta tutte le foto da una categoria all'altra
     * @param album album su cui si lavora
     * @param da indice della categoria da dove si deve spostare tutte le foto
     * @param a indice della categoria dove si deve spostare
     */
public void SpostaTutto(Album album, int da, int a)
{
	int size = album.getCategoria(da).getNumFoto();
	for (int i = 0; i < size; i++) {
		album.spostaFoto(0, album.getCategoria(da), album.getCategoria(a));
	}
}
    

/**
 * Questo metodo cancella tutto il pannello e ridisegna
 * chiama il metodo draw();
 * @param alb album attuale su cui si lavora
 * @param p pannello su cui si lavora
 */
 public void redraw(Album alb,JPanel p)
 {
	 p.removeAll();
	 for (int i = 0; i < alb.Size(); i++) {
		draw(alb,i);
	}
	p.revalidate();
	p.repaint();
 }
		
   
       
    
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {

	}
	/**
	 * Questo metodo garantisce la corretta chiusura del programma
	 * -chiede si si vuole salvare( ovvero serializzare tutto)
	 * -una volta aver dato ok di chiudere e salvare, viene svuotata tutto il vettore delle foto
	 *  e viene mantenuto solo il vettore delle path degli immagini, scopo: non salvare dati inutili
	 */
	private void handleClosing() {
            int answer = showWarningMessage();
             
            switch (answer) {
                case JOptionPane.YES_OPTION:
              		for (int i = 0; i < alb.Size(); i++) {
              			alb.getCategoria(i).svuotaVector();
              			alb.getCategoria(i).setOpened(false);
              		}
                      FileOutputStream fos = null;
                      ObjectOutputStream out = null;
                      try {
                          fos = new FileOutputStream(filename);
                        //  System.out.println("serialized!");
                          out = new ObjectOutputStream(fos);
                          out.writeObject(alb);
                          out.close();
                      } catch (Exception ex) {
                          ex.printStackTrace();
                      }
                    _frame.dispose();
                    System.exit(0);
                    break;
                     
                case JOptionPane.NO_OPTION:
                    _frame.dispose();
                    System.exit(0);
                    break;
                     
                case JOptionPane.CANCEL_OPTION:
                    break;
            }     
    }
     
	/**
	 * Il metodo che chiede se si vuole o no salvare  l'album
	 * ritorna:
	 * @return
	 * 1) si 
	 * 2) no
	 * 3) no
	 */
    private int showWarningMessage() {
        String[] buttonLabels = new String[] {"Sì", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        Icon icon = null;
         
        return JOptionPane.showOptionDialog(this,
                "Vuoi salvare l'album?",
                "Attenzione!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                icon,
                buttonLabels,
                defaultOption);    
    }
	
	@Override
	public void windowClosed(WindowEvent e) {	
	}

	@Override
	public void windowIconified(WindowEvent e) {	
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * A ogni attivazione del frame si ridisegna tutto
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		if(alb.Size()!=0)
			redraw(alb,_panel);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {	
	}
	
	/** 
	 * Data una stringa controlla se qusta esiste nell'array e in base a questo restituisce:
	 * @return
	 * false: se - il nome della categoria esiste già
	 * 			 - il nome della categoria inserita è vuota
	 * true: altrimenti (cioè il nome è ok)
	 * @param nameToCheck il nome della categoria e restituisce 
	 */
	public boolean CatCheckName(String nameToCheck)
	{
			if(nameToCheck.equals(""))
			{
				JOptionPane.showMessageDialog(_frame, "Il nome della categoria non può essere vuoto");
				return false;
			}
		    
		    int i=0;	
			while(i<alb.Size()) {
			if(alb.getCategoria(i).getNome().equals(nameToCheck)) //Il nome inserito eseite già
			{
				JOptionPane.showMessageDialog(_frame, "Il nome della categoria esiste già");
				return false;
			}		
			i++;
			}
	
		return true;
	}
	

    
	/**
	 * CheckPass garantisce la correttezza della password, prende la categoria cat
	 * Rischiede la password e se la password non corrisponde alla password già in memoria della cat
	 * restituisce il mesaggio di errore e false.
	 * altrimenti: true.
	 * 
	 * @param cat categoria sulla quale si lavora
	 * @return true se la password corrisponde, altrimenti false
	 */
	public boolean CheckPass(Categoria cat)
	{
		JPasswordField password = new JPasswordField();
		JOptionPane.showConfirmDialog(null, password, "Inserisci Password per " + cat.getNome(), JOptionPane.PLAIN_MESSAGE);
		String passText = new String(password.getPassword());
		if(!cat.GetExtraInfo().equals(passText))
		{
			JOptionPane.showMessageDialog(_frame, "Accesso negato");
			return false;
		}
		
	return true;	
	}
	


	/**
	 * Il metodo e un'altra versione della CatCheckName, però a differenza di quest'ultima 
	 * non conta se stessa mentre verifica se tra le categorie il nome inserito esiste già.
	 * Quindi se la incontra una volta la ignora.
	 * @param nameToCheck
	 * @return true se il nome della categoria non esiste, false se il nome della categoria esiste già
	 */
	public boolean CatCheckNameVer1(String nameToCheck)
	{
		if(nameToCheck.equals(""))
		{
			JOptionPane.showMessageDialog(_frame, "Il nome della categoria non può essere nulla");
			return false;
		}
		
		int incontrato=0;
		 int i=0;	
			while(i<alb.Size()) {
			if(alb.getCategoria(i).getNome().equals(nameToCheck)) //Il nome inserito eseite già
			{			
				incontrato++;
			}		
			i++;
			if(incontrato>1)
			{
		     JOptionPane.showMessageDialog(_frame, "Il nome della categoria esiste già");
		     return false;
			}		
			}
			return true;
	}
	

	/**
	 * Riceve una categoria e restituisce una foto random.
	 * @param c categoria
	 * @return foto random 
	 */ 
	public ImageIcon getRandomFoto(Categoria c)
	{
		
		int random = (int)(Math.random() * c.getNumFoto());
		return c.getFoto(random);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public boolean isSelectedMod() {
		return SelectedMod;
	}

	public void setSelectedMod(boolean selectedMod) {
		SelectedMod = selectedMod;
	}

	
}
