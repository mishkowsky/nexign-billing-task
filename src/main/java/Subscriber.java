import java.text.SimpleDateFormat;
import java.util.*;

public class Subscriber {

    public final static int UNLIMITED_MINUTES_LIMIT = 300;
    public final static int REGULAR_MINUTES_LIMIT = 100;

    // Все цены указаны в копейках
    public final static int UNLIMITED_MINUTE_PRICE = 100;
    public final static int REGULAR_MINUTE_PRICE = 50;
    public final static int PER_MINUTE_MINUTE_PRICE = 150;

    public enum TariffType {UNLIMITED, PER_MINUTE, REGULAR}

    private final String number;

    private final TariffType tariff;

    private final List<Call> calls = new LinkedList<>();

    public Subscriber(String number, TariffType tariffType) {
        this.number = number;
        this.tariff = tariffType;
    }

    public void addCall(Call call) {
        calls.add(call);
    }

    public int countAllCalls() {
        int sum = 0;
        int totalPrice = 0;
        for (Call call : calls) {
            int callPrice;
            int duration = call.getDuration();
            switch (tariff) {
                case UNLIMITED:
                    sum += call.getDuration();
                    if (sum > UNLIMITED_MINUTES_LIMIT) {
                        callPrice = duration * UNLIMITED_MINUTE_PRICE;
                        call.setPrice(callPrice);
                        totalPrice += callPrice;
                    }
                    break;
                case PER_MINUTE:
                    callPrice = duration * PER_MINUTE_MINUTE_PRICE;
                    call.setPrice(callPrice);
                    totalPrice += callPrice;
                    break;
                case REGULAR:
                    if (call.getType() == Call.CallType.OUTGOING) {
                        sum += call.getDuration();
                        if (sum > REGULAR_MINUTES_LIMIT) {
                            callPrice = duration * PER_MINUTE_MINUTE_PRICE;
                        } else {
                            callPrice = duration * REGULAR_MINUTE_PRICE;
                        }
                        call.setPrice(callPrice);
                        totalPrice += callPrice;
                    }
            }
        }
        if (tariff == TariffType.UNLIMITED) totalPrice += 100 * 100;
        return totalPrice;
    }

    @Override
    public String toString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat minutesFormat = new SimpleDateFormat("HH:mm:ss");

        Collections.sort(calls);

        int totalPrice = countAllCalls();

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);

        // TODO what is tariff index?
        int tariff_index = 11; // if tariff == REGULAR
        if (tariff == TariffType.PER_MINUTE) tariff_index = 3;
        if (tariff == TariffType.UNLIMITED) tariff_index = 6;

        formatter.format("Tariff index: %02d (%s)%n", tariff_index, tariff);
        formatter.format("%s%n", "-".repeat(76));
        formatter.format("Report for phone number %s:%n", number);
        formatter.format("%s%n", "-".repeat(76));
        formatter.format("| %9s | %19s | %19s | %8s | %-5s |%n", "Call Type", "Start Time", "End Time", "Duration", "Cost");
        formatter.format("%s%n", "-".repeat(76));

        for (Call call : calls) {
            formatter.format(
                    "|%11s| %s | %s | %s | %2d.%02d |%n",
                    call.getTypeString(),
                    dateFormat.format(call.getBeginDate()),
                    dateFormat.format(call.getEndDate()),
                    minutesFormat.format(call.getEndDate().getTime() - call.getBeginDate().getTime() - 3 * 3600000),
                    call.getPrice() / 100, call.getPrice() % 100
            );
        }

        formatter.format("%s%n", "-".repeat(76));
        formatter.format("|%55s|%8d.%02d rubles|%n", "Total Cost: ", totalPrice / 100, totalPrice % 100);

        formatter.format("%s%n", "-".repeat(76));
        return formatter.toString();
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Subscriber)) {
            return false;
        }

        Subscriber c = (Subscriber) o;

        return number.equals(c.getNumber());
    }
}