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
		board.testIsValidMove(1, 0, "black");
		board.testSetStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.testIsValidMove(0, 0, "white");
		board.testSetStone(board.getPointAt(0,0),"white");
		board.testIsValidMove(0, 1, "black");
		board.testSetStone(board.getPointAt(0,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,0)));
		assertEquals(Status.NONE, board.stones.get(board.getPointAt(0,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.stones.get(board.getPointAt(1,0)));

		//surround 4,0 (white) with black
		board.testIsValidMove(3, 0, "black");
		board.testSetStone(board.getPointAt(3,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		board.testIsValidMove(4, 0, "white");
		board.testSetStone(board.getPointAt(4,0),"white");
		board.testIsValidMove(4, 1, "black");
		board.testSetStone(board.getPointAt(4,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,1)));

		//surround 0,4 (white) with black
		board.testIsValidMove(0, 3, "black");
		board.testSetStone(board.getPointAt(0,3),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,3)));
		board.testIsValidMove(0, 4, "white");
		board.testSetStone(board.getPointAt(0,4),"white");
		board.testIsValidMove(1, 4, "black");
		board.testSetStone(board.getPointAt(1,4),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,4)));

		//surround 0,4 (white) with black
		board.testIsValidMove(4, 3, "black");
		board.testSetStone(board.getPointAt(4,3),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		board.testIsValidMove(4, 4, "white");
		board.testSetStone(board.getPointAt(4,4),"white");
		board.testIsValidMove(3, 4, "black");
		board.testSetStone(board.getPointAt(3,4),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,4)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,4)));
	}

	@Test
	public void testMiddleStoneRemove(){
		//surround 2,2 (white) with black
		board.testReset(5);
		board.testIsValidMove(2, 2, "white");
		board.testSetStone(board.getPointAt(2,2),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,2)));
		board.testIsValidMove(2, 1, "black");
		board.testSetStone(board.getPointAt(2,1),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		board.testIsValidMove(2, 3, "black");
		board.testSetStone(board.getPointAt(2,3),"black");
		board.testIsValidMove(1, 2, "black");
		board.testSetStone(board.getPointAt(1,2),"black");
		board.testIsValidMove(3, 2, "black");
		board.testSetStone(board.getPointAt(3,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,3)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,2)));	
	}

	@Test
	public void testSideStoneRemove(){
		//surround 0,2 (white) with black
		board.testReset(5);
		board.testIsValidMove(2, 0, "white");
		board.testSetStone(board.getPointAt(2,0),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,0)));
		board.testIsValidMove(1, 0, "black");
		board.testSetStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.testIsValidMove(3, 0, "black");
		board.testSetStone(board.getPointAt(3,0),"black");
		board.testIsValidMove(2, 1, "black");
		board.testSetStone(board.getPointAt(2,1),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertFalse(board.testIsValidMove(2, 0, "white"));
		assertTrue(board.testIsKo(board.getPointAt(2,0), "white"));
		board.testSetStone(board.getPointAt(2,0),"white");
	}

	@Test
	public void testCornerDoubleRemove(){
		//surround 0,0 and 0,1 (white) with black
		board.testReset(5);
		board.testIsValidMove(0, 0, "white");
		board.testSetStone(board.getPointAt(0,0),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(0,0)));
		board.testIsValidMove(0, 1, "white");
		board.testSetStone(board.getPointAt(0,1),"white");

		board.testIsValidMove(1, 0, "black");
		board.testSetStone(board.getPointAt(1,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		board.testIsValidMove(1, 1, "black");
		board.testSetStone(board.getPointAt(1,1),"black");
		board.testIsValidMove(0, 2, "black");
		board.testSetStone(board.getPointAt(0,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,0)));
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(0,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,2)));

	}

	@Test
	public void testMiddleDoubleRemove(){
		//surround 2,2 and 2,3 (white) with black
		board.testReset(5);
		board.testIsValidMove(2, 2, "white");
		board.testSetStone(board.getPointAt(2,2),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,2)));
		board.testIsValidMove(2, 3, "white");
		board.testSetStone(board.getPointAt(2,3),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(2,3)));

		board.testIsValidMove(1, 2, "black");
		board.testSetStone(board.getPointAt(1,2),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(1,2)));
		board.testIsValidMove(1, 3, "black");
		board.testSetStone(board.getPointAt(1,3),"black");
		board.testIsValidMove(2, 1, "black");
		board.testSetStone(board.getPointAt(2,1),"black");
		board.testIsValidMove(2, 4, "black");
		board.testSetStone(board.getPointAt(2,4),"black");
		board.testIsValidMove(3, 2, "black");
		board.testSetStone(board.getPointAt(3,2),"black");
		board.testIsValidMove(3, 3, "black");
		board.testSetStone(board.getPointAt(3,3),"black");
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
		board.testReset(5);
		board.testIsValidMove(0, 1, "white");
		board.testSetStone(board.getPointAt(0,1),"white");
		assertEquals(Status.WHITE, board.getStatus(board.getPointAt(0,1)));
		board.testIsValidMove(0, 2, "white");
		board.testSetStone(board.getPointAt(0,2),"white");

		board.testIsValidMove(0, 0, "black");
		board.testSetStone(board.getPointAt(0,0),"black");
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(0,0)));
		board.testIsValidMove(0, 3, "black");
		board.testSetStone(board.getPointAt(0,3),"black");
		board.testIsValidMove(1, 1, "black");
		board.testSetStone(board.getPointAt(1,1),"black");
		board.testIsValidMove(1, 2, "black");
		board.testSetStone(board.getPointAt(1,2),"black");
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
		board.testReset(5);
		//group1
		board.testIsValidMove(1, 2, "white");
		board.testSetStone(board.getPointAt(1,2),"white");
		board.testIsValidMove(0, 2, "black");
		board.testSetStone(board.getPointAt(0,2),"black");
		board.testIsValidMove(1, 1, "black");
		board.testSetStone(board.getPointAt(1,1),"black");
		board.testIsValidMove(1, 3, "black");
		board.testSetStone(board.getPointAt(1,3),"black");
		//group2
		board.testIsValidMove(2, 3, "white");
		board.testSetStone(board.getPointAt(2,3),"white");
		board.testIsValidMove(2, 4, "black");
		board.testSetStone(board.getPointAt(2,4),"black");
		board.testIsValidMove(3, 3, "black");
		board.testSetStone(board.getPointAt(3,3),"black");
		//group3
		board.testIsValidMove(3, 2, "white");
		board.testSetStone(board.getPointAt(3,2),"white");
		board.testIsValidMove(4, 2, "white");
		board.testSetStone(board.getPointAt(4,2),"white");
		board.testIsValidMove(3, 1, "black");
		board.testSetStone(board.getPointAt(3,1),"black");
		board.testIsValidMove(4, 1, "black");
		board.testSetStone(board.getPointAt(4,1),"black");
		board.testIsValidMove(4, 3, "black");
		board.testSetStone(board.getPointAt(4,3),"black");
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
		board.testIsValidMove(2, 2, "black");
		board.testSetStone(board.getPointAt(2,2),"black");
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
		board.testReset(5);
		//prepare white
		board.testIsValidMove(0, 2, "white");
		board.testSetStone(board.getPointAt(0,2),"white");
		board.testIsValidMove(1, 1, "white");
		board.testSetStone(board.getPointAt(1,1),"white");
		board.testIsValidMove(1, 3, "white");
		board.testSetStone(board.getPointAt(1,3),"white");
		//prepare black
		//System.out.println(board.toString() + " /n");
		board.testIsValidMove(2, 1, "black");
		board.testSetStone(board.getPointAt(2,1),"black");
		board.testIsValidMove(2, 3, "black");
		board.testSetStone(board.getPointAt(2,3),"black");
		board.testIsValidMove(3, 2, "black");
		board.testSetStone(board.getPointAt(3,2),"black");
		//place 2,2 white (valid)
		//System.out.println(board.toString() + " /n");
		assertTrue(board.testIsValidMove(2, 2, "white"));
		board.testIsValidMove(2, 2, "white");
		board.testSetStone(board.getPointAt(2,2),"white");
		//places 1,2 black (valid)
		//System.out.println(board.toString() + " /n");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(1,2)));
		assertTrue(board.testIsValidMove(1, 2, "black"));
		board.testIsValidMove(1, 2, "black");
		board.testSetStone(board.getPointAt(1,2),"black");
		//check if 2,2 is removed
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(2,2)));
		//place 2,2 white (invalid by KO-rule)
		assertFalse(board.testIsValidMove(2, 2, "white"));

	}
	@Test
	public void testHarakiri(){
		//points at 1,2 and 2,2 are the center
		board.testReset(5);
		//test if stone is removed in case of suicide
		board.testIsValidMove(0, 2, "white");
		board.testSetStone(board.getPointAt(0,2),"white");
		board.testIsValidMove(1, 1, "white");
		board.testSetStone(board.getPointAt(1,1),"white");
		board.testIsValidMove(1, 3, "white");
		board.testSetStone(board.getPointAt(1,3),"white");
		board.testIsValidMove(2, 2, "white");
		board.testSetStone(board.getPointAt(2,2),"white");
		board.testIsValidMove(1, 2, "black");
		board.testSetStone(board.getPointAt(1,2),"black");
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(1,2)));
	}

	@Test
	public void caseTestDoubleRing(){
		//points at 1,2 and 2,2 are the center
		board.testReset(5);
		//set outerring
		board.testIsValidMove(2, 0, "white");
		board.testSetStone(board.getPointAt(2,0),"white");
		board.testIsValidMove(1, 1, "white");
		board.testSetStone(board.getPointAt(1,1),"white");
		board.testIsValidMove(2, 2, "white");
		board.testSetStone(board.getPointAt(2,2),"white");
		board.testIsValidMove(3, 2, "white");
		board.testSetStone(board.getPointAt(3,2),"white");
		board.testIsValidMove(4, 2, "white");
		board.testSetStone(board.getPointAt(4,2),"white");
		//set corner
		board.testIsValidMove(4, 0, "white");
		board.testSetStone(board.getPointAt(4,0),"white");
		//set innerring
		board.testIsValidMove(3, 0, "black");
		board.testSetStone(board.getPointAt(3,0),"black");
		board.testIsValidMove(2, 1, "black");
		board.testSetStone(board.getPointAt(2,1),"black");
		board.testIsValidMove(3, 1, "black");
		board.testSetStone(board.getPointAt(3,1),"black");
		board.testIsValidMove(4, 1, "black");
		board.testSetStone(board.getPointAt(4,1),"black");
		//check if corner is set to none
		assertEquals(Status.NONE, board.getStatus(board.getPointAt(4,0)));
		//check if black still stands
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,0)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(2,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(3,1)));
		assertEquals(Status.BLACK, board.getStatus(board.getPointAt(4,1)));
	}
}
