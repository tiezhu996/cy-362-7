package com.generated.ldmurdergame.model;

import java.util.List;

public record MemberLevelOverview(
  int totalMembers,
  double totalMonthlySpend,
  double totalMonthlyDiscount,
  List<MemberLevel> levels,
  List<MemberLevelConfig> configs
) {
}
