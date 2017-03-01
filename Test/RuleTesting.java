package Test;

import goGame.*;
import goGame.Board.Status;
import Players.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class RuleTesting {

	private Player player1;
	private Player player2;
	private Board board;
	private Game game;

	@Before
	public void setUp(){
		player1 = new HumanPlayer("speler1", Status.BLACK, 5);
		player2 = new HumanPlayer("speler2", Status.WHITE, 5);
		game = new Game(player1,player2,5);
		board = game.board;
	}

	@Test
	public void testCornerRemove(){
		//surround 0,0 (white) with black
		board.isValidMove(1, 0, "black");
		board.setStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.isValidMove(0, 0, "white");
		board.setStone(board.getPointAt(0,0),"white");
		board.isValidMove(0, 1, "black");
		board.setStone(board.getPointAt(0,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,0)));
		assertEquals(Status.NONE, board.stones.get(board.getPointAt(0,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(1,0)));

		//surround 4,0 (white) with black
		board.isValidMove(3, 0, "black");
		board.setStone(board.getPointAt(3,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		board.isValidMove(4, 0, "white");
		board.setStone(board.getPointAt(4,0),"white");
		board.isValidMove(4, 1, "black");
		board.setStone(board.getPointAt(4,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,1)));

		//surround 0,4 (white) with black
		board.isValidMove(0, 3, "black");
		board.setStone(board.getPointAt(0,3),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,3)));
		board.isValidMove(0, 4, "white");
		board.setStone(board.getPointAt(0,4),"white");
		board.isValidMove(1, 4, "black");
		board.setStone(board.getPointAt(1,4),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,4)));

		//surround 0,4 (white) with black
		board.isValidMove(4, 3, "black");
		board.setStone(board.getPointAt(4,3),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		board.isValidMove(4, 4, "white");
		board.setStone(board.getPointAt(4,4),"white");
		board.isValidMove(3, 4, "black");
		board.setStone(board.getPointAt(3,4),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,4)));
	}
	
	@Test
	public void testMiddleStoneRemove(){
		//surround 2,2 (white) with black
		board.reset(5);
		board.isValidMove(2, 2, "white");
		board.setStone(board.getPointAt(2,2),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,2)));
		board.isValidMove(2, 1, "black");
		board.setStone(board.getPointAt(2,1),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		board.isValidMove(2, 3, "black");
		board.setStone(board.getPointAt(2,3),"black");
		board.isValidMove(1, 2, "black");
		board.setStone(board.getPointAt(1,2),"black");
		board.isValidMove(3, 2, "black");
		board.setStone(board.getPointAt(3,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,2)));	
	}
	
	@Test
	public void testSideStoneRemove(){
		//surround 0,2 (white) with black
		board.reset(5);
		board.isValidMove(2, 0, "white");
		board.setStone(board.getPointAt(2,0),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,0)));
		board.isValidMove(1, 0, "black");
		board.setStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.isValidMove(3, 0, "black");
		board.setStone(board.getPointAt(3,0),"black");
		board.isValidMove(2, 1, "black");
		board.setStone(board.getPointAt(2,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertFalse(board.isValidMove(2, 0, "white"));
		assertTrue(board.isKo(board.getPointAt(2,0), "white"));
		board.setStone(board.getPointAt(2,0),"white");
	}
	
	@Test
	public void testCornerDoubleRemove(){
		//surround 0,0 and 0,1 (white) with black
		board.reset(5);
		board.isValidMove(0, 0, "white");
		board.setStone(board.getPointAt(0,0),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(0,0)));
		board.isValidMove(0, 1, "white");
		board.setStone(board.getPointAt(0,1),"white");
		
		board.isValidMove(1, 0, "black");
		board.setStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.isValidMove(1, 1, "black");
		board.setStone(board.getPointAt(1,1),"black");
		board.isValidMove(0, 2, "black");
		board.setStone(board.getPointAt(0,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,0)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,2)));

	}
	
	@Test
	public void testMiddleDoubleRemove(){
		//surround 2,2 and 2,3 (white) with black
		board.reset(5);
		board.isValidMove(2, 2, "white");
		board.setStone(board.getPointAt(2,2),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,2)));
		board.isValidMove(2, 3, "white");
		board.setStone(board.getPointAt(2,3),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,3)));
		
		board.isValidMove(1, 2, "black");
		board.setStone(board.getPointAt(1,2),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		board.isValidMove(1, 3, "black");
		board.setStone(board.getPointAt(1,3),"black");
		board.isValidMove(2, 1, "black");
		board.setStone(board.getPointAt(2,1),"black");
		board.isValidMove(2, 4, "black");
		board.setStone(board.getPointAt(2,4),"black");
		board.isValidMove(3, 2, "black");
		board.setStone(board.getPointAt(3,2),"black");
		board.isValidMove(3, 3, "black");
		board.setStone(board.getPointAt(3,3),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,3)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,3)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,2)));


	}
	
	@Test
	public void testDoubleSideStoneRemove(){
		//surround 0,1 and 0,2 (white) with black
		board.reset(5);
		board.isValidMove(0, 1, "white");
		board.setStone(board.getPointAt(0,1),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(0,1)));
		board.isValidMove(0, 2, "white");
		board.setStone(board.getPointAt(0,2),"white");
		
		board.isValidMove(0, 0, "black");
		board.setStone(board.getPointAt(0,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,0)));
		board.isValidMove(0, 3, "black");
		board.setStone(board.getPointAt(0,3),"black");
		board.isValidMove(1, 1, "black");
		board.setStone(board.getPointAt(1,1),"black");
		board.isValidMove(1, 2, "black");
		board.setStone(board.getPointAt(1,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,1)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		
	}
	@Test
	/*
	 * surrounds 3 groups with the placement of 2,2 (black)
	 * group 1: 1,2
	 * group 2: 2,3
	 * group 3: 3,2 and 4,2
	 */
	public void testMutipleGroupsStoneRemove(){
		board.reset(5);
		//group1
		board.isValidMove(1, 2, "white");
		board.setStone(board.getPointAt(1,2),"white");
		board.isValidMove(0, 2, "black");
		board.setStone(board.getPointAt(0,2),"black");
		board.isValidMove(1, 1, "black");
		board.setStone(board.getPointAt(1,1),"black");
		board.isValidMove(1, 3, "black");
		board.setStone(board.getPointAt(1,3),"black");
		//group2
		board.isValidMove(2, 3, "white");
		board.setStone(board.getPointAt(2,3),"white");
		board.isValidMove(2, 4, "black");
		board.setStone(board.getPointAt(2,4),"black");
		board.isValidMove(3, 3, "black");
		board.setStone(board.getPointAt(3,3),"black");
		//group3
		board.isValidMove(3, 2, "white");
		board.setStone(board.getPointAt(3,2),"white");
		board.isValidMove(4, 2, "white");
		board.setStone(board.getPointAt(4,2),"white");
		board.isValidMove(3, 1, "black");
		board.setStone(board.getPointAt(3,1),"black");
		board.isValidMove(4, 1, "black");
		board.setStone(board.getPointAt(4,1),"black");
		board.isValidMove(4, 3, "black");
		board.setStone(board.getPointAt(4,3),"black");
		//check white before placement
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(1,2)));
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,3)));
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(3,2)));
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(4,2)));
		//check black before placement
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		//place stone at 2,2 (black)
		board.isValidMove(2, 2, "black");
		board.setStone(board.getPointAt(2,2),"black");
		//check if former white positions turned to NONE
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(1,2)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,3)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(3,2)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,2)));
		//check if former black positions are still black positions
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		
	}
	
	@Test
	public void testKoRule(){
		//points at 1,2 and 2,2 are the center
		board.reset(5);
		//prepare white
		board.isValidMove(0, 2, "white");
		board.setStone(board.getPointAt(0,2),"white");
		board.isValidMove(1, 1, "white");
		board.setStone(board.getPointAt(1,1),"white");
		board.isValidMove(1, 3, "white");
		board.setStone(board.getPointAt(1,3),"white");
		//prepare black
		//System.out.println(board.toString() + " /n");
		board.isValidMove(2, 1, "black");
		board.setStone(board.getPointAt(2,1),"black");
		board.isValidMove(2, 3, "black");
		board.setStone(board.getPointAt(2,3),"black");
		board.isValidMove(3, 2, "black");
		board.setStone(board.getPointAt(3,2),"black");
		//place 2,2 white (valid)
		//System.out.println(board.toString() + " /n");
		assertTrue(board.isValidMove(2, 2, "white"));
		board.isValidMove(2, 2, "white");
		board.setStone(board.getPointAt(2,2),"white");
		//places 1,2 black (valid)
		//System.out.println(board.toString() + " /n");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(1,2)));
		assertTrue(board.isValidMove(1, 2, "black"));
		board.isValidMove(1, 2, "black");
		board.setStone(board.getPointAt(1,2),"black");
		//check if 2,2 is removed
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,2)));
		//place 2,2 white (invalid by KO-rule)
		assertFalse(board.isValidMove(2, 2, "white"));
		
	}
	@Test
	public void testHarakiri(){
		//test if stone is removed in case of suicide
		board.isValidMove(0, 2, "white");
		board.setStone(board.getPointAt(0,2),"white");
		board.isValidMove(1, 1, "white");
		board.setStone(board.getPointAt(1,1),"white");
		board.isValidMove(1, 3, "white");
		board.setStone(board.getPointAt(1,3),"white");
		board.isValidMove(2, 2, "white");
		board.setStone(board.getPointAt(2,2),"white");
		board.isValidMove(1, 2, "black");
		board.setStone(board.getPointAt(1,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(1,2)));
	}
	
	
}
