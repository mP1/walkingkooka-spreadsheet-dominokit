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

package walkingkooka.spreadsheet.dominokit.label;

import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.function.Consumer;

/**
 * An empty {@link SuggestionsStore}, eventually this will call a server API to search for matching {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}
 */
final class SpreadsheetLabelComponentSuggestStore implements SuggestionsStore<SpreadsheetLabelName, SpanElement, SuggestOption<SpreadsheetLabelName>> {

    static SpreadsheetLabelComponentSuggestStore with(final Context context) {
        return new SpreadsheetLabelComponentSuggestStore(context);
    }

    private SpreadsheetLabelComponentSuggestStore(final Context context) {
        super();
        this.context = context;
    }

    @Override
    public void find(final SpreadsheetLabelName searchValue,
                     final Consumer<SuggestOption<SpreadsheetLabelName>> handler) {
        handler.accept(
            SuggestOption.create(searchValue)
        );
    }

    @Override
    public void filter(final String filter,
                       final SuggestionsHandler<SpreadsheetLabelName, SpanElement, SuggestOption<SpreadsheetLabelName>> handler) {
        handler.onSuggestionsReady(
            Lists.of(
                SuggestOption.create(
                    SpreadsheetSelection.labelName(filter)
                )
            )
        );
    }

    private final Context context;
}
