package com.generated.ldmurdergame.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.generated.ldmurdergame.model.MemberLevel;
import com.generated.ldmurdergame.model.MemberLevelConfig;
import com.generated.ldmurdergame.model.MemberLevelOverview;

@Service
public class MemberLevelService {

  private final Map<String, MemberLevelConfig> levelConfigs = new LinkedHashMap<>();
  private final Map<String, Integer> levelMemberCounts = new LinkedHashMap<>();
  private final Map<String, Double> levelMonthlySpends = new LinkedHashMap<>();

  public MemberLevelService() {
    initDefaultConfigs();
    initMockMemberData();
  }

  private void initDefaultConfigs() {
    levelConfigs.put("bronze", new MemberLevelConfig("bronze", "青铜", 0.95));
    levelConfigs.put("silver", new MemberLevelConfig("silver", "白银", 0.90));
    levelConfigs.put("gold", new MemberLevelConfig("gold", "黄金", 0.85));
    levelConfigs.put("platinum", new MemberLevelConfig("platinum", "铂金", 0.80));
    levelConfigs.put("diamond", new MemberLevelConfig("diamond", "钻石", 0.75));
  }

  private void initMockMemberData() {
    levelMemberCounts.put("bronze", 1280);
    levelMemberCounts.put("silver", 650);
    levelMemberCounts.put("gold", 320);
    levelMemberCounts.put("platinum", 150);
    levelMemberCounts.put("diamond", 45);

    levelMonthlySpends.put("bronze", 156000.0);
    levelMonthlySpends.put("silver", 128000.0);
    levelMonthlySpends.put("gold", 96000.0);
    levelMonthlySpends.put("platinum", 72000.0);
    levelMonthlySpends.put("diamond", 45000.0);
  }

  public MemberLevelOverview getOverview() {
    int totalMembers = levelMemberCounts.values().stream().mapToInt(Integer::intValue).sum();
    double totalSpend = levelMonthlySpends.values().stream().mapToDouble(Double::doubleValue).sum();
    double totalDiscount = 0.0;

    List<MemberLevel> levels = new ArrayList<>();

    for (Map.Entry<String, MemberLevelConfig> entry : levelConfigs.entrySet()) {
      String level = entry.getKey();
      MemberLevelConfig config = entry.getValue();
      int count = levelMemberCounts.getOrDefault(level, 0);
      double spend = levelMonthlySpends.getOrDefault(level, 0.0);
      double ratio = totalMembers > 0 ? (double) count / totalMembers : 0.0;
      double discount = spend * (1 - config.discountRate());
      totalDiscount += discount;

      levels.add(new MemberLevel(
        level,
        config.levelName(),
        count,
        ratio,
        config.discountRate(),
        spend,
        discount
      ));
    }

    levels.sort(Comparator.comparingInt(MemberLevel::memberCount).reversed());

    List<MemberLevelConfig> configs = new ArrayList<>(levelConfigs.values());

    return new MemberLevelOverview(
      totalMembers,
      totalSpend,
      totalDiscount,
      levels,
      configs
    );
  }

  public MemberLevelOverview updateConfigs(List<MemberLevelConfig> newConfigs) {
    for (MemberLevelConfig config : newConfigs) {
      if (levelConfigs.containsKey(config.level())) {
        double rate = Math.max(0.5, Math.min(1.0, config.discountRate()));
        levelConfigs.put(config.level(),
          new MemberLevelConfig(config.level(), config.levelName(), rate));
      }
    }
    return getOverview();
  }

  public List<MemberLevelConfig> getConfigs() {
    return new ArrayList<>(levelConfigs.values());
  }
}
