package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;


/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    public final Statement statementdata;
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.statementdata = new Statement(invoice, plays);
        this.invoice = invoice;
        this.plays = plays;
    }

    public Statement getStatementdata() {
        return statementdata;
    }

    public Map<String, Play> getPlays() {
        return plays;
    }

    public Invoice getInvoice() {
        return invoice;
    }
    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    private Play getPlay(Performance performance) {
        return getPlays().get(performance.getPlayID());
    }
    public int getTotalAmount() {
        int result = 0;
        for (Performance performance : getInvoice().getPerformances() ) {
            result  += getVolumeCredits(performance);
        }
        return result;
    }
    private int getTotalVolumeCredits() {
        int volumeCredits = 0;
        for (Performance performance : getInvoice().getPerformances() ) {
            volumeCredits += getVolumeCredits(performance);
        }
        return volumeCredits;
    }

    private int getVolumeCredits(Performance performance) {
        int result = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(performance.getAudience())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    private int getAmount(Performance performance) {
        int result;
        switch (getPlay(performance).getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result +=
                            Constants.HISTORY_OVER_BASE_CAPACITY_PER_PERSON
                                    * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", getPlay(performance).getType()));
        }
        return result;
    }

    public String statement() {
        return renderPlainText();
    }

    private String renderPlainText() {
        final StringBuilder result = new StringBuilder("Statement for "
                + statementdata.getCustomer() + System.lineSeparator());
        for (PerformanceData performancedata : statementdata.getPerformances()) {

            result.append(String.format("  %s: %s (%s seats)%n",
                    performancedata.getName(),
                    usd(performancedata.amountFor()),
                    performancedata.getAudience()));
        }

        result.append(String.format("Amount owed is %s%n", usd(statementdata.totalAmount())));
        result.append(String.format("You earned %s credits%n", statementdata.volumeCredits()));
        return result.toString();
    }

    public String usd(int totalAmount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount / Constants.PERCENT_FACTOR);
    }


}
