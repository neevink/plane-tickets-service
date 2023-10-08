package ru.egormit.starshipservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.library.Event;
import ru.itmo.library.Ticket;

/**
 * Репозиторий доступа к сущности {@link Ticket}.
 *
 * @author Egor Mitrofanov.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
