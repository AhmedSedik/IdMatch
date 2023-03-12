package org.example;

/**
 * @author zozz
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Utils {

    /**@return reversed @line

     */


    public static String reverseLine(String line) {


        // split string by no space
        String[] strSplit = line.split(",");

        // Now convert string into ArrayList
        ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));

        final int G = 4;
        final int NG = (strList.size() + G - 1) / G;

        List<List<String>> result = new ArrayList(NG);
        IntStream.range(0, strList.size())
                .forEach(i -> {
                    if (i % G == 0) {
                        result.add(i/G, new ArrayList<>());
                    }
                    result.get(i/G).add(strList.get(i));
                });
        List<String> p1 = new ArrayList<>();
        List<String> p2 = new ArrayList<>();

//        System.out.println("Merged Array List: " + result);

        for (int i = 0; i < result.size(); i++) {
            if (i==0){
                p1.add(String.valueOf(result.get(i)));}
            else{ p2.add(String.valueOf(result.get(i)));
                p2.add(",");
            }

        }
//        System.out.println("P1:" + p1);
//        System.out.println("P2:" + p2);

        //swap
        //merge
        p2.addAll(p1);
//        System.out.println("Merged: " + p2);

        StringBuilder builder2 = new StringBuilder();
        for (String s : p2) {
            builder2.append(s);
//            System.out.println("builder: " + builder2);
        }



        String revertedString = builder2.toString();


        return revertedString.replaceAll("[\\[\\]\\s]", "");
    }
}
