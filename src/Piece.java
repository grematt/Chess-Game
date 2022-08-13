public abstract class Piece { 
	private int xPos;
	private int yPos;
	private boolean color;
	private Type type;
	private boolean hasMoved;
	
	public Piece(int xPos, int yPos, boolean color, Type type)
	{
		this.setXPos(xPos);
		this.setYPos(yPos);
		this.setColor(color);
		this.setType(type);
		this.setHasMoved(false);
	}
	
	public int getXPos()
	{
		return this.xPos;
	}
	
	public void setXPos(int xPos) 
	{
		if(xPos>=0&&xPos<8)
			this.xPos = xPos;
	}
	
	public int getYPos()
	{
		return this.yPos;
	}
	
	public void setYPos(int yPos) 
	{
		if(yPos>=0&&yPos<8)
			this.yPos = yPos;
	}
	
	public boolean getColor()
	{
		return this.color;
	}
	
	public void setColor(boolean color)
	{
		this.color = color;
	}
	
	public Type getType()
	{
		return this.type;
	}
	
	public void setType(Type type)
	{
		if(type!=null)
			this.type = type;
	}
	
	public boolean getHasMoved()
	{
		return this.hasMoved;
	}
	
	public void setHasMoved(boolean b)
	{
		this.hasMoved = b;
	}
	
	public boolean validPosition(int xPos, int yPos) // not used as BoardManager prevents out of bounds moves
	{
		return xPos >= 0 && xPos < 8 && yPos >= 0 && yPos < 8; //returns true if the position is within the bounds of the board (8x8)	
	}
	
	public abstract boolean validMove(Piece piece, Piece[][] board);
	
	public abstract boolean validCapture(Piece piece, Piece[][] board);
	
}
