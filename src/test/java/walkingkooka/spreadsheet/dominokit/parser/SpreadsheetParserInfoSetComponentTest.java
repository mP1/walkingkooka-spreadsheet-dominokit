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

package walkingkooka.spreadsheet.dominokit.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;

public final class SpreadsheetParserInfoSetComponentTest implements ClassTesting2<SpreadsheetParserInfoSetComponent>,
        SpreadsheetMetadataTesting {

    @Test
    public void testParseAndText() {
        final SpreadsheetParserInfoSet infos = SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos();

        this.checkEquals(
                infos,
                SpreadsheetParserInfoSet.parse(infos.text())
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetParserInfoSetComponent> type() {
        return SpreadsheetParserInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
