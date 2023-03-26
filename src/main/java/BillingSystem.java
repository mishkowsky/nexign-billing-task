import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillingSystem {

    private final Map<String, Subscriber> subscribers = new HashMap<>();

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public void parseCDR(String inputPath) throws ParseException {

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(inputPath));
            String line = reader.readLine();

            while (line != null) {
                System.out.println(line);

                String patternString = "\\d+";

                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(line);

                Call.CallType callType;
                String number;
                Date beginDate;
                Date endDate;
                Subscriber.TariffType tariffType;

                if (matcher.find())
                    callType = matcher.group().equals("01") ? Call.CallType.OUTGOING : Call.CallType.INCOMING;
                else throw new IllegalStateException();

                if (matcher.find())
                    number = matcher.group();
                else throw new IllegalStateException();

                if (matcher.find())
                    beginDate = dateFormat.parse(matcher.group());
                else throw new IllegalStateException();

                if (matcher.find())
                    endDate = dateFormat.parse(matcher.group());
                else throw new IllegalStateException();

                if (matcher.find())
                    switch (matcher.group()) {
                        case "06":
                            tariffType = Subscriber.TariffType.UNLIMITED;
                            break;
                        case "03":
                            tariffType = Subscriber.TariffType.PER_MINUTE;
                            break;
                        case "11":
                            tariffType = Subscriber.TariffType.REGULAR;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                else throw new IllegalStateException();

                Subscriber subscriber = subscribers.get(number);
                if (subscriber == null) {
                    subscriber = new Subscriber(number, tariffType);
                    subscribers.put(subscriber.getNumber(), subscriber);
                }

                subscriber.addCall(new Call(beginDate, endDate, callType));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printReportsToFile(String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        for (Subscriber subscriber : subscribers.values()) {
            writer.write(subscriber.toString());
        }

        writer.close();

    }


}

