package com.mathgame.math;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputAdapter;

import com.mathgame.database.*;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;




/**
 * 
 * @author Hima
 *The main class of the program
 */
public class MathGame extends JApplet implements ActionListener
{

	int appWidth=900;//1300 or 900
	int appHeight=620;//
	//float scale = .7f;
	static int difficulty = 2;//from 2-5 represents how many cards to use
	//GhostGlassPane glassPane;
	JLayeredPane layer;
	
	static Calc calc;
	
	JPanel panel2;
	JPanel panel2a;
	JPanel panel2b;
	SidePanel sidePanel;
	
	JTextField textFieldA;
	JTextField textFieldS;
	JTextField textFieldM;
	JTextField textFieldD;
	JLabel card1;
	JLabel card2;
	JLabel card3;
	JLabel card4;
	JLabel card5;
	Rectangle home1;
	Rectangle home2;
	Rectangle home3;
	Rectangle home4;
	Rectangle home5;
	
	JLabel add1;
	JLabel add2;
	JLabel sub1;
	JLabel sub2;
	JLabel mult1;
	JLabel mult2;
	JLabel div1;
	JLabel div2;
	
	JLabel opSpace1;
	JLabel opSpace2;
	JLabel opSpace3;
	JLabel opSpace4;
	
	Point[] placesHomes = new Point[12];
	
	JLabel correct;
	int answerA;
	int answerS;
	int answerM;
	float answerD;
	
	
	JButton enter1;
	JButton enter2;
	
	int enterAction;//0-3
	JButton random;
	JButton clear;
	
	JCheckBox database;
	static boolean useDatabase = false;
	MySQLAccess sql;
	
	JCheckBox freeStyle;
	boolean practice = false;
	
	Double check; //stores the value that is generated by randomize
	
	
	JLabel opA;
	JLabel opS;
	JLabel opM;
	JLabel opD;
	JLabel opParen0;
	JLabel opParen1;
	//JComboBox operator;
	
	
	JLabel correction;
	
	GridBagConstraints c;
	
	JLabel[] cards = new JLabel[11];//card1, card2..opA,S...
	Rectangle[] cardHomes = new Rectangle[11];//home1, home2...opA,S...
	String[] cardVals = new String[11];
	
	JLabel[] places = new JLabel[12];//{add1, add2, sub1, sub2, mult1, mult2, div1, div2, opSpace1,2,3,4};
	String[] operations = {"+", "-", "*", "/"};
	
