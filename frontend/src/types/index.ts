export interface FeatureItem {
  id: number;
  title: string;
  description: string;
  status: string;
  metric: string;
}

export interface KpiItem {
  label: string;
  value: string;
  trend: string;
  tone: string;
}

export interface OperationRecord {
  key: string;
  name: string;
  owner: string;
  status: string;
  metric: string;
  priority: string;
}

export interface MemberLevel {
  level: string;
  levelName: string;
  memberCount: number;
  memberRatio: number;
  discountRate: number;
  monthlySpend: number;
  monthlyDiscount: number;
}

export interface MemberLevelConfig {
  level: string;
  levelName: string;
  discountRate: number;
}

export interface MemberLevelOverview {
  totalMembers: number;
  totalMonthlySpend: number;
  totalMonthlyDiscount: number;
  levels: MemberLevel[];
  configs: MemberLevelConfig[];
}

export interface OverviewResponse {
  appName: string;
  appCode: string;
  description: string;
  features: FeatureItem[];
  kpis: KpiItem[];
  records: OperationRecord[];
  memberLevel: MemberLevelOverview;
}
