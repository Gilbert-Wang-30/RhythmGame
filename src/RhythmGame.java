import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
	
public class RhythmGame extends JPanel implements KeyListener, MouseListener, Runnable
{
	// to store current position
    Long currentFrame;
    Clip clip;
    // current status of clip
    String status;
    AudioInputStream audioInputStream;
	// Images
	BufferedImage menuScreen;
	BufferedImage finalBG;
	BufferedImage Intro;
	// Stores the lineNumber I have in the text file
	public static int lineNumber = 0;
	// Stores the time
	public static int timer = 0;
	// Score
	public static int Score = 0;
	public static double delay = 5.12;
	/* 0 <- menu screen
	 * 1 <- in game
	 * 2 <- instruction
	 * 3 <- score screen
	 */
	public static int gameState = 0;
	public static int noteNumber =0;
	// hits
	public static int perfect = 0;
	public static int good = 0;
	public static int bad = 0;
	public static int mouseX;
	public static int mouseY;
	// Arrays that stores all the notes
	public static boolean[] notesD;
	public static boolean[] notesF;
	public static boolean[] notesJ;
	public static boolean[] notesK;
	public static boolean[] noteD;
	public static boolean[] noteF;
	public static boolean[] noteJ;
	public static boolean[] noteK;
	// If the key is pressed
	public static boolean pressedD = false;
	public static boolean pressedF = false;
	public static boolean pressedJ = false;
	public static boolean pressedK = false;
	// If the key is released
	public static boolean releasedD = true;
	public static boolean releasedF = true;
	public static boolean releasedJ = true;
	public static boolean releasedK = true;
	// If the player is able to score
	public static boolean scoreD = true;
	public static boolean scoreF = true;
	public static boolean scoreJ = true;
	public static boolean scoreK = true;		
	
	
	public RhythmGame() 
	{
		// JPanel Default Settings
		setPreferredSize(new Dimension(960, 640));
		setBackground(new Color(0,0,0));
		this.setFocusable(true);
		// Add the Listeners into the panel
		addKeyListener(this);
		addMouseListener(this);
		Thread thread = new Thread(this);
		thread.start();
		try 
		{
			menuScreen = ImageIO.read(new File("menuScreen.jpg"));
			finalBG = ImageIO.read(new File("finalBG.jpg"));
			Intro = ImageIO.read(new File("Instructions.png"));
		}
		catch(Exception e)
		{
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		JFrame frame = new JFrame("Gilbert Wang 4 - Rhythm Game");
		RhythmGame panel = new RhythmGame();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		Scanner inputFile = new Scanner(new File("music1.txt"));
		String newline;
		notesD= new boolean[0];
		notesF= new boolean[0];
		notesJ= new boolean[0];
		notesK= new boolean[0];
		while(inputFile.hasNextLine())
		{
			lineNumber +=1;
			newline = inputFile.nextLine();
			notesD = readNotesD(newline, notesD);
			notesF = readNotesF(newline, notesF);
			notesJ = readNotesJ(newline, notesJ);
			notesK = readNotesK(newline, notesK);
		}
		noteD= new boolean[lineNumber];
		noteF= new boolean[lineNumber];
		noteJ= new boolean[lineNumber];
		noteK= new boolean[lineNumber];
		for(int i = 0; i < lineNumber; i++)
		{
			if(notesD[i])
				noteNumber++;
			if(notesF[i])
				noteNumber++;
			if(notesJ[i])
				noteNumber++;
			if(notesK[i])
				noteNumber++;
			noteD[i] = notesD[i];
			noteF[i] = notesF[i];
			noteJ[i] = notesJ[i];
			noteK[i] = notesK[i];
		}
	}
	
	public void paintComponent(Graphics g)
	{
		if(gameState == 0)
		{
			g.drawImage(menuScreen, 0, 0, null);
		}
		else if(gameState ==1)
		{
			// Clear Screen
			super.paintComponent(g);
			g.setColor(new Color(7, 230, 218));
			g.fillRect(479, 0, 2, 640);
			g.fillRect(329, 0, 2, 640);
			g.fillRect(179, 0, 2, 640);
			g.fillRect(629, 0, 2, 640);
			g.fillRect(779, 0, 2, 640);

			g.setColor(new Color(245, 245, 7));
			for(int i=0; i<lineNumber; i++)
			{
				if(notesD[i])
				{
					g.fillRect(181, 560+ (int)Math.round(timer/delay*50)-i*50, 148, 40);
				}
				if(notesF[i])
				{
					g.fillRect(331, 560+ (int)Math.round(timer/delay*50)-i*50, 148, 40);					
				}
				if(notesJ[i])
				{
					g.fillRect(481, 560+ (int)Math.round(timer/delay*50)-i*50, 148, 40);
				}
				if(notesK[i])
				{
					g.fillRect(631, 560+ (int)Math.round(timer/delay*50)-i*50, 148, 40);
				}
				
			}
			
			g.setColor(new Color(255, 255, 255));
			if(pressedD)
			{
				g.fillRect(181, 561, 149, 39);
				if(scoreD&&releasedD)
				{
					if((int)Math.round(timer/delay)<lineNumber&&notesD[(int)Math.round(timer/delay)])
					{
					Score+=10;
					perfect++;
					notesD[(int)Math.round(timer/delay)] =false;
					}
					else if((int)Math.round(timer/delay-0.5)<lineNumber&&((int)Math.round(timer/delay-0.5))>0&&notesD[(int)Math.round(timer/delay-0.5)])
					{
					Score+=5;
					good++;
					notesD[(int)Math.round(timer/delay-0.5)] =false;
					}
					else if((int)Math.round(timer/delay+0.5)<lineNumber&&notesD[(int)Math.round(timer/delay+0.5)])
					{
					Score+=5;
					good++;
					notesD[(int)Math.round(timer/delay+0.5)] =false;
					}
					else if((int)Math.round(timer/delay-1)<lineNumber&&(int)Math.round(timer/delay-1)>0&&notesD[(int)Math.round(timer/delay-1)])
					{
					Score+=1;
					bad++;
					notesD[(int)Math.round(timer/delay-1)] =false;
					}
					else if((int)Math.round(timer/delay+1)<lineNumber&&notesD[(int)Math.round(timer/delay+1)])
					{
					Score+=1;
					bad++;
					notesD[(int)Math.round(timer/delay+1)] =false;
					}
					scoreD = false;
				}
			}
			if(pressedF)
			{
				g.fillRect(331, 561, 149, 39);
				if(scoreF&&releasedF)
				{
					if((int)Math.round(timer/delay)<lineNumber&&notesF[(int)Math.round(timer/delay)])
					{
					Score+=10;
					perfect++;
					notesF[(int)Math.round(timer/delay)] =false;
					}
					else if((int)Math.round(timer/delay-0.5)<lineNumber&&((int)Math.round(timer/delay-0.5))>0&&notesF[(int)Math.round(timer/delay-0.5)])
					{
					Score+=5;
					good++;
					notesF[(int)Math.round(timer/delay-0.5)] =false;
					}
					else if((int)Math.round(timer/delay+0.5)<lineNumber&&notesF[(int)Math.round(timer/delay+0.5)])
					{
					Score+=5;
					good++;
					notesF[(int)Math.round(timer/delay+0.5)] =false;
					}
					else if((int)Math.round(timer/delay-1)<lineNumber&&(int)Math.round(timer/delay-1)>0&&notesF[(int)Math.round(timer/delay-1)])
					{
					Score+=1;
					bad++;
					notesF[(int)Math.round(timer/delay-1)] =false;
					}
					else if((int)Math.round(timer/delay+1)<lineNumber&&notesF[(int)Math.round(timer/delay+1)])
					{
					Score+=1;
					bad++;
					notesF[(int)Math.round(timer/delay+1)] =false;
					}
					scoreF = false;
				}
			}
			if(pressedJ)
			{
				g.fillRect(481, 561, 149, 39);
				if(scoreJ&&releasedJ)
				{
					if((int)Math.round(timer/delay)<lineNumber&&notesJ[(int)Math.round(timer/delay)])
					{
					Score+=10;
					perfect++;
					notesJ[(int)Math.round(timer/delay)] =false;
					}
					else if((int)Math.round(timer/delay-0.5)<lineNumber&&((int)Math.round(timer/delay-0.5))>0&&notesJ[(int)Math.round(timer/delay-0.5)])
					{
					Score+=5;
					good++;
					notesJ[(int)Math.round(timer/delay-0.5)] =false;
					}
					else if((int)Math.round(timer/delay+0.5)<lineNumber&&notesJ[(int)Math.round(timer/delay+0.5)])
					{
					Score+=5;
					good++;
					notesJ[(int)Math.round(timer/delay+0.5)] =false;
					}
					else if((int)Math.round(timer/delay-1)<lineNumber&&(int)Math.round(timer/delay-1)>0&&notesJ[(int)Math.round(timer/delay-1)])
					{
					Score+=1;
					bad++;
					notesJ[(int)Math.round(timer/delay-1)] =false;
					}
					else if((int)Math.round(timer/delay+1)<lineNumber&&notesJ[(int)Math.round(timer/delay+1)])
					{
					Score+=1;
					bad++;
					notesJ[(int)Math.round(timer/delay+1)] =false;
					}
					scoreJ = false;
				}
			}
			if(pressedK)
			{
				g.fillRect(631, 561, 149, 39);
				if(scoreK&&releasedK)
				{
					if((int)Math.round(timer/delay)<lineNumber&&notesK[(int)Math.round(timer/delay)])
					{
					Score+=10;
					perfect++;
					notesK[(int)Math.round(timer/delay)] =false;
					}
					else if((int)Math.round(timer/delay-0.5)<lineNumber&&((int)Math.round(timer/delay-0.5))>0&&notesK[(int)Math.round(timer/delay-0.5)])
					{
					Score+=5;
					good++;
					notesK[(int)Math.round(timer/delay-0.5)] =false;
					}
					else if((int)Math.round(timer/delay+0.5)<lineNumber&&notesK[(int)Math.round(timer/delay+0.5)])
					{
					Score+=5;
					good++;
					notesK[(int)Math.round(timer/delay+0.5)] =false;
					}
					else if((int)Math.round(timer/delay-1)<lineNumber&&(int)Math.round(timer/delay-1)>0&&notesK[(int)Math.round(timer/delay-1)])
					{
					Score+=1;
					bad++;
					notesK[(int)Math.round(timer/delay-1)] =false;
					}
					else if((int)Math.round(timer/delay+1)<lineNumber&&notesK[(int)Math.round(timer/delay+1)])
					{
					Score+=1;
					bad++;
					notesK[(int)Math.round(timer/delay+1)] =false;
					}
					scoreK = false;
				}
			}
			g.setColor(new Color(218, 247, 247));
			g.fillRect(179, 560, 602, 2);
			g.fillRect(179, 600, 602, 2);
			g.setColor(new Color(0, 0, 0));
			g.fillRect(179, 601, 602, 40);
			// Score
			g.setColor(new Color(45, 135, 237));
			g.setFont(new Font("Impact", Font.ITALIC, 30));
			g.drawString("Score",790, 25);
			g.setColor(new Color(45, 135, 237));
			g.setFont(new Font("Impact", Font.ITALIC, 25));
			g.drawString(""+Score,790, 56);
			timer++;
		}			
		else if(gameState ==2)
		{
			super.paintComponent(g);
			g.drawImage(Intro, 0, 0, null);
		}
		else if(gameState ==3)
		{
			super.paintComponent(g);
			g.drawImage(finalBG, 0, 0, null);
			g.setColor(new Color(250,250,0));
			g.setFont(new Font("Impact", Font.PLAIN, 38));
			g.drawString(""+Score,300, 108);
			g.setColor(new Color(252,144,3));
			g.setFont(new Font("Serif", Font.BOLD, 28));
			g.drawString(""+perfect,240, 214);
			g.setColor(new Color(252,200,3));
			g.drawString(""+good,240, 254);
			g.setColor(new Color(71, 97, 245));
			g.drawString(""+bad,240, 295);
			g.setColor(new Color(109, 3, 171));
			g.drawString(""+ (noteNumber - perfect - good - bad),240, 338);
			g.setFont(new Font("Serif", Font.BOLD, 300));
			if(Score>=1000)
			{
				g.setColor(new Color(255,223,0));
				g.drawString("S",684, 380);
			}
			else if(Score>=800)
			{
				g.setColor(new Color(245, 99, 2));
				g.drawString("A",670, 380);
			}
			else if(Score>=600)
			{
				g.setColor(new Color(245, 2, 140));
				g.drawString("B",672, 380);
			}
			else if(Score>=400)
			{
				g.setColor(new Color(160, 2, 245));
				g.drawString("C",655, 380);
			}
			else if(Score>=200)
			{
				g.setColor(new Color(2, 6, 245));
				g.drawString("D",672, 380);
			}
			else if(Score>=0)
			{
				g.setColor(new Color(2, 245, 38));
				g.drawString("E",665, 380);
			}
		}
	}
	
	public void run() {
		while(true)
		{
			repaint();
			try
			{
				Thread.sleep(10);
				if((int)Math.round(timer/5-0.5)-5>lineNumber)
				{
					gameState = 3;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar()== 'd'&& releasedD)
		{
			pressedD= true;		
		}
		if(e.getKeyChar()== 'f'&& releasedF)
		{
			pressedF = true;
		}
		if(e.getKeyChar()== 'j' && releasedJ)
		{
			pressedJ = true;
		}
		if(e.getKeyChar()== 'k' && releasedK)
		{
			pressedK = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar()== 'd')
		{
			releasedD = true;
			pressedD = false;
			scoreD = true;
		}
		if(e.getKeyChar()== 'f')
		{
			releasedF = true;
			pressedF = false;
			scoreF = true;
		}
		if(e.getKeyChar()== 'j')
		{
			releasedJ = true;
			pressedJ = false;
			scoreJ = true;
		}
		if(e.getKeyChar()== 'k')
		{
			releasedK = true;
			pressedK = false;
			scoreK = true;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if(gameState ==0)
		{
			if(mouseX>= 350 && mouseX <= 610 && mouseY >= 400 && mouseY <= 490)
			{
				gameState = 1;
				paintComponent(this.getGraphics());
				try
		        {
		            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer();
		            audioPlayer.play();
		        } 
		        catch (Exception ex) 
		        {
		        }
			}
			if(mouseX>= 350 && mouseX <= 610 && mouseY >= 530 && mouseY <= 610)
			{
				gameState = 2;
				paintComponent(this.getGraphics());
			}
		}
		else if(gameState ==2)
		{
			if(mouseX>= 840 && mouseX <= 960 && mouseY >= 600 && mouseY <= 640)
			{
				gameState = 0;
				paintComponent(this.getGraphics());
			}
		}
		else if(gameState ==3)
		{
			if(mouseX>= 250 && mouseX <= 415 && mouseY >= 500 && mouseY <= 550)
			{
				gameState = 1;
				timer = 0;
				Score = 0;
				perfect = 0;
				good = 0;
				bad = 0;
				try
		        {
		            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer();
		            audioPlayer.play();
		        } 
		        catch (Exception ex) 
		        {
		        }
				for(int i = 0; i < lineNumber; i++)
				{
					notesD[i] = noteD[i];
					notesF[i] = noteF[i];
					notesJ[i] = noteJ[i];
					notesK[i] = noteK[i];
				}
				paintComponent(this.getGraphics());
			}
			if(mouseX>= 560 && mouseX <= 720 && mouseY >= 500 && mouseY <= 550)
			{
				gameState = 0;
				timer = 0;
				Score = 0;
				perfect = 0;
				good = 0;
				bad = 0;
				for(int i = 0; i < lineNumber; i++)
				{
					notesD[i] = noteD[i];
					notesF[i] = noteF[i];
					notesJ[i] = noteJ[i];
					notesK[i] = noteK[i];
				}
				paintComponent(this.getGraphics());
			}
		}
	}
	
	// These methods modifies the notes array from what is written in the text file
	public static boolean[] readNotesD(String str, boolean[] a)
	{
		boolean[] newArr = new boolean[a.length + 1];
		// Copy every element from the original array a, 
		// to the new array newArr
		for(int i = 0 ; i < a.length; i++)
		{
			newArr[i] = a[i];
		}
		newArr[newArr.length-1] = (Integer.parseInt(str.substring(0,1))==1);
		return newArr;
	}
	
	public static boolean[] readNotesF(String str, boolean[] a)
	{
		boolean[] newArr = new boolean[a.length + 1];
		// Copy every element from the original array a, 
		// to the new array newArr
		for(int i = 0 ; i < a.length; i++)
		{
			newArr[i] = a[i];
		}
		newArr[newArr.length-1] = (Integer.parseInt(str.substring(2,3))==1);
		return newArr;
	}
	
	public static boolean[] readNotesJ(String str, boolean[] a)
	{
		boolean[] newArr = new boolean[a.length + 1];
		// Copy every element from the original array a, 
		// to the new array newArr
		for(int i = 0 ; i < a.length; i++)
		{
			newArr[i] = a[i];
		}
		newArr[newArr.length-1] = (Integer.parseInt(str.substring(4,5))==1);
		return newArr;
	}

	public static boolean[] readNotesK(String str, boolean[] a)
	{
		boolean[] newArr = new boolean[a.length + 1];
		// Copy every element from the original array a, 
		// to the new array newArr
		for(int i = 0 ; i < a.length; i++)
		{
			newArr[i] = a[i];
		}
		newArr[newArr.length-1] = (Integer.parseInt(str.substring(6,7))==1);
		return newArr;
	}
	
	// Useless method
	public void keyTyped(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}