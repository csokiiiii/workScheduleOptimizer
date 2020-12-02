public class noSolutionException extends Exception {

    String str1;

    noSolutionException(String str2) {
        str1 = str2;
    }

    public String toString() {
        return ("NoSolutionException Occurred: " + str1);
    }
}
