import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;
import scala.Int;

import java.io.IOException;
import java.util.*;

public class CalcSchedule {

    double runTime;
    String errorMessage;
    MultiValuedMap<Integer, List<Domain>> solutionMap;
    MultiValuedMap<Integer, List<Domain>> solutionMap2;
    ArrayList<String> people;
    ArrayList<String> shops;
    ArrayList<String> days;
    int finalCost;
    int finalPayment;
    double runTimeSec;

    private static BooleanVar[] createBooleanVar(BooleanVar[] v2, Store store, String name, ArrayList<String> shops, ArrayList<String> days) {
        for (int i = 0; i < v2.length; i++) {
            v2[i] = new BooleanVar(store, name + "_" + days.get(i / shops.size()) + "_" + shops.get(i % shops.size()), 0, 1);
        }
        return v2;
    }

    private static void preferaltMegszoritas(Store store, IntVar[] v1, int min, int max) {
        IntVar sumV1 = new IntVar(store, "sumV1", min, max);
        Constraint ctrV1 = new SumInt(v1, "==", sumV1);
        store.impose(ctrV1);
    }

    private static void preferaltMegszoritasweighted(Store store, IntVar[] v1, int min, int max, ArrayList<Integer> weights) {
        IntVar sumV1 = new IntVar(store, "sumV1", min, max);
        Constraint ctrV1 = new SumWeight(Arrays.asList(v1), weights, sumV1);
        store.impose(ctrV1);
    }

