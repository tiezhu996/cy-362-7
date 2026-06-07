package com.generated.ldmurdergame.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldmurdergame.model.MemberLevelConfig;
import com.generated.ldmurdergame.model.MemberLevelOverview;
import com.generated.ldmurdergame.service.MemberLevelService;

@RestController
@RequestMapping({"/member-level", "/api/member-level"})
public class MemberLevelController {

  private final MemberLevelService memberLevelService;

  public MemberLevelController(MemberLevelService memberLevelService) {
    this.memberLevelService = memberLevelService;
  }

  @GetMapping({"/overview"})
  public MemberLevelOverview overview() {
    return memberLevelService.getOverview();
  }

  @GetMapping({"/configs"})
  public List<MemberLevelConfig> configs() {
    return memberLevelService.getConfigs();
  }

  @PostMapping({"/configs"})
  public MemberLevelOverview updateConfigs(@RequestBody List<MemberLevelConfig> configs) {
    return memberLevelService.updateConfigs(configs);
  }
}
