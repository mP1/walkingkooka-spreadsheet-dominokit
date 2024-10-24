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

package walkingkooka.spreadsheet.dominokit.comparator;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAliasSet;

public final class SpreadsheetComparatorAliasSetComponentTest implements ClassTesting2<SpreadsheetComparatorAliasSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetComparatorAliasSet alias = SpreadsheetComparatorAliasSet.parse("alias1 plugin1, plugin2");

        this.checkEquals(
                alias,
                SpreadsheetComparatorAliasSet.parse(alias.text())
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorAliasSetComponent> type() {
        return SpreadsheetComparatorAliasSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
