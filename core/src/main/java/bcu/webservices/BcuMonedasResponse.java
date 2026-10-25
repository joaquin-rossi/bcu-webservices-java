package bcu.webservices;

import module java.base;

public record BcuMonedasResponse(
        List<BcuMonedasResponseMoneda> values
) {
    public BcuMonedasResponse {
        values = List.copyOf(values);
    }
}
