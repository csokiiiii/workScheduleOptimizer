import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jacop.core.Domain;

import java.util.*;


public class Shops2 {

    private SimpleStringProperty shopName = new SimpleStringProperty("test");
    private CalcSchedule schedule;
    private ArrayList<List<Domain>> maxSolutions = new ArrayList<>();




    private ArrayList<ArrayList<String>> namesList = new ArrayList<>();


    private ArrayList<String> days = new ArrayList<String>();

    ArrayList<List<Domain>> vmi2 = new ArrayList<>();


    public Shops2(String shopName, CalcSchedule schedule, int shopNumber, int dayNumber, int solutionNumber) throws MyException{ //shopnamet áttenni daynamere
        // this.shopName = new SimpleStringProperty(shopName);
        this.schedule = schedule;
        if (schedule.solutionMap.get(schedule.finalCost).size() != 0) {
            vmi2 = new ArrayList<>(schedule.solutionMap.get(schedule.finalCost));
            days.add(schedule.days.get(dayNumber));
            List<Domain> simpleSolution = vmi2.get(solutionNumber);
            System.out.println("vmi" + vmi2.get(solutionNumber));
            for (int i = dayNumber * schedule.shops.size() + shopNumber; i < simpleSolution.size(); i += (schedule.shops.size() * schedule.days.size())) { //itt is a napok számán kell végigmenni

                System.out.println("i:     " + i);

                System.out.println("i+1:     " + (i + 1));

                for(int j=0;j<schedule.shops.size();j++){
                    namesList.add(new ArrayList<>());
                    if (simpleSolution.get(i + j).toString().equals("1")) {
                        if (namesList.get(j).size() ==0) {
                            namesList.get(j).add(schedule.people.get(i / (schedule.shops.size() * schedule.days.size())));
                        }else{
                            namesList.get(j).add("\n" +schedule.people.get(i / (schedule.shops.size() * schedule.days.size())));
                        }
                        System.out.println("name:  " + schedule.people.get((i + j) / (schedule.shops.size() * schedule.days.size())) + "\n");
                    }
                }

            }
        }


        System.out.println("---------------------------------------------------------------------------------------");
    }


    public String getShopName() {
        return shopName.get();
    }

    public void setShopName(String shopname) {
        shopName.set(shopname);
    }

    public CalcSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(CalcSchedule schedule) {
        this.schedule = schedule;
    }

    public String getDays() {
        return days.toString().substring(1, days.toString().length() - 1);
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public ArrayList<ArrayList<String>> getNamesList() {

        return namesList;
    }

    public ObservableList<ArrayList<String>> getObservableNamesList() {
        return FXCollections.observableList(namesList);
    }

    public void setNamesList(ArrayList<ArrayList<String>> namesList) {
        this.namesList = namesList;
    }

}
