package theater;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 *
 * Initializer.
 */
class Statement {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final List<PerformanceData> performances;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
        this.performances = new ArrayList<>();
        for (Performance performance : invoice.getPerformances()) {
            performances.add(createPerformanceData(performance));
        }
    }

    private PerformanceData createPerformanceData(Performance performance) {
        return new PerformanceData(performance, plays.get(performance.getPlayID()));
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
     * Get.
     * @return Get.
     */
    public int volumeCredits() {
        int result = 0;
        for (PerformanceData performanceData : performances) {
            result += performanceData.volumeCredits();
        }
        return result;
    }

}
