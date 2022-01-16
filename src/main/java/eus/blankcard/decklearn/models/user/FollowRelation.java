package eus.blankcard.decklearn.models.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "followed")
@IdClass(FollowRelation.class)
public class FollowRelation implements Serializable {

    @Id
    private Integer followed_id;

    @Id
    private Integer follower_id;

    @Column(name = "follow_date", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Date follow_date;

    public Integer getFollowed_id() {
        return followed_id;
    }

    public void setFollowed_id(Integer followed_id) {
        this.followed_id = followed_id;
    }

    public Integer getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(Integer follower_id) {
        this.follower_id = follower_id;
    }

    public Date getFollow_date() {
        return follow_date;
    }

    public void setFollow_date(Date follow_date) {
        this.follow_date = follow_date;
    }
}
