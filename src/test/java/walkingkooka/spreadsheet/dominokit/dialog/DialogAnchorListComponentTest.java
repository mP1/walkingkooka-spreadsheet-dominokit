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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.FakeComponentWithErrors;
import walkingkooka.spreadsheet.dominokit.FakeRefreshContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DialogAnchorListComponentTest implements HtmlComponentTesting<DialogAnchorListComponent<Locale>, HTMLDivElement>,
    ValueComponentTesting<HTMLDivElement, Locale, DialogAnchorListComponent<Locale>>,
    SpreadsheetMetadataTesting,
    ComponentLifecycleMatcherTesting {

    private final static String ID_PREFIX = "Test123-";

    private final static Optional<Locale> VALUE = Optional.of(LOCALE);

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

    // setComponentWithErrors...........................................................................................

    @Test
    public void testSetComponentWithErrorsWithNull() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setComponentWithErrors(null)
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
    public void testSaveSetValueAndDisableSave() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                    ID_PREFIX,
                    this.createContext()
                ).save()
                .setValue(VALUE)
                .disableSave(),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n"
        );
    }

    @Test
    public void testSaveDisableSaveSetValue() {
        this.treePrintAndCheck(
            DialogAnchorListComponent.empty(
                    ID_PREFIX,
                    this.createContext()
                ).save()
                .disableSave()
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
    public void testClearValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE);

        this.clearValueAndCheck(component);

        component.refresh(
            new FakeRefreshContext()
        );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testSetValueAfterSaveAutoDisableWhenMissingValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .saveAutoDisableWhenMissingValue()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE);

        this.clearValueAndCheck(component);

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
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testSetValueAndSetComponentWithErrorsAndNoErrors() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .saveAutoDisableWhenMissingValue()
            .clearLink()
            .undo()
            .close()
            .setComponentWithErrors(
                new FakeComponentWithErrors<>() {
                    @Override
                    public List<String> errors() {
                        return Lists.empty();
                    }
                }
            ).setValue(VALUE);

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
    public void testSetValueAndSetComponentWithErrorsAndSeveralErrors() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .saveAutoDisableWhenMissingValue()
            .clearLink()
            .undo()
            .close()
            .setComponentWithErrors(
                new FakeComponentWithErrors<>() {
                    @Override
                    public List<String> errors() {
                        return Lists.of(
                            "Error111"
                        );
                    }
                }
            ).setValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n" + // disabled because of errors
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    // SetHistoryTokenPreProcessor......................................................................................

    @Test
    public void testSetHistoryTokenPreProcessorWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setHistoryTokenPreProcessor(null)
        );
    }

    @Test
    public void testSetHistoryTokenPreProcessorSetValue() {
        final DialogAnchorListComponent<TextStyle> component = DialogAnchorListComponent.<TextStyle>empty(
                ID_PREFIX,
                this.createContext(
                    HistoryToken.cellStyle(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.COLOR
                        )
                    ), // current HistoryToken
                    Optional.of(
                        TextStyle.parse("color: white; text-align: LEFT")
                    )// undo value
                )
            ).save()
            .clearLink()
            .undo()
            .close()
            .setHistoryTokenPreProcessor(
                (HistoryToken historyToken) -> historyToken.setStylePropertyName(Optional.empty())
            );

        this.treePrintAndCheck(
            component.setValue(
                Optional.of(
                    TextStyle.parse("color: black; font-weight: bold;")
                )
            ),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/color:%20black;%20font-weight:%20BOLD;] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/color:%20white;%20text-align:%20LEFT;] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testSetHistoryTokenPreProcessorSetValue2() {
        final DialogAnchorListComponent<TextStyle> component = DialogAnchorListComponent.<TextStyle>empty(
                ID_PREFIX,
                this.createContext(
                    HistoryToken.cellStyle(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.COLOR
                        )
                    ), // current HistoryToken
                    Optional.of(
                        TextStyle.parse("color: white; text-align: LEFT")
                    )// undo value
                )
            ).save()
            .clearLink()
            .setHistoryTokenPreProcessor(
                (HistoryToken historyToken) -> historyToken.setStylePropertyName(Optional.empty())
            ).undo()
            .close();

        this.treePrintAndCheck(
            component.setValue(
                Optional.of(
                    TextStyle.parse("color: black; font-weight: bold;")
                )
            ),
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/color:%20black;%20font-weight:%20BOLD;] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/color:%20white;%20text-align:%20LEFT;] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Test123-close-Link\n"
        );
    }

    // onHistoryToken...................................................................................................

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
                "        \"Save\" [#/2/SpreadsheetName222/cell/A1/locale/save/] id=Test123-save-Link\n" +
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
                "        \"Save\" [#/2/SpreadsheetName222/cell/A1/locale/save/en-AU] id=Test123-save-Link\n" +
                "        \"Clear\" [#/2/SpreadsheetName222/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/2/SpreadsheetName222/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/2/SpreadsheetName222/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenAfterSetValueAndNewErrors() {
        this.errors = Lists.empty();

        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setValue(VALUE)
            .setComponentWithErrors(
                new FakeComponentWithErrors<>() {
                    @Override
                    public List<String> errors() {
                        return DialogAnchorListComponentTest.this.errors;
                    }
                }
            );

        this.errors = Lists.of("Error111");

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

    private List<String> errors;

    // appendChild......................................................................................................

    @Test
    public void testAppendChildWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .appendChild(null)
        );
    }

    @Test
    public void testAppendChild() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .appendChild(
                HistoryTokenAnchorComponent.empty()
                    .setTextContent("111")
            );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"111\" DISABLED\n"
        );
    }

    @Test
    public void testAppendChild2() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .appendChild(
                HistoryTokenAnchorComponent.empty()
                    .setTextContent("Click!")
                    .setHistoryToken(
                        Optional.of(
                            HistoryToken.parseString("/2/SpreadsheetName222/cell/A1/locale")
                        )
                    )
            ).appendChild(
                HistoryTokenAnchorComponent.empty()
                    .setTextContent("222")
            );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Click!\" [#/2/SpreadsheetName222/cell/A1/locale]\n" +
                "        \"222\" DISABLED\n"
        );
    }

    @Test
    public void testAppendChildSaveUndoClearValueClose() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .undo()
            .clearLink()
            .close()
            .appendChild(
                HistoryTokenAnchorComponent.empty()
                    .setTextContent("111")
            );

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"111\" DISABLED\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testDisableSetValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close();

        component.disableSave()
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
    public void testDisableClearValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close();

        component.disableSave()
            .clearValue();

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
        );
    }

    // ValueWatcher.....................................................................................................

    @Test
    public void testOnValue() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close();

        component.onValue(VALUE);

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
    public void testOnValueWithoutErrors() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setComponentWithErrors(
                new FakeComponentWithErrors<>() {
                    @Override
                    public List<String> errors() {
                        return Lists.empty();
                    }
                }
            );

        component.onValue(VALUE);

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
    public void testOnValueWithErrors() {
        final DialogAnchorListComponent<Locale> component = this.createComponent()
            .save()
            .clearLink()
            .undo()
            .close()
            .setComponentWithErrors(
                new FakeComponentWithErrors<>() {
                    @Override
                    public List<String> errors() {
                        return Lists.of("Error111");
                    }
                }
            );

        component.onValue(VALUE);

        this.treePrintAndCheck(
            component,
            "DialogAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Save\" DISABLED id=Test123-save-Link\n" +
                "        \"Clear\" [#/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-clear-Link\n" +
                "        \"Undo\" [#/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=Test123-undo-Link\n" +
                "        \"Close\" [#/1/SpreadsheetName111/cell/A1] id=Test123-close-Link\n"
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

    private <T> DialogAnchorListComponentContext<T> createContext(final HistoryToken historyToken,
                                                                   final Optional<T> undo) {
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
            public Optional<T> undo() {
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
