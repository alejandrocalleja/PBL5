package eus.blankcard.decklearn.repository.follow;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import eus.blankcard.decklearn.models.FollowRelation;

@Repository
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public int countFollowers(Integer user_id) {
        int result = 0;

        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM followed WHERE followed_id=?", FollowRelation.class);
        query.setParameter(1, user_id);

        try {
            result = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            System.out.println("Failed to load followers from user " + user_id);
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int countFollowing(Integer user_id) {
        int result = 0;

        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM followed WHERE follower_id=?", FollowRelation.class);
        query.setParameter(1, user_id);

        try {
            result = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            System.out.println("Failed to load following from user " + user_id);
            e.printStackTrace();
        }

        return result;
    }
}
