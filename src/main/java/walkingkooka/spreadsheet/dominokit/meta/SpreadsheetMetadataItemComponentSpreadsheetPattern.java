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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.button.Button;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataItemComponent} that only displays a link that when clicked opens a {@link SpreadsheetMetadataPanelComponent}.
 */
final class SpreadsheetMetadataItemComponentSpreadsheetPattern<T extends SpreadsheetPattern> extends SpreadsheetMetadataItemComponent<T> {

    static <T extends SpreadsheetPattern> SpreadsheetMetadataItemComponentSpreadsheetPattern<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                                     final SpreadsheetPatternKind patternKind,
                                                                                                     final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        Objects.requireNonNull(patternKind, "patternKind");
        checkContext(context);

        return new SpreadsheetMetadataItemComponentSpreadsheetPattern<>(
                propertyName,
                patternKind,
                context
        );
    }

    private SpreadsheetMetadataItemComponentSpreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                               final SpreadsheetPatternKind patternKind,
                                                               final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );
        this.patternKind = patternKind;
        this.anchor = Anchor.empty()
                .setId(SpreadsheetMetadataPanelComponent.id(propertyName) + "-link");
    }

    @Override
    Optional<Button> defaultButton() {
        return Optional.empty();
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final String pattern = context.spreadsheetMetadata()
                .getIgnoringDefaults(this.propertyName)
                .map(SpreadsheetPattern::text)
                .orElse("edit");

        final HistoryToken historyToken = context.historyToken()
                .setPatternKind(
                        Optional.of(
                                this.patternKind
                        )
                );
        context.debug(this.getClass().getSimpleName() + ".refresh " + historyToken + " " + CharSequences.quoteAndEscape(pattern));

        this.anchor.setHistoryToken(
                Optional.of(historyToken)
        ).setTextContent(pattern)
                .setDisabled(false);
    }

    /**
     * The {@link SpreadsheetPatternKind} for the property
     */
    private final SpreadsheetPatternKind patternKind;

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.anchor.element();
    }

    private final Anchor anchor;
}
