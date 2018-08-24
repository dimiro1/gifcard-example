package br.com.ideiasuteis.axon.events;

import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Revision("1.0")
public class RedeemedEvent {
    private final String id;
    private final Integer amount;
}
