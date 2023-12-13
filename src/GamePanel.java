import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    ScorePanel scorePanel = new ScorePanel();
    private GamePanel gamePanel= this;
    private GameFrame gameFrame;
    GameGround gameGround = new GameGround(scorePanel,gamePanel);

    public GamePanel(GameFrame gameFrame){
        this.gameFrame= gameFrame;
        setLayout(new BorderLayout());
        //화면 구성
        splitPanel();
    }

    //게임 실행 화면을 구성하는 함수
    private void splitPanel(){
        JSplitPane hSplitPane = new JSplitPane();
        hSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hSplitPane.setDividerLocation(1500); // Divider 위치 설정
        add(hSplitPane);

        hSplitPane.setRightComponent(scorePanel); // 오른쪽 패널을 ScorePanel로 설정
        hSplitPane.setLeftComponent(gameGround); // 왼쪽 패널을 GameGround로 설정
    }
    //게임 종료시 호출돼서 메인메뉴로 돌아감
    public void endGame() {
        gameFrame.showMainMenu();
    }
}


