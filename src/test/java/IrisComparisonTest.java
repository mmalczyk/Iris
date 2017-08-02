import main.Main;
import main.comparator.HammingDistance;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.util.ArrayList;

public class IrisComparisonTest extends BaseTest {

    private int personLimit = 10;
    private int photoLimit = 3;

    @Test
    public void compareWithIdenticalImage() {
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                try {
                    Main.main(new String[]{path.toString(), path.toString()});
                } catch (UnsupportedOperationException | AssertionError e) {
                    //no iris found or error; case irrelevant to this test
                    continue;
                }
                HammingDistance HD = Main.getHammingDistance();
                System.out.println(path.getFileName().toString() + " HD: " + HD.getHD());
                Assert.assertTrue(HD.getHD() == 0);
            }
        }
    }

    @Test
    public void compareWithSameEyeSameSide() {
        boolean sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Left);
        Assert.assertTrue(sameEye);
    }

    @Test
    public void compareWithSameEyeOppositeSide() {
        boolean sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Right);
        Assert.assertFalse(sameEye);
    }

    private boolean compareWithSameEye(TestDirectory.Eye side1, TestDirectory.Eye side2) {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            for (int j = 1; j < photoLimit; j++) {
                Path path1 = TestDirectory.CASIA_Image(i, side1, 0);
                Path path2 = TestDirectory.CASIA_Image(i, side2, j);
                try {
                    Main.main(new String[]{path1.toString(), path2.toString()});
                } catch (UnsupportedOperationException | AssertionError e) {
                    //no iris found or error; case irrelevant to this test
                    continue;
                }
                HammingDistance HD = Main.getHammingDistance();
                System.out.println(path2.getFileName().toString() + " HD: " + HD.getHD() + " " + HD.sameEye());
                results.add(HD);
            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.sameEye());
        return avgHD.sameEye();
    }

    @Test
    public void compareWithOtherPeople() {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                if (i != j) { //same person
                    Path path1 = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, 0);
                    Path path2 = TestDirectory.CASIA_Image(j, TestDirectory.Eye.Left, 0);
                    try {
                        Main.main(new String[]{path1.toString(), path2.toString()});
                    } catch (UnsupportedOperationException | AssertionError e) {
                        //no iris found or error; case irrelevant to this test
                        continue;
                    }
                    HammingDistance HD = Main.getHammingDistance();
                    System.out.println(path2.getFileName().toString() + " HD: " + HD.getHD() + " " + HD.sameEye());
                    results.add(HD);
                }
            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.sameEye());
        Assert.assertFalse(avgHD.sameEye());
    }

    @Test
    public void allComparisonsPossible() {
        double errors = 0;
        double total = personLimit * photoLimit;
        for (int i = 0; i < photoLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                try {
                    Main.main(new String[]{path.toString(), path.toString()});
                } catch (UnsupportedOperationException | AssertionError e) {
                    errors++;
                }
            }
        }
        System.out.println("Errors: " + errors);
        double errorPercentage = ((total - errors) / total) * 100.;
        System.out.println("Possible comparisons: " + errorPercentage + "%");
        Assert.assertTrue(errorPercentage > 90);
    }
}
