package br.com.ideiasuteis.axon.projections;

import lombok.Value;

@Value
public class FetchCardSummariesQuery {

    private final Integer size;
    private final Integer offset;
}
