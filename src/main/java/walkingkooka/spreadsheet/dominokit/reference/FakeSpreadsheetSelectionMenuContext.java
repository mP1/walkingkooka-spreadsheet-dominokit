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

import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetSelectionSummary;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;

import java.util.List;
import java.util.Set;

public class FakeSpreadsheetSelectionMenuContext extends FakeHistoryTokenContext implements SpreadsheetSelectionMenuContext {

    @Override
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String idPrefix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetSelectionSummary selectionSummary() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                          final ProviderContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
        throw new UnsupportedOperationException();
    }
}
