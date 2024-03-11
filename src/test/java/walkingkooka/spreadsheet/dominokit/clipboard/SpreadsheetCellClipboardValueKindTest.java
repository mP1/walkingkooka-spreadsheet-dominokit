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

package walkingkooka.spreadsheet.dominokit.clipboard;

import org.junit.jupiter.api.Test;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

public final class SpreadsheetCellClipboardValueKindTest implements ClassTesting<SpreadsheetCellClipboardValueKind>,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellClipboardValueKind> {

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseStringAndCheck(
                "cell",
                SpreadsheetCellClipboardValueKind.CELL
        );
    }

    @Test
    public void testParseFormula() {
        this.parseStringAndCheck(
                "formula",
                SpreadsheetCellClipboardValueKind.FORMULA
        );
    }

    @Override //
    public SpreadsheetCellClipboardValueKind parseString(final String string) {
        return SpreadsheetCellClipboardValueKind.parse(string);
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
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
                UrlFragment.with("cell")
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                UrlFragment.with("formula")
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                UrlFragment.with("format-pattern")
        );
    }

    @Test
    public void testUrlFragmentStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                UrlFragment.with("style")
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellClipboardValueKind> type() {
        return SpreadsheetCellClipboardValueKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
