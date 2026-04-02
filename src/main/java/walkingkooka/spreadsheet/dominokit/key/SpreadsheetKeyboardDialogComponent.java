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

package walkingkooka.spreadsheet.dominokit.key;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;

import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays keyboard bindings and possibly in the future other keyboard related config.
 */
public final class SpreadsheetKeyboardDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetKeyboardDialogComponent}.
     */
    public static SpreadsheetKeyboardDialogComponent with(final SpreadsheetKeyboardDialogComponentContext context) {
        return new SpreadsheetKeyboardDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetKeyboardDialogComponent(final SpreadsheetKeyboardDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        this.table = this.table();
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = "keyboard";

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final SpreadsheetKeyboardDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).setTitle(
                context.dialogTitle()
            ).appendChild(
                this.table
            ).appendChild(
                AnchorListComponent.empty()
                    .appendChild(this.close)
            );
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    // KeyBindingTableComponent.........................................................................................

    private KeyBindingTableComponent table() {
        return KeyBindingTableComponent.empty(this.idPrefix())
            .setValue(
                Optional.of(
                    this.context.spreadsheetViewportComponentKeyBindings()
                    .all()
                )
            );
    }

    private final KeyBindingTableComponent table;

    // ComponentLifecycleMatcherDelegator...............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    // dialog links.....................................................................................................
    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // DialogComponentLifecycle.........................................................................................

    @Override
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.table.setValue(
            Optional.of(
                this.context.spreadsheetViewportComponentKeyBindings()
                    .all()
            )
        );
        this.refreshLinks();
    }

    private void refreshLinks() {
        final HistoryToken historyToken = this.context.historyToken();

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_KEYBOARD_COMPONENT_LIFECYCLE;
    }

    private final SpreadsheetKeyboardDialogComponentContext context;
}
