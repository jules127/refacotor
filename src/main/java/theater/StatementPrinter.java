package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;


/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    public final statementData statementdata;
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.statementdata = new statementData(invoice, plays);
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */

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

        result.append(String.format("Amount owed is %s%n", usd(statementdata.getTotalAmount())));
        result.append(String.format("You earned %s credits%n", statementdata.volumeCredits()));
        return result.toString();
    }

    private String usd(int totalAmount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount / Constants.PERCENT_FACTOR);
    }


}
