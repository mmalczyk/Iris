package main.utils;

/**
 * Created by Magda on 11/07/2017.
 */
public class FilterConstants {

    //TODO see if there's any rule for normalised iris size -> Daugman said sth about 8x124 filter

    public final int FILTER_SIZE = 31;  //the bigger the filter the more detailed gabor; too big and we run out of memory
    public final int FILTERS_IN_COL = /*4*/8;
    public final int FILTERS_IN_ROW = /*124*/50;

    public FilterConstants() {
        //noinspection ConstantConditions
        assert FILTER_SIZE % 2 == 1;
    }

    public double getTotalRows() {
        return FILTERS_IN_COL * FILTER_SIZE;
    }

    public double getTotalCols() {
        return FILTERS_IN_ROW * FILTER_SIZE;
    }

}
