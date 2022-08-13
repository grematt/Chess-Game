import java.awt.Color;
import java.awt.Graphics;

public class BoardDrawer {
	public static int BOARD_OFFSET = 100;
	public static int BOARD_SIZE = 800;
	public static int IMAGE_SIZE = 100;
	
	public void paintBoard(Graphics g)
	{
		Color[] swap  = {Color.gray,Color.darkGray};
		String[] rowNum = {"8","7","6","5","4","3","2","1"};
		String[] ColumnLetter = {"A","B","C","D","E","F","G","H"};
		int rowCount = 0;
		int columnCount = 0;
		for(int i=BOARD_OFFSET;i<BOARD_SIZE + IMAGE_SIZE;i+=IMAGE_SIZE)
		{
			for(int j=BOARD_OFFSET;j<900;j+=IMAGE_SIZE)
			{
				if(i==100)
				{
					g.setColor(Color.black);
					g.drawString(rowNum[rowCount++],i-20,j+60);
				}
				if(j==100)
				{
					g.setColor(Color.black);
					g.drawString(ColumnLetter[columnCount++],i+50,j+820);
				}
				g.setColor(swap[0]);
				swapColor(swap);
				g.fillRect(i,j,IMAGE_SIZE,IMAGE_SIZE);
				if(j==800)
					swapColor(swap);
			}
		}
	}
	
	private void swapColor(Color[] swap)
	{
		Color temp = swap[0];
		swap[0] = swap[1];
		swap[1] = temp;
	}
}
