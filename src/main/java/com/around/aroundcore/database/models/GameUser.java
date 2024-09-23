package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.*;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Entity
@Table(name = "game_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Integer id;
    @Column(name = "level")
    @Getter
    private Integer level;
    @Column(name = "coins")
    @Getter
    private Integer coins;
    @Column(name = "username", unique = true)
    @Setter
    private String username;
    @Column(name = "avatar")
    @Getter
    private String avatar;
    @Column(unique=true)
    @Getter
    private String email;
    @Column
    private String password;
    @Column(columnDefinition = "boolean default false")
    @Setter
    @Getter
    private Boolean verified;

    @Column
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "city")
    @Getter
    private String city;

    @ManyToMany(fetch = FetchType.EAGER)
    @Getter
    @JoinTable(
            name = "user_round_team",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeam> userRoundTeams;

    @OneToMany(mappedBy = "owner")
    private List<GameChunk> capturedChunks;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private List<GameUser> friends;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
    )
    private List<GameUser> followers;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
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
    public void addSkillToUserSkillList(List<GameUserSkill> gameUserSkill){
        userSkills.addAll(gameUserSkill);
    }
    public GameUserSkill getUserSkillBySkillId(Integer skillId) throws GameUserSkillNullException {
        return userSkills.stream()
                .filter(gameUserSkill -> Objects.equals(gameUserSkill.getGameUserSkillEmbedded().getSkill().getId(), skillId))
                .findFirst().orElseThrow(GameUserSkillNullException::new);
    }
    public List<GameUserSkill> getUserSkills() {
        return Collections.unmodifiableList(userSkills);
    }

    public void setCity(String s){
        this.city = Objects.requireNonNullElse(s, "Екатеринбург");
    }
    public void setAvatar(String s){
        this.avatar = Objects.requireNonNullElse(s, "1");
    }
    public Team getTeam(Round round){
        UserRoundTeam urt = userRoundTeams.stream().filter(urt1 -> urt1.getRound() == round)
                .findFirst().orElseThrow(TeamNullException::new);
        return urt.getTeam();
    }
    public List<GameChunk> getCapturedChunks(Integer roundId){
        return capturedChunks.stream().filter(chunk -> chunk.getRound().getId().equals(roundId)).toList();
    }
    public void setPassword(String newPassword){
        if(this.password.equals(newPassword)){
            throw new GameUserPasswordSame();
        }
        this.password = newPassword;
    }
    public void followUser(GameUser user) throws GameUserAlreadyFollowed, GameUserUsernameNotUnique{
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
