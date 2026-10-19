package com.transucv.services;

import com.transucv.models.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MonederoServicio {

    private final Map<String, Monedero> monederos = new ConcurrentHashMap<>();
    private final Set<String> referenciasRegistradas = Collections.synchronizedSet(new HashSet<>());
    private final TarifaServicio tarifaServicio;

    public MonederoServicio(TarifaServicio tarifaServicio) {
        this.tarifaServicio = tarifaServicio;
    }

    public Monedero obtenerOCrearMonedero(String idUsuario) {
        return monederos.computeIfAbsent(idUsuario, Monedero::new);
    }

    // Recarga del monedero virtual (pago movil)
    public synchronized String recargarMonedero(String idUsuario, String banco, String telefono, 
                                                String cedula, String referencia, BigDecimal monto) {
        if (banco == null || banco.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            cedula == null || cedula.trim().isEmpty() ||
            referencia == null || referencia.trim().isEmpty()) {
            return "Todos los campos del pago móvil son obligatorios";
        }

        if (!telefono.matches("^04\\d{9}$")) {
            return "Formato de teléfono inválido. Debe ser de 11 dígitos (Ej: 04121234567)";
        }

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return "El monto de la recarga debe ser mayor a cero";
        }

        if (referenciasRegistradas.contains(referencia.trim())) {
            return "El número de referencia ya fue registrado";
        }

        Monedero monedero = obtenerOCrearMonedero(idUsuario);
        String idTx = "REC-" + System.currentTimeMillis();
        Transaccion tx = new Transaccion(idTx, referencia.trim(), monto, TipoTransaccion.RECARGA, 
                                         "Recarga Pago Móvil - Banco: " + banco);

        monedero.acreditar(monto, tx);
        referenciasRegistradas.add(referencia.trim());

        return "Recarga exitosa. Saldo disponible: Bs. " + monedero.getSaldo();
    }

    // Pago del pasaje con monedero virtual
    public synchronized String pagarPasaje(String idUsuario, Rol rolUsuario, String tipoRuta, String idItinerario, String detalleRuta) {
        Monedero monedero = obtenerOCrearMonedero(idUsuario);

        BigDecimal tarifa;
        if ("Urbana".equalsIgnoreCase(tipoRuta)) {
            tarifa = BigDecimal.ZERO; // Rutas urbanas no generan cobro
        } else {
            tarifa = tarifaServicio.obtenerTarifaVigente(rolUsuario);
        }

        if (tarifa.compareTo(BigDecimal.ZERO) == 0) {
            String idTx = "PAG-" + System.currentTimeMillis();
            Transaccion tx = new Transaccion(idTx, null, BigDecimal.ZERO, TipoTransaccion.PAGO_PASAJE, 
                                             "Pasaje Ruta Urbana: " + detalleRuta);
            monedero.debitar(BigDecimal.ZERO, tx);
            return "Pago exitoso (Ruta Gratuita). Saldo restante: Bs " + monedero.getSaldo();
        }

        if (monedero.getSaldo().compareTo(tarifa) < 0) {
            return "Saldo insuficiente. Recargue su monedero para continuar";
        }

        String idTx = "PAG-" + System.currentTimeMillis();
        Transaccion tx = new Transaccion(idTx, null, tarifa, TipoTransaccion.PAGO_PASAJE, 
                                         "Pasaje Extraurbano: " + detalleRuta + " [" + rolUsuario + "]");

        monedero.debitar(tarifa, tx);

        return "Pago exitoso. Tarifa cobrada: Bs. " + tarifa + ". Saldo restante: Bs " + monedero.getSaldo();
    }
}
