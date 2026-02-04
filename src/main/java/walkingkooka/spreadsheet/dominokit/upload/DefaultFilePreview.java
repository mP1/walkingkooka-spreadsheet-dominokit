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

/*
 * Copyright © 2019 Dominokit
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
 */
package walkingkooka.spreadsheet.dominokit.upload;

import elemental2.dom.Event;
import elemental2.dom.FileReader;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.config.HasComponentConfig;
import org.dominokit.domino.ui.config.UploadConfig;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.i18n.HasLabels;
import org.dominokit.domino.ui.i18n.UploadLabels;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.style.CompositeCssClass;
import org.dominokit.domino.ui.style.SwapCssClass;
import org.dominokit.domino.ui.thumbnails.Thumbnail;
import org.dominokit.domino.ui.typography.BlockHeader;
import org.dominokit.domino.ui.upload.FileItem;
import org.dominokit.domino.ui.upload.FileUpload;
import org.dominokit.domino.ui.upload.FileUploadStyles;
import org.dominokit.domino.ui.upload.IsFilePreview;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.ChildHandler;

import static org.dominokit.domino.ui.utils.Domino.div;
import static org.dominokit.domino.ui.utils.Domino.dui_border;
import static org.dominokit.domino.ui.utils.Domino.dui_border_error;
import static org.dominokit.domino.ui.utils.Domino.dui_border_solid;
import static org.dominokit.domino.ui.utils.Domino.dui_border_success;
import static org.dominokit.domino.ui.utils.Domino.dui_border_warning;
import static org.dominokit.domino.ui.utils.Domino.dui_fg_error;
import static org.dominokit.domino.ui.utils.Domino.dui_fg_grey;
import static org.dominokit.domino.ui.utils.Domino.dui_flex;
import static org.dominokit.domino.ui.utils.Domino.dui_font_size_24;
import static org.dominokit.domino.ui.utils.Domino.dui_gap_2;
import static org.dominokit.domino.ui.utils.Domino.dui_image_responsive;
import static org.dominokit.domino.ui.utils.Domino.dui_items_center;
import static org.dominokit.domino.ui.utils.Domino.dui_justify_center;
import static org.dominokit.domino.ui.utils.Domino.dui_m_b_2;
import static org.dominokit.domino.ui.utils.Domino.dui_m_x_auto;
import static org.dominokit.domino.ui.utils.Domino.dui_max_h_full;
import static org.dominokit.domino.ui.utils.Domino.dui_max_w_full;
import static org.dominokit.domino.ui.utils.Domino.dui_min_h_52;
import static org.dominokit.domino.ui.utils.Domino.dui_min_h_64;
import static org.dominokit.domino.ui.utils.Domino.dui_overflow_y_hidden;
import static org.dominokit.domino.ui.utils.Domino.dui_text_center;
import static org.dominokit.domino.ui.utils.Domino.dui_text_ellipsis;
import static org.dominokit.domino.ui.utils.Domino.dui_w_full;
import static org.dominokit.domino.ui.utils.Domino.img;

/**
 * Represents a default file preview component for file uploads.
 *
 * <p>This component displays information about the uploaded file, including its name, size, and
 * provides actions for file upload, removal, and cancellation.
 *
 * @see BaseDominoElement
 */
