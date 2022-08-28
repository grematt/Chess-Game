package main;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class BoardManager extends JPanel{
	/*
	 * This class is the JPanel that contains the board. The purpose of this class is to manage the location of the pieces on the board GUI by updating their positions
	 * in the board Piece[][] and reflecting that change in the GUI. 
	 */
	public static int BOARD_OFFSET = 100;
	public static int BOARD_SIZE = 800;
	public static int IMAGE_SIZE = 100;
	
	private BoardDrawer drawer;
	private ChessBoard board;
	private Point location; // location of image currently being interacted with
    private Point prevLocation;
    private MouseEvent pressed;
    private Utils util;
    private boolean turn; //true == white player's turn, false == black player's turn
    private boolean enabled;
    private int moveNum;
    private boolean gameOver;
    private boolean wait;
	
	public BoardManager()
	{
		this.setLayout(null);
		this.turn = true;
		this.moveNum = 1;
		this.gameOver = false;
		this.wait = false;
		drawer = new BoardDrawer();
		board = new ChessBoard();
		this.util = new Utils();
		populateGUI(board);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawer.paintBoard(g);
	}
	
	public void populateGUI(ChessBoard board)
	{
		Piece[][] arr = board.getBoard();
		for(int i=0;i<arr.length;i++)
		{
			for(int j=0;j<arr[i].length;j++)
			{
				Piece piece = arr[i][j];
				if(piece.getType() != Type.None)
				{
					String path = piece.getType().name();
					String color;
					if(piece.getColor())
						color = "white";
					else
						color = "black";
					path = color+path;
					JLabel img = new JLabel();
					img.setSize(IMAGE_SIZE,IMAGE_SIZE);
			    	img.setIcon(new ImageIcon(getClass().getResource("res/"+path+".png")));
			    	ClickListener clickListener = new ClickListener();
					DragListener dragListener = new DragListener();
					img.addMouseListener(clickListener);
			    	img.addMouseMotionListener(dragListener);
			    	int x = util.pieceToBoardX(piece.getXPos());
			    	int y = util.pieceToBoardY(piece.getYPos());
			    	this.add(img);
			    	img.setLocation(x,y);
				}
			}
		}
	}
	
	private class DragListener extends MouseInputAdapter {
		
		public void mouseDragged(MouseEvent e)
	    {
			if(!enabled)
				return;
	        Component image = e.getComponent();
	        location = image.getLocation(location);
	        int x = location.x - pressed.getX() + e.getX();
	        int y = location.y - pressed.getY() + e.getY();
	        image.setLocation(x, y);
	     }
	}
	
	private class ClickListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e)
	    {
			if(gameOver || wait)
			{
				enabled = false;
				return;
			}
			int x = e.getComponent().getX();
			int y = e.getComponent().getY();
			x = util.boardToPieceX(x);
			y = util.boardToPieceY(y);
			if(board.getBoard()[x][y].getColor() == turn)
			{
		    	prevLocation = e.getComponent().getLocation();
		        pressed = e;
		        enabled = true;
			}
			else
				enabled = false;
	    }
			
		public void mouseReleased(MouseEvent e)
	    {
			if(!enabled)
				return;
			Piece piece = board.getBoard()[util.boardToPieceX(prevLocation.x)][util.boardToPieceY(prevLocation.y)]; 
	    	Point target = e.getComponent().getLocation();
		    Component image = e.getComponent();
	    	int x = location.x;
	    	int y = location.y;
	    	if(x%IMAGE_SIZE == 0 && y%IMAGE_SIZE == 0) //prevents bug if dragged image has exact same position as target image
	    	{
	    		image.setLocation(x+1,y+=1);
	    		location = e.getComponent().getLocation();
	    	}
	    	Piece[][] undoBoard = board.copyBoard(board.getBoard()); // copy of current board to undo a move if needed
	    	if(util.outOfBounds(x,y) || !board.move(piece.getXPos(), piece.getYPos(), util.boardToPieceX(target.x), util.boardToPieceY(target.y), moveNum, undoBoard)) 
	    		undoImageMove(image);
	    	else //valid move
			{
				x = util.round(x);
				y = util.round(y);
				image.setLocation(x+1,y+1); // prevents bug where if x and y of image are less than the nearest hundred, the wrong image is captured.
				if(util.didEnPassnat(piece, moveNum)) //removing image of captured pawn if en passant took place
				{
					int pawnX = util.pieceToBoardX(piece.getXPos()); 
					int pawnY = util.pieceToBoardY(piece.getYPos()); 
					if(piece.getColor())
						remove(getComponentAt(pawnX,pawnY+IMAGE_SIZE));
					else
						remove(getComponentAt(pawnX,pawnY-IMAGE_SIZE));
				}
				moveNum++;
				if(piece.getType() == Type.King)
					checkForCastling(target, piece); // as when castling only the king is dragged by the player, the rook must be removed separately
				piece.setHasMoved(true);
				if(getComponentAt(x,y) != image) //remove the captured pieces JLabel
		    		remove(getComponentAt(x,y));
				location = new Point(x,y);
				image.setLocation(x,y);
				turn = !turn; //switch turns if a valid move was made
				checkPromoting();
				checkForCheckmate();
				checkForStalemate();
			}	
	    	repaint();
	    }
   
	}

	private void undoImageMove(Component image) // returns in image to its previous location
	{
		int x = prevLocation.x;
		int y = prevLocation.y;
		image.setLocation(x,y);
		location = new Point(x,y);
	}
	
	private void checkForCastling(Point target, Piece piece) // moves the rook while castling
	{
		int targetX = util.boardToPieceX(target.x);
		
		// if the difference between the initial location of the king and new location is 2, the king is castling
		if(Math.abs(util.boardToPieceX(prevLocation.x) - targetX) == 2) 
		{
			// deciding which rook to move
			if(util.boardToPieceX(prevLocation.x) > targetX && piece.getColor()) 
			{
				board.forceMove(0,0,3,0);
				getComponentAt(100,800).setLocation(400,800);
			}
			
			else if(util.boardToPieceX(prevLocation.x) < targetX && piece.getColor())
			{
				board.forceMove(7,0,5,0);
				getComponentAt(800,800).setLocation(600,800);
			}
			
			else if(util.boardToPieceX(prevLocation.x) > targetX && !piece.getColor())
			{
				board.forceMove(0,7,3,7);
				getComponentAt(100,100).setLocation(400,100);
			}
			
			else
			{
				board.forceMove(7,7,5,7);
				getComponentAt(800,100).setLocation(600,100);
			}
		}
	}

	private void checkForCheckmate()
	{
		String path;
		King myKing;
    	if(turn)
    	{
    		myKing = board.getWhiteKing();
    		path = "matedWhiteKing";
    	}
    	else
    	{
    		myKing = board.getBlackKing();
    		path = "matedBlackKing";
    	}
    	if(!board.inCheckmate(myKing, moveNum))
    		return;
    	gameOver = true;
		int x = util.pieceToBoardX(myKing.getXPos());
		int y = util.pieceToBoardY(myKing.getYPos());
		remove(getComponentAt(x,y));
		JLabel img = new JLabel();
		img.setSize(IMAGE_SIZE,IMAGE_SIZE);
    	img.setIcon(new ImageIcon(getClass().getResource("res/"+path+".png")));
    	add(img);
    	img.setLocation(x, y);
	}
	
	private void checkPromoting()
	{
		Piece[][] currentBoard = board.getBoard();
		for(int x=0;x<8;x++)
		{
			int y = -1;
			int loc = -1;
			String color = "";
			boolean promote = false;
			boolean isWhite = false;
			if(currentBoard[x][7].getType() == Type.Pawn)
			{
				y = 7;
				loc = 100;
				color = "white";
				promote = true;
				isWhite = true;
			}
			else if(currentBoard[x][0].getType() == Type.Pawn)
			{
				y = 0;
				loc = 800;
				color = "black";
				promote = true;
				isWhite = false;
			}
			if(promote)
			{
				boolean localIsWhite = isWhite;
				int localY = y;
				int localX = x;
				int localLoc = loc;
				JButton knight = new JButton("Knight");
				JButton bishop = new JButton("Bishop");
				JButton rook = new JButton("Rook");
				JButton queen = new JButton("Queen");
				add(knight);
				add(bishop);
				add(rook);
				add(queen);
				knight.setLocation(300,0);
				knight.setSize(IMAGE_SIZE,IMAGE_SIZE);
				knight.setIcon(new ImageIcon(getClass().getResource("res/"+color+"Knight.png")));
				bishop.setLocation(400,0);
				bishop.setSize(IMAGE_SIZE,IMAGE_SIZE);
				bishop.setIcon(new ImageIcon(getClass().getResource("res/"+color+"Bishop.png")));
				rook.setLocation(500,0);
				rook.setSize(IMAGE_SIZE,IMAGE_SIZE);
				rook.setIcon(new ImageIcon(getClass().getResource("res/"+color+"Rook.png")));
				queen.setLocation(600,0);
				queen.setSize(IMAGE_SIZE,IMAGE_SIZE);
				queen.setIcon(new ImageIcon(getClass().getResource("res/"+color+"Queen.png")));
				wait = true;
				knight.addActionListener(event -> {
						board.getBoard()[localX][localY] = new Knight(localX,localY,localIsWhite,Type.Knight);
						replacePromoteImage(knight,bishop,rook,queen,localX,localLoc,knight);
					});
				bishop.addActionListener(event -> {
						board.getBoard()[localX][localY] = new Bishop(localX,localY,localIsWhite,Type.Bishop);
						replacePromoteImage(knight,bishop,rook,queen,localX,localLoc,bishop);
						});
				rook.addActionListener(event -> {
						board.getBoard()[localX][localY] = new Rook(localX,localY,localIsWhite,Type.Rook);
						replacePromoteImage(knight,bishop,rook,queen,localX,localLoc,rook);
					});
				queen.addActionListener(event -> {
						board.getBoard()[localX][localY] = new Queen(localX,localY,localIsWhite,Type.Queen);
						replacePromoteImage(knight,bishop,rook,queen,localX,localLoc,queen);
					});
				repaint();
				break;
			}
		}
	}
	
	private void replacePromoteImage(JButton knight, JButton bishop, JButton rook, JButton queen, int x, int y, JButton promoteChoice)
	{
		remove(getComponentAt(util.pieceToBoardX(x),y));
		JLabel newImg = new JLabel();
		ClickListener clickListener = new ClickListener();
		DragListener dragListener = new DragListener();
		newImg.addMouseListener(clickListener);
		newImg.addMouseMotionListener(dragListener);
		newImg.setSize(IMAGE_SIZE,IMAGE_SIZE);
		newImg.setIcon(promoteChoice.getIcon());
		add(newImg);
		newImg.setLocation(util.pieceToBoardX(x),y);
		remove(knight);
		remove(bishop);
		remove(rook);
		remove(queen);
		checkForCheckmate();
		checkForStalemate();
		wait = false;
		repaint();
	}
	
	private void checkForStalemate()
	{
    	if(!board.inStalemate(moveNum, turn))
    		return;
    	gameOver = true;
    	King blackKing = board.getBlackKing();
		King whiteKing = board.getWhiteKing();
		int x = util.pieceToBoardX(whiteKing.getXPos());
		int y = util.pieceToBoardY(whiteKing.getYPos());
		remove(getComponentAt(x,y));
		JLabel img = new JLabel();
		img.setSize(IMAGE_SIZE,IMAGE_SIZE);
    	img.setIcon(new ImageIcon(getClass().getResource("res/staleWhiteKing.png")));
    	add(img);
    	img.setLocation(x, y);
    	
    	x = util.pieceToBoardX(blackKing.getXPos());
		y = util.pieceToBoardY(blackKing.getYPos());
		remove(getComponentAt(x,y));
		JLabel img2 = new JLabel();
		img2.setSize(IMAGE_SIZE,IMAGE_SIZE);
    	img2.setIcon(new ImageIcon(getClass().getResource("res/staleBlackKing.png")));
    	add(img2);
    	img2.setLocation(x, y);
	}
}
