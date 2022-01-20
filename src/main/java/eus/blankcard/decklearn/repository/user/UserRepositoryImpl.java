package eus.blankcard.decklearn.repository.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eus.blankcard.decklearn.models.UserModel;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /*@Override
    public UserModel loadUser(String username, String password) {
        UserModel user = null;
        Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE username=? AND password=?",
                UserModel.class);

        query.setParameter(1, username);
        query.setParameter(2, password);

        try {
            user = (UserModel) query.getSingleResult();
        } catch (Exception e) {
        }

        return user;
    }

    @Override
    public void addUserEncrypted(UserModel user) {
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            entityManager.persist(user);
        } catch (EntityExistsException e) {
        }
    }

    @Override
    public int countFollowers(Integer user_id) {
        int result = 0;

        Query query = entityManager.createQuery("SELECT COUNT(*) FROM FollowRelation WHERE followed_id=?1");
        query.setParameter(1, user_id);

        try {
            result = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
        }

        return result;
    }

    @Override
    public int countFollowing(Integer user_id) {
        int result = 0;

        Query query = entityManager.createQuery("SELECT COUNT(*) FROM FollowRelation WHERE follower_id=?1");
        query.setParameter(1, user_id);

        try {
            result = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
        }

        return result;
    }

    @Override
    public List<UserModel> loadFollowers(Integer user_id) {
        List<UserModel> userList = new ArrayList<>();

        TypedQuery<UserModel> query = entityManager.createQuery(
                "SELECT u.user_id, u.username, u.first_name, u.img_path from UserModel u JOIN FollowedRelation f " +
                        "ON u.user_id = f.follower_id " +
                        "WHERE f.followed_id=?1",
                UserModel.class);
        query.setParameter(1, user_id);

        try {
            userList = query.getResultList();
        } catch (Exception e) {
        }

        return userList;
    }

    @Override
    public List<UserModel> loadFollowing(Integer user_id) {
        List<UserModel> userList = new ArrayList<>();

        TypedQuery<UserModel> query = entityManager.createQuery(
                "SELECT u.user_id, u.username, u.first_name, u.img_path from user u JOIN followed f on u.user_id = f.followed_id WHERE f.follower_id=?1",
                UserModel.class);
        query.setParameter(1, user_id);

        try {
            userList = query.getResultList();
        } catch (Exception e) {
        }

        return userList;
    }
    */
    @Override
    public boolean checkFollowed(UserModel user, UserModel targetUser) {
        System.out.println("Checking followed");
        return user.getFollowed().contains(targetUser);
    }
/*
    @Override
    public void follow(UserModel user, UserModel targetUser) {

        user.addFollowed(targetUser);
        targetUser.addFollower(user);
        System.out.println("FOLLOWING");
        
        try {
            entityManager.refresh(user);
            entityManager.refresh(targetUser);
        } catch (Exception e) {
        }
    }

    @Override
    public void unfollow(UserModel user, UserModel targetUser) {
        user.removeFollowed(targetUser);
        targetUser.removeFollower(user);
        System.out.println("UNFOLLOW");
        
        try {
            entityManager.refresh(user);
            entityManager.refresh(targetUser);
        } catch (Exception e) {
        }
    }*/
}