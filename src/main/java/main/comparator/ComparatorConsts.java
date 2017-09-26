package main.comparator;

public class ComparatorConsts {

    // the method of compare arrays
    public static final ComparisonType COMPARISON_FUNCTION = ComparisonType.XOR;
    public static final int MARGIN_UP = 8;         // compare rows from MARGIN_UP to matA.height()-MARGIN_DOWN
    public static final int MARGIN_DOWN = 16;    // 24;
    public static final int MARGIN_LEFT = 40;       // compare columns from MARGIN_LEFT to matA.width()-MARGIN_RIGHT
    public static final int MARGIN_RIGHT = 40;

    public enum ComparisonType {
        EQUAL, NOT_EQUAL, XOR
    }


}
