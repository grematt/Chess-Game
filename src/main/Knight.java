package main;

public class Knight extends Piece {
	
	public Knight(int xPos, int yPos, boolean color, Type type)
	{
		super(xPos, yPos, color, type);
	}
	
	public boolean validMove(Piece piece, Piece[][] board)
	{
		if(piece.getType() != Type.None)
			return false;
		if(Math.abs(this.getXPos() - piece.getXPos()) * Math.abs(this.getYPos() - piece.getYPos()) == 2) // 1 square displacement in one axis, 2 in the other, multiplied should always == 2
			return true;
		return false;
	}
	
	public boolean validCapture(Piece piece, Piece[][] board)
	{
		if(piece.getType() == Type.None)
			return false;
		if(piece.getColor() == this.getColor())
			return false;
		return validMove(new NullPiece(piece.getXPos(),piece.getYPos(),piece.getColor(), Type.None), board); //using move method to determine if the piece is in a reachable piece
	}
}
