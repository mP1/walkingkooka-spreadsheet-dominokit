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

package walkingkooka.spreadsheet.dominokit.insert;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetIntegerBox;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A model dialog with a text field which accepts a count value, and when entered trigger the inserting of columns and rows.
 */
public final class SpreadsheetColumnRowInsertCountDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetColumnRowInsertCountDialogComponent}.
     */
    public static SpreadsheetColumnRowInsertCountDialogComponent with(final SpreadsheetColumnRowInsertCountDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetColumnRowInsertCountDialogComponent(context);
    }

    private SpreadsheetColumnRowInsertCountDialogComponent(final SpreadsheetColumnRowInsertCountDialogComponentContext context) {
        this.context = context;

        this.count = this.count();
        this.insert = this.anchor("Insert");
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which includes a few text boxes to edit the count.
     */
    private DialogComponent dialogCreate() {
        final SpreadsheetColumnRowInsertCountDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.count)
            .appendChild(
                AnchorListComponent.empty()
                    .appendChild(this.insert)
                    .appendChild(this.close)
            );
    }

    private final DialogComponent dialog;

    private final SpreadsheetColumnRowInsertCountDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "columnRowInsert";

    private final static String ID_PREFIX = ID + "-";

    // SpreadsheetLabelComponent........................................................................................

    private SpreadsheetIntegerBox count() {
        return SpreadsheetIntegerBox.empty()
            .setId(ID_PREFIX + "count-TextBox")
            .min(1)
            .setLabel("Count")
            .required()
            .addKeyupListener(
                (e) -> this.refreshInsert()
            ).addChangeListener(
                (oldValue, newValue) -> this.refreshInsert()
            );
    }

    private final SpreadsheetIntegerBox count;

    private final static int DEFAULT_COUNT = 1;

    // go...............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // insert...........................................................................................................

    private void refreshInsert() {
        this.insert.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .setCount(
                        OptionalInt.of(
                            this.count.value()
                                .orElse(DEFAULT_COUNT)
                        )
                    )
            )
        );
    }

    private final HistoryTokenAnchorComponent insert;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    @Override
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        this.count.setValue(Optional.empty()); // clear old value
        context.giveFocus(
            this.count::focus
        );
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshInsert();
        this.refreshClose();
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
