package edu.school21.info.service;

import edu.school21.info.model.entity.Friends;
import edu.school21.info.repository.FriendsRepository;
import edu.school21.info.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsService extends GenericService<Friends, Long> {

    private final FriendsRepository friendsRepository;

    @Autowired
    protected FriendsService(FriendsRepository friendsRepository) {
        super(friendsRepository);
        this.friendsRepository = friendsRepository;
    }

}
