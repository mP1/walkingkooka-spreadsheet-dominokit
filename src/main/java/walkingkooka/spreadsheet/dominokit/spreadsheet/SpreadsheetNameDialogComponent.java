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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Displays a dialog box allowing the user to edit and save a {@link walkingkooka.spreadsheet.SpreadsheetName}
 * for the selected {@link walkingkooka.spreadsheet.SpreadsheetId}.
 */
public final class SpreadsheetNameDialogComponent implements SpreadsheetDialogComponentLifecycle,
        SpreadsheetMetadataFetcherWatcher,
        NopFetcherWatcher {

    public static SpreadsheetNameDialogComponent with(final SpreadsheetNameDialogComponentContext context) {
        return new SpreadsheetNameDialogComponent(
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetNameDialogComponent(final SpreadsheetNameDialogComponentContext context) {
        super();

        context.addHistoryTokenWatcher(this);
        this.context = context;

        this.name = this.name();

        this.save = this.anchor("Save")
                .setDisabled(true);
        this.undo = this.anchor("Undo")
                .setDisabled(true);
        this.close = this.anchor("Close");

        this.lastSave = null;

        this.dialog = this.dialogCreate();

        if (context.shouldLoadSpreadsheetMetadata()) {
            context.addSpreadsheetMetadataFetcherWatcher(this);
        }
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        return SpreadsheetDialogComponent.with(
                        ID + SpreadsheetElementIds.DIALOG,
                        "Spreadsheet Name",
                        true, // includeClose
                        this.context
                ).appendChild(this.name)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.save)
                                .appendChild(this.undo)
                                .appendChild(this.close)
                );
    }

    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "spreadsheetName";

    final static String ID_PREFIX = ID + '-';

    // name.............................................................................................................

    private SpreadsheetNameComponent name() {
        return SpreadsheetNameComponent.empty()
                .setId(ID_PREFIX + "TextBox") // SpreadsheetElementIds.TEXT_BOX
                .addKeyupListener(
                        (e) -> this.onNameChange(this.name.value())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onNameChange(newValue)
                );
    }

    private void onNameChange(final Optional<SpreadsheetName> name) {
        this.refreshSave(
                name.map(SpreadsheetName::value).orElse(null)
        );
    }

    private void setName(final Optional<SpreadsheetName> name) {
        if (name.isPresent()) {
            this.name.setValue(name);
            this.lastSave = name.map(SpreadsheetName::value)
                    .orElse(null);
        }
    }

    /**
     * The {@link SpreadsheetNameComponent} that holds the {@link walkingkooka.spreadsheet.SpreadsheetName}.
     */
    private final SpreadsheetNameComponent name;

    /**
     * A SAVE link which will be updated each time the name box is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A UNDO link which will be updated each time the name is saved.
     */
    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    @Override
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        final SpreadsheetNameDialogComponentContext dialogContext = this.context;

        final SpreadsheetId id = dialogContext.spreadsheetId();
        this.spreadsheetId = id;
        this.setName(dialogContext.spreadsheetName());

        if (dialogContext.shouldLoadSpreadsheetMetadata()) {
            dialogContext.loadSpreadsheetMetadata(id);
        }
    }

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetNameDialogComponentContext dialogContext = this.context;
        final Optional<SpreadsheetName> name = dialogContext.spreadsheetName();

        if (name.isPresent()) {
            this.refreshSave(
                    name.get()
                            .value()
            );
        }

        this.refreshUndo();

        this.refreshClose();
    }

    private void refreshClose() {
        this.close.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .close()
                )
        );
    }

    private void refreshSave(final String name) {
        this.save.setHistoryToken(
                Optional.ofNullable(
                        CharSequences.isNullOrEmpty(name) ?
                                null :
                                this.context.historyToken()
                                        .setSave(name)
                )
        );
    }

    private void refreshUndo() {
        final String lastSave = this.lastSave;

        this.undo.setHistoryToken(
                Optional.ofNullable(
                        CharSequences.isNullOrEmpty(lastSave) ?
                                null :
                                this.context.historyToken()
                                        .setSave(lastSave)
                )
        );
    }

    private String lastSave;

    private final SpreadsheetNameDialogComponentContext context;

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onEmptyResponse(final AppContext context) {
        // ignore
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        if (this.isOpen()) {
            if (Objects.equals(
                    metadata.id().orElse(null),
                    this.spreadsheetId)) {
                this.setName(
                        metadata.name()
                );
            }
        } else {
            this.spreadsheetId = null;
        }
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // ignore
    }

    private SpreadsheetId spreadsheetId;
}
