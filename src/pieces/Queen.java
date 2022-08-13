package pieces;

public class Queen extends Piece {
	
	public Queen(int xPos, int yPos, boolean color, Type type)
	{
		super(xPos, yPos, color, type);
	}
	
	public boolean validMove(Piece piece, Piece[][] board)
	{
		if(piece.getType() != Type.None)
			return false;
		if(validDiagonalMove(piece,board))
			return true;
		return validStraightMove(piece,board);
	}
	
	public boolean validCapture(Piece piece, Piece[][] board)
	{
		if(piece.getType() == Type.None)
			return false;
		if(piece.getColor() == this.getColor())
			return false;
		return validMove(new NullPiece(piece.getXPos(),piece.getYPos(),piece.getColor(), Type.None),board);
	}
	
	private boolean validDiagonalMove(Piece piece, Piece[][] board) //bishop move method
	{
		if(Math.abs(this.getXPos() - piece.getXPos()) != Math.abs(this.getYPos() - piece.getYPos())) // check if target is on a diagonal to this piece
			return false;
		int count = Math.abs(this.getXPos() - piece.getXPos())-1; // distance diagonally target is from piece, -1 because the searching does not start on the current square
		if(this.getXPos() > piece.getXPos() && this.getYPos() > piece.getYPos()) // target square is down to the left
		{
			int x = this.getXPos()-1;
			int y = this.getYPos()-1;
			while(count>0)
			{
				if(board[x][y].getType() != Type.None && board[x][y] != piece)
					return false;
				x--;
				y--;
				count--;
			}
			return true;
		}
		if(this.getXPos() < piece.getXPos() && this.getYPos() > piece.getYPos()) // target square is down to the right
		{
			int x = this.getXPos()+1;
			int y = this.getYPos()-1;
			while(count>0)
			{
				if(board[x][y].getType() != Type.None && board[x][y] != piece)
					return false;
				x++;
				y--;
				count--;
			}
			return true;
		}
		if(this.getXPos() > piece.getXPos() && this.getYPos() < piece.getYPos()) // target square is up to the left
		{
			int x = this.getXPos()-1;
			int y = this.getYPos()+1;
			while(count>0)
			{
				if(board[x][y].getType() != Type.None && board[x][y] != piece)
					return false;
				x--;
				y++;
				count--;
			}
			return true;
		}
		int x = this.getXPos()+1; // target square is up to the right
		int y = this.getYPos()+1;
		while(count>0)
		{
			if(board[x][y].getType() != Type.None && board[x][y] != piece)
				return false;
			x++;
			y++;
			count--;
		}
		return true;
	}
	
	private boolean validStraightMove(Piece piece, Piece[][] board) //rook move method
	{
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
}
