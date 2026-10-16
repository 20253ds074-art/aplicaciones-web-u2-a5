package mx.edu.utez.proyecto1C.service.juego;

import mx.edu.utez.proyecto1C.controller.juego.dto.RequestIniciarPartidaDTO;
import mx.edu.utez.proyecto1C.controller.juego.dto.RequestRecargarSaldoDTO;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.exception.customExceptions.NotFoundException;
import mx.edu.utez.proyecto1C.model.jugador.Jugador;
import mx.edu.utez.proyecto1C.model.partida.EstadoPartida;
import mx.edu.utez.proyecto1C.model.partida.Partida;
import mx.edu.utez.proyecto1C.model.tiro.Tiro;
import mx.edu.utez.proyecto1C.repository.jugador.JugadorRepository;
import mx.edu.utez.proyecto1C.repository.partida.PartidaRepository;
import mx.edu.utez.proyecto1C.repository.tiro.TiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class JuegoService {

    private final JugadorRepository jugadorRepository;
    private final PartidaRepository partidaRepository;
    private final TiroRepository tiroRepository;
    private final Random random = new Random();

    public JuegoService(JugadorRepository jugadorRepository, PartidaRepository partidaRepository, TiroRepository tiroRepository) {
        this.jugadorRepository = jugadorRepository;
        this.partidaRepository = partidaRepository;
        this.tiroRepository = tiroRepository;
    }

    @Transactional
    public Partida iniciarPartida(RequestIniciarPartidaDTO dto) {
        Jugador jugador = jugadorRepository.findById(dto.getJugadorId())
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));

        if (!Boolean.TRUE.equals(jugador.getActivo())) {
            throw new BadRequestException("El jugador no está activo");
        }

        if (jugador.getSaldo() < dto.getApuesta()) {
            throw new BadRequestException("Saldo insuficiente para realizar la apuesta");
        }

        // Descontar apuesta
        jugador.setSaldo(jugador.getSaldo() - dto.getApuesta());
        jugadorRepository.save(jugador);

        // Crear partida
        Partida partida = new Partida();
        partida.setFecha(new Date());
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(dto.getApuesta());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Tiro realizarTiro(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida no encontrada"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new BadRequestException("La partida no se encuentra en curso (EN_JUEGO)");
        }

        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;

        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(dado1);
        tiro.setValorDado2(dado2);
        tiro.setSuma(suma);

        if (suma == 7 || suma == 11) {
            tiro.setEsGanador(true);
            partida.setEstado(EstadoPartida.FINALIZADA);

            // Jugador gana el doble de la apuesta
            Jugador jugador = partida.getJugador();
            jugador.setSaldo(jugador.getSaldo() + (partida.getApuesta() * 2));
            jugadorRepository.save(jugador);
            partidaRepository.save(partida);

        } else if (suma == 2 || suma == 3 || suma == 12) {
            tiro.setEsGanador(false);
            partida.setEstado(EstadoPartida.FINALIZADA);
            partidaRepository.save(partida);

        } else {
            tiro.setEsGanador(false);
            // Continúa la partida, no cambia estado
        }

        return tiroRepository.save(tiro);
    }

    @Transactional
    public Partida finalizarPartidaManual(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida no encontrada"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new BadRequestException("Solo se pueden finalizar manualmente partidas en curso (EN_JUEGO)");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }

    @Transactional
    public Jugador recargarSaldo(RequestRecargarSaldoDTO dto) {
        Jugador jugador = jugadorRepository.findById(dto.getJugadorId())
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));

        jugador.setSaldo(jugador.getSaldo() + dto.getMonto());
        return jugadorRepository.save(jugador);
    }

    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new NotFoundException("Jugador no encontrado");
        }
        return partidaRepository.findByJugadorId(jugadorId);
    }
}