package eus.blankcard.decklearn.repository.follow;

public interface FollowRepositoryCustom {
    int countFollowers(Integer user_id);
    int countFollowing(Integer user_id);
}
