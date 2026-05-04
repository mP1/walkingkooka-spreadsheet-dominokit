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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontfamily;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.select.SelectComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStyleDialogComponentFilter;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link FontFamily}.
 */
public final class FontFamilyComponent implements TextStylePropertyComponent<HTMLFieldSetElement, FontFamily, FontFamilyComponent>,
    SelectComponentDelegator<FontFamily, FontFamilyComponent> {

    public static FontFamilyComponent empty(final String idPrefix,
                                            final FontFamilyComponentContext context) {
        return new FontFamilyComponent(
            idPrefix,
            context
        );
    }

    private FontFamilyComponent(final String idPrefix,
                                final FontFamilyComponentContext context) {
        final SelectComponent<FontFamily> select = SelectComponent.empty(
            (v) -> {
                final FontFamily fontFamily = v.orElseThrow(() -> new IllegalArgumentException("Missing FontFamily"));
                final String nameText = fontFamily.text();

                return context.selectOption(
                    idPrefix + CaseKind.TITLE.change(
                        nameText,
                        CaseKind.CAMEL
                    ) + SpreadsheetElementIds.OPTION, // id
                    nameText, // text
                    v, // value
                    Optional.empty() // HistoryToken
                );
            }
        );

        for (final FontFamily fontFamily : context.fontFamilies()) {
            select.appendOption(
                Optional.of(fontFamily)
            );
        }

        this.select = select;
        this.setId(
            CharSequences.subSequence(
                idPrefix,
                0,
                -1
            ) + SpreadsheetElementIds.SELECT
        );

        this.context = context;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStyleDialogComponentFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            filter.testFontFamilies(context.fontFamilies());
    }

    private final FontFamilyComponentContext context;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<FontFamily> name() {
        return TextStylePropertyName.FONT_FAMILY;
    }

    // SelectComponentDelegator.........................................................................................

    @Override
    public SelectComponent<FontFamily> selectComponent() {
        return this.select;
    }

    private final SelectComponent<FontFamily> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.select.printTree(printer);
        }
        printer.outdent();
    }
}