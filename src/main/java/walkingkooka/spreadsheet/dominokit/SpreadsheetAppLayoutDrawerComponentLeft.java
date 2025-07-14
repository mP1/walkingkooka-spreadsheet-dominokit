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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.layout.AppLayout;

import java.util.Objects;

/**
 * A {@link OpenableComponent} for a {@link AppLayout} left drawer.
 */
final class SpreadsheetAppLayoutDrawerComponentLeft<E extends HTMLElement> extends SpreadsheetAppLayoutDrawerComponent<E, SpreadsheetAppLayoutDrawerComponentLeft<E>> {

    static <E extends HTMLElement> SpreadsheetAppLayoutDrawerComponentLeft<E> with(final AppLayout appLayout,
                                                                                   final SpreadsheetFormComponentLifecycle<E, ?> form) {
        return new SpreadsheetAppLayoutDrawerComponentLeft<>(
            Objects.requireNonNull(appLayout, "appLayout"),
            Objects.requireNonNull(form, "form")
        );
    }

    private SpreadsheetAppLayoutDrawerComponentLeft(final AppLayout appLayout,
                                                    final SpreadsheetFormComponentLifecycle<E, ?> form) {
        super(appLayout, form);
    }

    @Override
    public boolean isOpen() {
        return this.appLayout.isLeftDrawerOpen();
    }

    @Override
    void showDrawer() {
        this.appLayout.showLeftDrawer();
    }

    @Override
    void hideDrawer() {
        this.appLayout.hideLeftDrawer();
    }
}
