package com.transucv.services;

import com.transucv.models.Rol;
import java.math.BigDecimal;

public class TarifaServicio {
    public BigDecimal obtenerTarifaVigente(Rol rol) {
        if (rol == null) return new BigDecimal("15.00");
        switch (rol) {
            case ESTUDIANTE: return new BigDecimal("5.00");
            case EMPLEADO:
            case PROFESOR: return new BigDecimal("10.00");
            default: return new BigDecimal("15.00");
        }
    }
}
