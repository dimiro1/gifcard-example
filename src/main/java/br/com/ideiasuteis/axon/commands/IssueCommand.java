package br.com.ideiasuteis.axon.commands;

import lombok.Value;
import org.axonframework.serialization.Revision;

/**
 * Command to create to issue a GiftCard.
 */
@Value
@Revision("1.0")
public class IssueCommand {

    /**
     * The id of the aggregate.
     */
    private final String id;

    /**
     * The amount to issue the GiftCard.
     */
    private final Integer amount;
}
