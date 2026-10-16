package mx.edu.utez.proyecto1C.controller.juego;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1C.controller.juego.dto.RequestIniciarPartidaDTO;
import mx.edu.utez.proyecto1C.controller.juego.dto.RequestRecargarSaldoDTO;
import mx.edu.utez.proyecto1C.model.jugador.Jugador;
import mx.edu.utez.proyecto1C.model.partida.Partida;
import mx.edu.utez.proyecto1C.model.tiro.Tiro;
import mx.edu.utez.proyecto1C.service.juego.JuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Partida> iniciarPartida(@Valid @RequestBody RequestIniciarPartidaDTO dto) {
        return new ResponseEntity<>(juegoService.iniciarPartida(dto), HttpStatus.CREATED);
    }

    @PostMapping("/tiro/{partidaId}")
    public ResponseEntity<Tiro> realizarTiro(@PathVariable Long partidaId) {
        return ResponseEntity.ok(juegoService.realizarTiro(partidaId));
    }

    @PutMapping("/finalizar-manual/{partidaId}")
    public ResponseEntity<Partida> finalizarPartidaManual(@PathVariable Long partidaId) {
        return ResponseEntity.ok(juegoService.finalizarPartidaManual(partidaId));
    }

    @PostMapping("/recargar-saldo")
    public ResponseEntity<Jugador> recargarSaldo(@Valid @RequestBody RequestRecargarSaldoDTO dto) {
        return ResponseEntity.ok(juegoService.recargarSaldo(dto));
    }

    @GetMapping("/historial/{jugadorId}")
    public ResponseEntity<List<Partida>> obtenerHistorial(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(juegoService.obtenerHistorialPartida(jugadorId));
    }
}