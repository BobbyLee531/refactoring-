package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 * 2.4
 */
public class StatementPrinter {

    private static final String TYPE_TRAGEDY = "tragedy";
    private static final String TYPE_COMEDY = "comedy";

    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     *
     * @return the formatted statement
     * @throws UnknownPlayTypeException if one of the play types is not known
     */
    public String statement() {
        final StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());

        // lines for each performance
        for (Performance performance : invoice.getPerformances()) {
            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    getPlay(performance).getName(),
                    usd(getAmount(performance)),
                    performance.getAudience()));
        }

        // totals
        result.append(String.format(
                "Amount owed is %s%n", usd(getTotalAmount())));
        result.append(String.format(
                "You earned %s credits%n", getTotalVolumeCredits()));

        return result.toString();
    }

    /**
     * Returns the Play associated with the given Performance.
     *
     * @param performance the performance
     * @return the corresponding play
     */
    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Calculates the amount (in cents) for a given performance.
     *
     * @param performance the performance
     * @return the amount in cents
     * @throws UnknownPlayTypeException if the play type is not recognized
     */
    private int getAmount(Performance performance) {
        final Play play = getPlay(performance);
        final String type = play.getType();
        final int audience = performance.getAudience();

        int result;
        switch (type) {
            case TYPE_TRAGEDY:
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;

            case TYPE_COMEDY:
                result = Constants.COMEDY_BASE_AMOUNT;
                if (audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (audience - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * audience;
                break;

            default:
                throw new UnknownPlayTypeException(
                        String.format("Unknown play type: %s", type));
        }

        return result;
    }

    /**
     * Calculates the volume credits for a single performance.
     *
     * @param performance the performance
     * @return the volume credits earned from this performance
     */
    private int getVolumeCredits(Performance performance) {
        final Play play = getPlay(performance);
        final int audience = performance.getAudience();

        int result = Math.max(
                audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        if (TYPE_COMEDY.equals(play.getType())) {
            result += audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }

    /**
     * Calculates the total volume credits for the invoice.
     *
     * @return the total volume credits
     */
    private int getTotalVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }

    /**
     * Calculates the total amount (in cents) for the invoice.
     *
     * @return the total amount in cents
     */
    private int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    /**
     * Formats an amount in cents as a US currency string.
     *
     * @param amountInCents the amount in cents
     * @return the formatted amount as US dollars
     */
    private String usd(int amountInCents) {
        final NumberFormat formatter =
                NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(
                amountInCents / (double) Constants.PERCENT_FACTOR);
    }

    /**
     * Exception thrown when a play has an unknown or unsupported type.
     */
    public static class UnknownPlayTypeException extends RuntimeException {
        public UnknownPlayTypeException(String message) {
            super(message);
        }

        public UnknownPlayTypeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