    public CalcSchedule(int beosztasMod, Beosztas beosztas) throws IOException, MyException, noSolutionException, OutOfMemoryError {


        long startTime = System.nanoTime();
        Store store = new Store();

        BooleanVar[][] workdaysAll = new BooleanVar[beosztas.numberOfPeople][beosztas.shops.size() * beosztas.numberOfDays];

        for (int i = 0; i < workdaysAll.length; i++) {
            workdaysAll[i] = createBooleanVar(workdaysAll[i], store, beosztas.people.get(i), beosztas.shops, beosztas.days);
        }


        //Cost
        IntVar[] costs = new IntVar[beosztas.shops.size() * beosztas.numberOfDays * beosztas.numberOfPeople];
        IntVar[] costs2 = new IntVar[beosztas.shops.size() * beosztas.numberOfDays * beosztas.numberOfPeople];
        //Összesített egy dimenziós array a solution megtalálásához
        BooleanVar[] sumV = new BooleanVar[beosztas.shops.size() * beosztas.numberOfDays * beosztas.numberOfPeople];
        for (int i = 0; i < beosztas.numberOfPeople; i++) {
            for (int j = 0; j < beosztas.numberOfDays * beosztas.shops.size(); j++) {
                sumV[(i * workdaysAll[i].length + j)] = workdaysAll[i][j];

                //Ide akarom betenni a costokat
                costs[i * beosztas.numberOfDays * beosztas.shops.size() + j] = new IntVar(store, "c" + (i * beosztas.numberOfDays * beosztas.shops.size() + j), beosztas.tudaszSzint.get(i), beosztas.tudaszSzint.get(i));
                costs2[i * beosztas.numberOfDays * beosztas.shops.size() + j] = new IntVar(store, "payment" + (i * beosztas.numberOfDays * beosztas.shops.size() + j), beosztas.fizetesIgeny.get(i), beosztas.fizetesIgeny.get(i));
            }
            //preferált napszám megszorítás
            preferaltMegszoritas(store, workdaysAll[i], beosztas.preferredWorkDayNOsMin.get(i), beosztas.preferredWorkDayNOsMax.get(i));
            for (int j = 0; j < workdaysAll[i].length; j++) {
                //System.out.println(workdaysAll[i][j]);
                //System.out.println("min "+ preferredWorkDayNOsMin[i]+" max " + preferredWorkDayNOsMax[i]);
            }
            // System.out.println("--------------------------");
        }

        //Egyszerre csak 1 boltban dolgozzunk      (ezt optimálisabb lenne betenni az előző kódba, de így talán átláthatóbb

        for (int i = 0; i < workdaysAll.length; i++) {
            for (int j = 0; j < workdaysAll[i].length / beosztas.shops.size(); j++) {
                IntVar[] vijAB = new IntVar[beosztas.shops.size()];
                for (int k = 0; k < beosztas.shops.size(); k++) {
                    vijAB[k] = workdaysAll[i][j * beosztas.shops.size() + k];
                }
                preferaltMegszoritas(store, vijAB, 0, 1);
            }
        }

        //preferált emberszámmegszorítás per bolt
        for (int i = 0; i < beosztas.shops.size(); i++) {
            BooleanVar[][] aShopWorkdays = new BooleanVar[beosztas.numberOfDays][beosztas.numberOfPeople];
            for (int j = 0; j < beosztas.numberOfDays; j++) {
                for (int z = 0; z < beosztas.numberOfPeople; z++) {
                    aShopWorkdays[j][z] = workdaysAll[z][j * beosztas.shops.size() + i];
                }
                //pref emberszámmegszorítás per bolt
                preferaltMegszoritas(store, aShopWorkdays[j], beosztas.preferredWorkersNOMinX.get(i).get(j), beosztas.preferredWorkersNOMaxX.get(i).get(j));

                //elvárt tudásszint
                preferaltMegszoritasweighted(store, aShopWorkdays[j], beosztas.elvartTudasSzintX.get(i).get(j), beosztas.numberOfPeople * beosztas.maxTudasSzint, beosztas.tudaszSzint);
            }
        }

        final boolean consistency = store.consistency();
        // search for a solution and print results
        if (consistency) {
            System.out.println("belépett a keresésbe");
            Search<IntVar> search = new DepthFirstSearch<IntVar>();
            search.getSolutionListener().recordSolutions(true);
            search.getSolutionListener().searchAll(true);

            search.getSolutionListener().setSolutionLimit( 1000000 ); //------------maximum ennyi megoldás


            SelectChoicePoint<IntVar> select2 =
                    new SimpleSelect<IntVar>(sumV, new SmallestDomain<>(), new IndomainMin<>());
            search.setTimeOut(20);
            search.setPrintInfo(false);

            boolean result2 = search.labeling(store, select2);
            //search.getSolutionListener().printAllSolutions();

           // System.out.println(" megoldások száma: " + search.getSolutionListener().solutionsNo());
            MultiValuedMap<Integer, List<Domain>> solutionMap = new ArrayListValuedHashMap<>();
            MultiValuedMap<Integer, List<Domain>> solutionMap2 = new ArrayListValuedHashMap<>();

            if (consistency && result2) {
                int start = 1;
                Domain[] finalSolution = search.getSolution(start);
                System.out.println("------------");
              //  System.out.println(search.getSolution(start));

                List<Domain> solutionInNumber = new ArrayList<>();
                int finalCost = 0;
                int finalPayment = 0;
                for (int i = 0; i < finalSolution.length; i++) {
                    finalCost += Integer.parseInt(String.valueOf(finalSolution[i])) * costs[i].value();
                    finalPayment += Integer.parseInt(String.valueOf(finalSolution[i])) * costs2[i].value();
                    solutionInNumber.add(finalSolution[i]);
                }
                // System.out.println("solnum"+solutionInNumber.toString());
                solutionMap.put(finalCost, solutionInNumber);
                solutionMap2.put(finalPayment, solutionInNumber);

                for (int i = start + 1; i <= search.getSolutionListener().solutionsNo(); i++) {
                    Domain[] sol2 = search.getSolution(i);
                    List<Domain> solutionInNumber2 = new ArrayList<>();
                    int cost = 0;
                    int payment = 0;
                    for (int j = 0; j < sol2.length; j++) {
                        cost += Integer.parseInt(String.valueOf(sol2[j])) * costs[j].value();
                        payment += Integer.parseInt(String.valueOf(sol2[j])) * costs2[j].value();
                        solutionInNumber2.add(sol2[j]);
                    }

                    solutionMap.put(cost, solutionInNumber2);
                    solutionMap2.put(payment, solutionInNumber2);

                    if (finalCost <= cost) {
                        finalCost = cost;
                    }

                    if (finalPayment >= payment) {
                        finalPayment = payment;
                    }
                }
                this.solutionMap = solutionMap;
                this.finalCost = finalCost;

                this.solutionMap2 = solutionMap2;
                this.finalPayment = finalPayment;

            } else {

                System.out.println("*** No");
                throw new noSolutionException("No solution found");
            }
        } else {
            System.out.println("non-consistent");
            throw new noSolutionException("No solution exists, please check your input\n");
        }
        long stopTime = System.nanoTime();
        runTimeSec = (double) (stopTime - startTime) / 1000000000;
        System.out.println("runTime in sec: " + (runTimeSec));
        runTime = runTimeSec;
        this.shops = beosztas.shops;
        this.people = beosztas.people;
        this.days = beosztas.days;
    }
    // }
}

