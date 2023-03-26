import java.util.Date;

public class Call implements Comparable<Call> {

    public enum CallType {INCOMING, OUTGOING}

    private final Date beginDate;
    private final Date endDate;
    private final CallType type;
    private final int duration; // in minutes
    private int price;

    public Call(Date beginDate, Date endDate, CallType type) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.type = type;
        long timeDelta = endDate.getTime() - beginDate.getTime();
        this.duration = (int) (timeDelta / 60000);
        price = 0;
    }

    public int getDuration() {
        return duration;
    }

    public CallType getType() {
        return type;
    }

    public String getTypeString() {
        return (type == CallType.INCOMING) ? "02" : "01";
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int compareTo(Call o) {
        return beginDate.compareTo(o.beginDate);
    }

}