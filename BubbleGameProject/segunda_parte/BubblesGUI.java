/*			PRACTICA REALIZADA POR:
 * 			Alvaro Gomez Ramos				NIA:100307009
 * 			Carlos Contreras Sanz			NIA:100303562*/

package segunda_parte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import segunda_parte.Tablero.Celda;



/** Implementación base del juego del Puzzle Bobble  
 *  @author Juan Gomez Romero 
 *  
 *  NO MODIFICAR ESTA CLASE */

public abstract class BubblesGUI {
	
	/* Game frame constants */
	private static final int CELL_SIZE     = 30;
    private static final int BORDER_SIZE   = 50; 
    private static final int VSPACE        = 2;
    private int frameHeight;
    private int frameWidth;
    
    /* Game frame */
	private JFrame frame;
    
    /* Board */
    private Tablero tablero;
    
    /* Number of rows of the board */
    private int rows;
    /* Number of columns of the board */
    private int cols;
    
 	//--------------------------------------------------------------------
    /** Constructor */
    public BubblesGUI() {
    	init();
    }
        
    /** Initialization method */
    private void init() {
    	/* Initialize board */
    	this.rows = Tablero.FILAS;
    	this.cols = Tablero.COLUMNAS;                        
        
        /* Initialize frame */
    	frame = new JFrame() {
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
    		        Image buffer = frame.createImage(frameWidth, frameHeight);
    		        Graphics sg = buffer.getGraphics();
    		        sg.clearRect(0, 0, frameWidth, frameHeight);
    		        paintBoard(sg);
    		        g.drawImage(buffer, 0, 0, frame);
    		    }
    	};
    	
