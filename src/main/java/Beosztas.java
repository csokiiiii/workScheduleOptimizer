import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

class Beosztas {

    ArrayList<String> shops;
    ArrayList<String> days;
    ArrayList<String> people;
    int numberOfDays;
    int numberOfPeople;
    ArrayList<Integer> preferredWorkDayNOsMin; // ezt beolvasva így, v majd meglátjuk h jön
    ArrayList<Integer> preferredWorkDayNOsMax; // ezt beolvasva így, v majd meglátjuk h jön

    ArrayList<Integer> preferredWorkersNOMinA;
    ArrayList<Integer> preferredWorkersNOMaxA;

    ArrayList<Integer> preferredWorkersNOMinB;
    ArrayList<Integer> preferredWorkersNOMaxB;

    public ArrayList<ArrayList<Integer>> preferredWorkersNOMinX;
    public ArrayList<ArrayList<Integer>> preferredWorkersNOMaxX;
    ArrayList<Integer> tudaszSzint;
    int maxTudasSzint;
    ArrayList<Integer> elvartTudasszintA;
    ArrayList<Integer> elvartTudasszintB;

    ArrayList<ArrayList<Integer>> elvartTudasSzintX;

    public Beosztas(int mod) throws IOException {
        switch (mod) {
            case 1:
                shops = new ArrayList<>(Arrays.asList("a", "b"));
                days = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday"));
                people = new ArrayList<>(Arrays.asList("Agoston", "Bob", "Cintia", "Dominik"));
                numberOfDays = 3;
                numberOfPeople = 4;
                preferredWorkDayNOsMin = new ArrayList<>(Arrays.asList(0, 0, 0, 0));//  {0, 0, 0, 0}; // ezt beolvasva így, v majd meglátjuk h jön
                preferredWorkDayNOsMax = new ArrayList<>(Arrays.asList(3, 2, 2, 2)); // ezt beolvasva így, v majd meglátjuk h jön

                preferredWorkersNOMinA = new ArrayList<>(Arrays.asList(2, 1, 2));
                preferredWorkersNOMaxA = new ArrayList<>(Arrays.asList(2, 1, 2));

                preferredWorkersNOMinB = new ArrayList<>(Arrays.asList(1, 1, 1));
                preferredWorkersNOMaxB = new ArrayList<>(Arrays.asList(1, 1, 1));

                preferredWorkersNOMinX = new ArrayList<>(Arrays.asList(preferredWorkersNOMinA, preferredWorkersNOMinB));
                preferredWorkersNOMaxX = new ArrayList<>(Arrays.asList(preferredWorkersNOMaxA, preferredWorkersNOMaxB));
                tudaszSzint = new ArrayList<>(Arrays.asList(1, 2, 1, 0)); //------- ezt integernek kell felvenni
                maxTudasSzint = Collections.max(tudaszSzint);
                elvartTudasszintA = new ArrayList<>(Arrays.asList(2, 1, 2));
                elvartTudasszintB = new ArrayList<>(Arrays.asList(1, 1, 1));
                elvartTudasSzintX = new ArrayList<>(Arrays.asList(elvartTudasszintA, elvartTudasszintB)); //ezt is vhogy forral kell majd beolvasni
                break;
            case 2:
                shops = new ArrayList<>(Arrays.asList("a", "b"));
                numberOfDays = 5;
                numberOfPeople = 10;
                preferredWorkDayNOsMin = randomGenerator(numberOfPeople, 0, 1); // ezt beolvasva így, v majd meglátjuk h jön
                preferredWorkDayNOsMax = randomGenerator(numberOfPeople, 4, 5); // ezt beolvasva így, v majd meglátjuk h jön

                preferredWorkersNOMinX = new ArrayList<>(shops.size());
                for (int i = 0; i < shops.size(); i++) {
                    preferredWorkersNOMinX.add(randomGenerator(numberOfDays, 1, 1));
                }
                System.out.println("preferredWorkersNOMinX size: " + preferredWorkersNOMinX.size());

                preferredWorkersNOMaxX = new ArrayList<>(shops.size());
                for (int i = 0; i < shops.size(); i++) {
                    preferredWorkersNOMaxX.add(randomGenerator(numberOfDays, 1, 1));
                }
                System.out.println("preferredWorkersNOMaxX size: " + preferredWorkersNOMaxX.size());
                tudaszSzint = randomGenerator(numberOfPeople, 0, 2); //------- ezt integernek kell felvenni
                maxTudasSzint = Collections.max(tudaszSzint);
                elvartTudasSzintX = new ArrayList<>(shops.size());
                for (int i = 0; i < shops.size(); i++) {
                    elvartTudasSzintX.add(randomGenerator(numberOfDays, 0, 1));
                }
                System.out.println("elvarttudas size: " + elvartTudasSzintX.size());
                break;
            default:
                System.out.println("Rossz módot választottál az adatok feltötéséhez: 1-égetett, 2-random, 3-beolvasva");
        }
    }

