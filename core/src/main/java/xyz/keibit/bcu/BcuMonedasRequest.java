package xyz.keibit.bcu;

import module java.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Pedido de monedas.
 *
 * @param grupo grupo de monedas
 */
public record BcuMonedasRequest(Grupo grupo) {
    public BcuMonedasRequest {
        Objects.requireNonNull(grupo, "grupo no puede ser null");
    }

    /** Grupo de monedas a consultar en {@link BcuClient#monedas}. */
    @Getter
    @RequiredArgsConstructor
    public enum Grupo {
        MERCADO_INTERNACIONAL((byte) 1),
        COTIZACIONES_LOCALES((byte) 2),
        TASAS_LOCALES((byte) 5),
        TODOS((byte) 0),
        ;

        /** Valor usado por el webservice. */
        private final byte value;

        private static final Map<Byte, Grupo> BY_VALUE = new HashMap<>();

        static {
            for (var x : values()) {
                BY_VALUE.put(x.getValue(), x);
            }
        }

        /**
         * Devuelve la constante correspondiente a un valor del webservice.
         *
         * @throws IllegalArgumentException si el valor no corresponde a ninguna constante
         */
        public static Grupo fromValue(byte value) {
            var x = BY_VALUE.get(value);
            if (x == null) {
                throw new IllegalArgumentException(
                        "Valor desconocido para Grupo: %d".formatted(value));
            }
            return x;
        }
    }
}
