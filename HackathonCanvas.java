import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HackathonCanvas {
	boolean ShowText=false;
	

	int diff=100;
	int characterHorizontalVelocity=0;
	int characterVerticalVelocity=0;
	int health=1000;
	boolean showHealth=false;
	int characterX=650;
	int characterY=350;
	int powerupx=0;
	int powerupy=0;
	boolean powerUp=false;
	List<String> myList = new ArrayList<>();
	List<Integer> enemyPositions = new ArrayList<>();
	JFrame frame;
	boolean lose=false;
	int speed=20;
	int countdown=0;
	int score=0;
	public HackathonCanvas()
	{
		frame = new JFrame("Hackathon");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(2000,2000);
		frame.add(new DrawCharacter());
		frame.setVisible(true);
		frame.isResizable();
	}
	
	public class DrawCharacter extends JPanel implements KeyListener{
		private BufferedImage character,background,enemy, power;
		
		// constructor
		public DrawCharacter(){
			setBackground( new Color(0,0,0) );
			try{
			character=ImageIO.read(new File("spikeCharacter.png"));
			background=ImageIO.read(new File("background.png"));
			enemy=ImageIO.read(new File("UFO.png"));
			power=ImageIO.read(new File("power.png"));
			
			addKeyListener(this);
			}
			catch(IOException e){
				System.out.println("Image is not there");
			}

		}
		 @Override
			 public void paintComponent(Graphics g) {
			 	super.paintComponent(g);
			 	if (lose==true){
			 		health=0;
			 	}
			 	if (lose==false){
				 	changeCoordinatesOfCharacter();
			 	}
			 	

			 	g.drawImage(background, 0, 0, this);
				g.setColor( new Color(255, 255, 255) );
				
				if (lose==false)
				{
					g.fillRect(490,340,100,100);
					g.fillRect(890,340,100,100);
				}
				if ((int)(Math.random()*3000)==1){
					powerupx=(int)(Math.random()*1370);
					powerupy=(int)(Math.random()*720);
					powerUp=true;
					
				}
				countdown--;
			 	if (powerUp==true){
			 		g.drawImage(power, powerupx, powerupx, null, null);
			 		
			 	}
			 	powerupCheck();
				if ((int)(Math.random()*300)==1){
					int[] spawnPoint= spawnEnemyPoint();
					int enemyx=spawnPoint[0];
					int enemyy=spawnPoint[1];
					enemyPositions.add(enemyx);
					enemyPositions.add(enemyy);
					

				}

					enemyAI();
					destroy();

				for (int i=0;i<enemyPositions.size();i+=2){
					int enemyx=enemyPositions.get(i);
					int enemyy=enemyPositions.get(i+1);
					g.drawImage(enemy, enemyx,  enemyy,null,null);
				}
				g.drawImage(character, characterX, characterY ,null,null);
				if (showHealth==true){
					if (health<0){
						health=0;
					}

					int healthbar=health;
					g.setColor(Color.GREEN);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
				    Integer health = new Integer(healthbar);
					g.drawString(health.toString() ,50,50);
					
				}
				if (health<=0){
					health=0;
					g.setColor(Color.CYAN);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
					g.drawString("You LOSE!" ,600,100);
					lose=true;	
				}
				g.setColor(Color.RED);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
				g.drawString("Score: "+ score ,20 ,700);

	   	}
		 

		 
		 
		public void enemyAI(){
			for (int i=0;i<enemyPositions.size();i+=2){
				int destination=0;
				int enemyx=enemyPositions.get(i);
				int enemyy=enemyPositions.get(i+1);
				if (enemyx>710){
					destination=910;	
				}
				else
				{
					destination=510;
				}
				

				if (destination<enemyPositions.get(i)){
					enemyPositions.set(i, enemyPositions.get(i)-1);
				}
				else if (destination>enemyPositions.get(i)){
					enemyPositions.set(i, enemyPositions.get(i)+1);
				}
				if (360<enemyPositions.get(i+1)){
					enemyPositions.set(i+1, enemyPositions.get(i+1)-1);
				}
				else if (360>enemyPositions.get(i+1)){
					enemyPositions.set(i+1, enemyPositions.get(i+1)+1);
				}
				if (enemyx>800&&enemyy>1800){
					enemyPositions.set(i,1000);
					enemyPositions.set(i+1,2000);
				}
				if((enemyx == 910 && enemyy == 360) || (enemyx == 510 && enemyy == 360)){
					showHealth=true;
					health--;
				}
					
				
			}
		}
		public void destroy(){
			try{
				for (int i=0;i<enemyPositions.size();i+=2){
					if (((enemyPositions.get(i)-characterX)*(enemyPositions.get(i)-characterX))+((enemyPositions.get(i+1)-characterY)*(enemyPositions.get(i+1)-characterY))<2000){
						enemyPositions.set(i,1000);
						enemyPositions.set(i+1,2000);
						score++;
					}
				}
			}
			catch(Exception e){}
		}
		public void powerupCheck(){
			if (((powerupx-characterX)*(powerupx-characterX))+((powerupy-characterY)*(powerupy-characterY))<20000){
				powerUp=false;
				// hide the powerUP
				countdown=200;
			}
			
		}
		
		public int[] spawnEnemyPoint()
		{
			int enemyspawnwall = new Random().nextInt(4);
			int enemyspawnx= 0;
			int enemyspawny = 0;
				if(enemyspawnwall == 0){
					enemyspawny = new Random().nextInt(720) ;
				}
				else if(enemyspawnwall == 1){
					enemyspawnx = new Random().nextInt(1320);
				}
				else if(enemyspawnwall == 2){
					enemyspawnx = 1320;
					enemyspawny = new Random().nextInt(720);
				}
				else if(enemyspawnwall == 3){
					enemyspawny = 720;
					enemyspawnx = new Random().nextInt(1320);
				}
		int[] enemyspawncoordinates = {enemyspawnx, enemyspawny};
		return enemyspawncoordinates;
		}
		
		public void addNotify()
		{
			super.addNotify();
			requestFocus();
		}
	
		public void keyTyped(KeyEvent e) {

		}
	
		
	
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int key = e.getKeyCode();
			if ((key == KeyEvent.VK_LEFT)&&(myList.contains("left")==false)){
				myList.add("left");
			}
			if ((key == KeyEvent.VK_RIGHT)&&(myList.contains("right")==false)){
				myList.add("right");
			}
			if ((key == KeyEvent.VK_UP)&&(myList.contains("up")==false)){
				myList.add("up");
			}
			if ((key == KeyEvent.VK_DOWN)&&(myList.contains("down")==false)){
				myList.add("down");
			}
			if ((key == KeyEvent.VK_E)&&(myList.contains("e")==false)){
				myList.add("e");
			}
			
			

		}
	
	
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int key = e.getKeyCode();
			if ((key == KeyEvent.VK_LEFT)){
				myList.remove("left");
			}
			if ((key == KeyEvent.VK_RIGHT)){
				myList.remove("right");
			}
			if ((key == KeyEvent.VK_UP)){
				myList.remove("up");
			}
			if ((key == KeyEvent.VK_DOWN)){
				myList.remove("down");
			}
			if ((key == KeyEvent.VK_E)){
				myList.remove("e");
			}

		}
		public void changeCoordinatesOfCharacter(){
			if (countdown>0){
				speed=200;
			}
			else{
				speed=20;
			}
			if (myList.contains("up")&&characterVerticalVelocity>-150) {
			    characterVerticalVelocity-=speed;

			}
			if (myList.contains("down")&&characterVerticalVelocity<150) {
				characterVerticalVelocity+=speed;
			}
			if (myList.contains("right")&&characterHorizontalVelocity<150) {
				characterHorizontalVelocity+=speed;
			}
			if (myList.contains("left")&&characterHorizontalVelocity>-150) {
				characterHorizontalVelocity-=speed;
			}
			if (myList.contains("e")&&score>20) {
				score-=20;
				for (int i=0; i<enemyPositions.size();i++){
					enemyPositions.set(i, 3000);
				}
			}
			if (characterHorizontalVelocity<0){
				characterHorizontalVelocity++;
			}
			else if (characterHorizontalVelocity>0){
				characterHorizontalVelocity--;
			}
			if (characterVerticalVelocity<0){
				characterVerticalVelocity++;
			}
			else if (characterVerticalVelocity>0){
				characterVerticalVelocity--;
			}
			if (characterX<0){
				characterX=0;
			}
			if (characterY<0){
				characterY=0;
			}
			if (characterX>1370){
				characterX=1370;
			}
			if (characterY>720){
				characterY=720;
			}

			characterX+=characterHorizontalVelocity/50;
			characterY+=characterVerticalVelocity/50;
			

		}
	
	}
	public void delay(int x){
		try{
			Thread.sleep(x);
		}catch(Exception e){}
	
	
	}
}


	
