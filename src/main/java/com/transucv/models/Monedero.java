package com.transucv.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Monedero {
    private final String idUsuario;
    private BigDecimal saldo;
    private final List<Transaccion> historial;

    public Monedero(String idUsuario) {
        this.idUsuario = idUsuario;
        this.saldo = BigDecimal.ZERO;
        this.historial = new ArrayList<>();
    }

    public String getIdUsuario() { return idUsuario; }
    public synchronized BigDecimal getSaldo() { return saldo; }
    public synchronized List<Transaccion> getHistorial() { return Collections.unmodifiableList(historial); }

    public synchronized void acreditar(BigDecimal monto, Transaccion tx) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser mayor a cero");
        }
        this.saldo = this.saldo.add(monto);
        this.historial.add(tx);
    }

    public synchronized void debitar(BigDecimal monto, Transaccion tx) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto a debitar es inválido");
        }
        if (this.saldo.compareTo(monto) < 0) {
            throw new IllegalStateException("Saldo insuficiente. Recargue su monedero para continuar");
        }
        this.saldo = this.saldo.subtract(monto);
        this.historial.add(tx);
    }
}
