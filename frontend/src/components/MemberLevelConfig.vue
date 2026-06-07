<script setup lang="ts">
import { ref, computed } from "vue";
import type { MemberLevelConfig } from "../types";

const props = defineProps<{ configs: MemberLevelConfig[] }>();
const emit = defineEmits<{ (e: "update", configs: MemberLevelConfig[]): void }>();

const localConfigs = ref<MemberLevelConfig[]>([]);
const isSaving = ref(false);
const saveSuccess = ref(false);

const levelColors: Record<string, string> = {
  bronze: "#cd7f32",
  silver: "#c0c0c0",
  gold: "#ffd700",
  platinum: "#e5e4e2",
  diamond: "#b9f2ff",
};

function initConfigs() {
  localConfigs.value = props.configs.map((c) => ({ ...c }));
  saveSuccess.value = false;
}

initConfigs();

const discountPercentages = computed(() => {
  return localConfigs.value.map((c) => ({
    ...c,
    discountPercent: Math.round(c.discountRate * 100),
    offPercent: Math.round((1 - c.discountRate) * 100),
  }));
});

function updateDiscount(level: string, value: number) {
  const config = localConfigs.value.find((c) => c.level === level);
  if (config) {
    config.discountRate = Math.max(50, Math.min(100, value)) / 100;
    saveSuccess.value = false;
  }
}

async function applyChanges() {
  isSaving.value = true;
  try {
    emit("update", localConfigs.value.map((c) => ({ ...c })));
    saveSuccess.value = true;
    setTimeout(() => {
      saveSuccess.value = false;
    }, 2000);
  } finally {
    isSaving.value = false;
  }
}

function resetToDefaults() {
  const defaults: MemberLevelConfig[] = [
    { level: "bronze", levelName: "青铜", discountRate: 0.95 },
    { level: "silver", levelName: "白银", discountRate: 0.90 },
    { level: "gold", levelName: "黄金", discountRate: 0.85 },
    { level: "platinum", levelName: "铂金", discountRate: 0.80 },
    { level: "diamond", levelName: "钻石", discountRate: 0.75 },
  ];
  localConfigs.value = defaults;
  saveSuccess.value = false;
}

defineExpose({ initConfigs });
</script>

<template>
  <section class="member-level-config" aria-label="等级规则配置">
    <header class="panel-header">
      <div>
        <h2>等级规则配置</h2>
        <p class="panel-subtitle">调整各等级折扣比例，保存后实时重算让利数据</p>
      </div>
      <div class="config-actions">
        <button
          v-if="saveSuccess"
          class="success-indicator"
          disabled
        >
          ✓ 已生效
        </button>
        <button
          class="btn btn-secondary"
          @click="resetToDefaults"
          :disabled="isSaving"
        >
          恢复默认
        </button>
        <button
          class="btn btn-primary"
          @click="applyChanges"
          :disabled="isSaving"
        >
          {{ isSaving ? "保存中..." : "应用配置" }}
        </button>
      </div>
    </header>

    <div class="config-list">
      <div
        v-for="config in discountPercentages"
        :key="config.level"
        class="config-item"
      >
        <div class="config-header">
          <span
            class="level-badge"
            :style="{ backgroundColor: levelColors[config.level] }"
          ></span>
          <strong class="level-name">{{ config.levelName }}</strong>
          <span class="discount-display">
            {{ config.discountPercent }}%
            <small>({{ config.offPercent }}% OFF)</small>
          </span>
        </div>

        <div class="slider-wrapper">
          <span class="slider-label">50%</span>
          <input
            type="range"
            :value="config.discountPercent"
            @input="updateDiscount(config.level, Number(($event.target as HTMLInputElement).value))"
            min="50"
            max="100"
            step="1"
            class="discount-slider"
            :style="{
              '--slider-color': levelColors[config.level],
              '--slider-progress': ((config.discountPercent - 50) / 50 * 100) + '%',
            }"
            :disabled="isSaving"
          />
          <span class="slider-label">100%</span>
        </div>

        <div class="preset-buttons">
          <button
            v-for="preset in [95, 90, 85, 80, 75]"
            :key="preset"
            class="preset-btn"
            :class="{ active: config.discountPercent === preset }"
            @click="updateDiscount(config.level, preset)"
            :disabled="isSaving"
          >
            {{ preset }}%
          </button>
        </div>
      </div>
    </div>

    <div class="config-hint">
      <span class="pill">提示</span>
      <p>折扣比例范围 50% ~ 100%，调整后点击「应用配置」立即生效。系统将根据新折扣重新计算各等级让利金额与总让利。</p>
    </div>
  </section>
