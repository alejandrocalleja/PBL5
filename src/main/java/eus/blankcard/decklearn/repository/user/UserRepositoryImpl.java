package eus.blankcard.decklearn.repository.user;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Query;

import eus.blankcard.decklearn.models.UserModel;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserModel loadUser(String username, String password) {
        UserModel user = null;
        Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE username=? AND password=?", UserModel.class);
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
            e.printStackTrace();
        }
    }
}