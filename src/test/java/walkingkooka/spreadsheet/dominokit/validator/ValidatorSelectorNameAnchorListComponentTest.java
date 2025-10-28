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

package walkingkooka.spreadsheet.dominokit.validator;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;

public final class ValidatorSelectorNameAnchorListComponentTest implements ValueComponentTesting<HTMLDivElement, ValidatorSelector, ValidatorSelectorNameAnchorListComponent>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetId ID = SpreadsheetId.parse("1");

    private final static SpreadsheetName NAME = SpreadsheetName.with("Spreadsheet111");

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "ValidatorSelectorNameAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        id=TestId123links\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/apple] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/banana] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/carrot] id=TestId123-Link\n"
        );
    }

    @Test
    public void testClearValueSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        ValidatorSelector.parse("edit 123")
                    )
                ),
            "ValidatorSelectorNameAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        id=TestId123links\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/apple%20123] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/banana%20123] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/carrot%20123] id=TestId123-Link\n"
        );
    }

    @Test
    public void testSetValueOnlyName() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        ValidatorSelector.parse("edit")
                    )
                ),
            "ValidatorSelectorNameAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        id=TestId123links\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/apple] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/banana] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/carrot] id=TestId123-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        ValidatorSelector.parse("edit 123")
                    )
                ),
            "ValidatorSelectorNameAnchorListComponent\n" +
                "  AnchorListComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        id=TestId123links\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/apple%20123] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/banana%20123] id=TestId123-Link\n" +
                "          [#/1/Spreadsheet111/cell/A1/validator/save/carrot%20123] id=TestId123-Link\n"
        );
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent createComponent() {
        return ValidatorSelectorNameAnchorListComponent.with(
            "TestId123",
            new FakeValidatorSelectorNameAnchorListComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellValidatorSelect(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                    );
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return METADATA_EN_AU.set(
                        SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
                        ValidatorAliasSet.parse("apple, banana, carrot")
                    );
                }

                @Override
                public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                    return null;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValidatorSelectorNameAnchorListComponent> type() {
        return ValidatorSelectorNameAnchorListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
