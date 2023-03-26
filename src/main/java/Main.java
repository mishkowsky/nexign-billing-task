import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        BillingSystem bs = new BillingSystem();
        bs.parseCDR("./CDR/cdr.txt");
        bs.printReportsToFile("./reports/out.txt");
    }
}
