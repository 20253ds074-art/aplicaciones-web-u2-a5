package mx.edu.utez.proyecto1C.controller.juego.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRecargarSaldoDTO {

    @NotNull(message = "El id del jugador es obligatorio")
    private Long jugadorId;

    @NotNull(message = "Monto de recarga obligatorio")
    @Positive(message = "El monto a recargar debe ser mayor a 0")
    private Double monto;
}