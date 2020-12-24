package gb.l2hw;

public class MyArrayDataException extends NumberFormatException {
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public MyArrayDataException(String s, int row, int col) {
        super(s);
        this.row = row;
        this.col = col;
    }
}