	@Override
	public void init(){
	 
		setSize(appWidth, appHeight);
		setLayout(null);
		((JComponent) getContentPane()).setBorder(new LineBorder(Color.yellow));
		//setBorder(new LineBorder(Color.yellow));
		
		layer = new JLayeredPane();
		layer.setLayout(null);
		layer.setBounds(5, 0, getSize().width, getSize().height);
		
		sidePanel = new SidePanel();//control bar
		sidePanel.setBounds(750, 0, 150, 620);//x, y, width, height
		sidePanel.init();
		
		add(layer);
		add(sidePanel);
		
		
		
		enter1 = new JButton("Enter 1");
		enter2 = new JButton("Enter 2");
		
		database = new JCheckBox("Use Database");
		database.setMnemonic(KeyEvent.VK_D);
		
		sql = new MySQLAccess();
		
		freeStyle = new JCheckBox("Practice Mode");
		freeStyle.setMnemonic(KeyEvent.VK_P);
		
		
	
		//biggest panel that contains all the other ones (except for the layered pane of course)
		JPanel bigPanel = new JPanel(new GridBagLayout());
		bigPanel.setBounds(0, 0, getSize().width-150, getSize().height);
		bigPanel.setBorder(BorderFactory.createEtchedBorder(Color.blue, Color.pink));
		layer.add(bigPanel, new Integer(0));
		
		//Top parent panel, contains the given cards
		JPanel panel1 = new JPanel(new FlowLayout());
		
		//Middle parent panel
		panel2 = new JPanel(new GridBagLayout());
		//GroupLayout group = new GroupLayout(panel2);

		//GridLayout grid = new GridLayout(1, 9);
		//grid.setHgap(10);
		FlowLayout flow = new FlowLayout();
		
		//entry panel where the cards are dragged to
		panel2a = new JPanel(flow);
		//panel2a.setLayout(new BoxLayout(panel2a, BoxLayout.LINE_AXIS));
		panel2a.setBorder(new LineBorder(Color.green));
		
		//second entry panel
		panel2b = new JPanel(new FlowLayout());
		panel2b.setBorder(new LineBorder(Color.GRAY));
		
		//panel for answer and explanation
		JPanel panel2e = new JPanel(new FlowLayout());
		
		//contains the buttons
		JPanel panel3 = new JPanel(new FlowLayout());
	
		//contains panel3 
		JPanel panel5 = new JPanel(new BorderLayout());
		//panel.setSize(200, 200);
		
		textFieldA = new JTextField();
		textFieldA.setHorizontalAlignment(JTextField.CENTER);
		textFieldA.setEditable(false);
		textFieldA.setBorder(new LineBorder(Color.blue));
		
		textFieldS = new JTextField();
		textFieldS.setHorizontalAlignment(JTextField.CENTER);
		textFieldS.setEditable(false);
		textFieldS.setBorder(new LineBorder(Color.blue));
		
		textFieldM = new JTextField();
		textFieldM.setHorizontalAlignment(JTextField.CENTER);
		textFieldM.setEditable(false);
		textFieldM.setBorder(new LineBorder(Color.blue));
		
		textFieldD = new JTextField();
		textFieldD.setHorizontalAlignment(JTextField.CENTER);
		textFieldD.setEditable(false);
		textFieldD.setBorder(new LineBorder(Color.blue));
		
		//Border b1 = new Border();
		TitledBorder cardBorder = BorderFactory.createTitledBorder("Your Cards");
		TitledBorder blankCards = BorderFactory.createTitledBorder("The Operation Cards");
		TitledBorder Actions = BorderFactory.createTitledBorder("Actions");
		Border b2 = BorderFactory.createEtchedBorder(Color.blue, Color.pink);
	
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border matte = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	
		Border compound2 = BorderFactory.createCompoundBorder(Actions, raisedetched);
		Border opBorder = BorderFactory.createLineBorder(Color.red);
		
		card1 = new JLabel();
		card1.setHorizontalAlignment(JLabel.CENTER);
		card1.setTransferHandler(new TransferHandler("text"));
		card1.setBorder(b2);
		
		
		card2 = new JLabel();
		card2.setHorizontalAlignment(JLabel.CENTER);
		card2.setTransferHandler(new TransferHandler("text"));
		card2.setBorder(b2);
		
		
		
		card3 = new JLabel();
		card3.setHorizontalAlignment(JLabel.CENTER);
		card3.setTransferHandler(new TransferHandler("text"));
		card3.setBorder(b2);
	
		
		card4 = new JLabel();
		card4.setHorizontalAlignment(JLabel.CENTER);
		card4.setTransferHandler(new TransferHandler("text"));
		card4.setBorder(b2);
		
	
		
		card5 = new JLabel();
		card5.setHorizontalAlignment(JLabel.CENTER);
		card5.setTransferHandler(new TransferHandler("text"));
		card5.setBorder(b2);
		
		
		add1 = new JLabel("", JLabel.CENTER);
		
		add1.setBorder(b2);
		add2 = new JLabel("", JLabel.CENTER);
		//add2.setTransferHandler(transfer);
		add2.setBorder(b2);
		
		sub1 = new JLabel("", JLabel.CENTER);
		//sub1.setTransferHandler(transfer);
		sub1.setBorder(b2);
		sub2 = new JLabel("", JLabel.CENTER);
		//sub2.setTransferHandler(transfer);
		sub2.setBorder(b2);
		
		mult1 = new JLabel("", JLabel.CENTER);
		//mult1.setTransferHandler(transfer);
		mult1.setBorder(b2);
		mult2 = new JLabel("", JLabel.CENTER);
		//mult2.setTransferHandler(transfer);
		mult2.setBorder(b2);
		
		div1 = new JLabel("", JLabel.CENTER);
		//div1.setTransferHandler(transfer);
		div1.setBorder(b2);
		div2 = new JLabel("", JLabel.CENTER);
		//div2.setTransferHandler(transfer);
		div2.setBorder(b2);
		
		opSpace1 = new JLabel("", JLabel.CENTER);
		opSpace1.setBorder(opBorder);
		opSpace2 = new JLabel("", JLabel.CENTER);
		opSpace2.setBorder(opBorder);
		opSpace3 = new JLabel("", JLabel.CENTER);
		opSpace3.setBorder(opBorder);
		opSpace4 = new JLabel("", JLabel.CENTER);
		opSpace4.setBorder(opBorder);
		
	 
		
		places[0] = add1;
		places[1] = add2;
		places[2] = sub1;
		places[3] = sub2;
		places[4] = mult1;
		places[5] = mult2;
		places[6] = div1;
		places[7] = div2;
		places[8] = opSpace1;
		places[9] = opSpace2;
		places[10] = opSpace3;
		places[11] = opSpace4;
		
		
		correct = new JLabel("Answer", JLabel.CENTER);
		correct.setBorder(new LineBorder(Color.black));
		correction = new JLabel("", JLabel.CENTER);
		
		opA = new JLabel("+", JLabel.CENTER);
		opS = new JLabel("-", JLabel.CENTER);
		opM = new JLabel("*", JLabel.CENTER);
		opD = new JLabel("/", JLabel.CENTER);
		opParen0 = new JLabel("(", JLabel.CENTER);
		opParen1 = new JLabel(")", JLabel.CENTER);
	
		
		cards[0] = card1;
		cards[1] = card2;
		cards[2] = card3;
		cards[3] = card4;
		cards[4] = card5;
		cards[5] = opA;
		cards[6] = opS;
		cards[7] = opM;
		cards[8] = opD;
		cards[9] = opParen0;
		cards[10] = opParen1;
		
		
		CompMover mover = new CompMover(this);
		
		
		card1.addMouseListener(mover);		
		card2.addMouseListener(mover);
		card3.addMouseListener(mover);
		card4.addMouseListener(mover);
		card5.addMouseListener(mover);
		
		card1.addMouseMotionListener(mover);
		card2.addMouseMotionListener(mover);
		card3.addMouseMotionListener(mover);
		card4.addMouseMotionListener(mover);
		card5.addMouseMotionListener(mover);
		
		opA.addMouseListener(mover);
		opS.addMouseListener(mover);
		opM.addMouseListener(mover);
		opD.addMouseListener(mover);
		opParen0.addMouseListener(mover);
		opParen1.addMouseListener(mover);
		
		opA.addMouseMotionListener(mover);
		opS.addMouseMotionListener(mover);
		opM.addMouseMotionListener(mover);
		opD.addMouseMotionListener(mover);
		opParen0.addMouseMotionListener(mover);
		opParen1.addMouseMotionListener(mover);
		
		enter1.addActionListener(this);
		enter2.addActionListener(this);
		
				
		enter1.setMargin(null);
		enter2.setMargin(null);
		
		
		random = new JButton("Randomize");
		random.addActionListener(this);
		//panel.add(card1);
		
		clear = new JButton("Clear");
		clear.addActionListener(this);
		
		//operator = new JComboBox(operations);
		//((JLabel)operator.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
	
		opA.setBorder(opBorder);
		opS.setBorder(opBorder);
		opM.setBorder(opBorder);
		opD.setBorder(opBorder);
		opParen0.setBorder(opBorder);
		opParen1.setBorder(opBorder);
		
		Font big = new Font("Verdana", Font.BOLD, 12);
		Font bigger = new Font("Verdana", Font.BOLD, 18);
		opA.setFont(big);
		opS.setFont(bigger);
		opM.setFont(big);
		opD.setFont(big);
		opParen0.setFont(big);
		opParen1.setFont(big);
		//panel.setBorder(b2);
		Dimension d = card1.getPreferredSize(); 
		System.out.println(""+d);
		int width = d.width+ 100;
		int height = d.height + 50;
	
		add1.setPreferredSize(new Dimension(width, height));
		add2.setPreferredSize(new Dimension(width, height));
		sub1.setPreferredSize(new Dimension(width, height));
		sub2.setPreferredSize(new Dimension(width, height));
		mult1.setPreferredSize(new Dimension(width, height));
		mult2.setPreferredSize(new Dimension(width, height));
		div1.setPreferredSize(new Dimension(width, height));
		div2.setPreferredSize(new Dimension(width, height));
		opSpace1.setPreferredSize(new Dimension(width, height));
		opSpace2.setPreferredSize(new Dimension(width, height));
		opSpace3.setPreferredSize(new Dimension(width, height));
		opSpace4.setPreferredSize(new Dimension(width, height));
		correct.setPreferredSize(new Dimension(width, height));
		textFieldA.setPreferredSize(new Dimension(width, height));
		textFieldS.setPreferredSize(new Dimension(width, height));
		textFieldM.setPreferredSize(new Dimension(width, height));
		textFieldD.setPreferredSize(new Dimension(width, height));

	
		enter1.setPreferredSize(new Dimension(95, 20));
		enter2.setPreferredSize(new Dimension(95, 20));
		
		//panel2a.add(Box.createRigidArea(new Dimension(10,0)));
		
		//width=(int)(width);//width=(int)(width*scale);
		//height= (int)(height);
		
		//System.out.println("card1: " + card1.getX() + " " + card1.getY() + " " + card1.getWidth() + " " + card1.getHeight());
		//if(scale == .7f)
		width = 73;
		height = 38;
			card1.setBounds(75, 40, width, height);
	//	else
		//	card1.setBounds(150, 40, width, height);
		card2.setBounds((int) (card1.getX()+width*1.5), card1.getY(), width, height);
		card3.setBounds((int) (card2.getX()+width*1.5), card1.getY(), width, height);
		card4.setBounds((int) (card3.getX()+width*1.5), card1.getY(), width, height);
		card5.setBounds((int) (card4.getX()+width*1.5), card1.getY(), width, height);
		
		//System.out.println(width);
		opA.setBounds((int) (card1.getX()-width*.75), 45+height, width, height);
		opS.setBounds((int) (opA.getX()+width*1.5), 45+height, width, height);
		opM.setBounds((int) (opS.getX()+width*1.5), 45+height, width, height);
		opD.setBounds((int) (opM.getX()+width*1.5), 45+height, width, height);
		opParen0.setBounds((int) (opD.getX()+width*1.5), 45+height, width, height);
		opParen1.setBounds((int) (opParen0.getX()+width*1.5), 45+height, width, height);
		
		card1.setPreferredSize(new Dimension(width, height));
		card2.setPreferredSize(new Dimension(width, height));
		card3.setPreferredSize(new Dimension(width, height));
		card4.setPreferredSize(new Dimension(width, height));
		card5.setPreferredSize(new Dimension(width, height));
		
		opA.setPreferredSize(new Dimension(width, height));
		opS.setPreferredSize(new Dimension(width, height));
		opM.setPreferredSize(new Dimension(width, height));
		opD.setPreferredSize(new Dimension(width, height));
		opParen0.setPreferredSize(new Dimension(width, height));
		opParen1.setPreferredSize(new Dimension(width, height));
		
		
		
		home1 = new Rectangle(card1.getBounds());
		home2 = new Rectangle(card2.getBounds());
		home3 = new Rectangle(card3.getBounds());
		home4 = new Rectangle(card4.getBounds());
		home5 = new Rectangle(card5.getBounds());
		
		cardHomes[0] = card1.getBounds();
		cardHomes[1] = card2.getBounds();
		cardHomes[2] = card3.getBounds();
		cardHomes[3] = card4.getBounds();
		cardHomes[4] = card5.getBounds();
		cardHomes[5] = opA.getBounds();
		cardHomes[6] = opS.getBounds();
		cardHomes[7] = opM.getBounds();
		cardHomes[8] = opD.getBounds();
		cardHomes[9] = opParen0.getBounds();
		cardHomes[10] = opParen1.getBounds();
		
		
		
		
		
		panel1.setBorder(cardBorder);
		
		
		DropTarget dt = new DropTarget();
		dt.setActive(false);
		card1.setDropTarget(dt);
		card2.setDropTarget(dt);
		card3.setDropTarget(dt);
		card4.setDropTarget(dt);
		card5.setDropTarget(dt);
		
	
       // panel2a.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		/*panel2a.add(add1);
		panel2a.add(opSpace1);
		panel2a.add(add2);
		panel2a.add(enterA);
		panel2a.add(textFieldA);*/
		
		/*panel2b.add(mult1);
		panel2b.add(opSpace3);
		panel2b.add(mult2);
		panel2b.add(enterM);
		panel2b.add(textFieldM);*/
	
		
		//panel2a.setPreferredSize(new Dimension(getWidth(), card1.getHeight()));
		//panel2a.setMinimumSize(new Dimension(getWidth(), card1.getHeight()));
		
		panel2e.add(correct);
		panel2e.add(correction);
		
		
		
		//group.linkSize(SwingConstants.HORIZONTAL, panel2a, panel2b);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx =0;
		c2.gridy =0;
		c2.gridwidth = 3;
		c2.weighty =5;
		c2.weightx =1;
		c2.fill = GridBagConstraints.BOTH;
		
		panel2.add(panel2a, c2);
		c2.gridy =1;
		panel2.add(panel2b, c2);
		c2.gridy =2;
		c2.weighty =1;
		panel2.add(panel2e, c2);
		
		
		
		/*panel2.add(panel2a);
		panel2.add(panel2b);
		panel2.add(panel2c);
		panel2.add(panel2d);
		panel2.add(panel2e);*/
		panel2.setBorder(blankCards);
		panel2.setPreferredSize(new Dimension(500, 1000));
		
		//panel3.add(enter);
		//panel3.add(textFieldA);	
		
		panel3.add(random);
		panel3.add(clear);
		panel3.add(enter1);
		panel3.add(enter2);
		panel3.add(database);
		panel3.add(freeStyle);
		//panel4.setBorder(Actions);
		
		
		//panel5.add(panel3, BorderLayout.NORTH);
		panel5.add(panel3, BorderLayout.CENTER);	
		//panel5.setSize(getWidth(), 100);
		
		
		
		panel5.setBorder(Actions);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		
		c.weightx =5;
		c.weighty= 7;
		c.fill = GridBagConstraints.BOTH;
		//panel1Big.setOpaque(true);
		//layer.add(panel1, c, new Integer(0));
		bigPanel.add(panel1, c);
		
		//System.out.println(panel1.getBounds().x + " " + panel1.getBounds().y + " " + panel1.getBounds().width + " " + panel1.getBounds().height);
		
		c.gridy = 1;
		bigPanel.add(panel2, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 2;
		c.weighty =4;
		//panel5.setOpaque(true);
		bigPanel.add(panel5, c);
		//add(panel3);
		c.weighty = 1;
		c.weightx = 0;
		c.gridx=2;
		c.gridy=0;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 3;
		//TODO: bigPanel.add(sidePanel, c);
		
		//layer.setVisible(false);
		//add(panel4);
		layer.add(card1, new Integer(1));
		layer.add(card2, new Integer(1));
		layer.add(card3, new Integer(1));
		layer.add(card4, new Integer(1));
		layer.add(card5, new Integer(1));
		layer.add(opA, new Integer(1));
		layer.add(opS, new Integer(1));
		layer.add(opM, new Integer(1));
		layer.add(opD, new Integer(1));
		layer.add(opParen0, new Integer(1));
		layer.add(opParen1, new Integer(1));
		
		
	calc = new Calc(MathGame.this);
	//////////////////////////////////////
	calc.randomize();
	/////////////////////////////////////
	Items itemListener = new Items(this);
	database.addItemListener(itemListener);
	freeStyle.addItemListener(itemListener);
	
	
	}

	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getSource() == enter1)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(int r=0;r<panel2a.getComponentCount();r++)
			{
				temp.add( ( (JLabel) panel2a.getComponent(r)).getText() );
			}
			
