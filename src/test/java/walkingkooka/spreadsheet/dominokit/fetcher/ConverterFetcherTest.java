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

package walkingkooka.spreadsheet.dominokit.fetcher;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class ConverterFetcherTest implements ClassTesting<ConverterFetcher> {

    // verifyConverterSelector..........................................................................................

    // /api/spreadsheet/1/converter/SpreadsheetMetadataPropertyName<ConverterSelector>/verify

    @Test
    public void testVerifyConverterSelectorWithFindConverter() {
        final SpreadsheetMetadataPropertyName<ConverterSelector> propertyName = SpreadsheetMetadataPropertyName.FIND_CONVERTER;

        this.verifyConverterSelectorAndCheck(
            "/api/spreadsheet/1/converter/" + propertyName.value() + "/verify",
            propertyName.value()
        );
    }

    @Test
    public void testVerifyConverterSelectorWithFormulaConverter() {
        final SpreadsheetMetadataPropertyName<ConverterSelector> propertyName = SpreadsheetMetadataPropertyName.FORMULA_CONVERTER;

        this.verifyConverterSelectorAndCheck(
            "/api/spreadsheet/1/metadata/" + propertyName.value() + "/verify",
            propertyName.value()
        );
    }

    private void verifyConverterSelectorAndCheck(final String url,
                                                 final String expected) {
        this.verifyConverterSelectorAndCheck(
            Url.parseAbsoluteOrRelative(url),
            Cast.to(
                SpreadsheetMetadataPropertyName.with(expected)
            )
        );
    }

    private void verifyConverterSelectorAndCheck(final AbsoluteOrRelativeUrl url,
                                                 final SpreadsheetMetadataPropertyName<ConverterSelector> expected) {
        this.checkEquals(
            expected,
            ConverterFetcher.verifyConverterSelector(url),
            url::toString
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConverterFetcher> type() {
        return ConverterFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
