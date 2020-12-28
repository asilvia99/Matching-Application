public class UglyDucklingException extends Exception {
    String nameOfUnmatchedLittle;

    UglyDucklingException(String name){
        this.nameOfUnmatchedLittle = name;
    }
}
