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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that creates multiple links for each enum value. If the enum value is currently
 * selected in the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} that link will be disabled all others are enabled.
 */
final class SpreadsheetMetadataPanelComponentItemEnum<T extends Enum<T>> extends SpreadsheetMetadataPanelComponentItem<T> {

    static <T extends Enum<T>> SpreadsheetMetadataPanelComponentItemEnum<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                 final List<T> values,
                                                                                 final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        Objects.requireNonNull(values, "values");
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemEnum<>(
                propertyName,
                values,
                context
        );
    }

    private SpreadsheetMetadataPanelComponentItemEnum(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                      final List<T> values,
                                                      final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );

        this.list = this.uListElement();

        final HistoryToken token = context.historyToken();

        final Map<T, HistoryTokenAnchorComponent> valueToAnchors = Maps.hash();
        HistoryTokenAnchorComponent firstAnchor = null;

        for (final T value : values) {
            // anchor will be updated later with save value.
            final HistoryTokenAnchorComponent anchor = token
                    .link(SpreadsheetMetadataPanelComponent.id(propertyName) + "-" + CaseKind.kebabEnumName(value))
                    .setTabIndex(0)
                    .addPushHistoryToken(context)
                    .setTextContent(this.format(value));

            valueToAnchors.put(value, anchor);

            if (null == firstAnchor) {
                firstAnchor = anchor;
            }

            this.list.appendChild(
                    liElement()
                            .appendChild(anchor)
            );
        }

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        this.list.appendChild(
                liElement()
                        .appendChild(
                                defaultValueAnchor
                        )
        );
        this.firstAnchor = firstAnchor;
        this.defaultValueAnchor = defaultValueAnchor;

        this.valueToAnchors = valueToAnchors;
    }

    @Override
    void focus() {
        this.firstAnchor.focus();
    }

    /**
     * This will the {@link HistoryTokenAnchorComponent} that is given focus.
     */
    private final HistoryTokenAnchorComponent firstAnchor;

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName;
        final T metadataValue = context.spreadsheetMetadata()
                .getIgnoringDefaults(propertyName)
                .orElse(null);

        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(propertyName);

        for (final Entry<T, HistoryTokenAnchorComponent> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final T value = valueAndAnchor.getKey();
            final HistoryTokenAnchorComponent anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                    Optional.of(
                            token.setSave(
                                    Optional.of(
                                            valueAndAnchor.getKey()
                                    )
                            )
                    )
            );
            anchor.setDisabled(
                    Objects.equals(
                            metadataValue,
                            value
                    )
            );
        }

        this.refreshDefaultValue(
                this.defaultValueAnchor,
                context.spreadsheetMetadata()
                        .defaults()
                        .get(propertyName)
                        .map(this::format)
                        .orElse("")
        );
    }


    private final Map<T, HistoryTokenAnchorComponent> valueToAnchors;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    private String format(final Enum<?> value) {
        return CaseKind.SNAKE.change(
                value.name(),
                CaseKind.TITLE
        );
    }

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
