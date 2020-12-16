import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Kiir {

    FileWriter writer;
    List<String> solution;
    ObservableList<Shops2> studentsModels;

    public Kiir(File output, List solution, ObservableList<Shops2> studentsModels, ArrayList shops) throws Exception {
        this.solution = solution;
        this.studentsModels = studentsModels;
        writeExcel(output,this.studentsModels, shops);
    }

    public void writeExcel(File output,ObservableList<Shops2> studentsModels, ArrayList<String> shops) throws Exception {
        Writer writer = null;
            File file = output;
            writer = new BufferedWriter(new FileWriter(file));
            StringBuilder sb = new StringBuilder();
            sb.append("Days");
            sb.append(";");
            for(String str : shops){
                String text = "Shop "+str+";";
                sb.append(text);
            }
            sb.append("\n");

            for (Shops2 sh : studentsModels) {
                sb.append(sh.getDays());
                sb.append(";");
                for (int i = 0; i < sh.getNamesList().size(); i++) {
                    //text = (sh.getNamesList().get(i)).toString();
                    for(int j=0;j<sh.getNamesList().get(i).size();j++){
                        String bareName = sh.getNamesList().get(i).get(j).strip();
                        sb.append(bareName);
                        sb.append(" ");
                    }
                    System.out.print(sh.getNamesList().get(i).toString());
                    System.out.println(";");
                    //sb.append(sh.getNamesList().get(i).toString());
                    sb.append(";");
                }
                System.out.println("ujsor");
                sb.append("\n");
            }
            writer.write(sb.toString());



            writer.flush();
            writer.close();
    }


}


