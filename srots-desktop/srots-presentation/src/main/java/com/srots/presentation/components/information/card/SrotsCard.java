package com.srots.presentation.components.information.card;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Card with header / title / subtitle / content / footer regions. */
public class SrotsCard extends VBox {

    private final VBox header = new VBox(2);
    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final VBox content = new VBox(8);
    private final VBox footer = new VBox(4);
    private boolean customHeader;

    public SrotsCard() {
        this(null, null);
    }

    public SrotsCard(String title) {
        this(title, null);
    }

    public SrotsCard(String title, String subtitle) {
        super(8);
        getStyleClass().add("srots-card");

        titleLabel.getStyleClass().add("srots-card-title");
        subtitleLabel.getStyleClass().add("srots-card-subtitle");
        header.getStyleClass().add("srots-card-header");
        content.getStyleClass().add("srots-card-content");
        footer.getStyleClass().add("srots-card-footer");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        VBox.setVgrow(content, Priority.ALWAYS);

        getChildren().addAll(header, content, footer);
        setTitle(title);
        setSubtitle(subtitle);
        refreshRegionVisibility();
    }

    public void setHeader(Node headerNode) {
        header.getChildren().clear();
        customHeader = headerNode != null;
        if (headerNode != null) {
            header.getChildren().add(headerNode);
        } else {
            header.getChildren().addAll(titleLabel, subtitleLabel);
        }
        refreshRegionVisibility();
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
        boolean show = title != null && !title.isBlank();
        titleLabel.setVisible(show);
        titleLabel.setManaged(show);
        refreshRegionVisibility();
    }

    public void setSubtitle(String subtitle) {
        subtitleLabel.setText(subtitle == null ? "" : subtitle);
        boolean show = subtitle != null && !subtitle.isBlank();
        subtitleLabel.setVisible(show);
        subtitleLabel.setManaged(show);
        refreshRegionVisibility();
    }

    public void setContent(Node... nodes) {
        content.getChildren().setAll(nodes == null ? new Node[0] : nodes);
        refreshRegionVisibility();
    }

    public void setFooter(Node... nodes) {
        footer.getChildren().setAll(nodes == null ? new Node[0] : nodes);
        refreshRegionVisibility();
    }

    public VBox getContentPane() {
        return content;
    }

    public VBox getFooterPane() {
        return footer;
    }

    private void refreshRegionVisibility() {
        boolean showHeader = customHeader || titleLabel.isVisible() || subtitleLabel.isVisible();
        header.setVisible(showHeader);
        header.setManaged(showHeader);

        boolean hasContent = !content.getChildren().isEmpty();
        content.setVisible(hasContent);
        content.setManaged(hasContent);

        boolean hasFooter = !footer.getChildren().isEmpty();
        footer.setVisible(hasFooter);
        footer.setManaged(hasFooter);
    }
}
