package main;

public class Pawn extends Piece {
	private int moveNum;
	private boolean movedDouble;
	private int whenEnPass;
	
	public Pawn(int xPos, int yPos, boolean color, Type type)
	{
		super(xPos, yPos, color, type);
	}
	
	public Pawn(int xPos, int yPos, boolean color, Type type, int moveNum, boolean movedDouble, int whenEnPass, boolean hasMoved)
	{
		super(xPos, yPos, color, type);
		this.moveNum = moveNum;
		this.movedDouble = true;
		this.whenEnPass = whenEnPass;
		this.setHasMoved(hasMoved);
	}
	
	public int getMoveNum()
	{
		return this.moveNum;
	}
	
	public boolean hasMovedDouble()
	{
		return this.movedDouble;
	}
	
	public int getWhenEnPass()
	{
		return this.whenEnPass;
	}
	
	public boolean validMove(Piece piece, Piece[][] board, int moveNum) //is the pawn moving 1 or 2 squares forward
	{
		if(piece.getType() != Type.None) //the square contains a piece so the current piece cannot be moved to said square
			return false;
		if(this.enPassnat(piece, board, moveNum))
			return true;
		if(piece.getXPos() != this.getXPos())
			return false;
		if(this.getColor() == true && !this.getHasMoved() && piece.getYPos() == this.getYPos()+2) //allows for move of 2 squares on first move
		{
			if(board[this.getXPos()][this.getYPos()+1].getType() == Type.None)
			{
				this.moveNum = moveNum;
				this.movedDouble = true;
				return true;
			}
		}
		if(this.getColor() == false && !this.getHasMoved() && piece.getYPos() == this.getYPos()-2) //black pawn 2 square move
		{
			if(board[this.getXPos()][this.getYPos()-1].getType() == Type.None)
			{
				this.moveNum = moveNum;
				this.movedDouble = true;
				return true;
			}
		}
		if(this.getColor() == true && piece.getYPos() != this.getYPos()+1) //pawns move forwards one square
			return false;
		if(this.getColor() == false && piece.getYPos() != this.getYPos()-1) //black pawns start on line 8 (index 7) so a move forward is negative in yPos
			return false;
		return true;
	}
	
	public boolean validCapture(Piece piece, Piece[][] board) //check if piece being captured diagonally adjacent to the pawn
	{ 
		if(piece.getType() == Type.None) 
			return false;
		if(piece.getColor() == this.getColor()) //cannot capture same color piece
			return false;
		if(piece.getXPos() != this.getXPos()+1 && piece.getXPos() != this.getXPos()-1) //for both colors, a pawn capture will shift xPos by +1 or -1
			return false;
		if(this.getColor() == true && piece.getYPos() != this.getYPos()+1)
			return false;
		if(this.getColor() == false && piece.getYPos() != this.getYPos()-1)
			return false;
		return true;
		
		//pawns validCapture method contains extra code when compared to other classes because they capture differently than how they move
	}

	
	// returning false means this piece did not make an en passant move, true mean that the piece did en passant and the taken piece has been removed from the Piece[][]
	private boolean enPassnat(Piece piece, Piece[][] board, int moveNum)
	{
		Piece target;
		if(this.getColor())
		{
			if(piece.getXPos() > this.getXPos() && piece.getYPos() > this.getYPos())
				target = board[this.getXPos()+1][this.getYPos()];
			else if(piece.getXPos() < this.getXPos() && piece.getYPos() > this.getYPos())
				target = board[this.getXPos()-1][this.getYPos()];
			else
				return false;
		}
		else
		{
			if(piece.getXPos() < this.getXPos() && piece.getYPos() < this.getYPos())
				target = board[this.getXPos()-1][this.getYPos()];
			else if(piece.getXPos() > this.getXPos() && piece.getYPos() < this.getYPos())
				target = board[this.getXPos()+1][this.getYPos()];
			else
				return false;
		}
		if(target.getType() != Type.Pawn)
			return false;
		Pawn pawn = (Pawn) target;
		if(pawn.hasMovedDouble() && pawn.getMoveNum() == moveNum-1)
		{
			board[pawn.getXPos()][pawn.getYPos()] = new NullPiece(pawn.getXPos(),pawn.getYPos(),false,Type.None); // remove the pawn being captured
			this.whenEnPass = moveNum;
			return true;
		}
		return false;
	}
	
	// unused method
	@Override
	public boolean validMove(Piece piece, Piece[][] board) {
		// TODO Auto-generated method stub
		return false;
	}

}
