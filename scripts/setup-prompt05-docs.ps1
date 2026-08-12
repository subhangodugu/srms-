# Script to populate docs/design-system/ documentation suite for Prompt 05
$dir = "c:\srms\docs\design-system"
New-Item -ItemType Directory -Force -Path $dir | Out-Null

$docs = @{
    "overview.md" = "# SROTS Design System Overview`n`nSingle visual source of truth for all SROTS native desktop views.`n`n- Mode: Enterprise Dark`n- Stack: Java 21 + JavaFX 21 + FXML + CSS"
    "colors.md" = "# Color System`n`n- Background: `#0B0F14``n- Surface: `#111827``n- Surface Elevated: `#172033``n- Border: `#273244``n- Primary Accent: `#6D5DFB``n- Success: `#22C55E``n- Warning: `#F59E0B``n- Danger: `#EF4444``n- Info: `#38BDF8`"
    "typography.md" = "# Typography System`n`nScale:`n- Display: 32px (Bold)`n- Page Title: 24px (Semibold/Bold)`n- Section Title: 18px (Semibold)`n- Card Title: 15px (Semibold)`n- Body: 13px (Regular)`n- Secondary: 12px`n- Caption: 11px"
    "spacing.md" = "# Spacing System`n`nScale: 4px, 8px, 12px, 16px, 20px, 24px, 32px, 40px, 48px, 64px."
    "layout.md" = "# Layout System`n`nStandard App Shell: TopBar + Sidebar (240px) + Main Content Area (padding 24px) + Bottom Status Bar."
    "navigation.md" = "# Navigation System`n`nSidebar navigation with active left indicator bar (`#6D5DFB`)."
    "buttons.md" = "# Button System`n`nPrimary (`.btn-primary`), Secondary (`.btn-secondary`), Destructive, and Icon buttons."
    "forms.md" = "# Form Design System`n`nField label + Input + Helper Text + Actionable validation error message."
    "tables.md" = "# Table Design System`n`nDensity: Compact (36px), Standard (44px default), Comfortable (52px). Status badges & row hover/selection."
    "cards.md" = "# Card System`n`nKPI summary cards with equal height, surface background (`#172033`), border (`#273244`), and 8px radius."
    "dashboards.md" = "# Dashboard Grid System`n`nStandard 4-column KPI row + Main Chart area + Activity feed grid."
    "dialogs.md" = "# Dialog & Overlay System`n`nConfirmation dialogs, Form dialogs, and Side drawer detail panels."
    "notifications.md" = "# Notification & Alert System`n`nToast notifications with semantic badges (Success, Warning, Danger, Info)."
    "charts.md" = "# Chart Standards`n`nLine, Bar, Donut, and Progress charts tailored to SROTS enterprise dark palette."
    "accessibility.md" = "# Accessibility & Keyboard Navigation`n`n- Contrast: WCAG AA compliant contrast.`n- Non-color status indicators: Text + Icon + Color.`n- Visible focus indicators on all controls."
    "animation.md" = "# Motion & Animation Standards`n`nSubtle 120-220ms transitions for dialogs, sidebars, and status changes."
    "components.md" = "# Design System Component Catalog`n`nComprehensive listing of all reusable JavaFX controls and layouts in `com.srots.presentation.components`."
}

foreach ($key in $docs.Keys) {
    Set-Content -Path (Join-Path $dir $key) -Value $docs[$key]
}

Write-Host "Prompt 05 design system documentation suite created!" -ForegroundColor Green
