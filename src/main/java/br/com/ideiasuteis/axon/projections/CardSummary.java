package br.com.ideiasuteis.axon.projections;

import lombok.Value;

@Value
public class CardSummary {

    private final String id;
    private final Integer initialAmount;
    private final Integer remainingAmount;

    final CardSummary deductAmount(final Integer toBeDeducted) {
        return new CardSummary(id, initialAmount, remainingAmount - toBeDeducted);
    }
}