    public Beosztas(int mod, int numberOfShops, int numberOfPeople, int numberOfDays,
                    int prefWorkdayMinMin, int prefWorkdayMinMax, int prefWorkdayMaxMin,
                    int prefWorkdayMaxMax, int knowledgeLevelMin, int knowledgeLevelMax,
                    int prefWorkersMinMin, int prefWorkersMinMax, int prefWorkersMaxMin,
                    int prefWorkersMaxMax,int expectedKnowledgeMin, int expectedKnowledgeMax
                    ) throws IOException {

        shops = new ArrayList<>();
        people = new ArrayList<>();
        days = new ArrayList<>();
        for(int i=0;i<numberOfShops;i++){
            shops.add("Shop"+i+1);
        }
        for(int i=0;i<numberOfPeople;i++){
            people.add("person"+i);
        }
        for (int i=0;i<numberOfDays;i++){
            days.add("day"+i);
        }
        this.numberOfDays = numberOfDays;
        this.numberOfPeople = numberOfPeople;
        preferredWorkDayNOsMin = randomGenerator(numberOfPeople, prefWorkdayMinMin, prefWorkdayMinMax); // ezt beolvasva így, v majd meglátjuk h jön
        preferredWorkDayNOsMax = randomGenerator(numberOfPeople, prefWorkdayMaxMin, prefWorkdayMaxMax); // ezt beolvasva így, v majd meglátjuk h jön

        preferredWorkersNOMinX = new ArrayList<>(shops.size());
        for (int i = 0; i < shops.size(); i++) {
            preferredWorkersNOMinX.add(randomGenerator(numberOfDays, prefWorkersMinMin, prefWorkersMinMax));
        }
        System.out.println("preferredWorkersNOMinX size: " + preferredWorkersNOMinX.size());

        preferredWorkersNOMaxX = new ArrayList<>(shops.size());
        for (int i = 0; i < shops.size(); i++) {
            preferredWorkersNOMaxX.add(randomGenerator(numberOfDays, prefWorkersMaxMin, prefWorkersMaxMax));
        }
        System.out.println("preferredWorkersNOMaxX size: " + preferredWorkersNOMaxX.size());
        tudaszSzint = randomGenerator(numberOfPeople, knowledgeLevelMin, knowledgeLevelMax); //------- ezt integernek kell felvenni
        maxTudasSzint = Collections.max(tudaszSzint);
        elvartTudasSzintX = new ArrayList<>(shops.size());
        for (int i = 0; i < shops.size(); i++) {
            elvartTudasSzintX.add(randomGenerator(numberOfDays, expectedKnowledgeMin, expectedKnowledgeMax));
        }
        System.out.println("elvarttudas size: " + elvartTudasSzintX.size());
    }



    public Beosztas(int mod, String inputFile) throws IOException, MyException {
        switch (mod) {
            case 3:
                Beolvas beolvasas = new Beolvas(inputFile);
                shops = beolvasas.shops;
                people = beolvasas.people;
                days = beolvasas.days;
                numberOfDays = beolvasas.days.size();
                numberOfPeople = beolvasas.people.size();
                preferredWorkDayNOsMin = beolvasas.PreferredWorkdaysNOMin;
                preferredWorkDayNOsMax = beolvasas.PreferredWorkdaysNOMax;
                preferredWorkersNOMinX = beolvasas.workersNOMin;
                preferredWorkersNOMaxX = beolvasas.workersNOMax;
                tudaszSzint = beolvasas.knowledgeLevel;
                maxTudasSzint = Collections.max(tudaszSzint);
                elvartTudasSzintX = beolvasas.expectedKnowledgeLevel;
                if (people.size() != preferredWorkDayNOsMin.size() || people.size() != preferredWorkDayNOsMax.size()
                        || people.size() != tudaszSzint.size()) {
                    throw new MyException("Something is wrong with your data in the file, \nmissing section in people part");
                }

                if (days.size() != preferredWorkersNOMinX.get(0).size() || days.size() != preferredWorkersNOMaxX.get(0).size()
                        || days.size() != elvartTudasSzintX.get(0).size()) {
                    throw new MyException("Something is wrong with your data in the file,\nmissing section in shops part");
                }
                break;

            default:
                System.out.println("Rossz módot választottál az adatok feltötéséhez: 1-égetett, 2-random, 3-beolvasva");
        }
    }

    private static ArrayList<Integer> randomGenerator(int db, int min, int max) {
        ArrayList<Integer> temp = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < db; i++) {
            if (max - min == 0) {
                temp.add(min);
            } else {
                temp.add(min + rand.nextInt(max - min + 1));
            }
        }
        return temp;
    }

}