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

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.integerbox.SpreadsheetIntegerBox;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A model dialog with a text field which accepts a count value, and when entered trigger the inserting of columns and rows.
 */
public final class SpreadsheetColumnRowInsertCountComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired {

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
        this.go = this.anchor("Go");

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the label and the target.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetColumnRowInsertCountComponentContext context = this.context;
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                context.dialogTitle(),
                context
        );

        dialog.appendChild(this.count);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.go)
                        .appendChild(
                                this.closeAnchor(
                                        context.historyToken()
                                )
                        )
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
    public String idPrefix() {
        return ID_PREFIX;
    }

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetIntegerBox count() {
        return SpreadsheetIntegerBox.empty()
                .setId(ID_PREFIX + "count-TextBox")
                .min(1)
                .setLabel("Count")
                .required()
                .addKeyupListener(
                        (e) -> this.refreshGo()
                ).addChangeListener(
                        (oldValue, newValue) -> this.refreshGo()
                );
    }

    private final SpreadsheetIntegerBox count;

    private void refreshGo() {
        this.go.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .setCount(
                                        OptionalInt.of(
                                                this.count.value()
                                                        .orElse(0)
                                        )
                                )
                )
        );
    }

    private final HistoryTokenAnchorComponent go;

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
