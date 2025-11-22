package theater;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;


public class statementData {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final List<PerformanceData> performances;

    public statementData(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
        this.performances = new ArrayList<>();
        for (Performance performance : invoice.getPerformances()) {
            performances.add(new PerformanceData(performance, plays.get(performance.getPlayID())));
        }
    }

    public String getCustomer() {
        return invoice.getCustomer();
    }

    public List<PerformanceData> getPerformances() {
        return performances;
    }

    /**
     * get.
     * @return get.
     */
    public int totalAmount() {
        int result = 0;
        for (PerformanceData performanceData : performances ) {
            result  += performanceData.amountFor();
        }
        return result;
    }

    /**
     * get.
     * @return get.
     */
    public int volumeCredits() {
        int result = 0;
        for (PerformanceData performanceData : performances) {
            result += Math.max(performanceData.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
            // add extra credit for every five comedy attendees
            if ("comedy".equals(performanceData.getType())) {
                result += performanceData.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }
        }
        return result;
    }

}
