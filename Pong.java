/*Junyi Chen*/
/*Player 1 : W - move up S - move down
 * player 2: ¡ü - move up ¡ý-move down
 * press 1 - Single player play with AI 
 * press 2 - Double player  
 */
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Pong extends GameEngine {

	public static void main(String[] args) {
		
		createGame(new Pong());
		
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		this.setWindowSize(600, 500);
		gameStatus = 0;//game is on hold
		
		//initialise Data thats needed
		initData();
		
	}

	private void initData() {
		//Initialise position and score of player 1
		p1X = 30;
		p1Y = 200;
		p1Score = 0;
		
		moveUp1 = false;
		moveDown1 = false;
		//Initialise position and score of player 2 and (AI)
		p2X =560;
		p2Y = 200;
		p2Score = 0;
		
		moveUp2 = false;
		moveDown2 = false;		
		
		//initialise position of Ball
		ballVelocityX = 230;
		ballVelocityY = 180;
		resetBall();
		
		//get the audio learned from http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
		 try{
			 URL url1 = this.getClass().getClassLoader().getResource("score.wav");
			 URL url2 = this.getClass().getClassLoader().getResource("beep.wav");
			 scoreAudio = AudioSystem.getAudioInputStream(url1);
			 bounceAudio = AudioSystem.getAudioInputStream(url2);
			 scoreClip = AudioSystem.getClip();
			 scoreClip.open(scoreAudio);
			 
			 bounceClip = AudioSystem.getClip();
			 bounceClip.open(bounceAudio);
		 } catch(Exception ex){
			 System.out.println("file not found");
		 }
	}
	
	//Game status  0 = hold, 1 = single player, 2 = 2 players, 3= over
	int gameStatus;

	
	//Audio 
	AudioInputStream scoreAudio;
	AudioInputStream bounceAudio;
	Clip scoreClip, bounceClip;
	
	//Collision detection function for player 1
	public boolean isCollisedP1(){
		if(ballX <= p1X + 10){ 
			if(ballY > p1Y && ballY < p1Y+100){
				return true;
			}
		} 
			return false;
	}
	//if collised top of the paddle, only called when cliision is detected
	public boolean collisedP1Top(){
		if(ballY < p1Y + 50){ // the Y of the ball collised above the middle point of the paddle
			return true;
		} else {
			return false;
		}
	}

	//if collised bottom of the paddle, only called when cliision is detected
	public boolean collisedP1Bot(){
		if(ballY > p1Y + 50){  // the Y of the ball collised under the middle point of the paddle 
			return true;
		} else {
			return false;
		}
	}
	
	//Collision detection function for player 2
	public boolean isCollisedP2(){
		
		if(ballX >= p2X){
			if(ballY >  p2Y && ballY < p2Y + 100 ){
				return true;
			}
		}
		return false;
	}

	//if collised top of the paddle, only called when cliision is detected
	public boolean collisedP2Top(){
		if(ballY < p2Y + 50){
			return true;
		} else {
			return false;
		}
	}

	//if collised bottom of the paddle, only called when cliision is detected
	public boolean collisedP2Bot(){
		if(ballY > p2Y + 50){ 
			return true;
		} else {
			return false;
		}
	}
	
	
	//Player 1 
	boolean moveUp1;
	boolean moveDown1;
	
	int p1X, p1Y;
	int p1VelocityY = 230; 
	int p1Score, p1FinalScore;
	
	//Draw Player1
	public void drawP1(){
		//drawing player 1
		changeColor(white);
		drawSolidRectangle(p1X, p1Y, 10, 100); //width 10, height 100
		
	}
	
	//updating player1
	public void updateP1(double dt){
		//if player 1 moving up and not reaching the top
		if(moveUp1 && p1Y >= 0){
			
			p1Y -= p1VelocityY * dt;
			
		} else if(moveDown1 && p1Y <= 400){	//if player 1 moving down and not reaching the bottom
			
			p1Y += p1VelocityY *dt;
			
		}
		
	}
	
	
	
	//Player 2 
		boolean moveUp2;
		boolean moveDown2;
		
		int p2X, p2Y;
		int p2VelocityY = 230;
		int p2Score, p2FinalScore;
		
		//Draw Player2
		public void drawP2(){
			
			changeColor(white);
			drawSolidRectangle(p2X, p2Y, 10, 100);
			
		}
		//updating player 2
		public void updateP2(double dt){
			
			if(moveUp2 && p2Y >= 0){
				
				p2Y -= p2VelocityY * dt;
				
			} else if(moveDown2 && p2Y <= 400){
				
				p2Y += p2VelocityY *dt;
				
			}
			
		}
		
	//Ball
		int ballX, ballY;
		int ballVelocityX, ballVelocityY;
		
		//reset ball position in a random place
		public void resetBall(){
			ballX = 295;
			ballY = rand(450);
		}
		public void drawBall(){
			changeColor(white);
			//radius of 10;
			drawSolidCircle(ballX, ballY, 10);
		}
		
		public void updateBall(double dt){

			ballX += ballVelocityX *dt;
			ballY += ballVelocityY *dt;
			
			//ball reach the top reverse
			if(ballY < 0){
				//play the sound
				bounceClip.setFramePosition(0);
				bounceClip.start();
				
				ballVelocityY *= -1;
				ballY += 10;
				
			}
			//ball reach the bottom reverse
			if(ballY  > 490){
				//play the sound
				bounceClip.setFramePosition(0);
				bounceClip.start();
				ballVelocityY *= -1;
				ballY -= 10;
			}
			checkScore();
			
			//collision detection
			collisionDetection();
			
			checkWin();
		}

		private void collisionDetection() {
			//ball collised with player 1
			if(isCollisedP1()){
				//play the bounce sound
				bounceClip.setFramePosition(0);
				bounceClip.start();
				
				//slightly increase the speed of the ball
				ballVelocityX -=5;
				
				//collised top of the player1 paddle
				if(collisedP1Top()){
			
					if(ballVelocityY > 0){ //move up the ball
						ballVelocityY *=-1;
					}
				} else if(collisedP1Bot()){ //collised bot of the player1 paddle
				
					if(ballVelocityY < 0){//move down the ball
						ballVelocityY *=-1;
					}
				}
				
				//ball going opposite
				ballX = p1X + 10;
				ballVelocityX *= -1;
			} 
			//ball collised with player2
			else if (isCollisedP2()){
				
				//play the bounce sound
				bounceClip.setFramePosition(0);
				bounceClip.start();
				
				//slightly increase the speed of the ball
				ballVelocityX +=5;
				
				if(collisedP2Top()){
					
					if(ballVelocityY > 0){ //move up the ball
						ballVelocityY *=-1;
					}
				} else if(collisedP2Bot()){
					
					if(ballVelocityY < 0){//move down the ball
						ballVelocityY *=-1;
					}
				}
				//ball going opposite
				ballX = p2X;
				ballVelocityX *= -1;
			}
		}

		private void checkScore() {//if reach the left or right player score one point
			if(ballX >= 600){
				//play the sound
				scoreClip.setFramePosition(0);
				scoreClip.start();
				//p1 score +1
				p1Score ++ ;
				resetBall();
				//p1 score , ball moving to p2 side
				ballVelocityX *= -1;
			}
			
			if(ballX <= 0){
				//play the sound
				scoreClip.setFramePosition(0);
				scoreClip.start();
				//p2 score +1
				p2Score++;
				resetBall();
				//p2 score, ball start moving to p1 side
				ballVelocityX *= -1;
			}
		}

		private void checkWin() {
			if(p1Score == 5 || p2Score == 5){//Finish the game
				//get the final score
				p1FinalScore = p1Score;
				p2FinalScore = p2Score;
				//reset score to 0
				p1Score = 0;
				p2Score = 0;
				gameStatus = 3;
			}
		}
		
	//updateAi (use same data as player 2)
	
	public void updateAI(double dt){
		
		if(p2Y + 50 <= ballY && p2Y <= 400){
			p2Y += p2VelocityY *dt;
		} else if (p2Y + 50 > ballY && p2Y >= 0){
		
			p2Y -= p2VelocityY *dt;
		}
		updateP2(dt);
	}
		
	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		if(gameStatus == 2){
			updateP1(dt);
		
			updateP2(dt);
			
			updateBall(dt);
		
			}
		else if(gameStatus == 1){
			
			updateP1(dt);
			
			updateAI(dt);
			
			updateBall(dt);
		}
	}
	
	
	//Drawing the line in the middle
	public void drawMidLine(){
		
		int width = 5;
		int height = 30;
		
		changeColor(white);
		for(int y = 0; y< 500; y+=40){
			drawSolidRectangle(297.5,y,width,height);
		}
		
	}
	
	//Drawing the score
	public void drawScore(){
		
		changeColor(white);
		drawBoldText(260, 50,Integer.toString(p1Score));
		drawBoldText(320, 50, Integer.toString(p2Score));
		
		
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_1 && gameStatus != 2 && gameStatus != 1){//if user press 1 and the game is not playing
			initData(); //initialise data
			gameStatus = 1;//start game with 1 player
		}
		if(e.getKeyCode() == KeyEvent.VK_2 && gameStatus !=2 && gameStatus != 1){//if user press 2 and the game is not playing 
			initData();//initialise data(position of player and ball)
			gameStatus = 2;//start the game with 2 players
		}
		if(e.getKeyCode() == KeyEvent.VK_W){//player1 moving up when pressing W
			if(gameStatus == 1 || gameStatus == 2){
				moveUp1 = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_S ){//player1 moving down when pressing S
			if(gameStatus == 1 || gameStatus == 2){
				moveDown1 = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP && gameStatus == 2){ //player2 moving up when pressing UP
			moveUp2 = true; //only work with two players
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN && gameStatus == 2){//player2 moving down when pressing DOWN
			moveDown2 = true;//only work with two players
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_W ){
			if(gameStatus == 1 || gameStatus == 2){
				moveUp1 = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_S){
			if(gameStatus == 1 || gameStatus == 2){
				moveDown1 = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP && gameStatus == 2){ 
			moveUp2 = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN && gameStatus == 2){
			moveDown2 = false;
		}
		
		
	}

	@Override
	public void paintComponent() {
		// TODO Auto-generated method stub
		if(gameStatus == 0){//game is on hold
			
			changeBackgroundColor(black);
			clearBackground(600, 500);
			changeColor(white);
			
			drawText(30,100,"Pong - Junyi Chen 16192554");
			drawText(0,170,"Player 1: W-Move up S-Move down", "Arial", 17);
			drawText(0,210,"Player 2: ¡ü-Move up ¡ý-Move down", "Arial", 17);
			
			drawText(200,350,"Press 1 for 1 player", "Arial", 20);
			drawText(200,390,"Press 2 for 2 players", "Arial", 20);
		}
		else if (gameStatus == 1){
			
			changeBackgroundColor(black);
			clearBackground(600, 500);
			changeColor(white);
			
			drawMidLine();
			
			drawP1();
			
			drawP2();
			
			drawScore();
			
			drawBall();
			
		}
		else if(gameStatus == 2){ //game is playing
			changeBackgroundColor(black);
			clearBackground(600, 500);
			changeColor(white);
			
			drawMidLine();
			
			drawP1();
			
			drawP2();
			
			drawScore();
			
			drawBall();
		} else if(gameStatus ==3){
			
			changeBackgroundColor(black);
			clearBackground(600, 500);
			changeColor(white);
			
			drawBoldText(260, 50,Integer.toString(p1FinalScore));
			drawBoldText(320, 50, Integer.toString(p2FinalScore));
			
			if(p1FinalScore > p2FinalScore){
				drawBoldText(0, 150,"Player 1 won the game");
			} else {
				drawBoldText(0, 150,"Player 2 won the game");
			}
			
			drawText(200,350,"Press 1 for 1 player", "Arial", 20);
			drawText(200,390,"Press 2 for 2 players", "Arial", 20);
			
		}
		
	}	

}
