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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.contextmenu.FakeSpreadsheetSelectionMenuContext;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValidatorTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuValidator> {

    @Test
    public void testBuild() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
            SpreadsheetId.with(1),
            SpreadsheetName.with("Spreadsheet123"),
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            Lists.of(
                ValidatorSelector.parse("hello-validator-1"),
                ValidatorSelector.parse("hello-validator-2")
            ) // recent
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell A1 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenuValidator.build(
            historyToken,
            menu,
            context
        );

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Hello Validator 1\" [/1/Spreadsheet123/cell/A1/validator/save/hello-validator-1] CHECKED id=test-validator-hello-validator-1-MenuItem\n" +
                "  \"Hello Validator 2\" [/1/Spreadsheet123/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Hello Validator 1\" [/1/Spreadsheet123/cell/A1/validator/save/hello-validator-1] id=test-validator-recent-0-MenuItem\n" +
                "  \"Hello Validator 2\" [/1/Spreadsheet123/cell/A1/validator/save/hello-validator-2] id=test-validator-recent-1-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<ValidatorSelector> recentValidators) {
        return new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public List<ValidatorSelector> validatorSelectors() {
                return Lists.of(
                    ValidatorSelector.parse("hello-validator-1"),
                    ValidatorSelector.parse("hello-validator-2")
                );
            }

            @Override
            public List<ValidatorSelector> recentValidatorSelectors() {
                return recentValidators;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return Optional.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setValidator(
                            Optional.of(
                                ValidatorSelector.parse("hello-validator-1")
                            )
                        )
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValidator> type() {
        return SpreadsheetSelectionMenuValidator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