class DefaultFilePreview extends BaseDominoElement<HTMLElement, DefaultFilePreview>
    implements IsFilePreview<Thumbnail>,
    HasComponentConfig<UploadConfig>,
    HasLabels<UploadLabels>,
    FileUploadStyles {
    private final Thumbnail thumbnail;
    private final FileItem fileItem;
    private final DivElement messageElement;
    private final Icon<?> removeIcon;
    //  private final Icon<?> uploadIcon;
//  private final Icon<?> cancelIcon;
//  private final ProgressBar progressBar;
    private final SwapCssClass statusMessageCss = SwapCssClass.of();
    private final SwapCssClass statusCss = SwapCssClass.of();
    private static final CompositeCssClass successBorder =
        CompositeCssClass.of(dui_border, dui_border_solid, dui_border_success);
    private static final CompositeCssClass failedBorder =
        CompositeCssClass.of(dui_border, dui_border_solid, dui_border_error);
    private static final CompositeCssClass canceledBorder =
        CompositeCssClass.of(dui_border, dui_border_solid, dui_border_warning);
    //private final Progress progress;

    private final FileUpload fileUpload;

    /**
     * Constructs a new {@code DefaultFilePreview} instance.
     *
     * @param fileItem   The associated {@link FileItem} representing the file to be previewed.
     * @param fileUpload The parent {@link FileUpload} instance to which this preview belongs.
     */
    public DefaultFilePreview(FileItem fileItem, FileUpload fileUpload) {
        this.fileItem = fileItem;
        this.fileUpload = fileUpload;
        this.thumbnail =
            Thumbnail.create()
                .addCss(dui_min_h_64, dui_w_full, dui_file_preview)
                .withBody(
                    (parent, body) ->
                        body.addCss(
                            dui_min_h_52,
                            dui_overflow_y_hidden,
                            dui_flex,
                            dui_justify_center,
                            dui_items_center))
                .addClickListener(Event::stopPropagation);
        messageElement = div().addCss(dui_text_ellipsis, dui_m_b_2, dui_text_center);
        removeIcon = getConfig().getDefaultRemoveIcon().get();
//    uploadIcon = getConfig().getDefaultUploadIcon().get();
//    cancelIcon = getConfig().getDefaultCancelIcon().get().hide();
//    progress =
//            Progress.create()
//                    .addCss(dui_h_2)
//                    .appendChild(
//                            progressBar = ProgressBar.create(this.fileItem.getFile().size).setValue(20));

        init(this);
        if (this.fileItem.isImage()) {
            FileReader fileReader = new FileReader();
            fileReader.addEventListener(
                "load",
                evt -> thumbnail.appendChild(
                    img(fileReader.result.asString())
                        .setAttribute("alt", this.fileItem.getFile().name)
                        .addCss(dui_image_responsive, dui_max_w_full, dui_max_h_full, dui_m_x_auto)
                )
            );
            fileReader.readAsDataURL(
                this.fileItem.getFile()
            );
        } else {
            thumbnail.appendChild(Icons.file_upload().addCss(dui_fg_grey, dui_font_size_24));
        }

        thumbnail.withFooter(
            (parent, footer) -> {
                footer
                    .appendChild(
                        BlockHeader.create(this.fileItem.getFile().name, this.fileItem.readableFileSize())
                            .addCss(dui_text_center)
                            .withHeaderElement((blockHeader, header) -> header.addCss(dui_text_ellipsis))
                            .withDescriptionElement(
                                (blockHeader, description) -> description.addCss(dui_text_ellipsis))
                            .setTooltip(
                                this.fileItem.getFile().name + ": " + this.fileItem.readableFileSize()))
                    .appendChild(messageElement)
                    .appendChild(
                        div()
                            .addCss(dui_flex, dui_justify_center, dui_items_center, dui_gap_2)
                            .appendChild(
                                removeIcon
                                    .addCss(dui_fg_error)
                                    .clickable()
                                    .addClickListener(evt -> fileItem.remove()))
//                                      .appendChild(
//                                              uploadIcon
//                                                      .addCss(dui_fg_accent)
//                                                      .clickable()
//                                                      .addClickListener(
//                                                              evt -> {
//                                                                messageElement.clearElement().removeCss(statusMessageCss);
//                                                                fileItem.upload();
//                                                              }))
//                                      .appendChild(
//                                              cancelIcon
//                                                      .addCss(dui_fg_warning)
//                                                      .hide()
//                                                      .clickable()
//                                                      .addClickListener(evt -> fileItem.cancel()))
                    );
                //.appendChild(div().appendChild(progress));
            });
    }

    /**
     * Called when an upload operation fails.
     *
     * @param error The error message describing the failure.
     */
    @Override
    public void onUploadFailed(String error) {
//    SwapCssClass failedCss = statusMessageCss.replaceWith(dui_fg_error);
//    messageElement.addCss(failedCss).setTextContent(error).setTooltip(error);
//    //cancelIcon.hide();
//    //uploadIcon.toggleDisplay(!this.fileUpload.isAutoUpload());
//    removeIcon.show();
//    //progressBar.addCss(failedCss);
//    addCss(statusCss.replaceWith(failedBorder));
        throw new UnsupportedOperationException();
    }

    /**
     * Called when an upload operation is successful.
     */
    @Override
    public void onUploadSuccess() {
//    SwapCssClass successCss = statusMessageCss.replaceWith(dui_fg_success);
//    messageElement.addCss(successCss).setTextContent(getLabels().getDefaultUploadSuccessMessage());
//    //cancelIcon.hide();
//    //uploadIcon.toggleDisplay(!this.fileUpload.isAutoUpload());
//    removeIcon.show();
//    //progressBar.setValue(this.fileItem.getFile().size);
//    //progressBar.addCss(successCss);
//    addCss(statusCss.replaceWith(successBorder));
        throw new UnsupportedOperationException();
    }

    /**
     * Called when an upload operation is completed.
     */
    @Override
    public void onUploadCompleted() {
//    //cancelIcon.hide();
//    //uploadIcon.toggleDisplay(!this.fileUpload.isAutoUpload());
//    removeIcon.show();
        throw new UnsupportedOperationException();
    }

    /**
     * Called to update the progress of an ongoing upload operation.
     *
     * @param progress The progress value as a percentage (0.0 to 100.0).
     */
    @Override
    public void onUploadProgress(double progress) {
        //progressBar.setValue(progress);
        throw new UnsupportedOperationException();
    }

    /**
     * Called when an upload operation is canceled.
     */
    @Override
    public void onUploadCanceled() {
//    SwapCssClass cancelledCss = statusMessageCss.replaceWith(dui_fg_warning);
//    messageElement
//        .addCss(cancelledCss)
//        .setTextContent(getLabels().getDefaultUploadCanceledMessage());
//    //cancelIcon.hide();
//    //uploadIcon.toggleDisplay(!this.fileUpload.isAutoUpload());
//    removeIcon.show();
//    //progressBar.addCss(cancelledCss);
//    addCss(statusCss.replaceWith(canceledBorder));
        throw new UnsupportedOperationException();
    }

    /**
     * Called when an upload operation is started.
     */
    @Override
    public void onUploadStarted() {
//    //cancelIcon.show();
//    //uploadIcon.hide();
//    removeIcon.hide();
//    messageElement.removeCss(statusMessageCss).clearElement();
        throw new UnsupportedOperationException();
    }

    /**
     * Called when the file preview is reset.
     */
    @Override
    public void onReset() {
        //cancelIcon.hide();
        //uploadIcon.toggleDisplay(!this.fileUpload.isAutoUpload());
        removeIcon.show();
        messageElement.removeCss(statusMessageCss).clearElement();
        removeCss(statusCss);
        //progressBar.setValue(30.0);
        //progressBar.removeCss(statusMessageCss);
    }

    /**
     * Retrieves the thumbnail associated with this file preview.
     *
     * @return The {@link Thumbnail} element representing the file preview.
     */
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     * @return The {@link HTMLElement} representing this file preview.
     * @dominokit-site-ignore {@inheritDoc}
     * <p>Retrieves the HTML element of this file preview.
     */
    @Override
    public HTMLElement element() {
        return thumbnail.element();
    }

    /**
     * Configures the component with a child handler.
     *
     * @param handler The child handler to configure the component with.
     * @return This {@code DefaultFilePreview} instance for method chaining.
     */
    @Override
    public IsFilePreview<Thumbnail> withComponent(
        ChildHandler<IsFilePreview<Thumbnail>, Thumbnail> handler) {
        handler.apply(this, thumbnail);
        return this;
    }
}
