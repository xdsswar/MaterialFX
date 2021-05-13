/*
 *     Copyright (C) 2021 Parisi Alessandro
 *     This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 *     MaterialFX is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     MaterialFX is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.materialfx.controls;

import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.skins.MFXTextFieldSkin;
import io.github.palexdev.materialfx.validation.MFXDialogValidator;
import io.github.palexdev.materialfx.validation.base.AbstractMFXValidator;
import io.github.palexdev.materialfx.validation.base.Validated;
import javafx.beans.property.StringProperty;
import javafx.css.*;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;

import java.util.List;
import java.util.function.Supplier;

/**
 * This is the implementation of a TextField restyled to comply with modern standards.
 * <p></p>
 * Extends {@code TextField}, redefines the style class to "mfx-text-field" for usage in CSS and
 * includes a {@code MFXDialogValidator} for input validation.
 * <p></p>
 * Defines a new PseudoClass: ":invalid" to specify the control's look when the validator's state is invalid.
 */
public class MFXTextField extends TextField implements Validated<MFXDialogValidator> {
    //================================================================================
    // Properties
    //================================================================================
    private static final StyleablePropertyFactory<MFXTextField> FACTORY = new StyleablePropertyFactory<>(TextField.getClassCssMetaData());
    private final String STYLE_CLASS = "mfx-text-field";
    private final String STYLESHEET = MFXResourcesLoader.load("css/mfx-textfield.css");

    private MFXDialogValidator validator;
    protected static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    //================================================================================
    // Constructors
    //================================================================================
    public MFXTextField() {
        this("");
    }

    public MFXTextField(String text) {
        super(text);
        initialize();
    }

    //================================================================================
    // Validation
    //================================================================================

    /**
     * Configures the validator. The first time the error label can appear in two cases:
     * <p></p>
     * 1) The validator {@link AbstractMFXValidator#isInitControlValidation()} flag is true,
     * in this case as soon as the control is laid out in the scene the label visible property is
     * set accordingly to the validator state. (by default is false) <p>
     * 2) When the control lose the focus and the the validator's state is invalid.
     * <p></p>
     * Then the label visible property is automatically updated when the validator state changes.
     * <p></p>
     * The validator is also responsible for updating the ":invalid" pseudo class.
     */
    private void setupValidator() {
        validator = new MFXDialogValidator("Error");
        validator.setDialogType(DialogType.ERROR);
        validator.validProperty().addListener(invalidated -> pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !isValid()));

        sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                if (getValidator().isInitControlValidation()) {
                    pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !isValid());
                } else {
                    pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                }
        });
    }

    @Override
    public MFXTextField installValidator(Supplier<MFXDialogValidator> validatorSupplier) {
        this.validator = validatorSupplier.get();
        return this;
    }

    /**
     * Returns the validator instance of this control.
     */
    @Override
    public MFXDialogValidator getValidator() {
        return validator;
    }

    /**
     * Delegate method to get the validator's title.
     */
    public String getValidatorTitle() {
        return validator.getTitle();
    }

    /**
     * Delegate method to get the validator's title property.
     */
    public StringProperty validatorTitleProperty() {
        return validator.titleProperty();
    }

    /**
     * Delegate method to set the validator's title.
     */
    public void setValidatorTitle(String title) {
        validator.setTitle(title);
    }

    //================================================================================
    // Methods
    //================================================================================
    private void initialize() {
        getStyleClass().add(STYLE_CLASS);
        setupValidator();

        textProperty().addListener((observable, oldValue, newValue) -> {
            int limit = getTextLimit();
            if (limit == -1) {
                return;
            }

            if (newValue.length() > limit) {
                String s = newValue.substring(0, limit);
                setText(s);
            }
        });
    }

    //================================================================================
    // Styleable Properties
    //================================================================================
    private final StyleableIntegerProperty textLimit = new SimpleStyleableIntegerProperty(
            StyleableProperties.TEXT_LIMIT,
            this,
            "maxLength",
            -1
    );

    private final StyleableObjectProperty<Paint> lineColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.LINE_COLOR,
            this,
            "lineColor",
            Color.rgb(50, 120, 220)
    );

    private final StyleableObjectProperty<Paint> unfocusedLineColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.UNFOCUSED_LINE_COLOR,
            this,
            "unfocusedLineColor",
            Color.rgb(77, 77, 77)
    );

    private final StyleableDoubleProperty lineStrokeWidth = new SimpleStyleableDoubleProperty(
            StyleableProperties.LINE_STROKE_WIDTH,
            this,
            "lineStrokeWidth",
            2.0
    );

    private final StyleableObjectProperty<StrokeLineCap> lineStrokeCap = new SimpleStyleableObjectProperty<>(
            StyleableProperties.LINE_STROKE_CAP,
            this,
            "lineStrokeCap",
            StrokeLineCap.ROUND
    );

    private final StyleableBooleanProperty animateLines = new SimpleStyleableBooleanProperty(
            StyleableProperties.ANIMATE_LINES,
            this,
            "animateLines",
            true
    );

    private final StyleableBooleanProperty validated = new SimpleStyleableBooleanProperty(
            StyleableProperties.IS_VALIDATED,
            this,
            "isValidated",
            false
    );

    public int getTextLimit() {
        return textLimit.get();
    }

    /**
     * Specifies the maximum text length.
     */
    public StyleableIntegerProperty textLimitProperty() {
        return textLimit;
    }

    public void setTextLimit(int textLimit) {
        this.textLimit.set(textLimit);
    }

    public Paint getLineColor() {
        return lineColor.get();
    }

    /**
     * Specifies the line's color when the control is focused.
     */
    public StyleableObjectProperty<Paint> lineColorProperty() {
        return lineColor;
    }

    public void setLineColor(Paint lineColor) {
        this.lineColor.set(lineColor);
    }

    public Paint getUnfocusedLineColor() {
        return unfocusedLineColor.get();
    }

    /**
     * Specifies the line's color when the control is not focused.
     */
    public StyleableObjectProperty<Paint> unfocusedLineColorProperty() {
        return unfocusedLineColor;
    }

    public void setUnfocusedLineColor(Paint unfocusedLineColor) {
        this.unfocusedLineColor.set(unfocusedLineColor);
    }

    public double getLineStrokeWidth() {
        return lineStrokeWidth.get();
    }

    /**
     * Specifies the lines' stroke width.
     */
    public StyleableDoubleProperty lineStrokeWidthProperty() {
        return lineStrokeWidth;
    }

    public void setLineStrokeWidth(double lineStrokeWidth) {
        this.lineStrokeWidth.set(lineStrokeWidth);
    }

    public StrokeLineCap getLineStrokeCap() {
        return lineStrokeCap.get();
    }

    /**
     * Specifies the lines' stroke cap.
     */
    public StyleableObjectProperty<StrokeLineCap> lineStrokeCapProperty() {
        return lineStrokeCap;
    }

    public void setLineStrokeCap(StrokeLineCap lineStrokeCap) {
        this.lineStrokeCap.set(lineStrokeCap);
    }

    public boolean isAnimateLines() {
        return animateLines.get();
    }

    /**
     * Specifies if the lines switch between focus/un-focus should be animated.
     */
    public StyleableBooleanProperty animateLinesProperty() {
        return animateLines;
    }

    public void setAnimateLines(boolean animateLines) {
        this.animateLines.set(animateLines);
    }

    public boolean isValidated() {
        return validated.get();
    }

    /**
     * Specifies if validation is required for the control.
     */
    public StyleableBooleanProperty isValidatedProperty() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated.set(validated);
    }

    //================================================================================
    // CssMetaData
    //================================================================================
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFXTextField, Number> TEXT_LIMIT =
                FACTORY.createSizeCssMetaData(
                        "-mfx-text-limit",
                        MFXTextField::textLimitProperty,
                        -1
                );

        private static final CssMetaData<MFXTextField, Paint> LINE_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-line-color",
                        MFXTextField::lineColorProperty,
                        Color.rgb(50, 150, 205)
                );

        private static final CssMetaData<MFXTextField, Paint> UNFOCUSED_LINE_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-unfocused-line-color",
                        MFXTextField::unfocusedLineColorProperty,
                        Color.rgb(77, 77, 77)
                );

        private final static CssMetaData<MFXTextField, Number> LINE_STROKE_WIDTH =
                FACTORY.createSizeCssMetaData(
                        "-mfx-line-stroke-width",
                        MFXTextField::lineStrokeWidthProperty,
                        2.0
                );

        private static final CssMetaData<MFXTextField, StrokeLineCap> LINE_STROKE_CAP =
                FACTORY.createEnumCssMetaData(
                        StrokeLineCap.class,
                        "-mfx-line-stroke-cap",
                        MFXTextField::lineStrokeCapProperty,
                        StrokeLineCap.ROUND
                );

        private static final CssMetaData<MFXTextField, Boolean> ANIMATE_LINES =
                FACTORY.createBooleanCssMetaData(
                        "-mfx-animate-lines",
                        MFXTextField::animateLinesProperty,
                        true
                );

        private static final CssMetaData<MFXTextField, Boolean> IS_VALIDATED =
                FACTORY.createBooleanCssMetaData(
                        "-mfx-validate",
                        MFXTextField::isValidatedProperty,
                        false
                );

        static {
            cssMetaDataList = List.of(
                    TEXT_LIMIT,
                    LINE_COLOR, UNFOCUSED_LINE_COLOR,
                    LINE_STROKE_WIDTH, LINE_STROKE_CAP,
                    IS_VALIDATED
            );
        }

    }

    public static List<CssMetaData<? extends Styleable, ?>> getControlCssMetaDataList() {
        return StyleableProperties.cssMetaDataList;
    }

    //================================================================================
    // Override Methods
    //================================================================================
    @Override
    protected Skin<?> createDefaultSkin() {
        return new MFXTextFieldSkin(this);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return MFXTextField.getControlCssMetaDataList();
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }
}
