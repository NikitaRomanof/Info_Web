package edu.school21.info.repository;

import edu.school21.info.model.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends GenericRepository<Task, String> {

    Optional<Task> getTaskByTitle(String title);

    @Query(value = "select title from tasks",
            nativeQuery = true)
    List<String> findAllTitles();


    Optional<Task> findTaskByParentTaskIsNull();
}
