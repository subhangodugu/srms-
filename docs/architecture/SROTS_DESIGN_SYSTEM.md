# SROTS Design System & UI Specification

## 1. Design Aesthetics & Visual Tokens
SROTS Desktop features a modern, high-contrast dark enterprise theme with glassmorphic elements:

### Color Palette (JavaFX CSS Tokens)
- **Background Dark**: `#0F172A` (Deep Slate)
- **Card / Surface Dark**: `#1E293B` (Dark Navy)
- **Border / Divider**: `#334155` (Slate Gray)
- **Text Primary**: `#F8FAFC` (Pure Light)
- **Text Secondary**: `#94A3B8` (Muted Blue Gray)
- **Accent Primary**: `#3B82F6` (Electric Blue)
- **Accent Hover**: `#2563EB` (Vibrant Blue)
- **Status Success**: `#22C55E` (Emerald Green)
- **Status Warning**: `#F59E0B` (Amber Gold)
- **Status Danger**: `#EF4444` (Crimson Red)

## 2. Typography & Custom JavaFX Controls
- **Font Family**: System UI / Inter / Segoe UI (Clean sans-serif).
- **Custom Controls**:
  - `SRMSBadge`: Rounded status pill with dynamic color tokens (`PASS`, `WARN`, `FAIL`, `PENDING`).
  - `SRMSCard`: Styled `VBox` container with subtle border and dark card background.
  - `SRMSButton`: Primary and Secondary styled buttons with hover micro-animations.
