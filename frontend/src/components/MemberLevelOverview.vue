<script setup lang="ts">
import { computed } from "vue";
import type { MemberLevelOverview } from "../types";

const props = defineProps<{ data: MemberLevelOverview }>();

const levelColors: Record<string, string> = {
  bronze: "#cd7f32",
  silver: "#c0c0c0",
  gold: "#ffd700",
  platinum: "#e5e4e2",
  diamond: "#b9f2ff",
};

function formatCurrency(value: number): string {
  return "¥" + value.toLocaleString("zh-CN", { maximumFractionDigits: 0 });
}

function formatPercent(value: number): string {
  return (value * 100).toFixed(1) + "%";
}

function formatDiscount(rate: number): string {
  return (rate * 10).toFixed(0) + "折";
}

const sortedLevels = computed(() => {
  return [...props.data.levels].sort((a, b) => b.memberRatio - a.memberRatio);
});
</script>

<template>
  <section class="member-level-overview" aria-label="会员等级概览">
    <header class="panel-header">
      <div>
        <h2>会员等级权益</h2>
        <p class="panel-subtitle">各等级人数占比与本月让利分析</p>
      </div>
      <div class="summary-kpis">
        <div class="summary-item">
          <span class="pill">会员总数</span>
          <strong>{{ data.totalMembers.toLocaleString() }}</strong>
        </div>
        <div class="summary-item">
          <span class="pill">本月消费</span>
          <strong>{{ formatCurrency(data.totalMonthlySpend) }}</strong>
        </div>
        <div class="summary-item highlight">
          <span class="pill">本月让利</span>
          <strong>{{ formatCurrency(data.totalMonthlyDiscount) }}</strong>
        </div>
      </div>
    </header>

    <div class="level-grid">
      <article
        v-for="level in sortedLevels"
        :key="level.level"
        class="level-card"
      >
        <div class="level-header">
          <span
            class="level-badge"
            :style="{ backgroundColor: levelColors[level.level] }"
          ></span>
          <strong class="level-name">{{ level.levelName }}</strong>
          <span class="level-discount">{{ formatDiscount(level.discountRate) }}</span>
        </div>

        <div class="level-stats">
          <div class="stat-row">
            <span>会员数</span>
            <strong>{{ level.memberCount.toLocaleString() }}</strong>
          </div>
          <div class="stat-row">
            <span>占比</span>
            <strong>{{ formatPercent(level.memberRatio) }}</strong>
          </div>
          <div class="stat-row">
            <span>本月消费</span>
            <strong>{{ formatCurrency(level.monthlySpend) }}</strong>
          </div>
          <div class="stat-row">
            <span>本月让利</span>
            <strong class="discount-value">{{ formatCurrency(level.monthlyDiscount) }}</strong>
          </div>
        </div>

        <div class="ratio-bar">
          <div
            class="ratio-fill"
            :style="{
              width: (level.memberRatio * 100) + '%',
              backgroundColor: levelColors[level.level],
            }"
          ></div>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.member-level-overview {
  border: 1px solid color-mix(in srgb, #19212e 13%, transparent);
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  box-shadow: 0 18px 50px color-mix(in srgb, #19212e 10%, transparent);
  border-radius: 8px;
  padding: clamp(22px, 4vw, 42px);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 24px;
}

.panel-header h2 {
  margin: 0;
  font-size: clamp(20px, 2.5vw, 28px);
  font-weight: 800;
}

.panel-subtitle {
  margin: 6px 0 0;
  color: color-mix(in srgb, #19212e 65%, #3268b8 35%);
  font-size: 14px;
}

.summary-kpis {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
}

.summary-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 12px 16px;
  border-radius: 8px;
  background: color-mix(in srgb, #3268b8 8%, transparent);
  border: 1px solid color-mix(in srgb, #3268b8 15%, transparent);
}

.summary-item.highlight {
  background: color-mix(in srgb, #cf5c36 10%, transparent);
  border-color: color-mix(in srgb, #cf5c36 20%, transparent);
}

.summary-item strong {
  font-size: 20px;
  font-weight: 800;
}

.level-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 14px;
}

.level-card {
  border: 1px solid color-mix(in srgb, #19212e 13%, transparent);
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  box-shadow: 0 8px 24px color-mix(in srgb, #19212e 8%, transparent);
  border-radius: 8px;
  padding: 18px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.level-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px color-mix(in srgb, #19212e 12%, transparent);
}

.level-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
}

.level-badge {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  box-shadow: 0 0 0 3px color-mix(in srgb, currentColor 15%, transparent);
}

.level-name {
  flex: 1;
  font-size: 16px;
  font-weight: 700;
}

.level-discount {
  padding: 4px 10px;
  border-radius: 999px;
  background: color-mix(in srgb, #3268b8 14%, transparent);
  font-weight: 700;
  font-size: 13px;
}

.level-stats {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 14px;
}

.stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.stat-row span {
  color: color-mix(in srgb, #19212e 60%, transparent);
}

.stat-row strong {
  font-weight: 700;
}

.discount-value {
  color: #cf5c36;
}

.ratio-bar {
  height: 8px;
  background: color-mix(in srgb, #19212e 10%, transparent);
  border-radius: 999px;
  overflow: hidden;
}

.ratio-fill {
  height: 100%;
  border-radius: 999px;
  transition: width 0.3s ease;
}

@media (max-width: 640px) {
  .panel-header {
    flex-direction: column;
  }

  .summary-kpis {
    width: 100%;
  }

  .summary-item {
    flex: 1;
    min-width: 100px;
  }
}
</style>
