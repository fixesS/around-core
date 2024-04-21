package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.GameUserPasswordSame;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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

    @OneToMany(fetch=FetchType.EAGER,mappedBy = "gameUserSkillEmbedded.gameUser", cascade={CascadeType.ALL})
    private List<GameUserSkill> userSkills;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
