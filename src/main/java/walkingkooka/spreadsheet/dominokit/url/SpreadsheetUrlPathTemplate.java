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

package walkingkooka.spreadsheet.dominokit.url;

import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.template.Template;
import walkingkooka.template.TemplateContext;
import walkingkooka.template.TemplateValueName;
import walkingkooka.template.url.UrlPathTemplate;
import walkingkooka.template.url.UrlPathTemplateValues;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printer;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link Template} that supports extracting well known named parameters having values path components automatically converted.
 */
public final class SpreadsheetUrlPathTemplate implements Template {

    public final static TemplateValueName SPREADSHEET_ID = TemplateValueName.with(SpreadsheetId.class.getSimpleName());

    public final static TemplateValueName SPREADSHEET_NAME = TemplateValueName.with(SpreadsheetName.class.getSimpleName());

    public final static TemplateValueName LOCALE_TAG = TemplateValueName.with("LocaleTag");

    public static SpreadsheetUrlPathTemplate parse(final String template) {
        return new SpreadsheetUrlPathTemplate(
            UrlPathTemplate.parse(template)
        );
    }

    private SpreadsheetUrlPathTemplate(final UrlPathTemplate template) {
        this.template = template;
    }

    public SpreadsheetId spreadsheetId(final UrlPath path) {
        return (SpreadsheetId)
            getOrFail(
                path,
                SPREADSHEET_ID
            );
    }

    public SpreadsheetName spreadsheetName(final UrlPath path) {
        return (SpreadsheetName)
            getOrFail(
                path,
                SPREADSHEET_NAME
            );
    }

    public Object getOrFail(final UrlPath path,
                            final TemplateValueName name) {
        return this.get(
            path,
            name
        ).orElseThrow(() -> new IllegalArgumentException("Unknown placeholder: " + name));
    }

    public Optional<Object> get(final UrlPath path,
                                final TemplateValueName name) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(name, "name");

        return this.template.tryPrepareValues(path)
            .flatMap(v -> get(v, name));
    }

    public Map<TemplateValueName, Object> extract(final UrlPath path) {
        Map<TemplateValueName, Object> values;
        final UrlPathTemplateValues templateValues = this.template.tryPrepareValues(path)
            .orElse(null);
        if (null != templateValues) {
            values = Maps.sorted();

            for (final TemplateValueName name : this.template.templateValueNames()) {
                final Object value = get(
                    templateValues,
                    name
                ).orElseThrow(() -> new IllegalArgumentException("Url missing " + name));
                values.put(
                    name,
                    value
                );
            }
            values = Maps.readOnly(values);
        } else {
            values = Maps.empty();
        }

        return values;
    }

    private static Optional<Object> get(final UrlPathTemplateValues values,
                                        final TemplateValueName name) {
        return values.get(
            name,
            (final String s) -> {
                final Object v;

                switch (name.value()) {
                    case "LocaleTag":
                        v = LocaleTag.parse(s);
                        break;
                    case "SpreadsheetId":
                        v = SpreadsheetId.parse(s);
                        break;
                    case "SpreadsheetName":
                        v = SpreadsheetName.with(s);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown placeholder: " + name);
                }
                return v;
            }
        );
    }

    /**
     * Renders this template using the provided {@link Map} as the source of values, using the well known {@link TemplateValueName}.
     */
    public UrlPath render(final Map<TemplateValueName, Object> nameToValue) {
        Objects.requireNonNull(nameToValue, "nameToValue");

        return this.template.renderPath(
            (n) -> {
                final Object value = nameToValue.get(n);
                final String stringValue;

                switch(n.value()) {
                    case "LocaleTag":
                        stringValue = value.toString();
                        break;
                    case "SpreadsheetId":
                    case "SpreadsheetName":
                        stringValue = value.toString();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown placeholder: " + n);
                }

                return stringValue;
            }
        );
    }

    @Override
    public void render(final Printer printer,
                       final TemplateContext context) {
        this.template.render(
            printer,
            context
        );
    }

    @Override
    public String renderToString(final LineEnding lineEnding,
                                 final TemplateContext context) {
        return this.template.renderToString(
            lineEnding,
            context
        );
    }

    @Override
    public Set<TemplateValueName> templateValueNames() {
        return this.template.templateValueNames();
    }

    private final UrlPathTemplate template;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.template.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof SpreadsheetUrlPathTemplate && this.equals0((SpreadsheetUrlPathTemplate) other);
    }

    private boolean equals0(final SpreadsheetUrlPathTemplate other) {
        return this.template.equals(other.template);
    }

    @Override
    public String toString() {
        return this.template.toString();
    }

    @Override public Object value() {
        return null;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.template.printTree(printer);
        }
        printer.outdent();
    }
}
