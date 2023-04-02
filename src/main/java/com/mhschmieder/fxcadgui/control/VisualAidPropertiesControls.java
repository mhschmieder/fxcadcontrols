/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the FxCadGui Library
 *
 * You should have received a copy of the MIT License along with the FxCadGui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxcadgui
 */
package com.mhschmieder.fxcadgui.control;

import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxcadgraphics.GraphicalObjectCollection;
import com.mhschmieder.fxcadgraphics.VisualAid;
import com.mhschmieder.fxguitoolkit.GuiUtilities;
import com.mhschmieder.fxguitoolkit.control.IntegerSelector;
import com.mhschmieder.fxlayergraphics.model.LayerProperties;
import com.mhschmieder.fxlayergui.control.LayerSelector;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

public final class VisualAidPropertiesControls {

    public GraphicalObjectLabelEditor _visualAidLabelEditor;
    public LayerSelector              _layerSelector;
    public CheckBox                   _useAsProjectorCheckBox;
    public IntegerSelector            _projectionZonesSelector;

    // Default constructor
    public VisualAidPropertiesControls( final ClientProperties pClientProperties,
                                        final boolean applyToolkitCss,
                                        final String visualAidLabelDefault,
                                        final GraphicalObjectCollection< ? extends VisualAid > visualAidCollection,
                                        final String projectorType,
                                        final String projectionZonesType,
                                        final String projectionZonesUsageContext ) {
        // Make the Visual Aid Properties controls.
        _visualAidLabelEditor = new GraphicalObjectLabelEditor( pClientProperties,
                                                                visualAidLabelDefault,
                                                                visualAidCollection );

        _layerSelector = new LayerSelector( pClientProperties, applyToolkitCss, false );

        final String useAsProjectorLabel = "Use as " + projectorType;
        _useAsProjectorCheckBox = GuiUtilities.getCheckBox( useAsProjectorLabel, false );
        
        final StringBuilder projectionZonesTooltipText = new StringBuilder( projectionZonesType );
        if ( ( projectionZonesUsageContext != null ) && !projectionZonesUsageContext.isEmpty() ) {
            projectionZonesTooltipText.append( " for " );
            projectionZonesTooltipText.append( projectionZonesUsageContext );
        }
        _projectionZonesSelector = CadControlFactory.getProjectionZonesSelector( pClientProperties,
                                                                                 applyToolkitCss,
                                                                                 projectionZonesTooltipText.toString() );

        // Try to get the buttons to be as tall as possible.
        GridPane.setFillHeight( _visualAidLabelEditor, true );
        GridPane.setFillHeight( _layerSelector, true );
        GridPane.setFillHeight( _useAsProjectorCheckBox, true );
        GridPane.setFillHeight( _projectionZonesSelector, true );

        // Try to force sufficient width for custom label editing.
        _visualAidLabelEditor.setMinWidth( GuiUtilities.LABEL_EDITOR_WIDTH_DEFAULT );
        _visualAidLabelEditor.setPrefWidth( GuiUtilities.LABEL_EDITOR_WIDTH_DEFAULT );

        // Try to force minimum width on Use as Projector Check Box to
        // avoid clipping.
        _useAsProjectorCheckBox.setMinWidth( 140d );

        // Bind Projection Zones Pane enablement to the Projector Check Box.
        _projectionZonesSelector.disableProperty()
                .bind( _useAsProjectorCheckBox.selectedProperty().not() );
    }

    public String getLayerName() {
        // Forward this method to the Layer Selector.
        return _layerSelector.getLayerName();
    }

    public String getNewVisualAidLabelDefault() {
        // Forward this method to the Visual Aid Label Editor.
        return _visualAidLabelEditor.getNewGraphicalObjectLabelDefault();
    }

    public int getNumberOfProjectionZones() {
        // Forward this method to the Projection Zones Selector.
        return _projectionZonesSelector.getIntegerValue();
    }

    public String getUniqueVisualAidLabel( final String visualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Label Editor.
        return _visualAidLabelEditor.getUniqueGraphicalObjectLabel( visualAidLabelCandidate );
    }

    public boolean isUseAsProjector() {
        // Forward this method to the Use As Projector Check Box.
        return _useAsProjectorCheckBox.isSelected();
    }

    public boolean isVisualAidLabelUnique( final String visualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Label Editor.
        return _visualAidLabelEditor.isGraphicalObjectLabelUnique( visualAidLabelCandidate );
    }

    public void setLayerCollection( final ObservableList< LayerProperties > layerCollection ) {
        // Forward this method to the Layer Selector.
        _layerSelector.setLayerCollection( layerCollection );
    }

    public void setLayerNameCurrent( final String layerNameCurrent ) {
        // Forward this method to the Layer Selector.
        _layerSelector.setLayerNameIfChanged( layerNameCurrent );
    }

    public void setNumberOfProjectionZones( final int numberOfProjectionZones ) {
        // Forward this method to the Projection Zones Selector.
        _projectionZonesSelector.setIntegerValue( numberOfProjectionZones );
    }

    public void setUseAsProjector( final boolean useAsProjector ) {
        // Forward this method to the Use As Projector Check Box.
        _useAsProjectorCheckBox.setSelected( useAsProjector );
    }

}

