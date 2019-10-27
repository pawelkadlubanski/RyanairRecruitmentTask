package com.ryanair.flight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class IndirectRout {
    private final Rout firstLeg;
    private final Rout secondLeg;
}
