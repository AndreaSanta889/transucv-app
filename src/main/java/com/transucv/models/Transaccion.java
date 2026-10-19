package com.transucv.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaccion {
    private final String id;
    private final String referencia;
    private final BigDecimal monto;
    private final TipoTransaccion tipo;
    private final String concepto;
    private final LocalDateTime fechaHora;

    public Transaccion(String id, String referencia, BigDecimal monto, TipoTransaccion tipo, String concepto) {
        this.id = id;
        this.referencia = referencia;
        this.monto = monto;
        this.tipo = tipo;
        this.concepto = concepto;
        this.fechaHora = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getReferencia() { return referencia; }
    public BigDecimal getMonto() { return monto; }
    public TipoTransaccion getTipo() { return tipo; }
    public String getConcepto() { return concepto; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
