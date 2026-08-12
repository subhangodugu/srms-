package com.srots.presentation.components.utility.icons;

/**
 * Icon glyphs as Unicode/ASCII symbols (not emoji-first).
 */
public enum SrotsIcon {
    SEARCH("⌕"),
    REFRESH("↻"),
    EDIT("✎"),
    DELETE("⌫"),
    MORE("…"),
    SETTINGS("⚙"),
    PREFERENCES("☰"),
    USER("◎"),
    INFO("ⓘ"),
    SIGN_OUT("⎋"),
    MINIMIZE("\u2212"),
    MAXIMIZE("\u25A1"),
    RESTORE("\u25A3"),
    CLOSE("X"),
    BACK("←"),
    FORWARD("→"),
    ALERT("!"),
    HELP("?"),
    BELL("\uD83D\uDD14"),
    TASK("☑"),
    PROJECT("▦"),
    RELEASE("⇪"),
    DEPLOY("⇑"),
    SUPPORT("☎"),
    APPROVAL("✓"),
    SECURITY("⛨"),
    PRODUCT("◈"),
    CHECK("✓");

    private final String glyph;

    SrotsIcon(String glyph) {
        this.glyph = glyph;
    }

    public String getGlyph() {
        return glyph;
    }

    @Override
    public String toString() {
        return glyph;
    }
}
