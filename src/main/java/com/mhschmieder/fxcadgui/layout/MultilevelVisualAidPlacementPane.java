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
package com.mhschmieder.fxcadgui.layout;

import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxcadgraphics.MultilevelVisualAid;
import com.mhschmieder.fxguitoolkit.GuiUtilities;
import com.mhschmieder.fxguitoolkit.ScrollingSensitivity;
import com.mhschmieder.fxphysicstoolkit.layout.CartesianPositionPane;
import com.mhschmieder.fxphysicstoolkit.layout.PolarPositionPane;
import com.mhschmieder.physicstoolkit.AngleUnit;
import com.mhschmieder.physicstoolkit.DistanceUnit;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public final class MultilevelVisualAidPlacementPane extends HBox {

    protected GraphicalObjectPreviewPane _previewPane;
    public CartesianPositionPane         _inclinometerPositionPane;
    public PolarPositionPane             _startPolarPositionPane;
    public PolarPositionPane             _endPolarPositionPane;

    public MultilevelVisualAidPlacementPane( final ClientProperties pClientProperties ) {
        // Always call the superclass constructor first!
        super();

        try {
            initPane( pClientProperties );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    private void initPane( final ClientProperties pClientProperties ) {
        _previewPane = new GraphicalObjectPreviewPane( 80, 100 );
        final Node previewBorderNode = GuiUtilities.getTitledBorderWrappedNode( _previewPane,
                                                                                "Preview" ); //$NON-NLS-1$

        _inclinometerPositionPane = new CartesianPositionPane( pClientProperties );
        final Node inclinometerPositionBorderNode = GuiUtilities
                .getTitledBorderWrappedNode( _inclinometerPositionPane,
                                             "RangeFinder-Inclinometer Position" ); //$NON-NLS-1$

        _startPolarPositionPane = new PolarPositionPane( pClientProperties );
        final Node startPolarPositionBorderNode = GuiUtilities
                .getTitledBorderWrappedNode( _startPolarPositionPane, "First Point" ); //$NON-NLS-1$

        _endPolarPositionPane = new PolarPositionPane( pClientProperties );
        final Node endPolarPositionBorderNode = GuiUtilities
                .getTitledBorderWrappedNode( _endPolarPositionPane, "Second Point" ); //$NON-NLS-1$

        setSpacing( 6 );
        setPadding( new Insets( 3 ) );

        getChildren().addAll( previewBorderNode,
                              inclinometerPositionBorderNode,
                              startPolarPositionBorderNode,
                              endPolarPositionBorderNode );
    }

    public void saveEdits() {
        _inclinometerPositionPane.saveEdits();
        _startPolarPositionPane.saveEdits();
        _endPolarPositionPane.saveEdits();
    }

    protected void setEndPolarPosition( final MultilevelVisualAid multilevelVisualAid ) {
        _endPolarPositionPane.setPolarPosition( multilevelVisualAid.getEndAngleDegrees(),
                                                multilevelVisualAid.getEndDistance() );
    }

    public void setGesturesEnabled( final boolean gesturesEnabled ) {
        // Forward this method to the Start Polar Position Pane.
        _startPolarPositionPane.setGesturesEnabled( gesturesEnabled );

        // Forward this method to the End Polar Position Pane.
        _endPolarPositionPane.setGesturesEnabled( gesturesEnabled );
    }

    protected void setInclinometerPosition( final MultilevelVisualAid multilevelVisualAid ) {
        _inclinometerPositionPane
                .setCartesianPosition2D( multilevelVisualAid.getInclinometerPositionX(),
                                         multilevelVisualAid.getInclinometerPositioneY() );
    }

    /**
     * Set the new Scrolling Sensitivity for all of the sliders.
     *
     * @param scrollingSensitivity
     *            The sensitivity of the mouse scroll wheel
     */
    public void setScrollingSensitivity( final ScrollingSensitivity scrollingSensitivity ) {
        // Forward this method to the Start Polar Position Pane.
        _startPolarPositionPane.setScrollingSensitivity( scrollingSensitivity );

        // Forward this method to the End Polar Position Pane.
        _endPolarPositionPane.setScrollingSensitivity( scrollingSensitivity );
    }

    protected void setStartPolarPosition( final MultilevelVisualAid multilevelVisualAid ) {
        _startPolarPositionPane.setPolarPosition( multilevelVisualAid.getStartAngleDegrees(),
                                                  multilevelVisualAid.getStartDistance() );
    }

    public void syncMultilevelVisualAidToView( final MultilevelVisualAid multilevelVisualAid ) {
        final Point2D inclinometerPosition2D = _inclinometerPositionPane.getCartesianPosition2D();
        final double startAngleDegrees = _startPolarPositionPane.getRotationAngle();
        final double startDistance = _startPolarPositionPane.getDistance();
        final double endAngleDegrees = _endPolarPositionPane.getRotationAngle();
        final double endDistance = _endPolarPositionPane.getDistance();
        multilevelVisualAid.setLine( inclinometerPosition2D.getX(),
                                     inclinometerPosition2D.getY(),
                                     startAngleDegrees,
                                     startDistance,
                                     endAngleDegrees,
                                     endDistance );

        // Update the preview of the current Multilevel Visual Aid.
        updatePreview( multilevelVisualAid );
    }

    public void syncViewToMultilevelVisualAid( final MultilevelVisualAid multilevelVisualAid ) {
        // Make sure the positioning parameters are in sync with the data model
        // as they could change outside this editor, such as via mouse
        // move/rotate in the Sound Field.
        updatePositioning( multilevelVisualAid );
    }

    public void toggleGestures() {
        // Forward this method to the Start Polar Position Pane.
        _startPolarPositionPane.toggleGestures();

        // Forward this method to the End Polar Position Pane.
        _endPolarPositionPane.toggleGestures();
    }

    public void updateAngleUnit( final AngleUnit angleUnit ) {
        // Forward this method to the relevant subsidiary components.
        _startPolarPositionPane.updateAngleUnit( angleUnit );
        _endPolarPositionPane.updateAngleUnit( angleUnit );
    }

    public void updateDistanceUnit( final DistanceUnit distanceUnit ) {
        // Forward this method to the relevant subsidiary components.
        _inclinometerPositionPane.updateDistanceUnit( distanceUnit );
        _startPolarPositionPane.updateDistanceUnit( distanceUnit );
        _endPolarPositionPane.updateDistanceUnit( distanceUnit );
    }

    public void updatePositioning( final MultilevelVisualAid multilevelVisualAid ) {
        setInclinometerPosition( multilevelVisualAid );
        setStartPolarPosition( multilevelVisualAid );
        setEndPolarPosition( multilevelVisualAid );
    }

    public void updatePreview( final MultilevelVisualAid multilevelVisualAidCurrent ) {
        // Forward this to the preview pane, at the origin.
        final MultilevelVisualAid multilevelVisualAid =
                                                      new MultilevelVisualAid( multilevelVisualAidCurrent );
        final double x1 = 0.0d;
        final double y1 = 0.0d;
        final double x2 = multilevelVisualAid.getX2() - multilevelVisualAid.getX1();
        final double y2 = multilevelVisualAid.getY2() - multilevelVisualAid.getY1();
        multilevelVisualAid.setLine( x1, y1, x2, y2 );

        _previewPane.updatePreview( multilevelVisualAid, 2.0d );
    }

}// class MultilevelVisualAidPlacementPane
