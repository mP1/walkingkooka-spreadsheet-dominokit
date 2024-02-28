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

import org.junit.jupiter.api.Test;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

public final class SpreadsheetCellPropertySelectorTest implements ClassTesting<SpreadsheetCellPropertySelector>,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellPropertySelector> {

    // parse............................................................................................................

    @Test
    public void testParseStar() {
        this.parseStringAndCheck(
                "*",
                SpreadsheetCellPropertySelector.ALL
        );
    }

    @Test
    public void testParseFormula() {
        this.parseStringAndCheck(
                "formula",
                SpreadsheetCellPropertySelector.FORMULA
        );
    }

    @Override //
    public SpreadsheetCellPropertySelector parseString(final String string) {
        return SpreadsheetCellPropertySelector.parse(string);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragmentAll() {
        this.urlFragmentAndCheck(
                SpreadsheetCellPropertySelector.ALL,
                UrlFragment.with("*")
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                SpreadsheetCellPropertySelector.FORMULA,
                UrlFragment.with("formula")
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetCellPropertySelector.FORMAT_PATTERN,
                UrlFragment.with("format-pattern")
        );
    }

    @Test
    public void testUrlFragmentStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetCellPropertySelector.STYLE,
                UrlFragment.with("style")
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellPropertySelector> type() {
        return SpreadsheetCellPropertySelector.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
