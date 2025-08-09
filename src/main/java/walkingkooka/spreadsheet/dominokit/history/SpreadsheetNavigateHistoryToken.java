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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;


/**
 * This {@link HistoryToken} is used by viewport scrollbar left/right/top/down arrows, when no selection is active.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/navigate/B2/right 1500px
 * </pre>
 */
public final class SpreadsheetNavigateHistoryToken extends SpreadsheetNameHistoryToken {

    static SpreadsheetNavigateHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final SpreadsheetViewportHomeNavigationList navigation) {
        return new SpreadsheetNavigateHistoryToken(
            id,
            name,
            navigation
        );
    }

    private SpreadsheetNavigateHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final SpreadsheetViewportHomeNavigationList navigation) {
        super(
            id,
            name
        );
        this.navigation = Objects.requireNonNull(navigation, "navigation");
    }

    public SpreadsheetViewportHomeNavigationList navigation() {
        return this.navigation;
    }

    private final SpreadsheetViewportHomeNavigationList navigation;

    // /1/SpreadsheetName/navigate/Z9/right 400
    @Override
    UrlFragment spreadsheetNameUrlFragment() {
        return NAVIGATE.append(
            this.navigation.urlFragment()
        );
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    @Override
    public HistoryToken clearAction() {
        return spreadsheetSelect(
            this.id(),
            this.name()
        );
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
            id,
            name
        ).setNavigation(this.navigation);
    }

    @Override //
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // PATCH the metadata viewport
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                this.id(),
                SpreadsheetMetadataPropertyName.VIEWPORT,
                context.viewport(
                    this.navigation(),
                    Optional.empty() // no selection
                )
            );

        context.pushHistoryToken(previous);
    }
}
