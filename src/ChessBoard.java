import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.NullPiece;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Type;

public class ChessBoard {
	private Piece[][] board;
	
	public ChessBoard()
	{
		board = new Piece[8][8];
		fillBoard();
	}
	
	public Piece[][] getBoard()
	{
		return this.board;
	}
	
	public void setBoard(Piece[][] board)
	{
		this.board = board;
	}
	
	public King getWhiteKing()
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(board[i][j].getType() == Type.King && board[i][j].getColor())
					return (King) board[i][j];
		return null;
	}
	
	public King getBlackKing()
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(board[i][j].getType() == Type.King && !board[i][j].getColor())
					return (King) board[i][j];
		return null;
	}
	
	public boolean move(int xPos1, int yPos1, int xPos2, int yPos2, int moveNum, Piece[][] undoBoard)
	{
		if(xPos1<0 || xPos1>7 || xPos2<0 || xPos2>7 || yPos1<0 || yPos1>7 || yPos2<0 || yPos2>7)
			return false; 
		Piece current  = board[xPos1][yPos1];
		Piece target = board[xPos2][yPos2];
		// pawn needs a separate if statement because of en passant. The alternative is to pass moveNum to the move method for every piece and just leave it unused
		if(current.getType() == Type.Pawn) 
		{
			Pawn pawn = (Pawn) current;
			if(!pawn.validCapture(target, board) && !pawn.validMove(target, board, moveNum))
				return false;
		}
		else if(!current.validCapture(target, board) && !current.validMove(target, board))
			return false;
		King myKing;
		if(current.getColor())
			myKing = this.getWhiteKing();
		else
			myKing = this.getBlackKing();
		forceMove(xPos1, yPos1, xPos2, yPos2);
		if(myKing.inCheck(board))
		{
			this.setBoard(undoBoard);
			return false;
		}
		current.setHasMoved(true);
		return true;
	}
	
	// method for making a move which according to the board, is invalid. Used for 
	// situations such as castling where the rook moves through the king
	public void forceMove(int xPos1, int yPos1, int xPos2, int yPos2) 
	{
		Piece current  = board[xPos1][yPos1];
		current.setXPos(xPos2);
		current.setYPos(yPos2);
		board[xPos2][yPos2] = current;
		board[xPos1][yPos1] = new NullPiece(xPos1, yPos1, false, Type.None);
	}
	
	private void fillBoard()
	{
		board[0][0] = new Rook(0,0,true,Type.Rook);
		board[1][0] = new Knight(1,0,true,Type.Knight);
		board[2][0] = new Bishop(2,0,true,Type.Bishop);
		board[3][0] = new Queen(3,0,true,Type.Queen);
		board[4][0] = new King(4,0,true,Type.King);
		board[5][0] = new Bishop(5,0,true,Type.Bishop);
		board[6][0] = new Knight(6,0,true,Type.Knight);
		board[7][0] = new Rook(7,0,true,Type.Rook);
		for(int i=0;i<8;i++)
			board[i][1] = new Pawn(i,1,true,Type.Pawn);
		board[0][7] = new Rook(0,7,false,Type.Rook);
		board[1][7] = new Knight(1,7,false,Type.Knight);
		board[2][7] = new Bishop(2,7,false,Type.Bishop);
		board[3][7] = new Queen(3,7,false,Type.Queen);
		board[4][7] = new King(4,7,false,Type.King);
		board[5][7] = new Bishop(5,7,false,Type.Bishop);
		board[6][7] = new Knight(6,7,false,Type.Knight);
		board[7][7] = new Rook(7,7,false,Type.Rook);
		for(int i=0;i<8;i++)
			board[i][6] = new Pawn(i,6,false,Type.Pawn);
		for(int i=0;i<8;i++)
			for(int j=2;j<6;j++)
				board[i][j] = new NullPiece(i,j,false,Type.None);
		
	}
	
	public Piece[][] copyBoard(Piece[][] board) // make a copy of a board without copying references
	{
		Piece[][] undoBoard = new Piece[board.length][board[0].length];
		for(int i=0;i<8;i++) // populating undoBoard, need the pieces in undoBoard to store the same values as board.getBoard() but different references
    		for(int j=0;j<8;j++)
    		{
    			Type type = board[i][j].getType();
    			switch(type) 
    			{
	    			case King:
	    				undoBoard[i][j] = new King(i,j,board[i][j].getColor(),Type.King,board[i][j].getHasMoved());
	    				break;
	    			case Pawn:
	    			{
	    				Pawn pawn = (Pawn) board[i][j];
	    				undoBoard[i][j] = new Pawn(i,j,board[i][j].getColor(),Type.Pawn,pawn.getMoveNum(),pawn.hasMovedDouble(),pawn.getWhenEnPass(),pawn.getHasMoved());
	    				break;
	    			}
	    			case Knight:
	    				undoBoard[i][j] = new Knight(i,j,board[i][j].getColor(),Type.Knight);
	    				break;
	    			case Bishop:
	    				undoBoard[i][j] = new Bishop(i,j,board[i][j].getColor(),Type.Bishop);
	    				break;
	    			case Queen:
	    				undoBoard[i][j] = new Queen(i,j,board[i][j].getColor(),Type.Queen);
	    				break;
	    			case Rook:
	    				undoBoard[i][j] = new Rook(i,j,board[i][j].getColor(),Type.Rook,board[i][j].getHasMoved());
	    				break;
	    			default:
	    				undoBoard[i][j] = new NullPiece(i,j,board[i][j].getColor(),Type.None);
	    				break;
    			}
    		}
		return undoBoard;
	}
	
}