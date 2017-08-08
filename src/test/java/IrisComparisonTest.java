import main.Main;
import main.comparator.HammingDistance;
import main.utils.ImageUtils;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

public class IrisComparisonTest extends BaseTest {

    private int personLimit = 10;
    private int photoLimit = 10;

    public IrisComparisonTest() {
        clearResultsDirectory();
        makeResultsDirectory();
    }

    @Test
    public void compareWithIdenticalImage() {
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
                    System.out.println(path.getFileName().toString() + " HD: " + HD.getHD());
                    Assert.assertTrue(HD.getHD() == 0);
                }
            }
        }
    }

    @Test
    public void compareWithSamePersonSameSide() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Left);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.SAME));
    }

    @Test
    public void compareWithSamePersonOppositeSide() {
        HammingDistance.Comparison sameEye = compareWithSameEye(TestDirectory.Eye.Left, TestDirectory.Eye.Right);
        Assert.assertTrue(sameEye.equals(HammingDistance.Comparison.DIFFERENT));
    }

    private HammingDistance.Comparison compareWithSameEye(TestDirectory.Eye side1, TestDirectory.Eye side2) {
        ArrayList<HammingDistance> results = new ArrayList<>();
        for (int i = 0; i < personLimit; i++) {
            Path path1 = TestDirectory.CASIA_Image(i, side1, 0);
            if (Files.exists(path1)) {
                for (int j = 1; j < photoLimit; j++) {
                    Path path2 = TestDirectory.CASIA_Image(i, side2, j);
                    if (Files.exists(path2)) {
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
                            System.out.println(path2.getFileName().toString() + " HD: " + HD.getHD() + " " + HD.sameEye());
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
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.sameEye());
        Assert.assertTrue(avgHD.equals(HammingDistance.Comparison.DIFFERENT));
    }

    @Test
    public void allComparisonsPossible() {
        //counting how many error/exception encounters
        double errors = 0;
        double total = personLimit * photoLimit;
        for (int i = 0; i < personLimit; i++) {
            for (int j = 0; j < 10; j++) {
                Path path = TestDirectory.CASIA_Image(i, TestDirectory.Eye.Left, j);
                if (Files.exists(path)) {
                    try {
                        //signalling main with t to transpose
                        Main.main(new String[]{"t", path.toString(), path.toString()});
                    } catch (UnsupportedOperationException | AssertionError e) {
                        errors++;
                    }
                }
            }
        }
        System.out.println("Errors: " + errors);
        double errorPercentage = ((total - errors) / total) * 100.;
        System.out.println("Possible comparisons: " + errorPercentage + "%");
        Assert.assertTrue(errorPercentage == 100);
    }

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
        System.out.println("\n\n" + "avg HD: " + avgHD.getHD() + " " + avgHD.sameEye());
        Assert.assertTrue(avgHD.equals(HammingDistance.Comparison.DIFFERENT));

    }
}
