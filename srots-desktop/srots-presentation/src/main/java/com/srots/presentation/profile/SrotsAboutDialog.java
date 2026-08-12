package com.srots.presentation.profile;

import javafx.stage.Window;

/**
 * About SROTS dialog entry point. Never displays secrets.
 */
public final class SrotsAboutDialog {

    private SrotsAboutDialog() {
    }

    public static void show(Window owner, AboutInfo info) {
        new DialogService.Default().showAbout(owner, info);
    }
}
