package edu.school21.info.repository;

import edu.school21.info.model.entity.Friends;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends GenericRepository<Friends, Long> {
}
