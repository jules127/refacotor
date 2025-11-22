package theater;


public class PerformanceData {

    private final Performance performance;
    private final Play play;
    public PerformanceData(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public int getAudience() {
        return performance.getAudience();
    }

    public String getName() {
        return play.getName();
    }

    public String getType() {
        return play.getType();
    }

    public int amountFor() {
        int result = 0;
        switch (getType()) {
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
                throw new RuntimeException(String.format("unknown type: %s", getType()));
        }
        return result;
    }
}
