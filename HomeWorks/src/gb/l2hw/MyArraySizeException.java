package gb.l2hw;

public class MyArraySizeException extends ArrayIndexOutOfBoundsException {
    private int bound;

    public int getBound() {
        return bound;
    }

    public MyArraySizeException(String s, int bound) {
        super(s);
        this.bound = bound;
    }
}
