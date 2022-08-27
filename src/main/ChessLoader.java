package main;
import javax.swing.JFrame;

public class ChessLoader {

	public static void main(String[] args) 
	{
		JFrame chessGame = new JFrame();
		chessGame.setResizable(false);
		chessGame.setSize(1000,1000);
		chessGame.setTitle("Chess");
		chessGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		BoardManager manager = new BoardManager();
		chessGame.add(manager);
		chessGame.setVisible(true);
	}

}
