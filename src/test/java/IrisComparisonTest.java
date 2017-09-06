import main.Main;
import main.comparator.HammingDistance;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class IrisComparisonTest extends BaseTest {

    //TODO test with both versions of hamming distance

    private int personLimit = 10;
    private int photoLimit = 10;

    public IrisComparisonTest() {
        clearResultsDirectory();
        makeResultsDirectory();
    }

    @Test
    public void compareWithIdenticalImage() {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                if (Files.exists(path)) {
                    try {
                        Main.main(new String[]{path.toString(), path.toString()});
                    } catch (UnsupportedOperationException | AssertionError e) {
                        //no iris found or error; case irrelevant to this test
                        continue;
                    }
                    HammingDistance HD = Main.getHammingDistance();
                    System.out.println(path.getFileName().toString() + " HD: " + HD.getHD() + " " + HD.isSameEye());
                    results.add(HD);
                }
            }
        }
        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.isSameEye());
        Assert.assertTrue(avgHD.isSameEye().equals(HammingDistance.Comparison.SAME));

    }

    @Test
    public void compareWithSamePersonLeftSide() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Left);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.SAME));
    }

    @Test
    public void compareWithSamePersonRightSide() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Right, TestDirectory.Eye.Right);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.SAME));
    }


    @Test
    public void compareWithSamePersonLeftRight() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Right);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.DIFFERENT));
    }

    @Test
    public void compareWithSamePersonRightLeft() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Right, TestDirectory.Eye.Left);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.DIFFERENT));
    }


    private HammingDistance.Comparison compareWithSameEye(TestDirectory.Eye side1, TestDirectory.Eye side2) {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            for (int p = 0; p < photoLimit; p++) {
                Path path1 = TestDirectory.CASIA_Image(i, side1, p);
                if (Files.exists(path1)) {
                    for (int j = p + 1; j < photoLimit; j++) {
                        Path path2 = TestDirectory.CASIA_Image(i, side2, j);
                        if (Files.exists(path2)) {
                            try {
                                Main.main(new String[]{path1.toString(), path2.toString()});
                            } catch (UnsupportedOperationException | AssertionError e) {
                                //no iris found or error; case irrelevant to this test
                                continue;
                            }
                            HammingDistance HD = Main.getHammingDistance();
                            System.out.println(path1.getFileName().toString() + " " + path2.getFileName().toString()
                                    + " HD: " + HD.getHD() + " " + HD.isSameEye());
                            results.add(HD);
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
        return avgHD.isSameEye();
    }

    @Test
    public void compareWithOtherPeople() {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            Path path1 = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, 0);
            if (Files.exists(path1)) {
                for (int j = 0; j < photoLimit; j++) {
                    if (i != j) { //same person
                        Path path2 = TestDirectory.CASIA_Image(j, TestDirectory.Eye.Left, 0);
                        if (Files.exists(path2)) {
                            try {
                                Main.main(new String[]{path1.toString(), path2.toString()});
                            } catch (UnsupportedOperationException | AssertionError e) {
                                //no iris found or error; case irrelevant to this test
                                continue;
                            }
                            HammingDistance HD = Main.getHammingDistance();
                            System.out.println(path2.getFileName().toString() + " HD: " + HD.getHD() + " " + HD.isSameEye());
                            results.add(HD);
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
        Assert.assertTrue(avgHD.isSameEye().equals(HammingDistance.Comparison.DIFFERENT));
    }

    @Test
    public void allComparisonsPossible() {
        //counting how many error/exception encounters
        double errors = 0;
        double total = personLimit * photoLimit;
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                if (Files.exists(path)) {
                    try {
                        Main.main(new String[]{path.toString(), path.toString()});
                    } catch (UnsupportedOperationException | AssertionError e) {
                        System.out.println(e.getMessage());
                        errors++;
                    }
                }
            }
        }
        System.out.println("Errors: " + errors + "\n" + "Total: " + total);
        double possibleComparisons = ((total - errors) / total) * 100.;
        System.out.println("Possible comparisons: " + possibleComparisons + "%");
        Assert.assertTrue(possibleComparisons == 100.);
    }
/*
    @Test
    public void compareWithTransposedEye() {
        ArrayList<HammingDistance> results = new ArrayList<>();

        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < photoLimit; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                if (Files.exists(path)) {

                    String fileName = path.getFileName().toString();
                    Mat transposedImage = Imgcodecs.imread(path.toString(), CV_LOAD_IMAGE_GRAYSCALE);
                    int lastDot = fileName.lastIndexOf('.');
                    String transposedName = fileName.substring(0, lastDot) + "_T" + fileName.substring(lastDot);
                    ImageUtils.writeToFile(transposedImage, getResultsDirectory(), fileName);
                    ImageUtils.writeToFile(transposedImage.t(), getResultsDirectory(), transposedName);

                    Path path2 = Paths.get(getResultsDirectory().toString(), transposedName);
                    try {
                        Main.main(new String[]{path.toString(), path2.toString()});
                        ImageUtils.writeToFile(Main.getFinalResult1().getImageWithMarkedCircles(), getResultsDirectory(), fileName);
                        ImageUtils.writeToFile(Main.getFinalResult2().getImageWithMarkedCircles(), getResultsDirectory(), transposedName);

                        List<Mat> partialResults = Main.getComparatorPartialResult();
                        for (int k = 0; k < partialResults.size(); k++)
                            ImageUtils.writeToFile(
                                    partialResults.get(k),
                                    getResultsDirectory(),
                                    fileName.substring(0, lastDot) + "_" + k + fileName.substring(lastDot));
                    } catch (UnsupportedOperationException | AssertionError e) {
                        //no iris found or error; case irrelevant to this test
                        continue;
                    }
                    HammingDistance HD = Main.getHammingDistance();
                    results.add(HD);
                    System.out.println(path.getFileName().toString() + " HD: " + HD.getHD());
                }
            }
        }

        double totalHD = 0;
        for (HammingDistance result : results)
            totalHD += result.getHD();
        HammingDistance avgHD = new HammingDistance(totalHD / results.size());
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.isSameEye());
        Assert.assertTrue(avgHD.isSameEye().equals(HammingDistance.Comparison.SAME));

    }*/
}
