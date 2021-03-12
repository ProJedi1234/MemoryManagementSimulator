public class Hexadecimal {
    public final int pageFrame;
    public final int offset;

    public Hexadecimal(String hexValue) {
        pageFrame = Integer.parseInt((String.valueOf(hexValue.charAt(0)) + String.valueOf(hexValue.charAt(1))), 16);
        offset = Integer.parseInt((String.valueOf(hexValue.charAt(2)) + String.valueOf(hexValue.charAt(3))), 16);
    }

    public String pageFrameHex() {
        return String.format("%02X", pageFrame);
    }
    public String offsetHex() {
        return String.format("%02X", offset);
    }
    public String getHex() {
        return pageFrameHex() + offsetHex();
    }
}
