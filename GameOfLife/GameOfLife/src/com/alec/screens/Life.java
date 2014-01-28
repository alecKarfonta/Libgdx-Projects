package com.alec.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Life implements ApplicationListener {

	private static final int        FRAME_COLS = 4;        
    private static final int        FRAME_ROWS = 1;         
    
    Animation                       animation;         
    Texture                         boxSheet;              
    TextureRegion[]                 boxRegions;             
    SpriteBatch                     spriteBatch;           
    TextureRegion                   frame;       
    Camera 							camera;
    Vector3 						touch;
    Vector2 						v2Touch;
    
    float stateTime;                       
    
    int rows;
    int cols;
    int boxSize = 100;
    boolean[][] grid;
    float holdTime = 1f;
    
    @Override
    public void create() {
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(),
    									Gdx.graphics.getHeight());
    	
		boxSheet = new Texture(Gdx.files.internal("data/images/box_animation.png"));     
	    TextureRegion[][] tmp = TextureRegion.split(boxSheet, boxSheet.getWidth() / 
	    					FRAME_COLS, boxSheet.getHeight() / FRAME_ROWS);                               
	    boxRegions = new TextureRegion[FRAME_COLS * FRAME_ROWS];
	    int index = 0;
	    for (int i = 0; i < FRAME_ROWS; i++) {
	            for (int j = 0; j < FRAME_COLS; j++) {
	            	boxRegions[index++] = tmp[i][j];
	            }
	    }
	    animation = new Animation(1, boxRegions);             
	    spriteBatch = new SpriteBatch();                               
	    stateTime = 0f;    
	    frame = animation.getKeyFrame(0, true);

	    rows = Gdx.graphics.getHeight() / boxSize;
	    cols = Gdx.graphics.getWidth() / boxSize;
	    grid = new boolean[cols + 2][rows + 2];
	    for (int col = 0; col < cols + 2; col++) {
	    	for (int row = 0; row < rows + 2; row++) {
	    		grid[col][row] = false;
	    	}
	    }
	    /**/	// turn off every other element
	    for (int col = 1; col < cols; col += 2 ) {
	    	for (int row = 1; row < rows; row += 2) {
	    		grid[col][row] = true;
	    	}
	    }
	    for (int col = 3; col < cols; col += 3 ) {
	    	for (int row = 1; row < rows; row++) {
	    		grid[col][row] = true;
	    	}
	    }
	    /**/
	    print("Grid Dimensions", cols + 2, rows + 2);
	    print("Activegrid", cols, rows);
	    
	    touch = new Vector3();
	    v2Touch = new Vector2();
	    
	    Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean keyDown(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				touch.set(screenX, screenY, 0);
				camera.unproject(touch);
				v2Touch.set(touch.x, touch.y);
				print("Click", screenX, screenY);
				int gridX = Math.round(screenX  / boxSize);
				int gridY = Math.round(((screenY + (boxSize * .3f)) / boxSize));
				try {
					grid[gridX][gridY]  = grid[gridX][gridY] ? false : true;
					print("Box Clicked", gridX, gridY);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				touch.set(screenX, screenY, 0);
				camera.unproject(touch);
				v2Touch.set(touch.x, touch.y);
				print("Click", screenX, screenY);
				int gridX = Math.round(screenX  / boxSize);
				int gridY = Math.round(((screenY + (boxSize*.6f)) / boxSize));
				try {
					grid[gridX][gridY]  = grid[gridX][gridY] ? false : true;
					print("Box Clicked", gridX, gridY);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}
	    	
	    });
    }

    @Override
    public void render() {
    	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);                                           

        spriteBatch.begin();
    	for (int row = rows; row >= 0; row--) {
    		for (int col = 0; col <= cols; col++) {
    			frame = animation.getKeyFrame(grid[col][rows - row] ? 2 : 3);
    			spriteBatch.draw(frame, col * boxSize,  row * boxSize);
    		}
        }   
        spriteBatch.end();
        
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > holdTime) {
        	stateTime = 0;
        	updateGrid();
        }
    }
    
    public void updateGrid() {
    	int neighborCount;
    	// check each neighbor and make alive if it has 2 or 3 neighbores
    	for (int col = 1; col < cols; col++) {
    		for (int row = 1; row < rows; row++) {
    			neighborCount = neighborCount(col ,row);
    			// if the box is alive 
    			if (grid[col][row]) {
    				// underpopulation
					if (neighborCount < 2) {
    					//print("Underpopulation", col, row);
    					//print("Neighborcount", neighborCount + "");
						grid[col][row]  = false;
					// overpopulation
					} else if (neighborCount > 3) {
    					//print("Overpopulation", col, row);
    					//print("Neighborcount", neighborCount + "");
						grid[col][row] = false;
					}
				// else the box is dead
				} else {
    				if (neighborCount == 3) {
    					// reproduction
    					//print("Reproduction", col, row);
    					//print("Neighborcount", neighborCount + "");
    					grid[col][row] = true;
    				}
				}
    		}
    	}
    }

    public int neighborCount(int col, int row) {
    	int count = 0;
	   // for each cell determine how many live neighbors it has
	   for (int x = col - 1; x <= col+1; x++) // we dont have to bounds check here because we know row is >= 1 and row is <= max_row
	   {
	      for (int y = row - 1; y <= row + 1; y++)
	      {
		 	if (grid[x][y]) {
		 		count++;
		 	}
	      }
	   }
	   // dont count itself?
	   if (grid[col][row])
	    	  count--;

    	return count;
    }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		boxSheet.dispose();
		spriteBatch.dispose();
	}

	public void print(String desc, String item1, String item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, float item1, float item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, int item1, int item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, double item1, double item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, String text) {
		System.out.println(desc + ": " + text);
	}
	public void print(String text) {
		System.out.println(text);
	}

}
