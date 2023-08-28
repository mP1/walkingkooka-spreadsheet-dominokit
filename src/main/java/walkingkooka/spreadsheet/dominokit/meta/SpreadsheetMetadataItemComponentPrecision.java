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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.forms.IntegerBox;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataItemComponent} for {@link SpreadsheetMetadataPropertyName#PRECISION}
 */
final class SpreadsheetMetadataItemComponentPrecision extends SpreadsheetMetadataItemComponent<Integer> {

    static SpreadsheetMetadataItemComponentPrecision with(final SpreadsheetMetadataPanelComponentContext context) {
        checkContext(context);

        return new SpreadsheetMetadataItemComponentPrecision(
                context
        );
    }

    private SpreadsheetMetadataItemComponentPrecision(final SpreadsheetMetadataPanelComponentContext context) {
        super(
                SpreadsheetMetadataPropertyName.PRECISION,
                context
        );

        final UListElement list = ElementsFactory.elements.ul();

        final IntegerBox integerBox = this.integerBox(
                        this::save
                ).setMinValue(0)
                .setMaxValue(128)
                .setStep(1);
        this.integerBox = integerBox;

        list.appendChild(integerBox);

        // build links for 0 | 32 | 64 | 128
        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(SpreadsheetMetadataPropertyName.PRECISION);
        final Map<Integer, Anchor> valueToAnchors = Maps.sorted();

        for (final int value : Lists.of(0, 32, 64, 128)) {
            final Anchor anchor = token
                    .setSave(String.valueOf(value))
                    .link(SpreadsheetMetadataPanelComponent.id(SpreadsheetMetadataPropertyName.PRECISION) + "-" + value + "-link")
                    .setTabIndex(0)
                    .addPushHistoryToken(context)
                    .setTextContent(0 == value ? "Unlimited" : String.valueOf(value));

            valueToAnchors.put(value, anchor);

            list.appendChild(anchor);
        }

        this.list = list;
        this.valueToAnchors = valueToAnchors;
    }

    private void save() {
        final IntegerBox integerBox = this.integerBox;
        final String saveText;

        if (integerBox.isEmpty()) {
            saveText = "";
        } else {
            saveText = String.valueOf(integerBox.getValue());
        }

        final SpreadsheetMetadataPropertyName<?> propertyName = this.propertyName;
        final SpreadsheetMetadataPanelComponentContext context = this.context;
        context.debug(this.getClass().getSimpleName() + ".save " + propertyName + "=" + CharSequences.quoteAndEscape(saveText));

        context.save(
                propertyName,
                saveText
        );
    }

    private final IntegerBox integerBox;

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.integerBox.setValue(
                context.spreadsheetMetadata()
                        .get(this.propertyName)
                        .orElse(null)
        );

        final SpreadsheetMetadataPropertyName<Integer> propertyName = SpreadsheetMetadataPropertyName.PRECISION;

        final Integer metadataValue = context.spreadsheetMetadata()
                .getIgnoringDefaults(propertyName)
                .orElse(null);

        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(propertyName);

        for (final Entry<Integer, Anchor> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final Integer value = valueAndAnchor.getKey();
            final Anchor anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                    Optional.of(
                            token.setSave(
                                    String.valueOf(value)
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
    }

    private final Map<Integer, Anchor> valueToAnchors;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
