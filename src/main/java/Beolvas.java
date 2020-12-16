import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MyException extends Exception{
    String str1;
    /* Constructor of custom exception class
     * here I am copying the message that we are passing while
     * throwing the exception to a string and then displaying
     * that string along with the message.
     */
    MyException(String str2) {
        str1=str2;
    }
    public String toString(){
        return ("MyException Occurred: "+str1) ;
    }
}

public class Beolvas {

    public static List<String[]> readData(String fileName) throws IOException {
        int count = 0;
        List<String[]> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            String separator = ";";
            while ((line = br.readLine()) != null) {
                content.add(line.split(separator, -1));
            }
        } catch (FileNotFoundException e) {
            //Some error logging
        }
        return content;
    }

    public static ArrayList<Integer> fillArray(ArrayList<String[]> fullArray, ArrayList<Integer> arrayToFill, String columnHeader) throws MyException {
        boolean foundColumn = false;
        int columnNumber = 0;
        for (int i = 0; i < fullArray.get(0).length; i++) {
            if (fullArray.get(0)[i].toLowerCase().equals(columnHeader.toLowerCase())) {
                columnNumber = i;
                foundColumn = true;
                break;
            }
        }
        for (int i = 1; i < fullArray.size(); i++) {
            if (fullArray.get(i)[columnNumber].length() != 0 && foundColumn) {
                try {
                    Integer a = Integer.parseInt(fullArray.get(i)[columnNumber]);
                    arrayToFill.add(a);
                } catch (NumberFormatException e) {
                    System.out.println("szöveg nem szám");
                }
            } else if (!foundColumn) {
                System.out.println("nem található a header " + columnHeader);
                throw new MyException("nem található a header: " + columnHeader);
            } else {
                break;
            }
        }
        return arrayToFill;
    }

    public static ArrayList<String> fillStringAtRepeatExit(ArrayList<String[]> fullArray, ArrayList<String> arrayToFill, String columnHeader) throws MyException {
        System.out.println("size: "+fullArray.size());
        boolean foundColumn = false;
        int columnNumber = 0;
        for (int i = 0; i < fullArray.get(0).length; i++) {
            // System.out.println(fullArray.get(0)[i]);
            if (fullArray.get(0)[i].toLowerCase().contains(columnHeader.toLowerCase())) {
                columnNumber = i;
                foundColumn = true;
                break;
            }
        }
        for (int i = 1; i < fullArray.size(); i++) {
            if (fullArray.get(i)[columnNumber].toLowerCase().contains(columnHeader.toLowerCase())) {

                foundColumn = false;
                break;
            } else if (fullArray.get(i)[columnNumber].length() != 0 && foundColumn) {
                String a = fullArray.get(i)[columnNumber];

                arrayToFill.add(a);
            } else if (!foundColumn) {
                System.out.println("nem található a header: " + columnHeader);
                throw new MyException("nem található a header: " + columnHeader);
            } else {
                break;
            }
        }
        return arrayToFill;
    }

    public static ArrayList<String> fillShops(ArrayList<String[]> fullArray, ArrayList<String> arrayToFill, String columnHeader) {
        for (int i = 0; i < fullArray.size(); i++) {
            for (int j = 0; j < fullArray.get(i).length; j++) {
                // System.out.println(fullArray.get(0)[i]);
                if (fullArray.get(i)[j].toLowerCase().contains(columnHeader.toLowerCase())) {
                    arrayToFill.add(fullArray.get(i)[j].substring("BOLT ".length()));
                    // System.out.println(fullArray.get(i)[j].substring("BOLT ".length()));
                }
            }
        }
        return arrayToFill;
    }

    public static ArrayList<ArrayList<Integer>> fillArrayInArray(ArrayList<String[]> fullArray, ArrayList<ArrayList<Integer>> arrayToFill, String columnHeader) throws MyException {
        boolean foundColumn = false;
        int columnNumber = 0;
        for (int i = 0; i < fullArray.get(0).length; i++) {
            if (fullArray.get(0)[i].toLowerCase().equals(columnHeader.toLowerCase())) {
                columnNumber = i;
                foundColumn = true;
                break;
            }
        }
        int arrayCounter = 0;
        boolean firstElement = true;
        for (int i = 1; i < fullArray.size(); i++) {
            if (fullArray.get(i)[columnNumber].equals(columnHeader.toLowerCase())) {
                arrayCounter += 1;
                firstElement = true;
            } else if (fullArray.get(i)[columnNumber].length() != 0 && foundColumn) {
                try {
                    Integer a = Integer.parseInt(fullArray.get(i)[columnNumber]);
                    if (firstElement) {
                        ArrayList<Integer> tempArray = new ArrayList<>();
                        tempArray.add(a);
                        arrayToFill.add(tempArray);
                        firstElement = false;
                    } else {
                        arrayToFill.get(arrayCounter).add(a);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("szöveg nem szám");
                }
            } else if (!foundColumn) {
                System.out.println("nem található a header: "+ columnHeader);
                throw new MyException("nem található a header: " + columnHeader);
            }
        }
        return arrayToFill;
    }

    String fileName;
    ArrayList<String> people = new ArrayList<>();
    ArrayList<String> shops = new ArrayList<>();
    ArrayList<String> days= new ArrayList<>();
    ArrayList<Integer> PreferredWorkdaysNOMin = new ArrayList<>();
    ArrayList<Integer> PreferredWorkdaysNOMax= new ArrayList<>();
    ArrayList<Integer> knowledgeLevel= new ArrayList<>();
    ArrayList<Integer> paymentLevel= new ArrayList<>();
    ArrayList<ArrayList<Integer>> workersNOMin= new ArrayList<>();
    ArrayList<ArrayList<Integer>> workersNOMax= new ArrayList<>();
    ArrayList<ArrayList<Integer>> expectedKnowledgeLevel= new ArrayList<>();

    public Beolvas(String fileName) throws IOException, MyException {
        this.fileName = fileName;
        if(fileName.substring(fileName.length()-4).contains("csv")) {
            ArrayList<String[]> beAdat = (ArrayList<String[]>) readData(fileName);
            PreferredWorkdaysNOMin = fillArray(beAdat, PreferredWorkdaysNOMin, "Minimum workdays");
            PreferredWorkdaysNOMax = fillArray(beAdat, PreferredWorkdaysNOMax, "Maximum workdays");
            knowledgeLevel = fillArray(beAdat, knowledgeLevel, "Knowledge level");
            paymentLevel = fillArray(beAdat, paymentLevel, "Payment demand");
            days = fillStringAtRepeatExit(beAdat, days, "Bolt");
            shops = fillShops(beAdat, shops, "Bolt");
            people = fillStringAtRepeatExit(beAdat, people, "Name");
            workersNOMin = fillArrayInArray(beAdat, workersNOMin, "minimum labor number");
            workersNOMax = fillArrayInArray(beAdat, workersNOMax, "maximum labor number");
            expectedKnowledgeLevel = fillArrayInArray(beAdat, expectedKnowledgeLevel, "expected knowledge level");
        }else{
            throw new MyException("File extension is not csv");
        }
    }

}
