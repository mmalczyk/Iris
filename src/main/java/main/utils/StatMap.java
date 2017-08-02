package main.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Magda on 18/07/2017.
 */
public class StatMap {
    private final TreeMap<Integer, Integer> statMap = new TreeMap<>();
    private final String name;

    public StatMap(String name) {
        this.name = name;
    }

    public void increment(int key) {
        statMap.putIfAbsent(key, 0);
        statMap.replace(key, statMap.get(key) + 1);
    }

    public void writeToFile(Path path, boolean append) {
        try {
            System.out.println("\n\nFinal results:");

            //TODO findBugs is complaining about this line
            FileWriter fileStream = new FileWriter(path.toString(), append);
            BufferedWriter out = new BufferedWriter(fileStream);
            out.write(name + "\n");
            for (Map.Entry<Integer, Integer> entry : statMap.entrySet()) {
                String s = name + ": "
                        + entry.getKey() + "\t"
                        + entry.getValue() + "\t"
                        + getPercentage(entry.getValue()) + "\n";
                System.out.print(s);
                out.write(s);
            }
            out.write("\n\n");
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    private double getPercentage(double value) {
        value = 100. * value / statMap.values().stream().mapToInt(Integer::intValue).sum();
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}