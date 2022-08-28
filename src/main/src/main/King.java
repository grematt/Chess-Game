package main;

public class King extends Piece {

    public King(int xPos, int yPos, boolean color, Type type)
    {
        super(xPos, yPos, color, type);
    }

    public King(int xPos, int yPos, boolean color, Type type, boolean hasMoved)
    {
        super(xPos, yPos, color, type);
        this.setHasMoved(hasMoved);
    }

    public boolean validMove(Piece piece, Piece[][] board)
    {
        if(piece.getType() != Type.None)
            return false;
        if(validCastle(piece, board))
            return true;
        if(piece.getXPos() == this.getXPos()-1 && piece.getYPos() == this.getYPos()+1) //top left
            return true;
        if(piece.getXPos() == this.getXPos() && piece.getYPos() == this.getYPos()+1) //top
            return true;
        if(piece.getXPos() == this.getXPos()+1 && piece.getYPos() == this.getYPos()+1) //top right
            return true;
        if(piece.getXPos() == this.getXPos()+1 && piece.getYPos() == this.getYPos()) //right 
            return true;
        if(piece.getXPos() == this.getXPos()+1 && piece.getYPos() == this.getYPos()-1) //bottom right
            return true;
        if(piece.getXPos() == this.getXPos() && piece.getYPos() == this.getYPos()-1) //bottom
            return true;
        if(piece.getXPos() == this.getXPos()-1 && piece.getYPos() == this.getYPos()-1) //bottom left
            return true;
        if(piece.getXPos() == this.getXPos()-1 && piece.getYPos() == this.getYPos()) //left
            return true;
        return false;
    }

    public boolean validCapture(Piece piece, Piece[][] board)
    {
        if(piece.getType() == Type.None)
            return false;
        if(piece.getColor() == this.getColor())
            return false;
        return validMove(new NullPiece(piece.getXPos(),piece.getYPos(),piece.getColor(), Type.None),board); 
    }

    public boolean validCastle(Piece piece, Piece[][] board)
    {
        if(this.getHasMoved())
            return false;
        if(piece.getType() != Type.None)
            return false;
        if(piece.getXPos() == this.getXPos() || piece.getYPos() != this.getYPos())
            return false;
        if(this.inCheck(board))
            return false;
        int x = this.getXPos();
        int y = this.getYPos();
        if(piece.getXPos() > x) //castle to the right
        {
            if(board[x+1][y].getType() != Type.None || board[x+2][y].getType() != Type.None)
                return false;
            King dummyKing = new King(x+1,y,this.getColor(),Type.King);
            if(dummyKing.inCheck(board)) // checking if square the king moves through would be in check
                return false;
            Piece rook = board[7][y];
            if(rook.getType() != Type.Rook || rook.getHasMoved())
                return false;
            return rook.validMove(board[x+1][y], board);
        }
        if(board[x-1][y].getType() != Type.None || board[x-2][y].getType() != Type.None) //castle to the left
            return false;
        King dummyKing = new King(x-1,y,this.getColor(),Type.King);
        if(dummyKing.inCheck(board))
            return false;
        Piece rook = board[0][y];
        if(rook.getType() != Type.Rook || rook.getHasMoved())
            return false;
        return rook.validMove(board[x-1][y], board);
    }

    public boolean inCheck(Piece[][] board)
    {
        for(Piece pieceArr[]:board)
        {
            for(Piece piece:pieceArr)
            {
                if(piece.validCapture(this, board))
                    return true;
            }
        }
        return false;
    }

}
