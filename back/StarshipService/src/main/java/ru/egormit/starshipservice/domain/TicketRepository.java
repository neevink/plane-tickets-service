package ru.egormit.starshipservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.library.Ticket;

import java.util.List;

/**
 * Репозиторий доступа к сущности {@link ru.itmo.library.Ticket}.
 *
 * @author Egor Mitrofanov.
 */

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Query(value = "select * from ticket where event_id = :id", nativeQuery = true)
    List<Ticket> allTicketsByEventId(
            @Param("id") Long id
    );


//    List<Long> deleteAllByEventId();
//    @Modifying
//    @Query("delete from ticket t where t.event_id=:id")
//    void deleteAllByEventId(@Param("id") Long eventId);
}
