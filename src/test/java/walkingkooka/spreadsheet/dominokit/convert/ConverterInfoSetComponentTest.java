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

package walkingkooka.spreadsheet.dominokit.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public final class ConverterInfoSetComponentTest implements ClassTesting2<ConverterInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final ConverterInfoSet infos = ConverterProviders.converters()
                .converterInfos();

        this.checkEquals(
                infos,
                ConverterInfoSet.parse(infos.text())
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConverterInfoSetComponent> type() {
        return ConverterInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
