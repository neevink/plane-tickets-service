package com.soa.repository;

import com.soa.dto.FilterQueryDto;
import com.soa.entity.GroupsCount;
import com.soa.entity.LabWorkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWorkEntity, Integer> {
    @Query("select sum(l.minimalPoint) from LabWorkEntity l")
    Integer getSumOfMinimalPoint();

    @Query("select l.coordinates.x as x, l.coordinates.y as y, count (l.id) as cnt from LabWorkEntity l" +
            " group by l.coordinates.x, l.coordinates.y")
    List<GroupsCount> getGroupedByCoordinates();

    @Query("select distinct l.minimalPoint from LabWorkEntity l")
    List<Integer> getDistinctByMinimalPoint();

    @Query("select l from LabWorkEntity l where "
            + "((:#{#dto.name}) is null or l.name in (:#{#dto.name}))"
            + "and ((:#{#dto.id}) is null or l.id in (:#{#dto.id}))"
            + "and ((:#{#dto.minimalPoint}) is null or l.minimalPoint in (:#{#dto.minimalPoint}))"
            + "and ((:#{#dto.difficulty}) is null or l.difficulty in (:#{#dto.difficulty}))"
            + "and ((:#{#dto.coordinatesX}) is null or l.coordinates.x in (:#{#dto.coordinatesX}))"
            + "and ((:#{#dto.coordinatesY}) is null or l.coordinates.y in (:#{#dto.coordinatesY}))"
            + "and ((:#{#dto.disciplineName}) is null or l.discipline.name in (:#{#dto.disciplineName}))"
            + "and ((:#{#dto.disciplineSelfStudyHours}) is null or l.discipline.selfStudyHours in (:#{#dto.disciplineSelfStudyHours}))"
    )
    List<LabWorkEntity> getLabWorksWithFiltering(@Param("dto") FilterQueryDto dto, Pageable pageable);

    @Query(value = "select * from lab_work l order by difficulty desc limit 10", nativeQuery = true)
    List<LabWorkEntity> getHardestLabWorks();

    @Query(value = "select * from lab_work l " +
            "where l.labwork_name like concat('%', :name, '%') " +
            "limit :limit",
            nativeQuery = true)
    List<LabWorkEntity> suggest(@Param("name") String name, @Param("limit") int limit);

}
