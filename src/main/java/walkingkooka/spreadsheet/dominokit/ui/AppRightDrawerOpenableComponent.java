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

package walkingkooka.spreadsheet.dominokit.ui;

import org.dominokit.domino.ui.layout.AppLayout;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Objects;

/**
 * A {@link OpenableComponent} for a {@link AppLayout} right drawer.
 */
public final class AppRightDrawerOpenableComponent implements OpenableComponent {

    public static AppRightDrawerOpenableComponent with(final AppLayout appLayout) {
        Objects.requireNonNull(appLayout, "appLayout");

        return new AppRightDrawerOpenableComponent(appLayout);
    }

    private AppRightDrawerOpenableComponent(final AppLayout appLayout) {
        this.appLayout = appLayout;
    }

    @Override
    public boolean isOpen() {
        return this.appLayout.isRightDrawerOpen();
    }

    @Override
    public void open(AppContext context) {
        this.appLayout.showRightDrawer();
    }

    @Override
    public void close(final AppContext context) {
        this.appLayout.hideRightDrawer();
    }

    private final AppLayout appLayout;

    @Override
    public String toString() {
        return this.appLayout.toString();
    }
}
