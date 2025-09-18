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

package walkingkooka.spreadsheet.dominokit.format;

import org.gwtproject.core.shared.GWT;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetFormatterSelectorDialogComponentContext} for editing a metadata formatter.
 */
final class AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata extends AppContextSpreadsheetFormatterSelectorDialogComponentContext {

    static AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata(context);
    }

    private AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(
            this.spreadsheetMetadataPropertyName()
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
    public Optional<SpreadsheetFormatterSelector> undo() {
        return Cast.to(
            this.context.spreadsheetMetadata()
                .getIgnoringDefaults(
                    this.spreadsheetMetadataPropertyName()
                )
        );
    }

    private SpreadsheetMetadataPropertyName<?> spreadsheetMetadataPropertyName() {
        return this.historyToken()
            .patternKind()
            .get()
            .spreadsheetMetadataPropertyName();
    }

    @Override
    public void loadSpreadsheetFormattersEdit(final String text) {
        final AppContext context = this.context;
        final SpreadsheetMetadataPropertyHistoryToken<?> spreadsheetMetadataPropertyHistoryToken = context.historyToken()
            .cast(SpreadsheetMetadataPropertyHistoryToken.class);

        // HACK throttler and fetcher will in JVM
        if (GWT.isScript()) {
            this.throttler.add(
                () -> context.spreadsheetFormatterFetcher()
                    .getMetadataFormatterEdit(
                        spreadsheetMetadataPropertyHistoryToken.id(), // id
                        Cast.to(
                            spreadsheetMetadataPropertyHistoryToken.metadataPropertyName()
                                .orElse(null)
                        ),
                        text
                    )
            );
        }
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token.isMetadataFormatter();
    }
}
