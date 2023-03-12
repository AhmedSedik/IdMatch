package org.example;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App
{
    static final int ID = 42;
    static final int TYPE = 3;
    static final int MINSIZE = 15;
    static final String RELATION = "clones";
    static final String H2 = "org.h2.Driver";

  //TODO: Constant Class
    static final String URL = "jdbc:h2:~/git_enviroment/BigCloneEval/bigclonebenchdb/bcb";
    static final String USERNAME = "sa";

    public static void main( String[] args ) {
        try {
            String id;
            String id_2;
            Class.forName(H2);
            Connection conn = DriverManager.
                    getConnection(URL, USERNAME, "");
            Statement stmt = conn.createStatement();
            String sql = "SELECT FUNCTION_ID_ONE, FUNCTION_ID_TWO, FUNCTIONALITY_ID FROM "+ RELATION;
            sql=sql+" Where MIN_SIZE>="+MINSIZE + "AND SIMILARITY_LINE >=0.9 AND SYNTACTIC_TYPE ="+ TYPE+ "AND FUNCTIONALITY_ID = " + ID;
            ResultSet rs = stmt.executeQuery(sql);
            int id1 = 0;
            int id2 = 0;
            int id3 = 0;

            List<Integer> list1 = new ArrayList<Integer>();
            List<Integer> list2 = new ArrayList<Integer>();
            List<Integer> list3 = new ArrayList<Integer>();
            while (rs.next()) {
                id1 = rs.getInt("FUNCTION_ID_ONE");
                id2 = rs.getInt("FUNCTION_ID_TWO");
                list1.add(id1);
                list2.add(id2);
                id3 = rs.getInt("FUNCTIONALITY_ID");
                list3.add(id3);

            }
       /*     List<Integer[]> pairs =
                    list1.stream()
                            .flatMap(i -> list2.stream()
                                    .map(j -> new Integer[]{i, j})
                            ).
                            collect(Collectors.toList());

            pairs.forEach(pair -> System.out.println(pair[0] + " " + pair[1]));*/

            List<String> functionsID1 = new ArrayList<>();
            List<String> functionsID2 = new ArrayList<>();

            String  funcId1;
            String  funcId2;
            String startLine;
            String EndLine;
            String Type;
            //worst Implementation ever but gets the job done
            for (int i = 0; i < list1.size(); i++) {

//                System.out.println("Pair["+ i +"]" + "\t" + list1.get(i) + "\t" +list2.get(i));
                id = String.valueOf(list1.get(i));
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM FUNCTIONS WHERE ID = " + id);
                if (rs1.next()) {
                    funcId1 = rs1.getString("NAME");
                    startLine = rs1.getString("STARTLINE");
                    EndLine = rs1.getString("ENDLINE");
                    Type = rs1.getString("TYPE");
                    funcId1 = Type + "," + funcId1 + "," + startLine + "," + EndLine;
                    functionsID1.add(funcId1);
                }

                id_2 = String.valueOf(list2.get(i));
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM FUNCTIONS WHERE ID = " + id_2);
                if (rs2.next()) {
                    funcId2 = rs2.getString("NAME");
                    startLine = rs2.getString("STARTLINE");
                    EndLine = rs2.getString("ENDLINE");
                    Type = rs2.getString("TYPE");
                    funcId2  =  Type+ "," + funcId2 + "," + startLine + "," + EndLine;
                    functionsID2.add(funcId2);
                }

            }

//            idMatch();

            for (int j = 0; j < functionsID1.size(); j++) {
                System.out.println( functionsID1.get(j) + "," +functionsID2.get(j));
                System.out.println("Function Pairs["+ j +"]" + "\t" + functionsID1.get(j) + "," +functionsID2.get(j));

            }

            PrintStream myConsole = new PrintStream(new File("dataset.txt"));
            System.setOut(myConsole);
            for (int j = 0; j < functionsID1.size(); j++) {
                String out = functionsID1.get(j) + "," +functionsID2.get(j);
                String outReversed = Utils.reverseLine(functionsID1.get(j) + "," +functionsID2.get(j));
                myConsole.println( out);
                myConsole.println( outReversed);
//                myConsole.println( functionsID1.get(j) + "," +functionsID2.get(j));

//                System.out.println("Function Pairs["+ j +"]" + "\t" + functionsID1.get(j) + "," +functionsID2.get(j));

            }
            myConsole.close();




//            myConsole.println(functionsID2 + "\n");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public static ArrayList<String> readInputFile(String file) {
        ArrayList<String> wordList = new ArrayList<>();
        try {

            BufferedReader scanner = new BufferedReader(new FileReader(file));
            String code = "";

            while ((code = scanner.readLine()) != null) {
                wordList.add(code);
            }

            scanner.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }
    static void idMatch() {

//TODO: check length of str bzw. check if line contains cerain number of commas
        int matchCounter =0 ;
        int notFoundCounter =0 ;
        long end1 = 0;
        List<String> notFoundClones = new ArrayList<>();
        try {
            end1 = System.nanoTime();


            /*//preprocessing
//            Scanner s = new Scanner(new File("/home/zozz/git_enviroment/BigCloneEval/commands/output_test"));
            Scanner s = new Scanner(new File("xab"));
            ArrayList<String> wordList  = new ArrayList<>();
            while (s.hasNext()){
                wordList.add(s.next());
            }
            s.close();*/

            ArrayList<String> wordList =  readInputFile("output_hamming.txt");

            for (int iWord=0; iWord < wordList.size(); iWord++) {

                String word = wordList.get(iWord);
                //TODO:apply reverse Method
                String reversedLine = Utils.reverseLine(word);
//                System.out.println("reversed Line: " + reversedLine);


                BufferedReader stream = new BufferedReader(new FileReader("dataset.txt"));

                // Start a line count and declare a string to hold our current line.
                int linecount = 0;

                String line;
                // Let the user know what we are searching for
//                System.out.println("Searching for " + word + " in file...");

                // Loop through each line, stashing the line into our line variable.
                while ( ( line = stream.readLine() ) != null )
                {
                    // Increment the count and find the index of the word
                    linecount++; //TODO: Fix Line Count
                    //TODO: check with original and reversed line
                    int indexfound = line.indexOf(word);
                    int indexFoundReversed = line.indexOf(reversedLine);


                    // If greater than -1, means we found the clone
                    if (indexfound > -1 || indexFoundReversed > -1) {
                        matchCounter++;
                        //TODO: output not founded matches

                        System.out.println(word + " was found at position " + indexfound + " on line " + linecount);
//

                    } else {

                        if (!notFoundClones.contains(line) && !wordList.contains(line) && !notFoundClones.contains(word)) {
                            notFoundClones.add(line);
                            notFoundClones.add(Utils.reverseLine(line));
                        }
                    }
//                    System.out.println(linecount);

                }
//                System.out.println("Not Found: " + word);


                // Close the file after done searching
                stream.close();
//                System.out.println(wordList.get(iWord));
                if (matchCounter > 0) {
                    System.out.println("Counter: " + matchCounter);
                }
            } // end of for

            FileWriter writer = new FileWriter("notfoundclones.txt");
            for(String str: notFoundClones) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
//            System.out.println(notFoundClones);
//            System.out.println(wordList.size());
            System.out.println("Counter: " + matchCounter);
//            System.out.println("NotFound Counter: " + notFoundCounter);
        } //end of try
        catch (IOException e) {
            System.out.println("IO Error Occurred: " + e.toString());
        }
        long end2 = System.nanoTime();
        System.out.println("Time find clones= " + TimeUnit.SECONDS.convert(end2 - end1, TimeUnit.NANOSECONDS));
    }
}
/**
 *  String sql = "SELECT FUNCTION_ID_ONE, FUNCTION_ID_TWO, FUNCTIONALITY_ID FROM "+relation;
 *     sql=sql+" Where MIN_SIZE>=6  AND SIMILARITY_LINE>0.5 AND SIMILARITY_LINE<0.7  AND SIMILARITY_TOKEN>0.5 ";
 *     ResultSet rs = stmt.executeQuery(sql);
 * */