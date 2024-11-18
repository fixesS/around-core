package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.api.entity.*;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Slf4j
@Entity
@Table(name = "users", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Integer id;
    @Column(name = "level",nullable = false,columnDefinition = "int8 default 0")
    @Builder.Default
    @Getter
    private Integer level = 0;
    @Column(name = "coins",nullable = false, columnDefinition = "int8 default 0")
    @Builder.Default
    @Getter
    private Integer coins = 0;
    @Column(name = "username", unique = true)
    @Setter
    private String username;
    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "uuid", nullable = false, columnDefinition = "uuid default (b3feae74-7915-4ed8-9965-419b9a0a6283)::uuid")
    private Image avatar;
    @Column(unique=true)
    @Getter
    private String email;
    @Column
    private String password;
    @Column(nullable = false,columnDefinition = "boolean default false")
    @Builder.Default
    @Setter
    @Getter
    private Boolean verified = false;
    @Column(name = "captured_chunks",nullable = false, columnDefinition = "int8 default 0")
    @Builder.Default
    @Getter
    private Long capturedChunks = 0L;

    @Column
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "oAuthUserEmbedded.gameUser", cascade = CascadeType.ALL)
    @Getter
    private List<OAuthUser> oAuths;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Getter
    @JoinTable(
            name = "users_rounds_team_city",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false),
                    @JoinColumn(name = "city_id", referencedColumnName = "city_id",insertable=false, updatable=false)}
    )
    @Filter(name = "activeRoundFilter")
    private List<UserRoundTeamCity> userRoundTeamCities;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private List<GameUser> friends;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "users_followers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
    )
    private List<GameUser> followers;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name="events_users", schema = "map_events",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private List<MapEvent> visitedEvents;
    @OneToMany(mappedBy = "gameUserSkillEmbedded.gameUser", cascade={CascadeType.ALL})
    private List<GameUserSkill> userSkills;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    public void addSkillToUserSkillList(GameUserSkill gameUserSkill){
        userSkills.add(gameUserSkill);
    }
    public void addSkillToUserSkillList(List<GameUserSkill> gameUserSkill){
        if(userSkills != null){
            this.userSkills.addAll(gameUserSkill);
        }else{
            this.userSkills = new ArrayList<>(gameUserSkill);
        }
    }
    public void addOAuthToUser(OAuthUser oAuthUser){
        if(oAuths == null){
            this.oAuths = new ArrayList<>();
        }
        this.oAuths.add(oAuthUser);
    }
    public void followUser(GameUser user) throws GameUserAlreadyFollowed, GameUserUsernameNotUnique {
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
    public Team getTeam(Round round){
        UserRoundTeamCity urt = userRoundTeamCities.stream().filter(urt1 -> urt1.getRound() == round)
                .findFirst().orElseThrow(TeamNullException::new);
        return urt.getTeam();
    }
    public GameUserSkill getUserSkillBySkillId(Integer skillId) throws GameUserSkillNullException {
        return this.userSkills.stream()
                .filter(gameUserSkill -> Objects.equals(gameUserSkill.getGameUserSkillEmbedded().getSkill().getId(), skillId))
                .findFirst().orElseThrow(GameUserSkillNullException::new);
    }
    public List<GameUserSkill> getUserSkills() {
        return Collections.unmodifiableList(userSkills);
    }

    public void addCapturedChunks(Integer value){
        this.capturedChunks += value;
    }
    @Override
    public String getPassword() {
        return password;
    }
    public void setPassword(String newPassword){
        if(this.password.equals(newPassword)){
            throw new GameUserPasswordSame();
        }
        this.password = newPassword;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameUser gameUser = (GameUser) o;
        return Objects.equals(id, gameUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
