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

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

final class AppGiveViewportFocusRunnable implements Runnable {

    static AppGiveViewportFocusRunnable with(final SpreadsheetSelection selection,
                                             final AppContext context) {
        return new AppGiveViewportFocusRunnable(
                selection,
                context
        );
    }


    private AppGiveViewportFocusRunnable(final SpreadsheetSelection selection,
                                         final AppContext context) {
        this.selection = selection;
        this.context = context;
    }

    @Override
    public void run() {
        final SpreadsheetSelection selection = this.selection;
        final AppContext context = this.context;

        final Optional<Element> maybeElement = context.findViewportElement(selection);
        if (maybeElement.isPresent()) {
            Element element = maybeElement.get();

            boolean give = true;

            final Element active = DomGlobal.document.activeElement;
            if (null != active) {
                // verify active element belongs to the same selection. if it does it must have focus so no need to focus again
                give = false == Doms.isOrHasChild(
                        element,
                        active
                );
            }

            if (give) {
                // for column/row the anchor and not the TH/TD should receive focus.
                if (selection.isColumnReference() || selection.isRowReference()) {
                    element = element.firstElementChild;
                }

                context.debug("AppGiveViewportFocusRunnable " + selection + " focus element " + element);
                element.focus();
            }
        } else {
            context.debug("AppGiveViewportFocusRunnable " + selection + " element not found!");
        }
    }

    @Override
    public int hashCode() {
        return this.selection.hashCode();
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof AppGiveViewportFocusRunnable && this.equals0((AppGiveViewportFocusRunnable) other);
    }

    private boolean equals0(final AppGiveViewportFocusRunnable other) {
        return this.selection.equals(other.selection);
    }

    @Override
    public String toString() {
        return this.selection.toString();
    }

    private final SpreadsheetSelection selection;

    private final AppContext context;
}