</template>

<style scoped>
.member-level-config {
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

.config-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: 700;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: #3268b8;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #2a5aa0;
}

.btn-secondary {
  background: color-mix(in srgb, #19212e 10%, transparent);
  color: #19212e;
  border: 1px solid color-mix(in srgb, #19212e 15%, transparent);
}

.btn-secondary:hover:not(:disabled) {
  background: color-mix(in srgb, #19212e 15%, transparent);
}

.success-indicator {
  padding: 10px 16px;
  border-radius: 8px;
  background: color-mix(in srgb, #2ecc71 15%, transparent);
  color: #27ae60;
  font-weight: 700;
  border: 1px solid color-mix(in srgb, #2ecc71 30%, transparent);
  cursor: default;
}

.config-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.config-item {
  padding: 18px;
  border: 1px solid color-mix(in srgb, #19212e 12%, transparent);
  border-radius: 8px;
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
}

.config-header {
  display: flex;
  align-items: center;
  gap: 12px;
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

.discount-display {
  font-size: 20px;
  font-weight: 800;
  color: #3268b8;
}

.discount-display small {
  font-size: 12px;
  font-weight: 600;
  color: #cf5c36;
  margin-left: 6px;
}

.slider-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.slider-label {
  font-size: 13px;
  font-weight: 600;
  color: color-mix(in srgb, #19212e 55%, transparent);
  min-width: 40px;
}

.discount-slider {
  flex: 1;
  height: 8px;
  border-radius: 999px;
  background: linear-gradient(
    to right,
    var(--slider-color) 0%,
    var(--slider-color) var(--slider-progress),
    color-mix(in srgb, #19212e 10%, transparent) var(--slider-progress),
    color-mix(in srgb, #19212e 10%, transparent) 100%
  );
  outline: none;
  cursor: pointer;
  -webkit-appearance: none;
  appearance: none;
}

.discount-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--slider-color);
  border: 3px solid white;
  box-shadow: 0 2px 8px color-mix(in srgb, #19212e 20%, transparent);
  cursor: pointer;
  transition: transform 0.15s ease;
}

.discount-slider::-webkit-slider-thumb:hover {
  transform: scale(1.15);
}

.discount-slider::-moz-range-thumb {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--slider-color);
  border: 3px solid white;
  box-shadow: 0 2px 8px color-mix(in srgb, #19212e 20%, transparent);
  cursor: pointer;
}

.discount-slider:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.preset-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.preset-btn {
  padding: 6px 14px;
  border-radius: 999px;
  border: 1px solid color-mix(in srgb, #19212e 15%, transparent);
  background: color-mix(in srgb, #19212e 6%, transparent);
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.preset-btn:hover:not(:disabled) {
  background: color-mix(in srgb, #3268b8 12%, transparent);
  border-color: color-mix(in srgb, #3268b8 25%, transparent);
}

.preset-btn.active {
  background: color-mix(in srgb, #3268b8 20%, transparent);
  border-color: #3268b8;
  color: #3268b8;
}

.preset-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.config-hint {
  margin-top: 20px;
  padding: 14px 18px;
  border-radius: 8px;
  background: color-mix(in srgb, #3268b8 8%, transparent);
  border: 1px solid color-mix(in srgb, #3268b8 18%, transparent);
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.config-hint p {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: color-mix(in srgb, #19212e 70%, #3268b8 30%);
}

@media (max-width: 640px) {
  .panel-header {
    flex-direction: column;
  }

  .config-actions {
    width: 100%;
  }

  .config-actions .btn {
    flex: 1;
  }

  .config-header {
    flex-wrap: wrap;
  }
}
</style>
