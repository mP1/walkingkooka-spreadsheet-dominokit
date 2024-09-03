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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviders;

public final class SpreadsheetFormatterInfoSetComponentTest implements ClassTesting2<SpreadsheetFormatterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetFormatterInfoSet infos = SpreadsheetFormatterInfoSet.with(
                SpreadsheetFormatterProviders.spreadsheetFormatPattern()
                        .spreadsheetFormatterInfos()
        );

        this.checkEquals(
                infos,
                SpreadsheetFormatterInfoSet.parse(infos.text())
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormatterInfoSetComponent> type() {
        return SpreadsheetFormatterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}