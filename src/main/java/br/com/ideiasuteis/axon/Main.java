package br.com.ideiasuteis.axon;

import br.com.ideiasuteis.axon.aggregates.GiftCard;
import br.com.ideiasuteis.axon.commands.IssueCommand;
import br.com.ideiasuteis.axon.commands.RedeemCommand;
import br.com.ideiasuteis.axon.projections.CardSummary;
import br.com.ideiasuteis.axon.projections.CardSummaryProjection;
import br.com.ideiasuteis.axon.projections.FetchCardSummariesQuery;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;

public class Main {
    public static void main(String[] args) {
        var projection = new CardSummaryProjection();
        var eventHandlingConfiguration = new EventHandlingConfiguration();
        eventHandlingConfiguration.registerEventHandler(c -> projection);

        var configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard.class)
                .configureEventStore(c -> new EmbeddedEventStore(new InMemoryEventStorageEngine()))
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler(c -> projection)
                .buildConfiguration();

        configuration.start();

        var commandGateway = configuration.commandGateway();
        var queryGateway = configuration.queryGateway();

        commandGateway.sendAndWait(new IssueCommand("gc0", 100));
        commandGateway.sendAndWait(new IssueCommand("gc1", 100));
        commandGateway.sendAndWait(new IssueCommand("gc2", 50));
        commandGateway.sendAndWait(new RedeemCommand("gc1", 10));
        commandGateway.sendAndWait(new RedeemCommand("gc2", 20));

        queryGateway.query(
                new FetchCardSummariesQuery(3, 0),
                ResponseTypes.multipleInstancesOf(CardSummary.class)
        ).thenAccept(cardSummaries -> cardSummaries.forEach(System.out::println));
    }
}
