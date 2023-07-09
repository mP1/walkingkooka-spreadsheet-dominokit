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

package walkingkooka.spreadsheet.dominokit.pattern;

import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.util.BiFunctionTesting;

import java.util.List;

public abstract class SpreadsheetPatternEditorWidgetSampleRowProviderTestCase<T extends SpreadsheetPatternEditorWidgetSampleRowProvider> implements
        BiFunctionTesting<T, String, SpreadsheetPatternEditorWidgetSampleRowProviderContext, List<SpreadsheetPatternEditorWidgetSampleRow>>,
        ClassTesting<T>,
        ToStringTesting<T> {

    SpreadsheetPatternEditorWidgetSampleRowProviderTestCase() {
        super();
    }

    final void applyAndCheck(final String patternText,
                             final SpreadsheetPatternEditorWidgetSampleRowProviderContext context,
                             final SpreadsheetPatternEditorWidgetSampleRow... expected) {
        this.applyAndCheck(
                patternText,
                context,
                Lists.of(expected)
        );
    }

    final void applyAndCheck(final T provider,
                             final String patternText,
                             final SpreadsheetPatternEditorWidgetSampleRowProviderContext context,
                             final SpreadsheetPatternEditorWidgetSampleRow... expected) {
        this.applyAndCheck(
                provider,
                patternText,
                context,
                Lists.of(expected)
        );
    }

    abstract T createProvider();

    @Override
    public final T createBiFunction() {
        return this.createProvider();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
