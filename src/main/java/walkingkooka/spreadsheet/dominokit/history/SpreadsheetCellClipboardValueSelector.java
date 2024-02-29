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

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Objects;

/**
 * Used to mark the tyoe of a clipboard value in a {@link SpreadsheetCellClipboardHistoryToken}.
 */
public enum SpreadsheetCellClipboardValueSelector implements HasUrlFragment {

    /**
     * The clipboard value is a {@link java.util.Set} of {@link walkingkooka.spreadsheet.SpreadsheetCell}.
     */
    CELL("cell"),

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link String formula text}.
     */
    FORMULA("formula"),

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern}.
     */
    FORMAT_PATTERN("format-pattern"),

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern}.
     */
    PARSE_PATTERN("parse-pattern"),

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.tree.text.TextStyle}.
     */
    STYLE("style"),

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link String formatted text}.
     */
    FORMATTED("formatted");

    SpreadsheetCellClipboardValueSelector(final String urlFragment) {
        this.urlFragment = UrlFragment.parse(urlFragment);
    }

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final UrlFragment urlFragment;

    public static SpreadsheetCellClipboardValueSelector parse(final String string) {
        Objects.requireNonNull(string, "string");

        return Arrays.stream(values())
                .filter(e -> e.urlFragment.value().equals(string))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid cell clipboard selector: " + CharSequences.quote(string)));
    }
}
