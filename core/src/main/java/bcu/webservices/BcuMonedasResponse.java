package bcu.webservices;

import module java.base;

public record BcuMonedasResponse(
        List<BcuMonedasResponseMoneda> values
) {
}
