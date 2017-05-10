import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/** The main program. */
public class Main {
    
    /** Converts input files to adjacency matrices, and then to graphs.
     *  Runs HORSE on each graph, and writes to the output file.
     */
    public static void main(String[] args) {

        /* The directory holding the input files. */
        File directory = new File("cs170_final_inputs");
        /* The array of input files. */
        File[] inputs = directory.listFiles();
        /* The number of inputs. */
        int numInputs = inputs.length;
        /* Sorts the input files in numerical order. */
        Arrays.sort(inputs, new Comparator<File>() {
            /* Comparator to sort the input files in numerical order. */
            public int compare(File x, File y) {
                String a = x.getName().substring(0, x.getName().length() - 3);
                String b = y.getName().substring(0, y.getName().length() - 3);
                return Integer.parseInt(a) - Integer.parseInt(b);
            }
        });

        try {

            /* Creates the output file to which outputs will be written. */
            File outputFile = new File("output.out");
            File outputFile_w = new File("output_w.out");
            /* Creates a file in the directory if it does not already exist. */
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            if (!outputFile_w.exists()) {
                outputFile_w.createNewFile();
            }

            /* The writers. */
            FileWriter fileWriter = new FileWriter("output.out");
            FileWriter fileWriter_w = new FileWriter("output_w.out");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BufferedWriter bufferedWriter_w = new BufferedWriter(fileWriter_w);

            /* Runs HORSE on each input file and writes one line to the output file. */
            int sinkCount = 0;
            int sourceCount = 0;
            int bothCount = 0;
            int neitherCount = 0;
            int[] skip = {22, 61, 64, 71, 97, 150, 163, 261, 279,
                    289,290, 337, 355, 371, 463, 509, 538, 539,
                    540};
            HashSet<Integer> skipSet = new HashSet<Integer>();
            for (int i = 0; i < skip.length; i++){
                skipSet.add(skip[i]);
            }
//            HashMap< Integer, String > knownResult = new HashMap<Integer,String>();
//            knownResult.put(10,"0 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31 33 35 37 39 41 43 45 47 49; 1 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42 44 46 48");
//            knownResult.put(13,"3 0 1 2");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(49,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(74,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(187,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(255,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(364,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");
//            knownResult.put(25,"0 1 2 3; 4 7; 5 6");

            for (int i = 0; i < inputs.length; ++i) {

                /* The current input file being considered. */
                File input = inputs[i];
                /* Takes in input file content, line by line. */
                List<String> content = Files.readAllLines(input.toPath(), Charset.defaultCharset());
                /* Size of the instance (Line 1 of the input file). */
                int size = Integer.parseInt(content.get(0));
//                if (size>10){
//                    continue;
//                }
                System.out.println(i+"th size is: "+size);
                /* The adjacency matrix of the instance in list form (Rest of the input file). */
                List<String> matrix = content.subList(1, content.size());

                /* Converts the list into an adjacency matrix. */
                int[][] adjacency = new int[size][size];
                for (int j = 0; j < size; ++j) {
                    String[] row = matrix.get(j).split("\\s");
                    for (int k = 0; k < size; ++k) {
                        adjacency[j][k] = Integer.parseInt(row[k]);
                    }
                }
                String output ="";
                String outputWeight = "";


                /*
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                *
                * */

                if (skipSet.contains(i+1)){
                    Horse myHorse = new Horse(adjacency, size);
                    ArrayList<ArrayList<Integer>> group = new ArrayList<ArrayList<Integer>>();
                    ArrayList<Integer> team = new ArrayList<Integer>();
                    for (int j = 0; j < size; j ++){
                        team.add(j);
                    }
                    group.add(team);
                    ArrayList<ArrayList<Integer>> groupWeight = myHorse.getWeightRes(group);


                    for (int j = 0; j< group.size(); j++ ){
                        for (int k = 0; k<group.get(j).size(); k++){
                            if (k == group.get(j).size()-1){
                                output = output + group.get(j).get(k)+"; ";
                                continue;
                            }
                            output = output + group.get(j).get(k)+" ";
                        }

                    }
                    for (int j = 0; j< groupWeight.size(); j++ ){
                        for (int k = 0; k<groupWeight.get(j).size(); k++){
                            if (k == groupWeight.get(j).size()-1){
                                outputWeight = outputWeight + groupWeight.get(j).get(k)+"; ";
                                continue;
                            }
                            outputWeight = outputWeight + groupWeight.get(j).get(k)+" ";
                        }

                    }
                    output = output.substring(0,output.length()-2);

                    outputWeight = outputWeight.substring(0,outputWeight.length()-2);
                    int score = myHorse.calculateScore(group);
                    System.out.println(score);
                    System.out.println(output);
                    System.out.println(outputWeight);


                }
                else if(size > 150){
                    Horse myHorse = new Horse(adjacency, size);
                    ArrayList<ArrayList<Integer>> resByWeight = myHorse.greedyByWeight();
                    ArrayList<ArrayList<Integer>> groupWeight = myHorse.getWeightRes(resByWeight);

                    int score = myHorse.calculateScore(resByWeight);

                    for (int j = 0; j< resByWeight.size(); j++ ){
                        for (int k = 0; k<resByWeight.get(j).size(); k++){
                            if (k == resByWeight.get(j).size()-1){
                                output = output + resByWeight.get(j).get(k)+"; ";
                                continue;
                            }
                            output = output + resByWeight.get(j).get(k)+" ";
                        }

                    }
                    for (int j = 0; j< groupWeight.size(); j++ ){
                        for (int k = 0; k<groupWeight.get(j).size(); k++){
                            if (k == groupWeight.get(j).size()-1){
                                outputWeight= outputWeight + groupWeight.get(j).get(k)+"; ";
                                continue;
                            }
                            outputWeight = outputWeight + groupWeight.get(j).get(k)+" ";
                        }

                    }
                    output = output.substring(0,output.length()-2);

                    outputWeight = outputWeight.substring(0,outputWeight.length()-2);
                    System.out.println(score);
                    System.out.println(output);
                    System.out.println(outputWeight);
                }
                else {
                    Horse myHorse = new Horse(adjacency, size);
                    ArrayList<ArrayList<Integer>> resByWeight = myHorse.greedyByWeight();
                    ArrayList<ArrayList<Integer>> resByScore = myHorse.greedyByScore();
                    ArrayList<ArrayList<Integer>> resByLength = myHorse.greedyByLength();
                    ArrayList<ArrayList<Integer>> resByLength1 = myHorse.greedyByLength1();

                    int scoreWeight = myHorse.calculateScore(resByWeight);
                    int scoreScore = myHorse.calculateScore(resByScore);
                    int scoreLength = myHorse.calculateScore(resByLength);
                    int scoreLength1 = myHorse.calculateScore(resByLength1);
//                System.out.println("score by weight:" + scoreWeight);
//                System.out.println("score by score:"+scoreScore);
//                System.out.println("score by length:"+scoreLength);
//                System.out.println("score by length:"+scoreLength1);
                    int score = scoreWeight;
                    String method = "1";
                    if (scoreScore>score){
                        score = scoreScore;
                        method = "2";
                    }
                    if (scoreLength>score){
                        score = scoreLength;
                        method ="3";
                    }
                    if (scoreLength1>score) {
                        score = scoreLength1;
                        method = "4";
                    }


//                boolean hasSink;
//                boolean hasSource;
//                hasSink = myHorse.hasSink();
//                hasSource = myHorse.hasSource();
//                if (hasSink && hasSource){
//                    System.out.println(i+" has sink and source");
//                    bothCount ++;
//                }
//                else if (hasSink && !hasSource){
//                    System.out.println(i+" only has sink");
//                    sinkCount ++;
//                }
//                else if (!hasSink && hasSource){
//                    System.out.println(i+" only has source");
//                    sourceCount ++;
//                }
//                else {
//                    System.out.println(i+" has no source or sink");
//                    neitherCount ++;
//                }


                /* The output string to be passed into bufferedWriter. */
                    ArrayList<ArrayList<Integer>> groupWeight = new ArrayList<ArrayList<Integer>>();
                    switch (method){
                        case "1":
                            groupWeight = myHorse.getWeightRes(resByWeight);
                            for (int j = 0; j< resByWeight.size(); j++ ){
                                for (int k = 0; k<resByWeight.get(j).size(); k++){
                                    if (k == resByWeight.get(j).size()-1){
                                        output = output + resByWeight.get(j).get(k)+"; ";
                                        continue;
                                    }
                                    output = output + resByWeight.get(j).get(k)+" ";
                                }

                            }




                            break;
                        case "2":
                            groupWeight = myHorse.getWeightRes(resByScore);
                            for (int j = 0; j< resByScore.size(); j++ ){
                                for (int k = 0; k<resByScore.get(j).size(); k++){
                                    if (k == resByScore .get(j).size()-1){
                                        output = output + resByScore.get(j).get(k)+"; ";
                                        continue;
                                    }
                                    output = output + resByScore.get(j).get(k)+" ";
                                }

                            }


                            break;
                        case "3":
                            groupWeight = myHorse.getWeightRes(resByLength);
                            for (int j = 0; j< resByLength.size(); j++ ){
                                for (int k = 0; k<resByLength.get(j).size(); k++){
                                    if (k == resByLength.get(j).size()-1){
                                        output = output + resByLength.get(j).get(k)+"; ";
                                        continue;
                                    }
                                    output = output + resByLength.get(j).get(k)+" ";
                                }

                            }

                            break;
                        case "4":
                            groupWeight = myHorse.getWeightRes(resByLength1);
                            for (int j = 0; j< resByLength1.size(); j++ ){
                                for (int k = 0; k<resByLength1.get(j).size(); k++){
                                    if (k == resByLength1.get(j).size()-1){
                                        output = output + resByLength1.get(j).get(k)+"; ";
                                        continue;
                                    }
                                    output = output + resByLength1.get(j).get(k)+" ";
                                }

                            }

                            break;

                    }
                    for (int j = 0; j< groupWeight.size(); j++ ){
                        for (int k = 0; k<groupWeight.get(j).size(); k++){
                            if (k == groupWeight.get(j).size()-1){
                                outputWeight = outputWeight+ groupWeight.get(j).get(k)+"; ";
                                continue;
                            }
                            outputWeight = outputWeight + groupWeight.get(j).get(k)+" ";
                        }

                    }
                    output = output.substring(0,output.length()-2);
                    outputWeight = outputWeight.substring(0,outputWeight.length()-2);
//                    System.out.println(output);
                    System.out.println(score);
                    System.out.println(output);
                    System.out.println(outputWeight);
                }


//                String output = "1 4 5; 2 3 6"; // Replace with HORSE output

                /* Writes to the output file. */
                bufferedWriter.write(output);
                /* Adds a newline for all but the last line. */
                if (i + 1 != numInputs) {
                    bufferedWriter.write("\n");
                }
                bufferedWriter_w.write(outputWeight);
                if (i + 1 != numInputs) {
                    bufferedWriter_w.write("\n");
                }

            }
//            System.out.println("only sink: "+sinkCount);
//            System.out.println("only source: "+sourceCount);
//            System.out.println("both: "+bothCount);
//            System.out.println("neither: "+neitherCount);

            /* Closes the writers. */
            bufferedWriter.close();
            bufferedWriter_w.close();
            fileWriter.close();
            fileWriter_w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
