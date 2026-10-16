package mx.edu.utez.proyecto1C.repository.partida;

import mx.edu.utez.proyecto1C.model.partida.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByJugadorId(Long jugadorId);
}