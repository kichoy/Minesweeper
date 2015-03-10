import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {



int NUM_ROWS = 20; int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
	size(400, 400);
	textAlign(CENTER,CENTER);

	// make the manager
	Interactive.make( this );
	
	buttons = new MSButton[NUM_ROWS][NUM_COLS];
	for (int r = 0; r < NUM_ROWS; r++)
	{
		for (int c = 0; c < NUM_COLS; c++)
		{
			buttons[r][c] = new MSButton(r, c);
		}
	}

	bombs = new ArrayList<MSButton>();
	setBombs();

	// for (int r = 0; r < NUM_ROWS; r++)
	// {
	// 	for (int c = 0; c < NUM_COLS; c++)
	// 	{
	// 		println(buttons[r][c].countBombs(r, c));
	// 	}
	// }
}

public void setBombs()
{
	int numBombs = 10;
	for (int i = 0; i < numBombs; i++) 
	{
		int row = (int)(Math.random()*NUM_ROWS);
		int col = (int)(Math.random()*NUM_COLS);
		if (! bombs.contains(buttons[row][col])) //if there isn't a bomb at that spot, add a bomb there 
		{
			bombs.add(buttons[row][col]);
		}
	}
}

public void draw ()
{
	background( 0 );
	if(isWon())
		displayWinningMessage();
}

public boolean isWon()
{
	for (int r = 0; r < NUM_ROWS; r++)
	{
		for (int c = 0; c < NUM_COLS; c++)
		{
			if (buttons[r][c].isMarked())
			{
				return true;
			}
		}
	}
	return false;
}

public void displayLosingMessage()
{
	for (int r = 0; r < NUM_ROWS; r++)
	{
		for (int c = 0; c < NUM_COLS; c++)
		{
			if (bombs.contains(buttons[r][c]))
			{
				println("buttons["+r+"]["+c+"]: "+r+c);
				buttons[r][c].marked();
			}
		}
	}

	String msg = "You lose";
	int startBlock = 2;
	for(int j = 0; j < msg.length(); j++)
	{
		buttons[NUM_ROWS/2][j + startBlock].setLabel(msg.substring(j, j+1));
	}
	
}

public void displayWinningMessage()
{
 	String msg = "You win";
	int startBlock = 2;
	for(int j = 0; j < msg.length(); j++)
	{
		buttons[NUM_ROWS/2][j + startBlock].setLabel(msg.substring(j, j+1));
	}
}

public class MSButton
{
	private int r, c;
	private float x,y, width, height;
	private boolean clicked, marked;
	private String label;

	public MSButton ( int rr, int cc )
	{
		width = 400/NUM_COLS;
		height = 400/NUM_ROWS;
		r = rr;
		c = cc; 
		x = c*width;
		y = r*height;
		label = "";
		marked = clicked = false;
		Interactive.add( this ); // register it with the manager
	}

	public boolean isMarked()
	{
		return marked;
	}

	public boolean isClicked()
	{
		return clicked;
	}
	// called by manager
	
	public void mousePressed() 
	{
		clicked = true;
		if (keyPressed) 
		{
			marked = !marked; // toggle marked
		}
		else if (bombs.contains(this)) //if the button is a bomb
		{
			displayLosingMessage(); 
		}
		else if (countBombs(r, c) > 0)
		{
			setLabel(Integer.toString(countBombs(r, c))); //set the label of the button to countBombs(r, c)
		}
		else 
		{
			for (int i = -1; i <= 1; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					//check if the neighbors are valid and have not been clicked
					if (isValid(r + i, c + j) && buttons[r + i][c + j].clicked == false) 
					{
						//if (!bombs.contains(buttons[r + i][c + j]))//Checks if it does NOT have a bomb.
						buttons[r + i][c + j].mousePressed();
					}
				}
			}
			// if (isValid(r+1, c))
			// {
			// 	buttons[r+1][c].mousePressed();	
			// }
			// if (isValid(r-1, c))
			// {
			// 	buttons[r-1][c].mousePressed();	
			// }
			// if (isValid(r, c+1)) 
			// {
			// 	buttons[r][c+1].mousePressed();	
			// }
			// if (isValid(r, c-1))
			// {
			// 	buttons[r][c-1].mousePressed();	
			// }
		}
	}

	public void draw () 
	{
		if (marked)
			fill(0);
		else if( clicked && bombs.contains(this) ) 
			fill(255,0,0);
		else if(clicked)
			fill( 200 );
		else 
			fill( 100 );
			rect(x, y, width, height);
			fill(0);
			text(label,x+width/2,y+height/2);
	}

	public void setLabel(String newLabel)
	{
		label = newLabel;
	}

	public boolean isValid(int r, int c) // if given coord is a valid square on grid
	{
		if ((r >= 0 && r < NUM_ROWS) && (c >= 0 && c < NUM_COLS)) 
		{
			return true;
		}
		return false;
	}

	public int countBombs(int row, int col) // counts number of bombs touching the square
	{
		int numBombs = 0;
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if ((isValid(row+i, col+j)) && !((r + i == r) && (c + j == c)))
				{
					if (bombs.contains(buttons[row+i][col+j]))
						numBombs++;
				}
			}
		}
		return numBombs;
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
