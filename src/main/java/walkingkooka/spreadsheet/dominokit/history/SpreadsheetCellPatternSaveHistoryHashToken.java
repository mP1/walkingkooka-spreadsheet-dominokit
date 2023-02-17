package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public final class SpreadsheetCellPatternSaveHistoryHashToken extends SpreadsheetCellPatternHistoryHashToken {

    static SpreadsheetCellPatternSaveHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection,
                                                           final SpreadsheetPattern pattern) {
        return new SpreadsheetCellPatternSaveHistoryHashToken(
                viewportSelection,
                pattern
        );
    }

    private SpreadsheetCellPatternSaveHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                       final SpreadsheetPattern pattern) {
        super(viewportSelection);

        this.pattern = Objects.requireNonNull(pattern, "pattern");
    }

    public SpreadsheetPattern pattern() {
        return this.pattern;
    }

    private final SpreadsheetPattern pattern;

    @Override
    UrlFragment patternUrlFragment() {
        final SpreadsheetPattern pattern = this.pattern();

        return pattern.kind()
                .urlFragment()
                .append(SAVE)
                .append(pattern.urlFragment());
    }
}
