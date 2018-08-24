package br.com.ideiasuteis.axon.projections;

import br.com.ideiasuteis.axon.events.IssuedEvent;
import br.com.ideiasuteis.axon.events.RedeemedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CardSummaryProjection {

    private final List<CardSummary> cardSummaries = new CopyOnWriteArrayList<>();

    @EventHandler
    public final void on(final IssuedEvent evt) {
        CardSummary cardSummary = new CardSummary(evt.getId(), evt.getAmount(), evt.getAmount());
        cardSummaries.add(cardSummary);
    }

    @EventHandler
    public final void on(final RedeemedEvent evt) {
        cardSummaries.stream()
                .filter(cs -> evt.getId().equals(cs.getId()))
                .findFirst()
                .ifPresent(cardSummary -> {
                    CardSummary updatedCardSummary = cardSummary.deductAmount(evt.getAmount());
                    cardSummaries.remove(cardSummary);
                    cardSummaries.add(updatedCardSummary);
                });
    }

    @QueryHandler
    public final List<CardSummary> fetch(final FetchCardSummariesQuery query) {
        return cardSummaries.stream()
                .skip(query.getOffset())
                .limit(query.getSize())
                .collect(Collectors.toList());
    }
}
