
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

public class GameInterface extends Frame
{
	Button[][] buttons;
	
	public GameInterface()
	{
		Color color;
		
		setTitle(" Neko!");
		setBounds(100, 100, 230, 280);
		color = new Color(200, 0, 0);
		setBackground(color);
		setLayout(null);
		buildComponents();
	}// end constructor
	
	// Construcción de los componentes de la interfaz
	private void buildComponents()
	{
		buttons			= new Button[3][3];
		buttons[0][0]	= new Button();
		buttons[0][1]	= new Button();
		buttons[0][2]	= new Button();
		buttons[1][0]	= new Button();
		buttons[1][1]	= new Button();
		buttons[1][2]	= new Button();
		buttons[2][0]	= new Button();
		buttons[2][1]	= new Button();
		buttons[2][2]	= new Button();
		
		buttons[0][0].setBounds(20, 50, 50, 50);
		add(buttons[0][0]);
		
		buttons[0][1].setBounds(90, 50, 50, 50);
		add(buttons[0][1]);
		
		buttons[0][2].setBounds(160, 50, 50, 50);
		add(buttons[0][2]);
		
		buttons[1][0].setBounds(20, 120, 50, 50);
		add(buttons[1][0]);
		
		buttons[1][1].setBounds(90, 120, 50, 50);
		add(buttons[1][1]);
		
		buttons[1][2].setBounds(160, 120, 50, 50);
		add(buttons[1][2]);
		
		buttons[2][0].setBounds(20, 190, 50, 50);
		add(buttons[2][0]);
		
		buttons[2][1].setBounds(90, 190, 50, 50);
		add(buttons[2][1]);
		
		buttons[2][2].setBounds(160, 190, 50, 50);
		add(buttons[2][2]);
	}// end buildComponents
	
	public void setActionListener(Game controller)
	{
		buttons[0][0].addActionListener(controller);
		buttons[0][1].addActionListener(controller);
		buttons[0][2].addActionListener(controller);
		buttons[1][0].addActionListener(controller);
		buttons[1][1].addActionListener(controller);
		buttons[1][2].addActionListener(controller);
		buttons[2][0].addActionListener(controller);
		buttons[2][1].addActionListener(controller);
		buttons[2][2].addActionListener(controller);
	}// end setActionListener
	
	// Inicio de el proceso de la Interfaz (aplicación)
	public void startGame()
	{
		setVisible(true);
	}// end startGame
}// end GameInterface - class
