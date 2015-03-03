
import de.bezier.guido.*;
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
	ellipse(width/2, height/2, width/2, height/2);
	text ("you lose", width/2, height/2);
}

public void displayWinningMessage()
{

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
	
	public void mousePressed () 
	{
		clicked = true;
		if (keyPressed)
		{
			marked = !marked;
		}
		else if (bombs.contains(this))
		{
			displayLosingMessage();
		}
		else if (countBombs(r, c) > 0)
		{
			setLabel(Integer.toString(countBombs(r, c)));
		}
		else 
		{
			if (isValid(r+1, c))
			{
				buttons[r+1][c].mousePressed();	
			}
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

	public int countBombs(int row, int col) //counts number of bombs touching the square
	{
		int numBombs = 0;
		for (int j = -1; j <= 1; j++)
		{
			for (int i = -1; i <= 1; i++)
			{
				if ((isValid(row+j, col+i)) && ((j != 0) && (i != 0)))
				{	
					if (bombs.contains(buttons[row+j][col+i]))
					{
						numBombs++;
					}
				}
			}	
		}
		return numBombs;
	}
}