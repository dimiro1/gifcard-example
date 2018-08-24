package br.com.ideiasuteis.axon.commands;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Revision("1.0")
public class RedeemCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final Integer amount;
}
