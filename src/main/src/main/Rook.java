package main;

public class Rook extends Piece {

    public Rook(int xPos, int yPos, boolean color, Type type)
    {
        super(xPos, yPos, color, type);
    }

    public Rook(int xPos, int yPos, boolean color, Type type, boolean hasMoved)
    {
        super(xPos, yPos, color, type);
        this.setHasMoved(hasMoved);
    }

    public boolean validMove(Piece piece, Piece[][] board)
    {
        if(piece.getType() != Type.None)
            return false;
        if(this.getXPos() != piece.getXPos() && this.getYPos() != piece.getYPos())
            return false;
        if(this.getXPos() == piece.getXPos() && this.getYPos() > piece.getYPos()) // target piece is down
        {
            int count = Math.abs(this.getYPos() - piece.getYPos())-1;
            int x = this.getXPos();
            int y = this.getYPos()-1;
            while(count>0)
            {
                if(board[x][y].getType() != Type.None && board[x][y] != piece)
                    return false;
                count--;
                y--;
            }
            return true;
        }
        if(this.getXPos() == piece.getXPos() && this.getYPos() < piece.getYPos()) // target piece is up
        {
            int count = Math.abs(this.getYPos() - piece.getYPos())-1;
            int x = this.getXPos();
            int y = this.getYPos()+1;
            while(count>0)
            {
                if(board[x][y].getType() != Type.None && board[x][y] != piece)
                    return false;
                count--;
                y++;
            }
            return true;
        }
        if(this.getXPos() > piece.getXPos() && this.getYPos() == piece.getYPos()) // target piece is left
        {
            int count = Math.abs(this.getXPos() - piece.getXPos())-1;
            int x = this.getXPos()-1;
            int y = this.getYPos();
            while(count>0)
            {
                if(board[x][y].getType() != Type.None && board[x][y] != piece)
                    return false;
                count--;
                x--;
            }
            return true;
        }
        int count = Math.abs(this.getXPos() - piece.getXPos())-1; // target piece is right
        int x = this.getXPos()+1;
        int y = this.getYPos();
        while(count>0)
        {
            if(board[x][y].getType() != Type.None && board[x][y] != piece)
                return false;
            count--;
            x++;
        }
        return true;
    }

    public boolean validCapture(Piece piece, Piece[][] board)
    {
        if(piece.getType() == Type.None)
            return false;
        if(piece.getColor() == this.getColor())
            return false;
        return validMove(new NullPiece(piece.getXPos(),piece.getYPos(),piece.getColor(), Type.None),board);
    }

}
