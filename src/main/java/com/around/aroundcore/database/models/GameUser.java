package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "game_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "level")
    private Integer level;
    @Column(name = "coins")
    private Integer coins;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "avatar")
    private String avatar;
    @Column(unique=true)
    private String email;
    @Column
    private String password;
    @Column(columnDefinition = "boolean default false")
    private Boolean verified;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "city")
    private String city;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @OneToMany(mappedBy = "owner")
    private List<GameChunk> capturedChunks;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private List<GameUser> friends;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
    )
    private List<GameUser> followers;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="map_events_game_user",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private List<MapEvent> visitedEvents;
    @OneToMany(fetch=FetchType.EAGER,mappedBy = "gameUserSkillEmbedded.gameUser", cascade={CascadeType.ALL})
    private List<GameUserSkill> userSkills;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    public void addSkillToUserSkillList(GameUserSkill gameUserSkill){
        userSkills.add(gameUserSkill);
    }
    public void setCity(String s){
        this.city = Objects.requireNonNullElse(s, "Екатеринбург");
    }
    public void setAvatar(String s){
        this.avatar = Objects.requireNonNullElse(s, "1");
    }
    public Team  getTeam(){
        if(this.team == null){
            throw new TeamNullException();
        }
        return team;
    }
    public void setPassword(String newPassword){
        if(this.password.equals(newPassword)){
            throw new GameUserPasswordSame();
        }
        this.password = newPassword;
    }
    public void followUser(GameUser user){
        if(Objects.equals(user.getUsername(), getUsername())){
            throw new GameUserUsernameNotUnique();
        }
        if(user.followers.contains(this)){
            throw new GameUserAlreadyFollowed();
        }
        if(friends.contains(user)){
            return;
        }
        if(followers.contains(user)){
            followers.remove(user);
            friends.add(user);
            user.friends.add(this);
        }else{
            user.followers.add(this);
        }
    }
    public void unfollowUser(GameUser user){
        user.followers.remove(this);
    }
    public void removeUserFromFriends(GameUser user){
        if(friends.contains(user)){
            friends.remove(user);
            user.friends.remove(this);
            followers.add(user);
        }
    }
    public void addVisitedEvents(List<MapEvent> events){
        visitedEvents.addAll(events);
    }
    public void addCoins(Integer value){
        coins+=value;
    }
    public void reduceCoins(Integer value){
        if(coins-value>=0){
            coins-=value;
        }else{
            throw new GameUserNotEnoughCoins();
        }
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
