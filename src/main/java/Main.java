import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

import java.util.*;

public class Main {

    private static BooleanVar[] createBooleanVar(BooleanVar[] v2, Store store, int name, char[] shops) {
        for (int i = 0; i < v2.length; i++) {
            v2[i] = new BooleanVar(store, "x" + name + (i / shops.length + 1) + shops[i % shops.length], 0, 1);
        }
        return v2;
    }

    private static void preferaltMegszoritas(Store store, IntVar[] v1, int min, int max) {
        IntVar sumV1 = new IntVar(store, "sumV1", min, max);
        Constraint ctrV1 = new SumInt(v1, "==", sumV1);
        store.impose(ctrV1);
    }

    private static void preferaltMegszoritasweighted(Store store, IntVar[] v1, int min, int max, int[] weights) {
        IntVar sumV1 = new IntVar(store, "sumV1", min, max);
        Constraint ctrV1 = new SumWeight(v1, weights, sumV1);
        store.impose(ctrV1);
    }

    public static void main(String[] args) {

        Beosztas beosztas = new Beosztas(1);
        long startTime = System.nanoTime();
        Store store = new Store();

     /*   char[] shops = {'a', 'b'};
        int numberOfDays = 5;
        int numberOfPeople = 10;
        int[] preferredWorkDayNOsMin = randomGenerator(numberOfPeople, 0, 1); // ezt beolvasva így, v majd meglátjuk h jön
        int[] preferredWorkDayNOsMax = randomGenerator(numberOfPeople, 2, 3); // ezt beolvasva így, v majd meglátjuk h jön

        int[][] preferredWorkersNOMinX = new int[shops.length][];
        for (int i = 0; i < shops.length; i++) {
            preferredWorkersNOMinX[i] = randomGenerator(numberOfDays, 1, 1);
        }

        int[][] preferredWorkersNOMaxX = new int[shops.length][];
        for (int i = 0; i < shops.length; i++) {
            preferredWorkersNOMaxX[i] = randomGenerator(numberOfDays, 1, 2);
        }

       /* int[] preferredWorkersNOMinA = randomGenerator(numberOfDays, 1, 2);
        int[] preferredWorkersNOMaxA = randomGenerator(numberOfDays, 2, 2);

        int[] preferredWorkersNOMinB = randomGenerator(numberOfDays, 1, 1);
        int[] preferredWorkersNOMaxB = randomGenerator(numberOfDays, 1, 1);

        //int[] preferredWorkersNOMinC = randomGenerator(numberOfDays, 1, 1);
     //   int[] preferredWorkersNOMaxC = randomGenerator(numberOfDays, 2, 2);

      //  int[][] preferredWorkersNOMin = new int[][]{preferredWorkersNOMinA, preferredWorkersNOMinB,/* preferredWorkersNOMinC};
        //int[][] preferredWorkersNOMax = new int[][]{preferredWorkersNOMaxA, preferredWorkersNOMaxB, /*preferredWorkersNOMaxC};
    //
        int[] tudasSzintTemp = randomGenerator(numberOfPeople, 0, 2);
        Integer[] tudaszSzint = Arrays.stream(tudasSzintTemp).boxed().toArray(Integer[]::new); //------- ezt integernek kell felvenni
        int maxTudasSzint = Collections.max(Arrays.asList(tudaszSzint));
        int[][] elvartTudasszintX = new int[shops.length][];
        for (int i = 0; i < shops.length; i++) {
            elvartTudasszintX[i] = randomGenerator(numberOfDays, 1, 2);
        }
        int[] elvartTudasszintA = randomGenerator(numberOfDays, 1, 2);
        int[] elvartTudasszintB = randomGenerator(numberOfDays, 1, 1);
        //  int[] elvartTudasszintC = randomGenerator(numberOfDays, 2, 2);

        int[][] elvartTudasszint = new int[][]{elvartTudasszintA, elvartTudasszintB, elvartTudasszintC}; //ezt is vhogy forral kell majd beolvasni
*/

        BooleanVar[][] workdaysAll = new BooleanVar[beosztas.numberOfPeople][beosztas.shops.length * beosztas.numberOfDays];

        for (int i = 0; i < workdaysAll.length; i++) {
            workdaysAll[i] = createBooleanVar(workdaysAll[i], store, i + 1, beosztas.shops);
        }

        //Cost
        IntVar[] costs = new IntVar[beosztas.shops.length * beosztas.numberOfDays * beosztas.numberOfPeople];
        //Összesített egy dimenziós array a solution megtalálásához
        BooleanVar[] sumV = new BooleanVar[beosztas.shops.length * beosztas.numberOfDays * beosztas.numberOfPeople];
        for (int i = 0; i < beosztas.numberOfPeople; i++) {
            for (int j = 0; j < beosztas.numberOfDays * beosztas.shops.length; j++) {
                sumV[(i * workdaysAll[i].length + j)] = workdaysAll[i][j];

                //Ide akarom betenni a costokat
                costs[i * beosztas.numberOfDays * beosztas.shops.length + j] = new IntVar(store, "c" + (i * beosztas.numberOfDays * beosztas.shops.length + j), beosztas.tudaszSzint[i], beosztas.tudaszSzint[i]);
            }
            //preferált napszám megszorítás
            preferaltMegszoritas(store, workdaysAll[i], beosztas.preferredWorkDayNOsMin[i], beosztas.preferredWorkDayNOsMax[i]);
            for (int j = 0; j < workdaysAll[i].length; j++) {
                //System.out.println(workdaysAll[i][j]);
                //System.out.println("min "+ preferredWorkDayNOsMin[i]+" max " + preferredWorkDayNOsMax[i]);
            }
           // System.out.println("--------------------------");
        }

        //Egyszerre csak 1 boltban dolgozzunk      (ezt optimálisabb lenne betenni az előző kódba, de így talán átláthatóbb

        for (int i = 0; i < workdaysAll.length; i++) {
            for (int j = 0; j < workdaysAll[i].length / beosztas.shops.length; j++) {
                IntVar[] vijAB = new IntVar[beosztas.shops.length];
                for (int k = 0; k < beosztas.shops.length; k++) {
                    vijAB[k] = workdaysAll[i][j * beosztas.shops.length + k];
                }
                preferaltMegszoritas(store, vijAB, 0, 1);
            }
        }

        //preferált emberszámmegszorítás per bolt
        for (int i = 0; i < beosztas.shops.length; i++) {
            BooleanVar[][] aShopWorkdays = new BooleanVar[beosztas.numberOfDays][beosztas.numberOfPeople];
            for (int j = 0; j < beosztas.numberOfDays; j++) {
                for (int z = 0; z < beosztas.numberOfPeople; z++) {
                    aShopWorkdays[j][z] = workdaysAll[z][j * beosztas.shops.length + i];
                }
                //pref emberszámmegszorítás per bolt
                preferaltMegszoritas(store, aShopWorkdays[j], beosztas.preferredWorkersNOMinX[i][j], beosztas.preferredWorkersNOMaxX[i][j]);

                //elvárt tudásszint
                preferaltMegszoritasweighted(store, aShopWorkdays[j], beosztas.elvartTudasSzintX[i][j], beosztas.numberOfPeople * beosztas.maxTudasSzint, Arrays.stream(beosztas.tudaszSzint).mapToInt(Integer::intValue).toArray());
            }
        }

        final boolean consistency = store.consistency();
        // search for a solution and print results
        if (consistency) {
            System.out.println("belépett a keresésbe");
            Search<IntVar> search = new DepthFirstSearch<IntVar>();
            search.getSolutionListener().recordSolutions(true);
            search.getSolutionListener().searchAll(true);

            //search.getSolutionListener().setSolutionLimit( 3 ); //------------maximum ennyi megoldás


            SelectChoicePoint<IntVar> select2 =
                    new SimpleSelect<IntVar>(sumV, new SmallestDomain<>(), new IndomainMin<>());
            search.setTimeOut(5);
            search.setPrintInfo(true);
            //search.getSolutionListener().executeAfterSolution(search, select2);
            boolean result2 = search.labeling(store, select2);
            //search.getSolutionListener().printAllSolutions();
            search.getSolutionListener().getSolutions();
            System.out.println( " megoldások száma: " + search.getSolutionListener().solutionsNo());

            if (consistency && result2) {
                int start = 1;
                Domain[] finalSolution = search.getSolution(start);
                MultiValuedMap<Integer, List<Domain>> solutionMap = new ArrayListValuedHashMap<>();
                List<Domain> solutionInNumber = new ArrayList<>();
                int finalCost = 0;
                for (int i = 0; i < finalSolution.length; i++) {
                    finalCost += Integer.parseInt(String.valueOf(finalSolution[i])) * costs[i].value();
                   solutionInNumber.add(finalSolution[i]);
                }
                solutionMap.put(finalCost, solutionInNumber);
                try {
                    for (int i = start + 1; i <= search.getSolutionListener().solutionsNo(); i++) {
                        Domain[] sol2 = search.getSolution(i);
                        solutionInNumber.clear();
                        int cost = 0;
                        for (int j = 0; j < sol2.length; j++) {
                            cost += Integer.parseInt(String.valueOf(sol2[j])) * costs[j].value();
                            solutionInNumber.add(sol2[j]);

                        }
                        // System.out.println("Cost: " + cost);
                        solutionMap.put(cost, solutionInNumber);
                        if (finalCost <= cost) {
                            finalCost = cost;
                        }
                    }
                }catch(OutOfMemoryError oome){
                    System.out.println("túl sok ugyanolyan hasznosságú megoldás, kérlek szűkítsd a lehetőségeket");
                }finally {
                    System.out.println("MultiValuedMap filled: _______________" +solutionMap.containsKey(finalCost));
                    System.out.println("Size: "+ solutionMap.size());
                    //System.out.println("Size: "+ solutionMap.asMap());
                    Iterator itEntries = solutionMap.entries().iterator();
                    /*while (itEntries.hasNext()){
                        System.out.println(itEntries.next());
                    }*/
                    System.out.println("A maximum tudasSzint: " + finalCost);
                    System.out.println("Azonos tudásszinttel rendelkező megoldások száma: " + solutionMap.keys());
                }
            } else {
                System.out.println("*** No");
            }
        } else System.out.println("non-consistent");
        long stopTime = System.nanoTime();
        double runTimeSec = (double) (stopTime - startTime) / 1000000000;
        System.out.println("runTime in sec: " + (runTimeSec));
        //System.out.println("runTime in sec: "+ (convert));
    }
    // }
}

