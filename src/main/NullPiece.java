package main;

public class NullPiece extends Piece {
    
    public NullPiece(int xPos, int yPos, boolean color, Type type)
    {
        super(xPos, yPos, color, type);
    }
    
    public boolean validMove(Piece piece, Piece[][] board)
    {
        return false;
    }
    
    public boolean validCapture(Piece piece, Piece[][] board)
    {
        return false;
    }
}
