package cue.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.event.MouseInputListener;

import cue.controller.CueController;

@SuppressWarnings("serial")
public class CueFrame extends JFrame
{
	private CuePanel panel;
	
	/**
	 * Initializes the panel and {@link #setupFrame() sets up the frame}
	 * @param controller Passes down to the panel
	 */
	public CueFrame(CueController controller)
	{
		super();
		this.panel 		= new CuePanel(controller, this);
		
		setupFrame();
	}
	
	/**
	 * Makes the constructed frame visible
	 */
	public void display()
	{
		this.setVisible(true);
	}
	
	/**
	 * Sets up the necessary values of the frame
	 */
	private void setupFrame()
	{
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter()
		{
	    	@Override
	    	public void windowClosing(WindowEvent event)
	    	{
	    		JFrame frame = (JFrame)event.getSource();
	    		if (!panel.isSaved())
	    		{
		    		if (panel.confirmSave())
		    		{
		    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    		}
	    		}
	    		else
	    		{
	    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		}
	    	}
		});
	    
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.setSize(800, 600);
		this.setTitle("Cue Tester");
		this.setResizable(true);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Sets up the MenuBar to act as the application bar
	 * @param menuBar the, already setup, menuBar
	 */
	public void setupMenuBar(JMenuBar menuBar)
	{
		this.setUndecorated(true);
//		this.getRootPane().setWindowDecorationStyle(this.getRootPane().FRAME);
		
		JButton minimize = new JButton("_");
		JButton close = new JButton("X");
		
		minimize.setBorderPainted(false);
		minimize.setFocusable(false);
		close.setBorderPainted(false);
		close.setFocusable(false);

		menuBar.add(new JMenu("Cue Tester"), 0);
		menuBar.getMenu(0).setEnabled(false);
		menuBar.add(new JSeparator());
		menuBar.add(minimize);
		menuBar.add(close);

		Component[] comps = menuBar.getComponents();
		for(int i = 0; i < comps.length; i++)
		{
			if (comps[i] != null)
			{
				comps[i].setBackground(null);
				comps[i].setForeground(Color.WHITE);
			}
		}
		comps[comps.length - 3].setForeground(null);
		
		new CueMouseListener(this);
		
		
		JFrame frame = this;
		minimize.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setExtendedState(JFrame.ICONIFIED);
			}
		});
		
		close.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
	
	private class CueMouseListener implements MouseInputListener
	{
		JFrame frame;
		Point clickPoint;
		int resizeDir;
		
		int x = -1;
		int y = -1;
		int width = -1;
		int height = -1;

		/**
		 * A private array consolidate cursor directions
		 */
	    int[] cursors = {
	        Cursor.N_RESIZE_CURSOR,
	        Cursor.S_RESIZE_CURSOR,
	        Cursor.W_RESIZE_CURSOR,
	        Cursor.E_RESIZE_CURSOR,
	        Cursor.NW_RESIZE_CURSOR,
	        Cursor.NE_RESIZE_CURSOR,
	        Cursor.SW_RESIZE_CURSOR,
	        Cursor.SE_RESIZE_CURSOR
	    };
		
		/**
		 * The listener that makes an undecorated frame<br>
		 * resizable and movable. Automatically adds<br>
		 * itself to the given frame.
		 * @param frame an undecorated JFrame
		 */
		public CueMouseListener(JFrame frame)
		{
			this.frame = frame;
			this.frame.addMouseListener(this);
			this.frame.addMouseMotionListener(this);
		}

		/**
		 * Not Used<br><br>
		 * {@inheritDoc}
		 */
		@Override
		public void mouseClicked(MouseEvent e) {}

		/**
		 * Saves a couple variables as a state used to make<br>
		 * {@link #mouseDragged(MouseEvent)} work
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			clickPoint = e.getPoint();
			resizeDir = getResizeDir(e.getPoint());
			
			x = frame.getX();
			y = frame.getY();
			width = frame.getWidth();
			height = frame.getHeight();
		}

		/**
		 * Resets the variables saved on press
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{
			clickPoint = null;
			resizeDir = -1;
			
			x = -1;
			y = -1;
			width = -1;
			height = -1;
		}

		/**
		 * Not Used<br><br>
		 * {@inheritDoc}
		 */
		@Override
		public void mouseEntered(MouseEvent e) {}

		/**
		 * Not Used<br><br>
		 * {@inheritDoc}
		 */
		@Override
		public void mouseExited(MouseEvent e) {}

		/**
		 * Either moves or resizes the window<br>
		 * depending on the location initially clicked
		 */
		@Override
		public void mouseDragged(MouseEvent e)
		{			
			int x = this.x;
			int y = this.y;
			int width = this.width;
			int height = this.height;
			
			switch (resizeDir)
			{
				default:
					double xChange;
					double yChange;
					break;
				
				case -1:
					x = e.getXOnScreen() - clickPoint.x;
					y = e.getYOnScreen() - clickPoint.y;
					break;
				case 0: //N
					yChange = clickPoint.getY() - e.getY();
					height += yChange;
					y -= yChange;
//					clickPoint.setLocation(x, y - yChange);
					break;
				case 1: //S
					yChange = clickPoint.getY() - e.getY();
					height -= yChange;
					break;
				case 2: //W
					xChange = clickPoint.getX() - e.getX();
					width += xChange;
					x -= xChange;
//					clickPoint.setLocation(x - xChange, y);
					break;
				case 3: //E
					xChange = clickPoint.getX() - e.getX();
					width -= xChange;
					break;
				case 4: //NW
					yChange = clickPoint.getY() - e.getY();
					height += yChange;
					y -= yChange; 
					
					xChange = clickPoint.getX() - e.getX();
					width += xChange;
					x += xChange;
//					clickPoint.setLocation(x - xChange, y - yChange);
					break;
				case 5: //NE
					yChange = clickPoint.getY() - e.getY();
					height += yChange;
					y -= yChange;
					
					xChange = clickPoint.getX() - e.getX();
					width -= xChange;
//					clickPoint.setLocation(x, y - yChange);
					break;
				case 6: //SW
					yChange = clickPoint.getY() - e.getY();
					height -= yChange;
					
					xChange = clickPoint.getX() - e.getX();
					width += xChange;
					x -= xChange;
//					clickPoint.setLocation(x - xChange, y);
					break;
				case 7: //SE
					yChange = clickPoint.getY() - e.getY();
					height -= yChange;
					
					xChange = clickPoint.getX() - e.getX();
					width -= xChange;
					break;
			}
			
			if (resizeDir != -1)
			{
				if (width < 340) { width = 340; }
				if (height < 230) { height = 230; }
				frame.setSize(width, height);
			}
			frame.setLocation(x, y);
		}

		/**
		 * Checks if mouse is at a location to resize the window<br>
		 * If so, it applies the appropriate cursor<br>
		 */
		@Override
		public void mouseMoved(MouseEvent e)
		{
			int dir = getResizeDir(e.getPoint());
			Cursor cursor = Cursor.getDefaultCursor();
			if (dir != -1) { cursor = Cursor.getPredefinedCursor(cursors[dir]); }
			setCursor(cursor);
		}
		
		/**
		 * Used to determine the direction of which the<br>
		 * frame is about to be resized, based on the position<br>
		 * of the cursor. If it's not about to be resized, it<br>
		 * returns -1
		 * @param point where the cursor is
		 * @return an index to {@link #cursors} or -1
		 */
		private int getResizeDir(Point point)
		{
			Rectangle rect = frame.getBounds();
			
			boolean isEW = Math.min(point.getX(), rect.getWidth() - point.getX()) < 5.0;
			boolean isNS = Math.min(point.getY(), rect.getHeight() - point.getY()) < 5.0;
			
			boolean isSouth = point.getY() >= rect.getHeight() / 2.0;
			boolean isEast = point.getX() >= rect.getWidth() / 2.0;
			
//			double resultX = Math.min(point.getX(), rect.getWidth() - point.getX());
//			double resultY = Math.min(point.getY(), rect.getHeight() - point.getY());
//			System.out.println("X:  min: " + 0 + "\tpoint: " + point.getX() + "\tmax: " + rect.getWidth() + "\tresult: " + resultX);
//			System.out.println("Y:  min: " + 0 + "\tpoint: " + point.getY() + "\tmax: " + rect.getHeight() + "\tresult: " + resultY);
//			System.out.println("isEW\tisNS\tisSouth\tisEast");
//			System.out.println(isEW + "\t" + isNS + "\t" + isSouth + "\t" + isEast);
			
			int dir = -1;
			
			int bools = 0b0000;
			if (isNS) { bools |= 0b1000; }
			if (isEW) { bools |= 0b0100; }
			if (isSouth) {bools |= 0b0010; }
			if (isEast) {bools |= 0b0001; }
			
			switch (bools)
			{
				case 0b1000: //N
				case 0b1001:
					dir = 0;
					break;
				case 0b1010: //S
				case 0b1011:
					dir = 1;
					break;
				case 0b0100: //E
				case 0b0110:
					dir = 2;
					break;
				case 0b0101: //W
				case 0b0111:
					dir = 3;
					break;
				case 0b1100: //NE
					dir = 4;
					break;
				case 0b1101: //NW
					dir = 5;
					break;
				case 0b1110: //SE
					dir = 6;
					break;
				case 0b1111: //SW
					dir = 7;
					break;
				default:
					dir = -1;
					break;
			}
			
			return dir;
		}
	}
}
