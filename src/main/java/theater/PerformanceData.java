package theater;

/**
 * Performance data for a single performance.
 *
 * @null Required by the CSC207 Checkstyle configuration.
 */
public class PerformanceData {

    private final Performance performance;
    private final Play play;

    /**
     * Create performance data for one performance and its play.
     *
     * @param performance the performance
     * @param play        the play for this performance
     */
    public PerformanceData(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    /**
     * Return the audience size for this performance.
     *
     * @return the audience size
     */
    public int getAudience() {
        return performance.getAudience();
    }

    /**
     * Return the name of the play.
     *
     * @return the play name
     */
    public String getName() {
        return play.getName();
    }

    /**
     * Return the type of the play.
     *
     * @return the play type
     */
    public String getType() {
        return play.getType();
    }

    /**
     * Calculate the amount for this performance.
     *
     * @return the amount in cents
     * @throws IllegalArgumentException if the play type is unknown
     */
    public int amountFor() {
        int result;
        switch (getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.HISTORY_OVER_BASE_CAPACITY_PER_PERSON
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
                // 不用 RuntimeException，换成更具体的异常类型
                throw new IllegalArgumentException(String.format("unknown type: %s", getType()));
        }
        return result;
    }

    /**
     * Calculate the volume credits for this performance.
     *
     * @return the volume credits earned
     */
    public int volumeCredits() {
        int result = Math.max(getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(getType())) {
            result += getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }
}
