package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 *
 * @null Required by the CSC207 Checkstyle configuration.
 */
public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final Statement statementdata;

    /**
     * Create a statement printer for the given invoice and plays.
     *
     * @param invoice the invoice to print
     * @param plays   the plays for the performances in the invoice
     */
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.statementdata = new Statement(invoice, plays);
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Get the underlying statement object.
     *
     * @return the statement
     */
    public Statement getStatementdata() {
        return statementdata;
    }

    /**
     * Get the plays used by this printer.
     *
     * @return the map from play id to play
     */
    public Map<String, Play> getPlays() {
        return plays;
    }

    /**
     * Get the invoice used by this printer.
     *
     * @return the invoice
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Get the play corresponding to a performance.
     *
     * @param performance the performance
     * @return the play for the performance
     */
    private Play getPlay(Performance performance) {
        return getPlays().get(performance.getPlayID());
    }

    /**
     * Get the total amount by summing over all performances.
     *
     * @return the total amount
     */
    public int getTotalAmount() {
        int result = 0;
        for (Performance performance : getInvoice().getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }

    /**
     * Get the total volume credits for all performances.
     *
     * @return the total volume credits
     */
    private int getTotalVolumeCredits() {
        int volumeCredits = 0;
        for (Performance performance : getInvoice().getPerformances()) {
            volumeCredits += getVolumeCredits(performance);
        }
        return volumeCredits;
    }

    /**
     * Get the volume credits for a single performance.
     *
     * @param performance the performance
     * @return the volume credits earned for this performance
     */
    private int getVolumeCredits(Performance performance) {
        int result = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(performance.getAudience())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    /**
     * Get the amount for a single performance.
     *
     * @param performance the performance
     * @return the amount owed for this performance
     * @throws RuntimeException if the play type is not known
     */
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
                throw new RuntimeException(
                        String.format("unknown type: %s", getPlay(performance).getType()));
        }
        return result;
    }

    /**
     * Produce the plain-text statement.
     *
     * @return the rendered statement
     */
    public String statement() {
        return renderPlainText();
    }

    /**
     * Render the statement as plain text.
     *
     * @return the rendered statement
     */
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

    /**
     * Format a total amount as US currency.
     *
     * @param totalAmount the amount to format
     * @return the formatted amount
     */
    public String usd(int totalAmount) {
        return NumberFormat.getCurrencyInstance(Locale.US)
                .format(totalAmount / Constants.PERCENT_FACTOR);
    }
}
