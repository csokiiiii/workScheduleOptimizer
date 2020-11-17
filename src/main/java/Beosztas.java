import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

class Beosztas {

    char[] shops;
    int numberOfDays;
    int numberOfPeople;
    int[] preferredWorkDayNOsMin; // ezt beolvasva így, v majd meglátjuk h jön
    int[] preferredWorkDayNOsMax; // ezt beolvasva így, v majd meglátjuk h jön

    int[] preferredWorkersNOMinA;
    int[] preferredWorkersNOMaxA;

    int[] preferredWorkersNOMinB;
    int[] preferredWorkersNOMaxB;

    public int[][] preferredWorkersNOMinX;
    public int[][] preferredWorkersNOMaxX;
    Integer[] tudaszSzint; //------- ezt integernek kell felvenni
    int[] tudasSzintTemp;
    int maxTudasSzint;
    int[] elvartTudasszintA;
    int[] elvartTudasszintB;

    int[][] elvartTudasSzintX;

    public Beosztas(int mod){
        switch(mod){
            case 1:
                shops = new char[]{'a', 'b'};
                numberOfDays = 3;
                numberOfPeople = 4;
                preferredWorkDayNOsMin =new int[]  {0, 0, 0, 0}; // ezt beolvasva így, v majd meglátjuk h jön
                preferredWorkDayNOsMax = new int[] {3, 2, 2, 2}; // ezt beolvasva így, v majd meglátjuk h jön

                preferredWorkersNOMinA = new int[] {2, 1, 2};
                preferredWorkersNOMaxA = new int[]{2, 1, 2};

                preferredWorkersNOMinB = new int[] {1, 1, 1};
                preferredWorkersNOMaxB = new int[] {1, 1, 1};

                preferredWorkersNOMinX = new int[][]{preferredWorkersNOMinA, preferredWorkersNOMinB};
                preferredWorkersNOMaxX= new int[][]{preferredWorkersNOMaxA, preferredWorkersNOMaxB};;
                tudaszSzint = new Integer[]{1, 2, 1, 0}; //------- ezt integernek kell felvenni
                maxTudasSzint = Collections.max(Arrays.asList(tudaszSzint));
                elvartTudasszintA = new int[] {2, 1, 2};
                elvartTudasszintB = new int[] {1, 1, 1};
                elvartTudasSzintX = new int[][]{elvartTudasszintA,elvartTudasszintB}; //ezt is vhogy forral kell majd beolvasni
                break;
            case 2:
                shops = new char[]{'a', 'b', 'e'};
                numberOfDays = 5;
                numberOfPeople = 10;
                preferredWorkDayNOsMin = randomGenerator(numberOfPeople, 0, 1); // ezt beolvasva így, v majd meglátjuk h jön
                preferredWorkDayNOsMax = randomGenerator(numberOfPeople, 4, 5); // ezt beolvasva így, v majd meglátjuk h jön

                preferredWorkersNOMinX = new int[shops.length][];
                for (int i = 0; i < shops.length; i++) {
                    preferredWorkersNOMinX[i] = randomGenerator(numberOfDays, 1, 1);
                }

                preferredWorkersNOMaxX = new int[shops.length][];
                for (int i = 0; i < shops.length; i++) {
                    preferredWorkersNOMaxX[i] = randomGenerator(numberOfDays, 1, 2);
                }
                tudasSzintTemp = randomGenerator(numberOfPeople, 0, 2);
                tudaszSzint = Arrays.stream(tudasSzintTemp).boxed().toArray(Integer[]::new); //------- ezt integernek kell felvenni
                maxTudasSzint = Collections.max(Arrays.asList(tudaszSzint));
                elvartTudasSzintX = new int[shops.length][];
                for (int i = 0; i < shops.length; i++) {
                    elvartTudasSzintX[i] = randomGenerator(numberOfDays, 0, 1);
                }
                break;
            case 3:
                //csvről beolvasva
            default:
                System.out.println("Rossz módot választottál az adatok feltötéséhez: 1-égetett, 2-random, 3-beolvasva");
        }
    }

    private static int[] randomGenerator(int db, int min, int max) {
        int[] temp = new int[db];
        Random rand = new Random();
        for (int i = 0; i < db; i++) {
            if (max - min == 0) {
                temp[i] = min;
            } else {
                temp[i] = min + rand.nextInt(max - min + 1);
            }
        }
        return temp;
    }

}