			Double output = calc.calculate(temp);
			if(output != null)
			{
				if(practice == true)
				{
					correct.setText(String.valueOf(output));
					correct.setBorder(new LineBorder(Color.blue));
					correction.setText("");
				}
				else
				{
					if(output.equals(check) )
					{
						correct.setText("Congratulations!");
						correct.setBorder(new LineBorder(Color.blue));
						correction.setText(output + " is equal to " + check);
						sidePanel.updateCorrect();
					}
					else
					{
						correct.setText("Try Again!!");
						correct.setBorder(new LineBorder(Color.red));
						correction.setText(output + " is NOT equal to " + check);
						sidePanel.updateWrong();
					}
				}
			}
		}
		else if(evt.getSource() == enter2)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(int r=0;r<panel2b.getComponentCount();r++)
			{
				temp.add( ( (JLabel) panel2b.getComponent(r)).getText() );
			}
			
			Double output = calc.calculate(temp);
			if(output != null)
			{
				if(practice == true)
				{
					correct.setText(String.valueOf(output));
					correct.setBorder(new LineBorder(Color.blue));
					correction.setText("");
				}
				else
				{
					if(output.equals(check) )
					{
						correct.setText("Congratulations!");
						correct.setBorder(new LineBorder(Color.blue));
						correction.setText(output + " is equal to " + check);
						sidePanel.updateCorrect();
					}
					else
					{
						correct.setText("Try Again!!");
						correct.setBorder(new LineBorder(Color.red));
						correction.setText(output + " is NOT equal to " + check);
						sidePanel.updateWrong();
					}
				}
			}
			
		}
		else if(evt.getSource() == random)
			calc.randomize();
		else if(evt.getSource() == clear)
			calc.clear();
	
	}
	
	/**
	 * Sets the difficulty of the game.
	 * 
	 * Difficulty increases based on the number of cards used. If 5 cards are used, it will be much harder
	 * to figure out the right order to use the cards.
	 * 
	 * @param diff Number from 2- #of cards in game 
	 */
	public void setDifficulty(int diff){
		difficulty = diff;
		System.out.println("DIFFICULTY FROM MAIN SET: " + difficulty);
		calc.randomize();
		//randomize();
	}
	
	/**
	 * Gets the difficulty of the game
	 */
	public int getDifficulty(){
		System.out.println("DIFFICULTY FROM MAIN GET: " + difficulty);
		return difficulty;
		//randomize();
	}
	
	/**
	 * Booleans are immutable, so the value of practice can't be set by referencing it in the Items class
	 * @param prac Whether practice mode should be enabled or not
	 */
	public void setPractice(boolean prac){
		practice = prac;
	}
	
	
	public void setCheck(Double tCheck){
		check = tCheck;
	}
	/**
	 * Specifies whether or not to use the database
	 * @param database true or false
	 */
	public void setDatabase(boolean database){
		useDatabase = database;
		System.out.println("SET DATABASE " + useDatabase);
	}
	public boolean getDatabase(){
		System.out.println("GET DATABASE " + useDatabase);
		return useDatabase;
	}
	
	/*
	public Container getCompParent(int i){
		return cards[i].getParent();
	}
	
	public JLabel getComps(int i){
		return cards[i];
	}
	*/
	
//	public void error(){
	//	sidePanel.error.setText(sql.sqlError);
	//}
	
}//class file