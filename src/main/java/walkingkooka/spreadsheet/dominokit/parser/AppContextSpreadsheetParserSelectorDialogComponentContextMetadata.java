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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetParserSelectorDialogComponentContext} for editing a metadata parser.
 */
final class AppContextSpreadsheetParserSelectorDialogComponentContextMetadata extends AppContextSpreadsheetParserSelectorDialogComponentContext {

    static AppContextSpreadsheetParserSelectorDialogComponentContextMetadata with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new AppContextSpreadsheetParserSelectorDialogComponentContextMetadata(context);
    }

    private AppContextSpreadsheetParserSelectorDialogComponentContextMetadata(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(
            this.propertyName()
        );
    }

    @Override
    public boolean shouldShowTabs() {
        return true;
    }

    /**
     * Retrieves the {@link SpreadsheetPatternKind} from the history token,
     * and then reads the value for its {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName}.
     */
    @Override
    public Optional<SpreadsheetParserSelector> undo() {
        return Cast.to(
            this.context.spreadsheetMetadata()
                .getIgnoringDefaults(
                    this.propertyName()
                )
        );
    }

    private SpreadsheetMetadataPropertyName<?> propertyName() {
        return this.historyToken()
            .metadataPropertyName()
            .orElseThrow(() -> new IllegalStateException("Missing " + SpreadsheetMetadataPropertyName.class.getSimpleName()));
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token.metadataPropertyName()
            .map(SpreadsheetMetadataPropertyName::isSpreadsheetParserSelector)
            .orElse(false) &&
            false == token.isSave();
    }
}
