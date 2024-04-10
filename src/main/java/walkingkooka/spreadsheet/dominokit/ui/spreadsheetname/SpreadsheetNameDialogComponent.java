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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Displays a dialog box allowing the user to edit and save a {@link walkingkooka.spreadsheet.SpreadsheetName}
 * for the selected {@link walkingkooka.spreadsheet.SpreadsheetId}.
 */
public final class SpreadsheetNameDialogComponent implements SpreadsheetDialogComponentLifecycle {

    final static String ID = "name";

    final static String ID_PREFIX = ID + '-';


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
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                "Spreadsheet Name",
                true, // includeClose
                this.context
        );

        dialog.appendChild(this.name);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.save)
                        .appendChild(this.undo)
                        .appendChild(this.close)
        );

        return dialog;
    }

    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private SpreadsheetNameComponent name() {
        return SpreadsheetNameComponent.empty()
                .setId(ID_PREFIX + "name")
                .addKeyupListener(
                        (e) -> this.onNameChange(this.name.value())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onNameChange(newValue)
                );
    }

    private void onNameChange(final Optional<SpreadsheetName> name) {
        this.refreshSave(
                name.map(SpreadsheetName::value).orElse(null),
                this.context.historyToken()
        );
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
    public void openGiveFocus(final AppContext context) {
        final SpreadsheetNameHistoryToken nameHistoryToken = context.historyToken().cast(SpreadsheetNameHistoryToken.class);

        this.name.setValue(
                Optional.of(
                        nameHistoryToken.name()
                )
        );
        this.lastSave = nameHistoryToken.name()
                .value();
    }

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetNameHistoryToken historyToken = context.historyToken().cast(SpreadsheetNameHistoryToken.class);

        this.refreshSave(
                historyToken.name().value(),
                historyToken
        );

        this.refreshUndo(historyToken);

        this.refreshClose(historyToken);
    }

    private void refreshClose(final SpreadsheetNameHistoryToken historyToken) {
        this.close.setHistoryToken(
                Optional.of(
                        historyToken.close()
                )
        );
    }

    private void refreshSave(final String name,
                             final HistoryToken historyToken) {
        this.save.setHistoryToken(
                Optional.ofNullable(
                        CharSequences.isNullOrEmpty(name) ?
                                null :
                                historyToken.setSave(name)
                )
        );
    }

    private void refreshUndo(SpreadsheetNameHistoryToken historyToken) {
        final String lastSave = this.lastSave;

        this.undo.setHistoryToken(
                Optional.ofNullable(
                        CharSequences.isNullOrEmpty(lastSave) ?
                                null :
                                historyToken.setSave(lastSave)
                )
        );
    }

    private String lastSave;

    private final SpreadsheetNameDialogComponentContext context;
}
