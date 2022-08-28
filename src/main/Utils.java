package main;

public class Utils {

    public static int BOARD_OFFSET = 100;
    public static int BOARD_SIZE = 800;
    public static int IMAGE_SIZE = 100;

    public boolean didEnPassnat(Piece piece, int moveNum)
    {
        if(piece.getType() != Type.Pawn)
            return false;
        Pawn pawn = (Pawn) piece;
        if(pawn.getWhenEnPass() == moveNum)
            return true;
        return false;
    }

    public int round(int num) // round up or down to the nearest 100 
    {
        if(num % 100 > 49)
            num = (num+99) / 100 * 100;
        else
            num = num / 100 * 100;
        return num;
    }

    public boolean outOfBounds(int x, int y)
    {
        return x > 849 || y > 849 || x < 50 || y < 50;
    }

    public int boardToPieceX(int position) // converts board xy pos to GUI xy and vice versa
    {
        return Math.abs(round(position) - BOARD_OFFSET) / IMAGE_SIZE;
    }

    public int boardToPieceY(int position)
    {
        return Math.abs(round(position) - BOARD_SIZE) / IMAGE_SIZE;
    }

    public int pieceToBoardX(int position)
    {
        return position * BOARD_OFFSET + BOARD_OFFSET;
    }

    public int pieceToBoardY(int position)
    {
        return BOARD_SIZE - position * BOARD_OFFSET;
    }
}
