package mg.ny.adminui;

import java.text.NumberFormat;

public class TypeDoubleFormatter {

    public static String format(Double r){
        return String.format("%.2f", r);
    }
    public static String formatNumeric(Double r){
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setGroupingUsed(false);
        fmt.setMaximumIntegerDigits(999);
        fmt.setMaximumFractionDigits(999);
        return  fmt.format(r);
    }
}
