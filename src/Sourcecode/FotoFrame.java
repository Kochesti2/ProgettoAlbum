package Sourcecode;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FotoFrame extends JFrame implements ActionListener,WindowListener,KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Album a; 
	private Categoria actualCat;
	private JFrame frame; 
	private JPanel panel;
	private int selectedFoto; //identifica la foto selezionata
	private String path; 
	private int prevClick; //identifica quale è stata l'ultima foto selezionata
	private JButton temp = null; //è un bottone temporaneto che viene usato per disegnare le immagini rimpicciolite
	private int fotoOnDisplay; //identifica l'ultima foto visualizzato 
	private JFrame imageFrame;
	private JPanel imagePanel;

	
	/**
	 * Costruttore della Classe FotoFrame
	 * Disegna il Frame che contiene le foto e aggiunge la barra del menù
	 *
	 * @param alb  è l'istanza dell'album sulla quale si lavora
	 * @param cat	è l'istanza della categoria sulla quale si lavora
	 */
	public FotoFrame(Album alb, Categoria cat){
		setSelectedFoto(-1);
		setPrevClick(-2);
		this.a=alb;
		this.actualCat=cat;		
		
		
		frame = new JFrame(cat.getNome());
		panel = new JPanel();
		

		guiMenu();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMinimumSize(new Dimension((int)screenSize.getWidth()/3, (int)screenSize.getHeight()/4));	
		 panel.removeAll();
		 panel.setLayout(new GridBagLayout());
		
		 GridBagConstraints c = new GridBagConstraints();
		 c.fill = GridBagConstraints.NONE;
		 
		
		for (int i = 0; i < cat.getNumFoto(); i++) {		
			draw(cat,i,cat.getFoto(i));
		}
		Center(frame);
		frame.addWindowListener(this);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	

	/**
	 * Quando viene chiamata questo metodo si cancella tutto dal panel passato come parametro
	 * e si ridisegna tutto di nuovo
	 * @param cat Categoria su cui si lavora
	 * @param p Panel
	 */
	 public void redraw(Categoria cat,JPanel p)
	 {
		 p.removeAll();
		 for (int i = 0; i < cat.getNumFoto(); i++) {
			draw(cat,i,cat.getFoto(i));
		}
		p.revalidate();
		p.repaint();
	 }
	

	 /**
	  * Centra sulla schermata il frame passato come parametro
	  * @param frame Frame
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
	 *  Viene disegnata l'immagine con indice i, poi i viene trasformata in Stringa e diventa anche il suo nome
	 *  
	 * @param cat categoria sulla quale si lavora
	 * @param i l'indice dell'immagine che deve essere disegnata
	 * @param img l'immagine vera e propria che deve essere disegnata
	 */
    public void draw(Categoria cat,Integer i,ImageIcon img)
    {    	
    	    GridBagConstraints c = new GridBagConstraints();
			temp= new JButton(i.toString());
			temp.setIcon(img);
			temp.setBackground(Color.GREEN);	
					 c.fill = GridBagConstraints.NONE;
					 c.weightx = 0.0;
					 c.gridy = i/4;
					 c.gridx = i % 4;
					 temp.setText("");
					 panel.add(temp, c);		 
					 
					 temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {	
						setSelectedFoto(i);	
				    	
						frame.setTitle(cat.getNome() + " - " + cat.getLink(i));
						if(getPrevClick()==getSelectedFoto())
						{
							setFotoOnDisplay(i);
							show(actualCat.getLink(i));
							setSelectedFoto(-1);
							setPrevClick(-2);
							frame.setTitle(actualCat.getNome());
						}
				}
			});
		 

		
	 panel.revalidate();


    }
    

   /* public void redrawFotoFrame(JPanel jp,int id)
    {
    	redrawPanel(actualCat.getLink(id),jp);
    }*/
    

    /**
     * Crea e visualizza il frame della foto ingrandita
     * aggiunge il listener per muoversi a destra e a sinistra una volta aperta la foto
     * @param src path dell'immagine che si vuole disegnare
     */
    public void show(String src)
    {
    	imageFrame = new JFrame();
    	imagePanel = new JPanel(new BorderLayout());
    	imageFrame.addKeyListener(this);
    	imageFrame.add(imagePanel);
    	redrawPanel(src,imagePanel);
    	imageFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
    	imageFrame.pack();
    	imageFrame.setVisible(true);
    }
    

    /**
     * Il metodo prende il link dell'immagine da disegnare e il panel sulla quale si lavora
     * l'idea di questo metodo è cancellare quello che c'è e ridisegnare dentro una nuova foto.
     * La foto viene adattata allo schermo e ridimensionata in base alle esigenze 
     * @param src path
     * @param panel pannello
     */
    private void redrawPanel(String src,JPanel panel)
    {
    	panel.removeAll();
    	ImageIcon icon = new ImageIcon(src);
    	JLabel jl = new JLabel(icon);
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int width = (int)screenSize.getWidth();
    	int height = (int)screenSize.getHeight();
    	
    	//se la foto è grane in altezza e in larghezza
    	if(icon.getIconWidth()>width && icon.getIconHeight()>height){
    		Image im = getScaledImage(icon.getImage(), height*icon.getIconWidth()/icon.getIconHeight(), width*icon.getIconHeight()/icon.getIconWidth());
    		icon.setImage(im);
    					}
    	else
    	{
    		//se la voto è grande solo in larghezza
    		if(icon.getIconWidth()>width)
    		{
    			Image im = getScaledImage(icon.getImage(), width, width*icon.getIconHeight()/icon.getIconWidth());
        		icon.setImage(im);
    		}
    		//se la foto è grande solo in altezza
    		if(icon.getIconHeight()>height)
    		{
    			Image im = getScaledImage(icon.getImage(), height*icon.getIconWidth()/icon.getIconHeight(), height);
        		icon.setImage(im);
    		}
    	}
    	panel.setBackground(Color.BLACK);
    	panel.add(jl, BorderLayout.CENTER);
    	panel.revalidate();
    	panel.repaint();

    }
    
    /**
     * Un metodo che data un'immagine ridimensiona e la restituisce in base alle parametri
     * @param srcImg  immagine
     * @param w width desiderata
     * @param h  height desiderata 
     * @return l'immagine modificata in base a (w,h)
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    
    /**
     * Il metodo che gestisce la slideshow 
     * Semplicemente slideshow è concepito come:
     * "disegna la foto successiva atendendo tot secondi"
     * 
     */
    private void slideShow()
    {
    	if(actualCat.getNumFoto()==0)
    		return;
    	
    	Timer timer = new Timer();
    	show(actualCat.getLink(0));
    	TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
        		redrawPanel(actualCat.getLink(getNextFotoId()),imagePanel); //qui viene ridisegnato sul pannello la foto successiva
        		setFotoOnDisplay(getNextFotoId());
        		if(getFotoOnDisplay()==actualCat.getNumFoto()-1)
        			timer.cancel();
            }
        };
        
        timer.scheduleAtFixedRate(timerTask, 2*1000, 2*1000); //2 secondi di attesa
    } 
    
    
	//disegna menu
    /**
     * Viene aggiunta la barra dei menù
     * assegnando action lilsteners
     */
	public void guiMenu()
	{
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("Foto");
		JMenu m2 = new JMenu("Info");
		JMenuItem mi1 = new JMenuItem(new AbstractAction("Aggiungi") {
			/**
			 * Action listener , aggiunge la foto	
			 */
			@Override
				public void actionPerformed(ActionEvent e)
				{
					path = getLinkOfFoto();
					if(path.equals(""))
						return;
					actualCat.addFoto(path,actualCat.pathToImage(path));
					redraw(actualCat, panel);
					
				}
		});
		JMenuItem mi2 = new JMenuItem(new AbstractAction("Elimina") {
			/**
			 * Action listener
			 * elimina la foto controllando che è stata selezionata
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(actualCat.getNumFoto()==0)
					return;
				
				if(getSelectedFoto()==-1)
				{
					JOptionPane.showMessageDialog(frame, "Prima devi selezionare la foto", "Attenzione!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else
				{
					int n = JOptionPane.showConfirmDialog(
						    frame,
						    "Sei sicuro di eliminare la foto?",
						    "Elimina foto",
						    JOptionPane.YES_NO_OPTION);
					if(n!=JOptionPane.YES_OPTION)
						return;
				}
				
				
				actualCat.eliminaFoto(getSelectedFoto());
				setSelectedFoto(-1);
				frame.setTitle(actualCat.getNome());
				JOptionPane.showMessageDialog(frame, "Eliminato");
				redraw(actualCat,panel);
			}
	});
		JMenuItem mi3 = new JMenuItem(new AbstractAction("Sposta") {
			/**
			 * Action listener che sposta una foto da una categoria in un'altra
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				if(actualCat.getNumFoto()==0)
					return;
				
				if(getSelectedFoto()==-1)
				{
					JOptionPane.showMessageDialog(frame, "Prima devi selezionare la foto", "Attenzione!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				Categoria destinazione;
				Object[] possibilities = new Object[a.Size()];
				
				for(int i=0;i<a.Size();i++)
				{
					possibilities[i]=a.getCategoria(i).getNome();
				}
					
				
				String s = (String)JOptionPane.showInputDialog(frame,"Dove si vuole spostare?","Destinazione della foto",
				                    JOptionPane.PLAIN_MESSAGE,null, possibilities,null);

				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
				    destinazione = a.getCategoria(searchByName(s));  //cerca attraverso il nome,ottiene la categoria destinazione
				}
				else
				{
					setSelectedFoto(-1);
					frame.setTitle(actualCat.getNome());
					return;
				}
				

				a.spostaFoto(getSelectedFoto(), actualCat,destinazione);
				setSelectedFoto(-1);
				frame.setTitle(actualCat.getNome());
				redraw(actualCat,panel);				
			}
	});
		JMenuItem mi4 = new JMenuItem(new AbstractAction("Info") {
			/**
			 * restituisce la informazione sulla foto
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(frame, "Questa categoria contiene " + actualCat.getNumFoto() + " foto.");
			}
	});
		
		JMenuItem mi5 = new JMenuItem(new AbstractAction("Slide show") {
			/**
			 * Action listener, fa partire slide show
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				slideShow();
				
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
		frame.setJMenuBar(mb);
	}

//dando il nome della categoria restituisce l'indice altrimenti restituisce -1

	/**
	 * Cerca l'indice della foto nella categoria dando il nome della foto es: casa.jpg
	 * Se non lo trova restituisce -1
	 * @param nome nome della foto
	 * @return l'indice della foto / -1
	 */
public int searchByName(String nome)
{
	for(int i=0;i<a.Size();i++)
	{
		if (a.getCategoria(i).getNome().equals(nome)) {
			return i;
		}
	}
	return -1;
}


/**
 * Ottiene e restituisce path della foto, per aggiungere la foto si usa FileChooser.
 * @return path (stringa) della foto da aggiungere
 */
public String getLinkOfFoto()
{
	JFileChooser fileChooser = new JFileChooser(".");
	FileNameExtensionFilter filter = new FileNameExtensionFilter("(png, gif, jpg, jpeg)", "png", "jpeg", "gif", "jpg");
	fileChooser.setAcceptAllFileFilterUsed(false);
	fileChooser.setFileFilter(filter);
	String path="";

    fileChooser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    
    int status = fileChooser.showOpenDialog(null);



    if (status == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      path = selectedFile.getAbsolutePath();
    } else if (status == JFileChooser.CANCEL_OPTION) {
    	return "";
    }
    return path;
}



@Override
public void actionPerformed(ActionEvent e) {
	//


}

@Override
public void windowActivated(WindowEvent e) {

}

@Override
public void windowClosed(WindowEvent e) {
	
}

@Override
public void windowClosing(WindowEvent e) {
	actualCat.setOpened(false);
	
}

@Override
public void windowDeactivated(WindowEvent e) {
}

@Override
public void windowDeiconified(WindowEvent e) {
}

@Override
public void windowIconified(WindowEvent e) {
}

@Override
public void windowOpened(WindowEvent e) {
}

public int getSelectedFoto()
{
	return selectedFoto;
}


/**
 * Setter della foto che è stata selezionata e 
 * setter automatico della variabile che salva l'ultima foto selezionata
 * @param i indice della foto selezionata
 */
public void setSelectedFoto(int i)
{
	setPrevClick(getSelectedFoto());
	this.selectedFoto=i;
}

public int getPrevClick() {
	return prevClick;
}

public void setPrevClick(int prevClick) {
	this.prevClick = prevClick;
}

/**
 * Key Listener, controlla:
 * Quando viene premuta la freccia -> per mostrare la foto successiva
 * Quando viene premuta la freccia <- per mostrare la foto precedente
 */
@Override
public void keyPressed(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_RIGHT)
	{
		redrawPanel(actualCat.getLink(getNextFotoId()),imagePanel);
		setFotoOnDisplay(getNextFotoId());
	}
	if(e.getKeyCode() == KeyEvent.VK_LEFT)
	{
		redrawPanel(actualCat.getLink(getPrevFotoId()),imagePanel);
		setFotoOnDisplay(getPrevFotoId());
	}
}

@Override
public void keyReleased(KeyEvent e) {
}

@Override
public void keyTyped(KeyEvent e) {	
}

public int getFotoOnDisplay() {
	return fotoOnDisplay;
}

public void setFotoOnDisplay(int fotoOnDisplay) {
	this.fotoOnDisplay = fotoOnDisplay;
}



/**
 * Se la foto successiva nella categoria non c'è, restituisce la prima foto
 * @return l'indice della foto successiva 
 */
public int getNextFotoId() {
	int i=getFotoOnDisplay()+1;
	if(i>=actualCat.getNumFoto())
		i=i % (actualCat.getNumFoto());
	
	return i;
}

/**
 * Se la foto precedente non c'è nella categoria restituisce l'ultima foto nel vettore
 * @return la foto precedente
 */
public int getPrevFotoId() {
	int i=getFotoOnDisplay()-1;
	if(i<0)
		i=actualCat.getNumFoto()-1;
	
	return i;
}

}
