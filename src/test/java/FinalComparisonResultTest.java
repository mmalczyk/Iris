import main.Main;
import main.comparator.HammingDistance;
import main.utils.ImageData;
import main.utils.TestDirectory;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FinalComparisonResultTest extends BaseTest {

    private int personLimit = 12;
    private int photoLimit = 5;


    public FinalComparisonResultTest() {

        clearResultsDirectory();
        makeResultsDirectory();
        DATABASE = Database.CASIA_IRIS_THOUSAND;

    }

    @Test
    public void compareWithOtherPeople() {
        ArrayList<ComparisonResults> compResults = new ArrayList<>();
        File resultFile = new File(TestDirectory.resultFile("test_" + personLimit + "person_" + photoLimit + "photo.sql"));
        FileWriter fileWriter = null;
        try {

            resultFile.createNewFile();
            fileWriter = new FileWriter(resultFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < personLimit; i++) {
                for (int j = 0; j < photoLimit; j++) {

                    int i_except = i;
                    int j_except = j;

                    Path path1 = getImagePath(i, TestDirectory.Eye.Right, j);

                    if (Files.exists(path1)) {

                        ComparisonResults comparisonResults = new ComparisonResults();
                        comparisonResults.setEyeName(path1.getFileName().toString());

                        for (int ii = 0; ii < personLimit; ii++) {

                            ImageData finalResult1;
                            ImageData finalResult2;
                            HammingDistance HD;
                            boolean otherPerson = i != ii;
                            boolean testNext = true;

                            for (int jj = 0; jj < photoLimit && testNext; jj++) {

                                // don't compare the same photo
                                if (ii != i_except || jj != j_except) {
                                    Path path2 = getImagePath(ii, TestDirectory.Eye.Right, jj);
                                    try {

                                        System.out.print(
                                                path1.getFileName().toString() + " & " + path2.getFileName().toString());

                                        Main.main(new String[]{path1.toString(), path2.toString()});
                                        HD = Main.getHammingDistance();

                                        comparisonResults = updateComparisonResult(comparisonResults, HD, otherPerson);

                                        String data = generateRecord(path1, HD, path2);

                                        bufferedWriter.write(data);
                                        bufferedWriter.newLine();

                                    } catch (UnsupportedOperationException | AssertionError e) {

                                        finalResult1 = Main.getFinalResult1();
                                        finalResult2 = Main.getFinalResult2();

                                        if (finalResult1 == null)
                                            comparisonResults.rejectEye();
                                        else if (finalResult2 == null)
                                            comparisonResults.rejectOtherEye();
                                        else
                                            comparisonResults.rejectOtherError();

                                        System.out.println(" " + e.getMessage());

                                        if (finalResult1 == null) {
                                            testNext = false;
                                        }
                                    }
                                }
                            }

                        }
                        compResults.add(comparisonResults);
                    }
                }
            }
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareReport(compResults);
    }

    private void prepareReport(ArrayList<ComparisonResults> compResults) {
        int testedEyes;
        int comparisonCount;
        int correctlyMatchedCount;
        int correctlyNotMatchedCount;
        int incorrectlyMatchedCount;
        int incorrectlyNotMatchedCount;
        double hdSumOfDifferentEyes;
        double HDSumOfSameEyes;
        testedEyes = comparisonCount = correctlyMatchedCount = correctlyNotMatchedCount = incorrectlyMatchedCount = incorrectlyNotMatchedCount = 0;
        hdSumOfDifferentEyes = HDSumOfSameEyes = 0;

        for (int i = 0; i < compResults.size(); ++i) {
            ComparisonResults cr = compResults.get(i);
            if (cr.isCorrect() && cr.getComparisonCount() > 0) {
                testedEyes++;
                comparisonCount += cr.getComparisonCount();
                correctlyMatchedCount += cr.getCorrectlyMatchedCount();
                correctlyNotMatchedCount += cr.getNotMatchedCorrectlyCount();
                incorrectlyMatchedCount += cr.getIncorrectlyMatchedCount();
                incorrectlyNotMatchedCount += cr.getNotMatchedIncorrectlyCount();
                hdSumOfDifferentEyes += cr.getHDOtherSum();
                HDSumOfSameEyes += cr.getHDSameSum();
            }
        }

        printResults(
                testedEyes,
                comparisonCount,
                correctlyMatchedCount,
                correctlyNotMatchedCount,
                incorrectlyMatchedCount,
                incorrectlyNotMatchedCount,
                hdSumOfDifferentEyes,
                HDSumOfSameEyes);
    }

    private String generateRecord(Path path1, HammingDistance HD, Path path2) {
        return "INSERT INTO HD_TEST (PHOTO1,PHOTO2,HD) VALUES ('" +
                path1.getFileName().toString() + "','" +
                path2.getFileName().toString() + "'," + HD.getHD() + ");";
    }

    private void printResults(int testedEyes, int comparisonCount, int correctlyMatchedCount,
                              int correctlyNotMatchedCount, int incorrectlyMatchedCount, int incorrectlyNotMatchedCount,
                              double HDSumOfDifferentEyes, double HDSumOfSameEyes) {
        System.out.println("Number of comparisons performed:  " + comparisonCount + "\tNumber of eyes tested:" + testedEyes);
        System.out.println("Number of correctly matched eyes:  " + correctlyMatchedCount
                + " (" + (100 * (double) correctlyMatchedCount / (double) comparisonCount) + "%)");
        System.out.println("Number of incorrectly matched eyes  " + incorrectlyMatchedCount
                + " (" + (100 * (double) incorrectlyMatchedCount / (double) comparisonCount) + "%)");
        System.out.println("Average Hamming Distance for the same eye: "
                + HDSumOfSameEyes / (correctlyMatchedCount + incorrectlyNotMatchedCount));
        System.out.println("Average Hamming Distance for different eyes: "
                + HDSumOfDifferentEyes / (correctlyNotMatchedCount + incorrectlyMatchedCount));
    }

    private ComparisonResults updateComparisonResult(ComparisonResults comparisonResults, HammingDistance HD, boolean otherPerson) {
        if (HD.isSameEye().equals(HammingDistance.Comparison.SAME)
                || HD.isSameEye().equals(HammingDistance.Comparison.IDENTICAL))
            comparisonResults.matched(otherPerson, HD.getHD());
        else
            comparisonResults.notMatched(otherPerson, HD.getHD());
        return comparisonResults;
    }

    public class ComparisonResults {

        private String eyeName;
        private boolean correct;
        private int comparisonCount;
        private int rejectedOtherCount;
        private int correctlyMatchedCount;
        private int notMatchedCorrectlyCount;
        private int incorrectlyMatchedCount;
        private int notMatchedIncorrectlyCount;
        private double HDSameSum;
        private double HDOtherSum;

        public String getEyeName() {
            return eyeName;
        }

        public void setEyeName(String name) {
            eyeName = name;
            correct = true;
            comparisonCount = rejectedOtherCount = correctlyMatchedCount = notMatchedCorrectlyCount = incorrectlyMatchedCount = notMatchedIncorrectlyCount = 0;
            HDSameSum = HDOtherSum = 0;
        }

        public boolean isCorrect() {
            return correct;
        }

        public int getComparisonCount() {
            return comparisonCount;
        }

        public int getRejectedOtherCount() {
            return rejectedOtherCount;
        }

        public int getCorrectlyMatchedCount() {
            return correctlyMatchedCount;
        }

        public int getNotMatchedCorrectlyCount() {
            return notMatchedCorrectlyCount;
        }

        public int getIncorrectlyMatchedCount() {
            return incorrectlyMatchedCount;
        }

        public int getNotMatchedIncorrectlyCount() {
            return notMatchedIncorrectlyCount;
        }

        public double getHDSameSum() {
            return HDSameSum;
        }

        public double getHDOtherSum() {
            return HDOtherSum;
        }

        public void rejectEye() {
            correct = false;
        }

        public void rejectOtherEye() {
            rejectedOtherCount++;
        }

        public void rejectOtherError() {
            rejectedOtherCount++;
        }

        public void matched(boolean incorrectly, double hd) {
            if (incorrectly) {
                incorrectlyMatchedCount++;
                HDOtherSum += hd;
            } else {
                correctlyMatchedCount++;
                HDSameSum += hd;
            }
            comparisonCount++;
        }

        public void notMatched(boolean incorrectly, double hd) {
            if (incorrectly) {
                notMatchedCorrectlyCount++;
                HDOtherSum += hd;
            } else {
                notMatchedIncorrectlyCount++;
                HDSameSum += hd;
            }
            comparisonCount++;
        }
    }
}
