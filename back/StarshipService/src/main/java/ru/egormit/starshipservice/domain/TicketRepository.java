package ru.egormit.starshipservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.itmo.library.Ticket;

/**
 * Репозиторий доступа к сущности {@link ru.itmo.library.Ticket}.
 *
 * @author Egor Mitrofanov.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
}
