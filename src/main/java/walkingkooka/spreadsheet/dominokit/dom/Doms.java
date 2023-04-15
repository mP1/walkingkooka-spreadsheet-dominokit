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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.Element;
import jsinterop.base.Js;
import walkingkooka.reflect.PublicStaticHelper;

public final class Doms implements PublicStaticHelper {

    /**
     * Tests if the 2nd parameter is the parent element or a child.
     */
    public static boolean isOrHasChild(final Element parent,
                                       final Element child) {
        return toOldElement(parent)
                .isOrHasChild(
                        toOldElement(child)
                );
    }

    private static com.google.gwt.dom.client.Element toOldElement(final Element element) {
        return Js.<com.google.gwt.dom.client.Element>cast(element);
    }

    /**
     * Stop creation
     */
    private Doms() {
        throw new UnsupportedOperationException();
    }
}
