package br.com.ideiasuteis.axon.aggregates;

import br.com.ideiasuteis.axon.commands.IssueCommand;
import br.com.ideiasuteis.axon.commands.RedeemCommand;
import br.com.ideiasuteis.axon.events.IssuedEvent;
import br.com.ideiasuteis.axon.events.RedeemedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;

/**
 * Aggregate GiftCard.
 */
public class GiftCard {

    /**
     * The aggregate id.
     */
    @AggregateIdentifier
    private String id;

    /**
     * The still available value.
     */
    private int remainingValue;

    /**
     * Required empty constructor.
     */
    @SuppressWarnings("unused")
    public GiftCard() {
    }

    /**
     * Handle IssueCommand command.
     *
     * @param command the command
     */
    @CommandHandler
    public GiftCard(final IssueCommand command) {
        Assert.state(command.getAmount() > 0, () -> "amount must be > 0");

        AggregateLifecycle.apply(
                new IssuedEvent(command.getId(), command.getAmount()));
    }

    /**
     * Handle IssuedEvent event.
     *
     * @param event the event
     */
    @EventSourcingHandler
    public final void on(final IssuedEvent event) {
        id = event.getId();
        remainingValue = event.getAmount();
    }

    /**
     * Handle RedeemCommand command.
     *
     * @param command the command
     */
    @CommandHandler
    public final void handle(final RedeemCommand command) {
        Assert.state(command.getAmount() > 0, () -> "amount <= 0");
        Assert.state(
                command.getAmount() <= remainingValue,
                () -> "amount > remaining value");

        AggregateLifecycle.apply(new RedeemedEvent(id, command.getAmount()));
    }

    /**
     * Handle RedeemedEvent events.
     *
     * @param event the event
     */
    @EventSourcingHandler
    public final void on(final RedeemedEvent event) {
        remainingValue -= event.getAmount();
    }
}
