/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.pattern;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.forms.FieldStyle;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.jboss.elemento.EventType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CharSequences;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public final class PatternEditorWidget {

    /**
     * Creates a new {@link PatternEditorWidget}.
     */
    public static PatternEditorWidget with(
            final SpreadsheetPatternKind kind,
            final String title,
            final Supplier<String> loaded,
            final Consumer<String> save,
            final Runnable close,
            final AppContext context) {
        return new PatternEditorWidget(
                kind,
                title,
                loaded,
                save,
                close,
                context
        );
    }

    private PatternEditorWidget(final SpreadsheetPatternKind kind,
                                final String title,
                                final Supplier<String> loaded,
                                final Consumer<String> save,
                                final Runnable close,
                                final AppContext context) {
        this.context = context;
        this.kind = kind;

        this.loaded = loaded;
        this.save = save;
        this.close = close;

        this.patternTextBox = this.patternTextBox();
        this.modalDialog = this.createModalDialog(title);

        this.setPattern(loaded.get());
    }

    /**
     * The {@link TextBox} that holds the pattern in text form.
     */
    private final TextBox patternTextBox;

    /**
     * Creates the pattern text box and installs a value change listener.
     */
    private TextBox patternTextBox() {
        final TextBox textBox = new TextBox();
        textBox.setSpellCheck(false);
        textBox.setFieldStyle(FieldStyle.ROUNDED);
        textBox.setType("text");
        textBox.addEventListener(
                EventType.change,
                this::onPatternTextBox
        );
        return textBox;
    }

    /**
     * Tries to parse the text box text into a {@link SpreadsheetPattern}.
     * If that fails an error message will be displayed and the SAVE button disabled.
     */
    private void onPatternTextBox(final Event event) {
        // update UI here...
        this.context.debug("PatternEditorWidget.onPatternTextBox " + this.patternTextBox.getValue());
    }

    private void setPattern(final String pattern) {
        this.patternTextBox.setValue(pattern);
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private ModalDialog createModalDialog(final String title) {
        final ModalDialog modal = ModalDialog.create(title)
                .setAutoClose(true);
        modal.appendChild(this.patternTextBox);

        modal.appendFooterChild(this.saveButton());
        modal.appendFooterChild(this.undoButton());
        modal.appendFooterChild(this.closeButton());

        modal.open();

        return modal;
    }

    /**
     * Closes or hides the {@link ModalDialog}. THis is necessary when the history token changes and editing a pattern
     * is no longer true.
     */
    public void close() {
        this.modalDialog.close();
    }

    private final ModalDialog modalDialog;

    /**
     * When clicked the CLOSE button invokes {@link #close}.
     */
    private Button closeButton() {
        return this.button(
                "Close",
                StyleType.DANGER,
                this::onCloseButtonClick
        );
    }

    private void onCloseButtonClick(final Event event) {
        this.context.debug("PatternEditorWidget.onCloseButtonClick");

        this.close.run();
        this.modalDialog.close();
    }

    /**
     * Probably a {@link Runnable} that removes the pattern from the selection and then pushes the new history token.
     */
    private final Runnable close;

    /**
     * When clicked the SAVE button invokes {@link #save}.
     */
    private Button saveButton() {
        return this.button(
                "Save",
                StyleType.DEFAULT,
                this::onSaveButtonClick
        );
    }

    private void onSaveButtonClick(final Event event) {
        final String patternText = this.patternTextBox.getValue();

        try {
            this.kind.parse(patternText);
            this.context.debug("PatternEditorWidget.onSaveButtonClick " + CharSequences.quoteAndEscape(patternText));

            this.save.accept(patternText);
        } catch (final Exception cause) {
            this.error(cause.getMessage());
        }
    }

    /**
     * This {@link Consumer} most likely pushes a SaveHistoryToken.
     */
    private final Consumer<String> save;

    /**
     * When clicked the undo button invokes {@link #onUndoButtonClick}.
     */
    private Button undoButton() {
        return this.button(
                "undo",
                StyleType.PRIMARY,
                this::onUndoButtonClick
        );
    }

    /**
     * Reloads the last saved pattern text.
     */
    private void onUndoButtonClick(final Event event) {
        final String patternText = this.loaded.get();
        this.context.debug("PatternEditorWidget.onUndoButtonClick " + CharSequences.quoteAndEscape(patternText));

        this.setPattern(patternText);
    }

    /**
     * This {@link Supplier} provides the original loaded {@link String pattern}, and is used by UNDO.
     */
    private final Supplier<String> loaded;

    /**
     * Creates one of the modal action buttons that appear at the bottom of the modal dialog.
     */
    private Button button(final String text,
                          final StyleType type,
                          final EventListener listener) {
        final Button button = new Button(text);
        button.setButtonType(type);
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    /**
     * The {@link SpreadsheetPatternKind} will be used to customize this modal so the correct buttons are shown
     * for the user to append to the pattern or format values into text etc.
     */
    private final SpreadsheetPatternKind kind;

    /**
     * Display an error message.
     */
    private void error(final String errorMessage) {
        Notification.create(errorMessage)
                .setPosition(Notification.TOP_CENTER)
                .show();
    }

    private final AppContext context;
}
