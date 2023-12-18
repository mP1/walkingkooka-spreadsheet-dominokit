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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;

import java.util.Optional;
import java.util.OptionalInt;

public final class SpreadsheetCellFindHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellFindHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection selection,
                                                final Optional<SpreadsheetCellRangePath> path,
                                                final OptionalInt offset,
                                                final OptionalInt max,
                                                final Optional<String> valueType,
                                                final Optional<String> query) {
        return new SpreadsheetCellFindHistoryToken(
                id,
                name,
                selection,
                path,
                offset,
                max,
                valueType,
                query
        );
    }

    private SpreadsheetCellFindHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection selection,
                                            final Optional<SpreadsheetCellRangePath> path,
                                            final OptionalInt offset,
                                            final OptionalInt max,
                                            final Optional<String> valueType,
                                            final Optional<String> query) {
        super(
                id,
                name,
                selection
        );
        this.path = path;
        this.offset = offset;
        this.max = max;
        this.valueType = valueType;
        this.query = query;
    }

    public Optional<SpreadsheetCellRangePath> path() {
        return this.path;
    }

    private final Optional<SpreadsheetCellRangePath> path;

    public OptionalInt offset() {
        return this.offset;
    }
    private final OptionalInt offset;

    public OptionalInt max() {
        return this.max;
    }

    private final OptionalInt max;

    public Optional<String> valueType() {
        return this.valueType;
    }
    private final Optional<String> valueType;

    public Optional<String> query() {
        return this.query;
    }

    private final Optional<String> query;

    @Override
    UrlFragment cellUrlFragment() {
        UrlFragment urlFragment = FIND;

        final Optional<SpreadsheetCellRangePath> path = this.path;
        if (path.isPresent()) {
            urlFragment = urlFragment.append(UrlFragment.SLASH)
                    .append(
                            UrlFragment.with(
                                    path.get()
                                            .toString()
                            )
                    );

            final OptionalInt offset = this.offset;
            if (offset.isPresent()) {
                urlFragment = urlFragment.append(UrlFragment.SLASH)
                        .append(
                                UrlFragment.with(
                                        String.valueOf(
                                                offset.getAsInt()
                                        )
                                )
                        );

                final OptionalInt max = this.max;
                if (max.isPresent()) {
                    urlFragment = urlFragment.append(UrlFragment.SLASH)
                            .append(
                                    UrlFragment.with(
                                            String.valueOf(
                                                    max.getAsInt()
                                            )
                                    )
                            );

                    final Optional<String> valueType = this.valueType;
                    if (valueType.isPresent()) {
                        urlFragment = urlFragment.append(UrlFragment.SLASH)
                                .append(
                                        UrlFragment.with(
                                                valueType.get()
                                        )
                                );

                        final Optional<String> query = this.query;
                        if (query.isPresent()) {
                            urlFragment = urlFragment.append(UrlFragment.SLASH)
                                    .append(
                                            UrlFragment.with(
                                                    query.get()
                                            )
                                    );
                        }
                    }
                }
            }
        }

        return urlFragment;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override
    public HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public HistoryToken setFormula() {
        return setFormula0();
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.selection(),
                this.path,
                this.offset,
                this.max,
                this.valueType,
                this.query
        );
    }

    @Override
    public HistoryToken setParsePattern() {
        return this;
    }

    @Override
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
