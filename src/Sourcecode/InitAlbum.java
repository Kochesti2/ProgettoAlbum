package Sourcecode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InitAlbum extends JFrame implements ActionListener,KeyListener{
	/**
	 * Questa classe parte per primo nel programma e disegna la finestra del benvenuto "ciao"
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel jp;
	private JLabel lbl1,lbl2;
	private JTextField txt;
	private JButton button;
	private Album a;
	private String filename;
	private Image img;
	
	/**
	 * Il costruttore della classe, prende l'album e il nome del file serializzato
	 * Viene disegnato il frame
	 * @param alb 
	 * @param filename
	 */
	public InitAlbum(Album alb,String filename) {
		a=alb;
		this.filename=filename;
		frame = new JFrame("");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jp = new JPanel();
		
		BoxLayout boxlayout = new BoxLayout(jp, BoxLayout.Y_AXIS);
		jp.setLayout(boxlayout);
		jp.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));
		jp.setBackground(Color.WHITE);
		 
		
		
		lbl1 = new JLabel("CIAO");
		lbl1.setFont(new Font("Serif", Font.BOLD, 100));
		
		
		
		lbl2 = new JLabel("Inserisci il nome dell'album");
		txt = new JTextField(20);
		txt.grabFocus();
		button = new JButton("Crea");
		
		lbl1.setAlignmentX(CENTER_ALIGNMENT);
		lbl2.setAlignmentX(CENTER_ALIGNMENT);
		button.setAlignmentX(CENTER_ALIGNMENT);
		
		txt.addKeyListener(this);
		button.addActionListener(this);
		
		 
		 jp.add(lbl1);
		 jp.add(lbl2);
		 jp.add(txt);
		 jp.add(button);
		frame.add(jp);
		 frame.pack();
		 Center(frame);
		frame.setVisible(true);
		frame.setResizable(false);
     	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void paintComponent(Graphics g) {
		        g.drawImage(img, 0, 0 , getWidth(), getHeight(), null);
		    }

	

	/**
	 * Prendendo come il parametro frame lo mette nel centro dello schermo
	 * @param frame frame
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
	 * Controlla che il campo del nome dell'album non fosse vuoto quando premo crea
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().compareTo("Crea")==0)
		{
			if(txt.getText().toString().trim().length() == 0 || txt.getText().toString().length()>100)
			{
				txt.setText("");
				return;	
			}
			a = new Album(txt.getText());
			
			new FrameCat(a,filename);
			frame.dispose();	
		}
		else
		return;
		
		
	}




	@Override
	public void keyReleased(KeyEvent evt) {

		
	}




	@Override
	public void keyTyped(KeyEvent evt) {
		
		
	}



	/**
	 * Quando si preme invio viene fatto click sul button crea
	 */
	@Override
	public void keyPressed(KeyEvent evt) {
		
		if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
			button.grabFocus();
            button.doClick();

        }
		
	}

}