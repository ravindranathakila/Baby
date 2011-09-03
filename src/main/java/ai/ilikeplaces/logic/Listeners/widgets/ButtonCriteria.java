package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.util.InterWidgetMetadata;

public class ButtonCriteria implements InterWidgetMetadata<ButtonCriteria> {
    private boolean doRefreshPageOnClick;
    private String buttonText;
    private String buttonImage;
    private String buttonWidthCss;
    private Object[] metadata;

    public ButtonCriteria(final boolean doRefreshPageOnClick, final String buttonText, final String buttonImage) {
        this.doRefreshPageOnClick = doRefreshPageOnClick;
        this.buttonText = buttonText;
        this.buttonImage = buttonImage;
    }
    public ButtonCriteria(final boolean doRefreshPageOnClick, final String buttonText, final String buttonImage, final String buttonWidthCss) {
        this.doRefreshPageOnClick = doRefreshPageOnClick;
        this.buttonText = buttonText;
        this.buttonImage = buttonImage;
        this.buttonWidthCss = buttonWidthCss;
    }

    public boolean isDoRefreshPageOnClick() {
        return doRefreshPageOnClick;
    }

    public void setDoRefreshPageOnClick(final boolean doRefreshPageOnClick) {
        this.doRefreshPageOnClick = doRefreshPageOnClick;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(final String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(final String buttonImage) {
        this.buttonImage = buttonImage;
    }

    public String getButtonWidthCss() {
        return buttonWidthCss;
    }

    public void setButtonWidthCss(String buttonWidthCss) {
        this.buttonWidthCss = buttonWidthCss;
    }

    public Object[] getMetadata() {
        return metadata;
    }

    public ButtonCriteria setMetadata(final Object... metadata) {
        this.metadata = metadata;
        return this;
    }
}