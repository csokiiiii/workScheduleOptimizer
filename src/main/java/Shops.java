import javafx.beans.property.SimpleStringProperty;
import org.jacop.core.Domain;

import java.util.*;


public class Shops {

    private SimpleStringProperty shopName;
    private CalcSchedule schedule;
    private ArrayList<List<Domain>> maxSolutions = new ArrayList<>();



    private ArrayList<String> names = new ArrayList<String>();


    private ArrayList<ArrayList<String>> names2 = new ArrayList<>();

    public Shops(String shopName, CalcSchedule schedule, int shopNumber, int dayNumber) {
        this.shopName = new SimpleStringProperty(shopName);
        this.schedule = schedule;


        int counter = 0;
       for (Map.Entry<Integer, Collection<List<Domain>>> entry : schedule.solutionMap.asMap().entrySet()) {

            if (entry.getKey() == schedule.finalCost) {
                names2.add(new ArrayList<>());
                System.out.println("size "+entry.getValue().size());
                Iterator<List<Domain>> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    names.clear();
                    List<Domain> numb = it.next();
                    for (int i = shopNumber; i < numb.size(); i += (schedule.shops.size() * schedule.days.size())) {
                        System.out.println("i:     " + i);
                        if (numb.get(i).toString().equals("1")) {
                            names2.get(counter).add(schedule.people.get(i / (schedule.shops.size() * schedule.days.size())) + "\n");
                            names.add(schedule.people.get(i / (schedule.shops.size() * schedule.days.size())) + "\n");
                            System.out.println("name:  "+schedule.people.get(i / (schedule.shops.size() * schedule.days.size())) + "\n");
                        }
                    }
                }
                counter++;
                break;
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

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }


    public ArrayList<ArrayList<String>> getNames2() {
        return names2;
    }

    public void setNames2(ArrayList<ArrayList<String>> names2) {
        this.names2 = names2;
    }
}
