package edu.school21.info.repository;

import edu.school21.info.model.entity.Check;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckRepository extends GenericRepository<Check, Long> {

    @Query(value = "select id from checks order by id asc",
            nativeQuery = true)
    List<Long> findAllIdsAsc();

}
