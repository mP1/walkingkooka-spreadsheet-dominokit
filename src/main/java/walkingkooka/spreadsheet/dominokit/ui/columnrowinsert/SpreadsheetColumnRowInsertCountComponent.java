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

package walkingkooka.spreadsheet.dominokit.ui.columnrowinsert;

import elemental2.dom.Event;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.CloseableHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.integerbox.SpreadsheetIntegerBox;

import java.util.Objects;
import java.util.Optional;

/**
 * A model dialog with a text field which accepts a count value, and when entered trigger the inserting of columns and rows.
 */
public final class SpreadsheetColumnRowInsertCountComponent implements SpreadsheetDialogComponentLifecycle {

    /**
     * Creates a new {@link SpreadsheetColumnRowInsertCountComponent}.
     */
    public static SpreadsheetColumnRowInsertCountComponent with(final SpreadsheetColumnRowInsertCountComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetColumnRowInsertCountComponent(context);
    }

    private SpreadsheetColumnRowInsertCountComponent(final SpreadsheetColumnRowInsertCountComponentContext context) {
        this.context = context;

        this.count = this.count();
        this.goButton = this.goButton();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the label and the target.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetColumnRowInsertCountComponentContext context = this.context;
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(context);
        dialog.setTitle(context.dialogTitle());
        dialog.id(ID);

        dialog.appendChild(this.count);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(goButton)
                        .appendChild(this.closeButton())
        );

        return dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetColumnRowInsertCountComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public CloseableHistoryTokenContext closeableHistoryTokenContext() {
        return this.context;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetIntegerBox count() {
        return SpreadsheetIntegerBox.empty()
                .setId(ID_PREFIX + "count-TextBox")
                .min(1)
                .setLabel("Count")
                .required();
    }

    private final SpreadsheetIntegerBox count;

    // buttons..........................................................................................................

    /**
     * When clicked the GO button pushes a {@link HistoryToken} which performs the insert the requested column/rows
     */
    private Button goButton() {
        return this.button(
                "Go",
                StyleType.DEFAULT,
                this::onGoButtonClick
        );
    }

    private void onGoButtonClick(final Event event) {
        final SpreadsheetColumnRowInsertCountComponentContext context = this.context;
        final SpreadsheetIntegerBox count = this.count;
        count.validate();

        final Optional<Integer> value = count.value();

        if (value.isPresent()) {
            try {
                context.save(
                        value.get()
                );
            } catch (final Exception cause) {
                context.error(cause.getMessage());
            }
        }
    }

    private final Button goButton;

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        this.count.setValue(Optional.empty()); // clear old value
        context.giveFocus(
                this.count::focus
        );
    }

    @Override
    public void refresh(final AppContext context) {
        // NOP
    }

    // UI...............................................................................................................

    private final static String ID = "columnRowInsert";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