        frameWidth  = cols * CELL_SIZE + 2 * BORDER_SIZE;
        frameHeight = rows * CELL_SIZE + 4 * CELL_SIZE    + BORDER_SIZE + 2 * VSPACE;
        frame.setSize (frameWidth, frameHeight);
        frame.setTitle("Puzzle Bubble Game");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        
        /* Add key listener */
        frame.addKeyListener(
        		new KeyListener() {  				
					public void keyPressed(KeyEvent e) {
						char key = e.getKeyChar();
        				processKey(key);
        				frame.repaint();
					}
					public void keyReleased(KeyEvent e) {}
					public void keyTyped(KeyEvent e) {}																
        		}
    	);
        		       
    }
    
    /** Paint board on the frame. 
     *  This method is internally called by paint(Graphics g)
     *  @param g Graphic object to draw on */
    private void paintBoard(Graphics g) {
    	
    	// Board background
    	g.setColor(Color.BLACK); 
        g.fillRect(BORDER_SIZE, BORDER_SIZE, CELL_SIZE*cols + CELL_SIZE/3, CELL_SIZE*rows+CELL_SIZE/3);

        // Paint cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {            	           
            	Celda celda = null;
            	try {
            		celda = tablero.getCelda(i, j);
            	} catch(NullPointerException e) {
            		System.out.println("Board has not been initialized yet.");
            		return;
            	} catch(ArrayIndexOutOfBoundsException e) {
            		System.out.println("GUI initial size is not correct.");
            		return;
            	}
            	
            	if(celda != null && celda != Celda.EMPTY) {            	
            		Color c = getColorFromCell(celda);        		            	
            		g.setColor(c);
            		g.fillArc(BORDER_SIZE+CELL_SIZE*j, BORDER_SIZE+CELL_SIZE*i, CELL_SIZE+CELL_SIZE/3, CELL_SIZE+CELL_SIZE/3, 0, 360);
            	}
            }
        }

        // Shoot direction
        g.setColor(Color.DARK_GRAY);        
        g.fillRect(BORDER_SIZE, BORDER_SIZE + CELL_SIZE*rows + VSPACE + CELL_SIZE/3, CELL_SIZE*cols+CELL_SIZE/3, CELL_SIZE+CELL_SIZE/3);                      
                
        g.setColor(getColorFromCell(tablero.getSiguienteBurbuja()));        
        g.fillArc(BORDER_SIZE+CELL_SIZE*(cols/2), BORDER_SIZE+CELL_SIZE*rows+CELL_SIZE/3 + VSPACE, CELL_SIZE+CELL_SIZE/3, CELL_SIZE+CELL_SIZE/3, 0, 360);
        
        g.setColor(Color.LIGHT_GRAY);        
        HashMap<Integer, Integer> t = gtp(tablero.getDireccion());
        for(int r: t.keySet())       
        	g.fillRect(BORDER_SIZE+CELL_SIZE*t.get(r)+(CELL_SIZE)/2+4, BORDER_SIZE+CELL_SIZE*r+CELL_SIZE/2 + VSPACE, 3, 3);
        
        // Remaining bubbles
        g.setColor(Color.LIGHT_GRAY); 
        g.fillRect(BORDER_SIZE, BORDER_SIZE + CELL_SIZE*(rows+1) + 2*VSPACE + 2*CELL_SIZE/3, CELL_SIZE*cols+CELL_SIZE/3, CELL_SIZE+CELL_SIZE/3);
        g.setColor(Color.BLACK);
        //g.drawString("Remaining bubbles: " + board.remainingBubbles() + " | Stage: " + board.getStage(), BORDER_SIZE + 2, BORDER_SIZE + CELL_SIZE*(rows+2) + 2*VSPACE + CELL_SIZE/2);
          
    }
    
    /** Obtain color from cell name
     *  @param cell Cell value 
     *  @return java.awt.Color corresponding to cell value --black, if not found */ 
    
    private Color getColorFromCell(Celda celda) {
    	Color c = null;
    	try {
    		Field field = Class.forName("java.awt.Color").getField(celda.name());
    		c = (Color) field.get(null);
    	} catch (Exception e) {
    		// Empty cell (BLACK)
    		c = Color.BLACK;
    	}
    	return c;
    }
    
    /** Internal */
    private HashMap<Integer, Integer> gtp(int shootDirection) {		
		int dir=shootDirection;
		int i=rows-1, j=cols/2+dir;

		/* Check direction */
		int leftToRight;
		if(dir > 0)
			leftToRight = 1;	// -->
		else if(dir < 0)
			leftToRight = -1;	// <--
		else
			leftToRight = 0;	// |

		/* Calculate trajectory */
		HashMap<Integer, Integer> trajectory = new HashMap<Integer, Integer>();
		
		boolean obstacle = tablero.getCelda(i, j) != Celda.EMPTY;
		while(i>=0 && !obstacle) {
			// Add position
			trajectory.put(i, j);

			// Move column
			j += leftToRight;
			// Move row
			i--;
			
			// Check if the position is out of the board
			// and change direction
			if(j < 0) {
				// change to -->
				leftToRight = 1;
				j = 1;					

			} else if(j >= cols) {
				// change to <--
				leftToRight = -1;
				j = cols-2;					
			}	

			// Check obstacle
			if(i>=0 && (tablero.getCelda(i, j) != Celda.EMPTY || (tablero.getCelda(i, j-leftToRight) != Celda.EMPTY && tablero.getCelda(i+1, j) != Celda.EMPTY)))
					obstacle = true;			
		}

		return trajectory;
	}
    
    //--------------------------------------------------------------------   
    
    /** Draw method. Must be called from MyBubblesGame to repaint the board.
     *  @param board The board to draw  */
    public void draw(Tablero tablero) {
    	this.tablero = tablero;
        frame.setVisible(true);
    	frame.repaint();
    }         
    
    /** Show dialog method. To print a message on the screen. 
     *  @param message Message to s how */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
    
    /** Terminate: closes the window and finishes the program */
    protected void terminate() {
    	frame.dispose();
    }    
    
    //--------------------------------------------------------------------   
    /* Abstract methods: to be implemented in MyBubblesGame */
    /** This method must be implemented in MyBubblesGame by the students */
    public abstract void processKey(char key);
}
