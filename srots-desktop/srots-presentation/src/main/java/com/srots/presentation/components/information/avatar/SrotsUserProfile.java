package com.srots.presentation.components.information.avatar;

import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Display-only user profile row: avatar + name + role + chevron. Optional onClick.
 */
public class SrotsUserProfile extends HBox {

    private static final PseudoClass OPEN = PseudoClass.getPseudoClass("open");

    private final SrotsAvatar avatar;
    private final Label nameLabel = new Label();
    private final Label roleLabel = new Label();
    private final Label chevronLabel = new Label("▼");
    private final VBox text = new VBox(2);
    private Runnable onClick;
    private boolean menuOpen;

    public SrotsUserProfile() {
        this("", "");
    }

    public SrotsUserProfile(String name, String role) {
        getStyleClass().addAll("srots-user-profile", "srots-user-profile-button");
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setFocusTraversable(true);
        setAccessibleRole(javafx.scene.AccessibleRole.BUTTON);
        setAccessibleText("Open user profile menu");

        avatar = new SrotsAvatar(name == null ? "" : name);
        avatar.getStyleClass().add("srots-user-avatar");

        nameLabel.getStyleClass().addAll("srots-state-title", "srots-user-name");
        roleLabel.getStyleClass().addAll("srots-caption", "srots-user-role");
        chevronLabel.getStyleClass().add("srots-user-profile-chevron");

        text.getChildren().addAll(nameLabel, roleLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        getChildren().addAll(avatar, text, spacer, chevronLabel);

        setOnMouseClicked(e -> fireClick());
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
                fireClick();
                e.consume();
            }
        });

        setName(name);
        setRole(role);
    }

    public void setName(String name) {
        nameLabel.setText(name == null ? "" : name);
        avatar.setInitialsFrom(name);
    }

    public void setRole(String role) {
        roleLabel.setText(role == null ? "" : role);
        boolean show = role != null && !role.isBlank();
        roleLabel.setVisible(show);
        roleLabel.setManaged(show);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
        getStyleClass().remove("srots-clickable");
        if (onClick != null) {
            getStyleClass().add("srots-clickable");
        }
    }

    public void setMenuOpen(boolean open) {
        this.menuOpen = open;
        pseudoClassStateChanged(OPEN, open);
        chevronLabel.setText(open ? "▲" : "▼");
        setAccessibleText(open ? "Close user profile menu" : "Open user profile menu");
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setCompact(boolean compact) {
        text.setVisible(!compact);
        text.setManaged(!compact);
    }

    public SrotsAvatar getAvatar() {
        return avatar;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getRoleLabel() {
        return roleLabel;
    }

    public Label getChevronLabel() {
        return chevronLabel;
    }

    private void fireClick() {
        if (onClick != null) {
            onClick.run();
        }
    }
}
