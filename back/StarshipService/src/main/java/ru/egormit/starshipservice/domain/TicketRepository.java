package ru.egormit.starshipservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itmo.library.Ticket;

/**
 * Репозиторий доступа к сущности {@link ru.itmo.library.Ticket}.
 *
 * @author Egor Mitrofanov.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Query(value = "select count(*) from ticket where event_id = :id", nativeQuery = true)
    Long allTicketsByEventId(
            @Param("id") Long id
    );
}
