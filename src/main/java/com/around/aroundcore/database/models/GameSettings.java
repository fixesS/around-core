package com.around.aroundcore.database.models;

import com.around.aroundcore.database.converters.ListOfIntegerConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Entity
@Table(name = "game", schema = "settings")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameSettings implements Serializable {

    @Id
    private Integer id;
    @Column(name = "user_chunk_reward")
    private Integer userChunkReward;
    @Column(name = "user_level_exp_cost_rule")
    @Convert(converter = ListOfIntegerConverter.class)
    private List<Integer> userLevelCost;
    @Column(name = "team_change_cost")
    private Integer teamChangeCost;
    @Column(name = "team_win_reward_coins")
    private Integer teamWinRewardCoins;
    @Column(name = "team_win_reward_exp")
    private Integer teamWinRewardExp;
    @Column(name = "team_win_reward_coins_dividing_ratio")
    private Double teamWinRewardCoinsDividingRatio;
    @Column(name = "team_win_reward_exp_dividing_ratio")
    private Double teamWinRewardExpDividingRatio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSettings that = (GameSettings) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
