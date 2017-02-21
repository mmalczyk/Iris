import main.comparator.SimpleComparator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Magda on 16.02.2017.
 */
public class SimpleComparatorTest {
    //TODO: test with hexadecimals
    //TODO: adjust tests to byteshift


    @Test
    public void recogniseCompletelyDifferentCodes(){
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 1, 0, 1};
        byte[] codeB = new byte[]{0, 1, 0, 0, 1, 0};
        byte[] maskA = new byte[]{1, 1, 1, 1, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 1, 1};

        assertEquals("comparison of different images failed", 0.5555555555555556, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

    @Test
    public void recogniseIdenticalCodes() {
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 1, 0, 1};
        byte[] codeB = new byte[]{1, 0, 1, 1, 0, 1};
        byte[] maskA = new byte[]{1, 1, 1, 1, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 1, 1};

        assertEquals("comparison of identical images failed", 0.0, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

    @Test
    public void recogniseDifferentCodes() {
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 0, 1, 1};
        byte[] codeB = new byte[]{1, 0, 0, 1, 0, 1};
        byte[] maskA = new byte[]{1, 1, 1, 1, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 1, 1};

        assertEquals("comparison of different images failed", 0.5, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

    @Test
    public void recogniseCompletelyDifferentCodesWithinMask(){
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 1, 1, 1};
        byte[] codeB = new byte[]{0, 1, 0, 1, 1, 0};
        byte[] maskA = new byte[]{1, 1, 1, 0, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 0, 1};

        assertEquals("comparison of different images within mask failed", 0.56, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

    @Test
    public void recogniseIdenticalCodesWithinMask() {
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 1, 0, 1};
        byte[] codeB = new byte[]{1, 0, 1, 1, 0, 1};
        byte[] maskA = new byte[]{1, 1, 1, 0, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 0, 1};

        assertEquals("comparison of identical images within mask failed", 0.0, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

    @Test
    public void recogniseDifferentCodesWithinMask() {
        SimpleComparator comparator = new SimpleComparator();

        byte[] codeA = new byte[]{1, 0, 1, 0, 1, 1};
        byte[] codeB = new byte[]{1, 0, 0, 1, 0, 1};
        byte[] maskA = new byte[]{1, 1, 1, 0, 1, 1};
        byte[] maskB = new byte[]{1, 1, 1, 1, 0, 1};

        assertEquals("comparison of different images within mask failed", 0.25, comparator.compare(codeA, codeB, maskA, maskB), 0.0d);
    }

}
