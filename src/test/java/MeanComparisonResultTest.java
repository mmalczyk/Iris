import main.Main;
import main.comparator.HammingDistance;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class MeanComparisonResultTest extends BaseTest {

    private int personLimit = 6;
    private int photoLimit = 4;


    public MeanComparisonResultTest() {
        clearResultsDirectory();
        makeResultsDirectory();
        DATABASE = Database.CASIA_IRIS_THOUSAND;
    }


    @Test
    public void compareWithIdenticalImage() {
        ArrayList<HammingDistance> results = new ArrayList<>();
        int correctlyFoundCount = 0;
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = getImagePath(i, TestDirectory.Eye.Left, j);
                if (Files.exists(path)) {
                    System.out.print(path.getFileName().toString() + " & " + path.getFileName().toString());
                    try {
                        Main.main(new String[]{path.toString(), path.toString()});
                    } catch (UnsupportedOperationException | AssertionError e) {
//                        System.out.println("  No iris found or error");
                        System.out.println(" " + e.getMessage());
                        continue;
                    }
                    HammingDistance HD = Main.getHammingDistance();
                    results.add(HD);

                    if (HD.isSameEye().equals(HammingDistance.Comparison.SAME) || HD.isSameEye().equals(HammingDistance.Comparison.IDENTICAL))
                        correctlyFoundCount++;
                }
            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.isSameEye());
        Assert.assertTrue(avgHD.isSameEye().equals(HammingDistance.Comparison.SAME) || avgHD.isSameEye().equals(HammingDistance.Comparison.IDENTICAL));

        System.out.println(correctlyFoundCount(correctlyFoundCount, results.size()));

        System.out.println();

    }


    @Test
    public void compareWithSamePersonLeftSide() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Left);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.SAME));
    }


    private HammingDistance.Comparison compareWithSameEye(TestDirectory.Eye side1, TestDirectory.Eye side2) {
        ArrayList<HammingDistance> results = new ArrayList<>();
        int correctlyFoundCount = 0;

        for (int i = 0; i < personLimit; i++) {
            for (int p = 0; p < photoLimit; p++) {
                Path path1 = getImagePath(i, side1, p);
                if (Files.exists(path1)) {
                    for (int j = p + 1; j < photoLimit; j++) {
                        Path path2 = getImagePath(i, side2, j);
                        if (Files.exists(path2)) {
                            try {
                                System.out.print(path1.getFileName().toString() + " & " + path2.getFileName().toString());
                                Main.main(new String[]{path1.toString(), path2.toString()});
                                HammingDistance HD = Main.getHammingDistance();
                                results.add(HD);

                                if (HD.isSameEye().equals(HammingDistance.Comparison.SAME) || HD.isSameEye().equals(HammingDistance.Comparison.IDENTICAL))
                                    correctlyFoundCount++;
                            } catch (UnsupportedOperationException | AssertionError e) {
                                //no iris found or error; case irrelevant to this test
//                                System.out.println("  No iris found or error");
                                System.out.println(" " + e.getMessage());
                                continue;
                            }
                        }
                    }
                }

            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.isSameEye());

        System.out.println(correctlyFoundCount(correctlyFoundCount, results.size()));

        return avgHD.isSameEye();


    }

    @Test
    public void compareWithOtherPeople() {
        ArrayList<HammingDistance> results = new ArrayList<>();
        int correctlyFoundCount = 0;

        for (int i = 0; i < personLimit; i++) {
            Path path1 = getImagePath(i, TestDirectory.Eye.Left, 0);
            if (Files.exists(path1)) {
                for (int j = 0; j < photoLimit; j++) {
                    if (i != j) { //same person
                        Path path2 = getImagePath(j, TestDirectory.Eye.Left, 0);
                        if (Files.exists(path2)) {
                            try {
                                System.out.print(path1.getFileName().toString() + " & " + path2.getFileName().toString());
                                Main.main(new String[]{path1.toString(), path2.toString()});
                            } catch (UnsupportedOperationException | AssertionError e) {
                                //no iris found or error; case irrelevant to this test
//                                System.out.println("  No iris found or error");
                                System.out.println(" " + e.getMessage());
                                continue;
                            }
                            HammingDistance HD = Main.getHammingDistance();
                            results.add(HD);

                            if (HD.isSameEye().equals(HammingDistance.Comparison.SAME) || HD.isSameEye().equals(HammingDistance.Comparison.IDENTICAL))
                                correctlyFoundCount++;
                        }
                    }
                }

            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.isSameEye());

        System.out.println(correctlyFoundCount(correctlyFoundCount, results.size()));

        Assert.assertTrue(avgHD.isSameEye().equals(HammingDistance.Comparison.DIFFERENT));

    }
}
