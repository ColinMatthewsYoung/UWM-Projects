package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

public class MineSweapPart extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static final int WINDOW_HEIGHT = 760;
  private static final int WINDOW_WIDTH = 760;
  private static final int TOTAL_MINES = 16;
  
  
  private static int guessedMinesLeft = TOTAL_MINES;
  private static int actualMinesLeft = TOTAL_MINES;

  private static final String INITIAL_CELL_TEXT = "";
  private static final String UNEXPOSED_FLAGGED_CELL_TEXT = "@";
    private static final String EXPOSED_MINE_TEXT = "M";
  
  // visual indication of an exposed MyJButton
  private static final Color EXPOSED_CELL_BACKGROUND_COLOR = Color.lightGray;
  // colors used when displaying the getStateStr() String
  private static final Color EXPOSED_CELL_FOREGROUND_COLOR_MAP[] = {Color.lightGray, Color.blue, Color.green, Color.cyan, Color.yellow, 
                                           Color.orange, Color.pink, Color.magenta, Color.red, Color.red};

  
  // holds the "number of mines in perimeter" value for each MyJButton 
  private static final int MINEGRID_ROWS = 16;
  private static final int MINEGRID_COLS = 16;
  private int[][] mineGrid = new int[MINEGRID_ROWS][MINEGRID_COLS];

  private static final int NO_MINES_IN_PERIMETER_MINEGRID_VALUE = 0;
  private static final int ALL_MINES_IN_PERIMETER_MINEGRID_VALUE = 8;
  private static final int IS_A_MINE_IN_MINEGRID_VALUE = 9;
  
  private boolean running = true;
  
  public MineSweapPart()
  {
	  
    this.setTitle("MineSweap                                                         " + 
                  MineSweapPart.guessedMinesLeft +" Mines left");
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setResizable(false);
    this.setLayout(new GridLayout(MINEGRID_ROWS, MINEGRID_COLS, 0, 0));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    // set the grid of MyJbuttons
    this.createContents();
    
    // place MINES number of mines in sGrid and adjust all of the "mines in perimeter" values
    this.setMines();
    
    this.setVisible(true);
  }

  public void createContents()
  {
    for (int mgr = 0; mgr < MINEGRID_ROWS; ++mgr)
    {  
      for (int mgc = 0; mgc < MINEGRID_COLS; ++mgc)
      {  
        // set sGrid[mgr][mgc] entry to 0 - no mines in it's perimeter
        this.mineGrid[mgr][mgc] = NO_MINES_IN_PERIMETER_MINEGRID_VALUE; 
        
        // create a MyJButton that will be at location (mgr, mgc) in the GridLayout
        MyJButton but = new MyJButton(INITIAL_CELL_TEXT, mgr, mgc); 
        
        // register the event handler with this MyJbutton
        but.addActionListener(new MyListener());
        
        // add the MyJButton to the GridLayout collection
        this.add(but);
      }  
    }
  }


  // begin nested private class
  private class MyListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if ( running )
      {
        // used to determine if ctrl or alt key was pressed at the time of mouse action
        int mod = event.getModifiers();
        MyJButton mjb = (MyJButton)event.getSource();
        
        // is the MyJbutton that the mouse action occurred in flagged
        boolean flagged = mjb.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
        
        // is the MyJbutton that the mouse action occurred in already exposed
        boolean exposed = mjb.getBackground().equals(EXPOSED_CELL_BACKGROUND_COLOR);
       
        // flag a cell : ctrl + left click
        if ( !flagged && !exposed && (mod & ActionEvent.CTRL_MASK) != 0 )
        {
          mjb.setText(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
          --MineSweapPart.guessedMinesLeft;
          
          // if the MyJbutton that the mouse action occurred in is a mine
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
          {
        	  //removes a mine if you flag the correct spot.
        	  actualMinesLeft--;
        	  
        	  //checks if you have flagged all mines, and dont have extra flags to let you win.
        	  if(actualMinesLeft==0 && guessedMinesLeft ==0) {
        		  int result;
        		  
        	    	try {
        	    	  //I do not own this audio, but thougth it would be fun to include.
        			  //audio is cited to  Anno, Hideaki. Neon Genesis Evangelion: episode 26. Houston, TX: A.D.V. Films, 2000.
        			 
        			  File audioFile = new File("Congrats.wav");
        			  AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
        			  Clip clip = AudioSystem.getClip();
        			  clip.open(ais);
        			  clip.start();
        			  
        			  result = JOptionPane.showConfirmDialog(null, "Congratulations! You won!! Would you like to play again?" , "YOU WIN!", JOptionPane.YES_NO_OPTION);
        			  
        			  clip.close(); //stops audio once file is closed.
        	    	
        	    		}
        	    	  catch(Exception e) {
        				  //if error with audio file print win screen.
        	    		  result = JOptionPane.showConfirmDialog(null, "Congratulations! You won!! Would you like to play again?" , "YOU WIN!", JOptionPane.YES_NO_OPTION);
        			  
        		  		} 
     
        	    	  newGame(result, mjb);
        		  
        	  }
            
          }
          setTitle("MineSweap                                                         " + 
                   MineSweapPart.guessedMinesLeft +" Mines left");
        }
       
        // unflag a cell : alt + left click
        else if ( flagged && !exposed && (mod & ActionEvent.ALT_MASK) != 0 )
        {
          mjb.setText(INITIAL_CELL_TEXT);
          ++MineSweapPart.guessedMinesLeft;
          
          
          // if the MyJbutton that the mouse action occurred in is a mine
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
          {
        	  actualMinesLeft++;
        	 
            // what else do you need to adjust?
            // could the game be over?
          }
          setTitle("MineSweap                                                         " + 
                    MineSweapPart.guessedMinesLeft +" Mines left");
        }
     
        // expose a cell : left click
        else if ( !flagged && !exposed )
        {
          exposeCell(mjb);
        }  
      }
    }
    
    public void exposeCell(MyJButton mjb)
    {
      if ( !running )
        return;
      
      // expose this MyJButton 
      mjb.setBackground(EXPOSED_CELL_BACKGROUND_COLOR);
      mjb.setForeground(EXPOSED_CELL_FOREGROUND_COLOR_MAP[mineGrid[mjb.ROW][mjb.COL]]);
      mjb.setText(getGridValueStr(mjb.ROW, mjb.COL));
      
      	
  				
      // if the MyJButton that was just exposed is a mine
      if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
      {  
    	  
    	//game is over. goes though the grid and sets the mines to exposed.
    	//sets flagged mines to red. pop up to ask if you want to play again.
    	for(int i=0; i<MINEGRID_ROWS;i++) {
    		for(int j=0; j<MINEGRID_COLS; j++) {
    			int revele = i * MINEGRID_ROWS + j;
				MyJButton mjbn =(MyJButton)mjb.getParent().getComponent(revele);
				
    			if (mineGrid[i][j] == IS_A_MINE_IN_MINEGRID_VALUE){
    				
    				if(mjbn.getText().equals(UNEXPOSED_FLAGGED_CELL_TEXT) ) {
    					mjbn.setForeground(Color.red);
    				}
    				else {
    				   mjbn.setBackground(EXPOSED_CELL_BACKGROUND_COLOR);
    			       mjbn.setForeground(EXPOSED_CELL_FOREGROUND_COLOR_MAP[mineGrid[mjb.ROW][mjb.COL]]);
    			       mjbn.setText(getGridValueStr(mjb.ROW, mjb.COL));
    				}
    				
    			}
    		}
    	}
    	int result = JOptionPane.showConfirmDialog(null, "Nice try, you only had " +actualMinesLeft + " mines left. Would you like to play again?" , "Game Over", JOptionPane.YES_NO_OPTION);
		  newGame(result,mjb);

    	return;
      }
      
      // if the MyJButton that was just exposed has no mines in its perimeter
      //makes sure the button has not been exposed already by checking if the background color has changed to prevent stackoverflow
      //also checks if it is in col 0 or 15 to stop it from warping around the screen and exposing squares on the other side of the map.
      if ( mineGrid[mjb.ROW][mjb.COL] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
      {
    
    	  //get size of grid
    	  int gSize = MINEGRID_ROWS * MINEGRID_COLS;
    	  MyJButton mjbn;
    	  
    	  //finds the linear point of the button being exposed.
    	  int origin = mjb.ROW * MINEGRID_ROWS + mjb.COL;
    	  
    	  //recursive if statements. checks each square in the perimiter and will flip until it finds a non blank square.
    	  //
    	  if(origin-17>=0 && mjb.COL >0) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin-17);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     		 
     	 }
     	 if(origin-16>=0) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin-16);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 
     			 exposeCell(mjbn);
     	 }
     	 if(origin-15>=0 && mjb.COL<15) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin-15);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
     	 if(origin-1>=0&& mjb.COL >0) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin-1);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
     	 if(origin+1<=gSize-1 && mjb.COL <15) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin+1);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
     	 if(origin+15 <=gSize-1 && mjb.COL >0) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin+15);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
     	 if(origin+16<=gSize-1) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin+16);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
     	 if(origin+17<=gSize-1&& mjb.COL <15) {
     		 mjbn =(MyJButton)mjb.getParent().getComponent(origin+17);
     		 if(mjbn.getBackground() != EXPOSED_CELL_BACKGROUND_COLOR && !mjbn.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT))
     			 exposeCell(mjbn);
     	 }
    	 
      }
    }
  }
  // end nested private class


  public static void main(String[] args)
  {
    new MineSweapPart();
  }

  
  //************************************************************************************************

  // place MINES number of mines in sGrid and adjust all of the "mines in perimeter" values
  private void setMines()
  {
	  
	  Random random = new Random();
	  //loop mines into random spot on the grid.
	  for(int i = TOTAL_MINES; i >0 ; i--){
		  boolean placed = false;
	   while(!placed) {
		   //chose random point on grid
		int x = random.nextInt(MINEGRID_ROWS);
		int y = random.nextInt(MINEGRID_COLS);
		
		//make sure its not already a mine.
		if(this.mineGrid[x][y] != IS_A_MINE_IN_MINEGRID_VALUE) {
		//each if checks the 8 adjacent squares, adds 1 to its value to account for each mine its touching.
				if(x-1>=0 && y-1 >=0 && this.mineGrid[x-1][y-1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x-1][y-1]= this.mineGrid[x-1][y-1] + 1;
				
				if(x-1>=0 &&this.mineGrid[x-1][y] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x-1][y]= this.mineGrid[x-1][y] + 1;
				
				if(x-1>=0 && y+1 <=15 &&this.mineGrid[x-1][y+1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x-1][y+1]= this.mineGrid[x-1][y+1] + 1;
				
				if(y-1 >=0 &&this.mineGrid[x][y-1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x][y-1]= this.mineGrid[x][y-1] + 1;
				
				if(y+1 <=15 &&this.mineGrid[x][y+1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x][y+1]= this.mineGrid[x][y+1] + 1;
				
				if(x+1<=15 && y-1 >=0 &&this.mineGrid[x+1][y-1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x+1][y-1]= this.mineGrid[x+1][y-1] + 1;
				
				if(x+1<=15 &&this.mineGrid[x+1][y] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x+1][y]= this.mineGrid[x+1][y] + 1;
				
				if(x+1<=15 && y+1 <=15 &&this.mineGrid[x+1][y+1] != IS_A_MINE_IN_MINEGRID_VALUE)
					this.mineGrid[x+1][y+1]= this.mineGrid[x+1][y+1] + 1;

			this.mineGrid[x][y] = IS_A_MINE_IN_MINEGRID_VALUE;
			placed = true;
		}
			else
				placed = false;
		}
		
	   }
	  }
  
  
  private String getGridValueStr(int row, int col)
  {
    // no mines in this MyJbutton's perimeter
    if ( this.mineGrid[row][col] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
      return INITIAL_CELL_TEXT;
    
    // 1 to 8 mines in this MyJButton's perimeter
    else if ( this.mineGrid[row][col] > NO_MINES_IN_PERIMETER_MINEGRID_VALUE && 
              this.mineGrid[row][col] <= ALL_MINES_IN_PERIMETER_MINEGRID_VALUE )
      return "" + this.mineGrid[row][col];
    
    // this MyJButton in a mine
    else // this.mineGrid[row][col] = IS_A_MINE_IN_GRID_VALUE
      return MineSweapPart.EXPOSED_MINE_TEXT;
  }
  
  //prompts the user if they want to play again based on if they won or lost.
  private void newGame(int result, MyJButton mjb) {
		  
			  if(result == JOptionPane.YES_OPTION) {
				  
				  for(int i=0; i<MINEGRID_ROWS;i++) {
					  for(int j=0; j<MINEGRID_COLS; j++) {
					
						  mineGrid[i][j] = NO_MINES_IN_PERIMETER_MINEGRID_VALUE;
						  
						  int reset = i * MINEGRID_ROWS + j;
						  MyJButton mjbn =(MyJButton)mjb.getParent().getComponent(reset);
						  
						  mjbn.setText(INITIAL_CELL_TEXT);
						  mjbn.setBackground(new JButton().getBackground());
						  mjbn.setForeground(new JButton().getForeground());
					  }
				  }
				  guessedMinesLeft = TOTAL_MINES;
				  actualMinesLeft = TOTAL_MINES;
				  setMines();
    	
			  }
    	 
			  else {
				 this.dispose(); //closes the jframe, finishes running the program.
				  
			  }
			 
  }
  
  
  
  
  
}
