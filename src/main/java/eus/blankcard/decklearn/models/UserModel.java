package eus.blankcard.decklearn.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    private String username;

    @Column(name="first_name")
    private String name;

    @Column(name="second_name")
    private String surname;

    private String password;

    private String email;

    @Column(name="postal_code")
    private String postalCode;

    private String country;

    @Column(name="birth_date")
    private Date birthDate;

    private String img_path;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    List<DeckModel> decks;

    @ManyToMany
    @JoinTable( name = "saved_deck",
                joinColumns = { @JoinColumn( name = "user_id") },
                inverseJoinColumns = {@JoinColumn( name = "deck_id")}
                )
    private List<DeckModel> savedDecks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<TrainingModel> trainings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "followed",
                joinColumns = { @JoinColumn( name = "followed_id") },
                inverseJoinColumns = {@JoinColumn( name = "follower_id")}
                )
    private List<UserModel> followers;

    @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
    private List<UserModel> followed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    

    public String getPostalCode() {
      return postalCode;
    }
    

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }
   
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }


    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }    

    public List<UserModel> getFollowers() {
        return followers;
    }

    public Date getBirthDate() {
      return birthDate;
    }

    public void setBirthDate(Date birthDate) {
      this.birthDate = birthDate;
    }

    public List<DeckModel> getDecks() {
        return decks;
    }

    public void setDecks(List<DeckModel> decks) {
        this.decks = decks;
    }

    public List<TrainingModel> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingModel> trainings) {
        this.trainings = trainings;
    }

    public void setFollowers(List<UserModel> followers) {
        this.followers = followers;
    }

    public List<UserModel> getFollowed() {
        return followed;
    }

    public void setFollowed(List<UserModel> followed) {
        this.followed = followed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserModel other = (UserModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    public List<DeckModel> getSavedDecks() {
        return savedDecks;
    }

    public void setSavedDecks(List<DeckModel> savedDecks) {
        this.savedDecks = savedDecks;
    }
}
