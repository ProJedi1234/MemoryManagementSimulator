public class Octal {
    private int value;

    public Octal(String octalValue) {
        this.value = Integer.parseInt(octalValue,8);
    }
    public Octal(int decimalValue) {
        this.value = decimalValue;
    }
    public int valueInDecimal() {
        return value;
    }
    public String value() {
        return Integer.toOctalString(value);
    }
}
