package theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Statement for an invoice.
 * @null Required by the CSC207 Checkstyle configuration.
 */
class Statement {

    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final List<PerformanceData> performances;

    /**
     * Create a statement for an invoice and its plays.
     *
     * @param invoice the invoice
     * @param plays   the plays for the performances in the invoice
     */
    Statement(Invoice invoice, Map<String, Play> plays) {
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

    /**
     * Get the customer name for this statement.
     *
     * @return the customer name
     */
    public String getCustomer() {
        return invoice.getCustomer();
    }

    /**
     * Get the performances in this statement.
     *
     * @return the performances
     */
    public List<PerformanceData> getPerformances() {
        return performances;
    }

    /**
     * Get total amount for all performances.
     *
     * @return the total amount in cents
     */
    public int totalAmount() {
        int result = 0;
        for (PerformanceData performanceData : performances) {
            result += performanceData.amountFor();
        }
        return result;
    }

    /**
     * Get total volume credits for all performances.
     *
     * @return the total volume credits
     */
    public int volumeCredits() {
        int result = 0;
        for (PerformanceData performanceData : performances) {
            result += performanceData.volumeCredits();
        }
        return result;
    }
}

