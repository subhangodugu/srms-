package com.srots.presentation.components.information.avatar;

import com.srots.presentation.components.utility.SrotsSize;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Circular avatar with initials or optional image, and optional status dot.
 * Geometry via inline sizing; colors via CSS classes.
 */
public class SrotsAvatar extends StackPane {

    private final StackPane face = new StackPane();
    private final Circle clip = new Circle();
    private final Label initials = new Label();
    private final ImageView imageView = new ImageView();
    private final Circle statusDot = new Circle(4);
    private SrotsSize size = SrotsSize.STANDARD;

    public SrotsAvatar(String nameOrInitials) {
        this(nameOrInitials, SrotsSize.STANDARD);
    }

    public SrotsAvatar(String nameOrInitials, SrotsSize size) {
        getStyleClass().add("srots-avatar");
        setAlignment(Pos.CENTER);

        face.getStyleClass().add("srots-avatar-face");
        face.setAlignment(Pos.CENTER);

        initials.getStyleClass().add("srots-avatar-initials");
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setVisible(false);
        imageView.setManaged(false);

        face.getChildren().addAll(initials, imageView);
        face.setClip(clip);

        statusDot.getStyleClass().add("srots-avatar-status");
        statusDot.setVisible(false);
        statusDot.setManaged(false);
        StackPane.setAlignment(statusDot, Pos.BOTTOM_RIGHT);

        getChildren().addAll(face, statusDot);
        setInitialsFrom(nameOrInitials);
        setSize(size == null ? SrotsSize.STANDARD : size);
    }

    public void setSize(SrotsSize size) {
        this.size = size == null ? SrotsSize.STANDARD : size;
        double diameter = switch (this.size) {
            case SMALL -> 28;
            case LARGE -> 48;
            case STANDARD -> 36;
        };
        setMinSize(diameter, diameter);
        setPrefSize(diameter, diameter);
        setMaxSize(diameter, diameter);

        face.setMinSize(diameter, diameter);
        face.setPrefSize(diameter, diameter);
        face.setMaxSize(diameter, diameter);

        clip.setRadius(diameter / 2);
        clip.setCenterX(diameter / 2);
        clip.setCenterY(diameter / 2);

        imageView.setFitWidth(diameter);
        imageView.setFitHeight(diameter);

        getStyleClass().removeAll("srots-size-small", "srots-size-standard", "srots-size-large");
        switch (this.size) {
            case SMALL -> getStyleClass().add("srots-size-small");
            case LARGE -> getStyleClass().add("srots-size-large");
            case STANDARD -> getStyleClass().add("srots-size-standard");
        }
    }

    public void setInitials(String initialsText) {
        initials.setText(initialsText == null ? "" : initialsText);
        showInitials();
    }

    public void setInitialsFrom(String nameOrInitials) {
        initials.setText(toInitials(nameOrInitials));
        showInitials();
    }

    public void setImage(Image image) {
        if (image == null) {
            imageView.setImage(null);
            showInitials();
            return;
        }
        imageView.setImage(image);
        imageView.setVisible(true);
        imageView.setManaged(true);
        initials.setVisible(false);
        initials.setManaged(false);
    }

    public void setStatusVisible(boolean visible) {
        statusDot.setVisible(visible);
        statusDot.setManaged(visible);
    }

    public void setStatusStyleClass(String styleClass) {
        statusDot.getStyleClass().setAll("srots-avatar-status");
        if (styleClass != null && !styleClass.isBlank()) {
            statusDot.getStyleClass().add(styleClass);
        }
    }

    private void showInitials() {
        initials.setVisible(true);
        initials.setManaged(true);
        imageView.setVisible(false);
        imageView.setManaged(false);
    }

    private static String toInitials(String name) {
        String initials = UserInitials.fromDisplayName(name);
        return initials.isBlank() ? "?" : initials;
    }
}
