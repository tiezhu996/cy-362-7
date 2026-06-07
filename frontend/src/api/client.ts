import { API_BASE_URL } from "../constants/app";
import type { OverviewResponse, MemberLevelOverview, MemberLevelConfig } from "../types";

export async function fetchOverview(): Promise<OverviewResponse> {
  const response = await fetch(`${API_BASE_URL}/overview`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Overview request failed: ${response.status}`);
  }

  return response.json() as Promise<OverviewResponse>;
}

export async function fetchMemberLevelOverview(): Promise<MemberLevelOverview> {
  const response = await fetch(`${API_BASE_URL}/member-level/overview`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Member level overview request failed: ${response.status}`);
  }

  return response.json() as Promise<MemberLevelOverview>;
}

export async function fetchMemberLevelConfigs(): Promise<MemberLevelConfig[]> {
  const response = await fetch(`${API_BASE_URL}/member-level/configs`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Member level configs request failed: ${response.status}`);
  }

  return response.json() as Promise<MemberLevelConfig[]>;
}

export async function updateMemberLevelConfigs(
  configs: MemberLevelConfig[]
): Promise<MemberLevelOverview> {
  const response = await fetch(`${API_BASE_URL}/member-level/configs`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(configs),
  });

  if (!response.ok) {
    throw new Error(`Update member level configs failed: ${response.status}`);
  }

  return response.json() as Promise<MemberLevelOverview>;
}
