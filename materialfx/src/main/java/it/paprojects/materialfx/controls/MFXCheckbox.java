package it.paprojects.materialfx.controls;

import it.paprojects.materialfx.MFXResources;
import it.paprojects.materialfx.controls.enums.MarkType;
import it.paprojects.materialfx.skins.MFXCheckboxSkin;
import javafx.css.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class MFXCheckbox extends CheckBox {
    //================================================================================
    // Properties
    //================================================================================
    private static final StyleablePropertyFactory<MFXCheckbox> FACTORY = new StyleablePropertyFactory<>(CheckBox.getClassCssMetaData());
    private final String STYLE_CLASS = "mfx-checkbox";
    private final String STYLESHEET = MFXResources.load("css/mfx-checkbox.css").toString();

    //================================================================================
    // Constructors
    //================================================================================
    public MFXCheckbox() {
        setText("CheckBox");
        init();
    }

    public MFXCheckbox(String text) {
        super(text);
        init();
    }

    //================================================================================
    // Methods
    //================================================================================
    private void init() {
        getStyleClass().add(STYLE_CLASS);
    }

    //================================================================================
    // Stylesheet properties
    //================================================================================
    private final StyleableObjectProperty<Paint> checkedColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.CHECKED_COLOR,
            this,
            "checkedColor",
            Color.rgb(15, 157, 88)
    );

    private final StyleableObjectProperty<Paint> uncheckedColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.UNCHECKED_COLOR,
            this,
            "uncheckedColor",
            Color.rgb(90, 90, 90)
    );

    private final StyleableObjectProperty<MarkType> markType = new SimpleStyleableObjectProperty<>(
            StyleableProperties.MARK_TYPE,
            this,
            "markType",
            MarkType.MODENA
    );

    public Paint getCheckedColor() {
        return checkedColor.get();
    }

    public StyleableObjectProperty<Paint> checkedColorProperty() {
        return checkedColor;
    }

    public void setCheckedColor(Paint checkedColor) {
        this.checkedColor.set(checkedColor);
    }

    public Paint getUncheckedColor() {
        return uncheckedColor.get();
    }

    public StyleableObjectProperty<Paint> uncheckedColorProperty() {
        return uncheckedColor;
    }

    public void setUncheckedColor(Paint uncheckedColor) {
        this.uncheckedColor.set(uncheckedColor);
    }

    public MarkType getMarkType() {
        return markType.get();
    }

    public StyleableObjectProperty<MarkType> markTypeProperty() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType.set(markType);
    }

    //================================================================================
    // CssMetaData
    //================================================================================
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFXCheckbox, Paint> CHECKED_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-checked-color",
                        MFXCheckbox::checkedColorProperty,
                        Color.rgb(15, 157, 88)
                );

        private static final CssMetaData<MFXCheckbox, Paint> UNCHECKED_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-unchecked-color",
                        MFXCheckbox::uncheckedColorProperty,
                        Color.rgb(90, 90, 90)
                );

        private static final CssMetaData<MFXCheckbox, MarkType> MARK_TYPE =
                FACTORY.createEnumCssMetaData(
                        MarkType.class,
                        "-mfx-mark-type",
                        MFXCheckbox::markTypeProperty,
                        MarkType.MODENA
                );

        static {
            cssMetaDataList = List.of(CHECKED_COLOR, UNCHECKED_COLOR, MARK_TYPE);
        }
    }

    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaDataList() {
        return MFXCheckbox.StyleableProperties.cssMetaDataList;
    }

    //================================================================================
    // Override Methods
    //================================================================================

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MFXCheckboxSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return this.getControlCssMetaDataList();
    }

}
