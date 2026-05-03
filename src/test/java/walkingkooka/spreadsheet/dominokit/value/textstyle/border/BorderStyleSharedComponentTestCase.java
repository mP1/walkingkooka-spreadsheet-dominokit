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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.tree.text.BorderStyle;

import java.util.List;

public abstract class BorderStyleSharedComponentTestCase<C extends BorderStyleSharedComponent<C>> implements TextStylePropertyEnumComponentTesting<BorderStyle, C> {

    public BorderStyleSharedComponentTestCase() {
        super();
    }

    // filterTest.......................................................................................................

    @Override
    public List<BorderStyle> enumValues() {
        return Lists.of(
            BorderStyle.values()
        );
    }

    // class............................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
