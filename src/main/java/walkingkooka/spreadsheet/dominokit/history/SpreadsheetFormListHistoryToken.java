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
import walkingkooka.validation.form.FormName;

import java.util.Objects;
import java.util.Optional;

/**
 * Selects a Form for viewing or editing.
 * <pre>
 * #/1/SpreadsheetName/form/
 * #/1/SpreadsheetName/form/&star;/offset/0/count/1
 * </pre>
 */
public final class SpreadsheetFormListHistoryToken extends SpreadsheetFormHistoryToken {

    static SpreadsheetFormListHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final HistoryTokenOffsetAndCount offsetAndCount) {
        return new SpreadsheetFormListHistoryToken(
            id,
            name,
            offsetAndCount
        );
    }

    private SpreadsheetFormListHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final HistoryTokenOffsetAndCount offsetAndCount) {
        super(
            id,
            name
        );
        this.offsetAndCount = Objects.requireNonNull(offsetAndCount, "offsetAndCount");
    }

    final HistoryTokenOffsetAndCount offsetAndCount;

    @Override
    public Optional<FormName> formName() {
        return Optional.empty();
    }

    // #/1/SpreadsheetName/form/
    // #/1/SpreadsheetName/form/*/offset/0/count/1
    @Override
    UrlFragment formUrlFragment() {
        return countAndOffsetUrlFragment(
            this.offsetAndCount,
            UrlFragment.EMPTY
        );
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.offsetAndCount
        );
    }

    // /1/SpreadsheetName/form
    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitFormList(
            this.id,
            this.name,
            this.offsetAndCount
        );
    }
}
