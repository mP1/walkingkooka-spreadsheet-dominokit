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

package walkingkooka.spreadsheet.dominokit.dialog;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.FakeRefreshContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DialogAnchorListComponentTest implements HtmlComponentTesting<DialogAnchorListComponent<Locale>, HTMLDivElement>,
    ValueComponentTesting<HTMLDivElement, Locale, DialogAnchorListComponent<Locale>>,
    SpreadsheetMetadataTesting {

    private final static String ID_PREFIX = "Test123-";

    private final static Optional<Locale> VALUE = Optional.of(LOCALE);

    private final static RefreshContext REFRESH_CONTEXT = new FakeRefreshContext();

    // with.............................................................................................................

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> DialogAnchorListComponent.empty(
                null,
                new FakeDialogAnchorListComponentContext<>()
            )
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> DialogAnchorListComponent.empty(
                "",
                new FakeDialogAnchorListComponentContext<>()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> DialogAnchorListComponent.empty(
                ID_PREFIX,
                null
            )
        );
    }

    // TreePrint........................................................................................................

    @Test
    public void testSave() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).save(),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n"
        );
    }

    @Test
    public void testSaveAndSetValue() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                    ID_PREFIX,
                    this.createContext()
                ).save()
                .setValue(VALUE),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-AU] id=Test123-save-Link\n"
        );
    }

    @Test
    public void testClear() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).clearLink(),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n"
        );
    }

    @Test
    public void testClearAndSetValue() {
        final DialogAnchorListComponent<Locale> component = DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).clearLink()
            .setValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n"
        );
    }

    @Test
    public void testUndo() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).undo(),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n"
        );
    }

    @Test
    public void testUndoAndSetValue() {
        final DialogAnchorListComponent<Locale> component = DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).undo()
            .setValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n"
        );
    }

    @Test
    public void testClose() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).close(),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testCloseAndSetValue() {
        final DialogAnchorListComponent<Locale> component = DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).close()
            .setValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testSaveClearUndoCloseAndSetValue() {
        final DialogAnchorListComponent<Locale> component = DialogAnchorListComponent.empty(
                ID_PREFIX,
                this.createContext()
            ).save()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-AU] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE);

        this.setValueAndCheck(
            component,
            Optional.of(
                Locale.forLanguageTag("en-GB")
            )
        );

        component.refresh(
            new FakeRefreshContext()
        );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-GB] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryToken() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close();

        final HistoryToken previous = component.context.historyToken();
        component.context.pushHistoryToken(
            HistoryToken.parseString("/2/SpreadsheetName222/cell/A1/locale")
        );

        component.onHistoryTokenChange(
            previous,
            new FakeAppContext()
        );

        component.refresh(
            new FakeRefreshContext()
        );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n" +
                "        \"Clear\" [#/2/SpreadsheetName222/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/2/SpreadsheetName222/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/2/SpreadsheetName222/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenAfterSetValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE);

        final HistoryToken previous = component.context.historyToken();
        component.context.pushHistoryToken(
            HistoryToken.parseString("/2/SpreadsheetName222/cell/A1/locale")
        );

        component.onHistoryTokenChange(
            previous,
            new FakeAppContext()
        );

        component.refresh(
            new FakeRefreshContext()
        );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-AU] id=Test123-save-Link\n" +
                "        \"Clear\" [#/2/SpreadsheetName222/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/2/SpreadsheetName222/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/2/SpreadsheetName222/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Override
    public DialogAnchorListComponent<Locale> createComponent() {
        return DialogAnchorListComponent.empty(
            ID_PREFIX,
            this.createContext()
        );
    }

    private DialogAnchorListComponentContext<Locale> createContext() {
        return this.createContext(
            HistoryToken.parseString("/1/SpreadsheetName111/cell/A1/locale"),
            Optional.of(
                Locale.forLanguageTag("en-NZ")
            )
        );
    }

    private DialogAnchorListComponentContext<Locale> createContext(final HistoryToken historyToken,
                                                                   final Optional<Locale> undo) {
        return new FakeDialogAnchorListComponentContext<>() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.watchers.add(watcher);
            }

            @Override
            public HistoryToken historyToken() {
                return currentHistoryToken;
            }

            private HistoryToken currentHistoryToken = historyToken;

            @Override
            public Optional<Locale> undo() {
                return undo;
            }

            @Override
            public void fireCurrentHistoryToken() {
                this.watchers.onHistoryTokenChange(
                    this.historyToken(),
                    new FakeAppContext()
                );
            }

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                this.watchers.onHistoryTokenChange(
                    this.historyToken(),
                    new FakeAppContext()
                );
                this.currentHistoryToken = token;
            }

            private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
        };
    }

    // class............................................................................................................

    @Override
    public Class<DialogAnchorListComponent<Locale>> type() {
        return Cast.to(DialogAnchorListComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
