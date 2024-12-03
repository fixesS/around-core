package com.around.aroundcore.database.models.user;

import com.around.aroundcore.core.exceptions.api.entity.*;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.database.models.oauth.OAuthUser;
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
    @Column(name = "experience")
    @Builder.Default
    @Getter
    private Integer experience = 0;
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
    @Builder.Default
    @Setter
    @Getter
    private Boolean active = true;
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
    private List<UserRoundTeamCity> userRoundTeamCities = new ArrayList<>();
    @ManyToMany( cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private List<GameUser> friends = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name = "users_followers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
    )
    private List<GameUser> followers = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @JoinTable(
            name="events_users", schema = "map_events",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private List<MapEvent> visitedEvents = new ArrayList<>();
    @OneToMany(mappedBy = "gameUserSkillEmbedded.gameUser", cascade={CascadeType.ALL})
    private List<GameUserSkill> userSkills = new ArrayList<>();
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
        if(user.getFollowers().contains(this)){
            throw new GameUserAlreadyFollowed();
        }
        if(getFriends().contains(user)){
            return;
        }
        if(getFollowers().contains(user)){
            getFollowers().remove(user);
            getFriends().add(user);
            user.getFriends().add(this);
        }else{
            user.getFollowers().add(this);
        }
    }
    public void unfollowUser(GameUser user){
        user.getFollowers().remove(this);
    }
    public void removeUserFromFriends(GameUser user){
        if(getFriends().contains(user)){
            getFriends().remove(user);
            user.getFriends().remove(this);
            getFollowers().add(user);
        }
    }
    public void addVisitedEvents(List<MapEvent> events){
        visitedEvents.addAll(events);
    }
    public void addExperience(Integer value, List<Integer> levelUpRule){
        experience=experience+value;
        level = findClosestIndexBelow(levelUpRule, experience);
    }
    public Integer findClosestIndexBelow(List<Integer> list, int target) {
        int low = 0;
        int high = list.size() - 1;
        int closestIndex = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midValue = list.get(mid);

            if (midValue == target) {
                return mid;
            } else if (midValue < target) {
                closestIndex = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return closestIndex;
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
    public void checkTeamForCurrentRoundAndAnyCity() throws GameUserTeamNullForRoundAndAneCity {
        if(userRoundTeamCities == null || userRoundTeamCities.isEmpty()){
            throw new GameUserTeamNullForRoundAndAneCity();
        }
    }
    public Team getTeam(City city){
        UserRoundTeamCity urt = userRoundTeamCities.stream().filter(urt1 -> urt1.getCity() == city)
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
        return active;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
