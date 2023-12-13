import javax.swing.*;
import java.awt.*;

public class Bomb {
    //GameGround에 입력하는 사용자 입력창
    private JTextField userInput;
    //Bomb add와 remove를 위한 레퍼런스
    private GameGround gameGround;
    protected JLabel bombLabel;
    
    private ImageIcon bombImg = new ImageIcon("images/bomb.png");
    private ImageIcon bombShotImg = new ImageIcon("images/bombshot.png");
    //폭탄에 적힐 단어 선정을 위해 textSource 레퍼런스 전달
    protected TextSource textSource = new TextSource();
    //폭탄에 적힌 단어
    private String word;
    //gameGround에서 중단시키기 위한 스레드 레퍼런스
    private BombThread bombThread;
    //맞았는지에 대한 플래그
    private boolean correctFlag = false;

    Bomb(JTextField userInput, GameGround gameGround) {
        this.userInput = userInput;
        this.gameGround = gameGround;
        bombLabel = new JLabel();
        bombLabel.setIcon(bombImg);
        this.word = textSource.getNext();
        bombLabel.setText(word);
        bombLabel.setForeground(Color.WHITE);
        bombLabel.setFont(new Font("고딕체", Font.BOLD, 30));
        bombLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트 위치 조정
        bombLabel.setVerticalTextPosition(JLabel.CENTER);
        //bomb 객체 생성시 스레드 실행
        //bomb가 없으면 스레드가 작동하지 않는 최적화를 위해
        //gameGround에서 bomb 답 맞추면 참조 변수를 null로 초기화
        bombThread = new BombThread();
        bombThread.start();
    }

    public JLabel getBombLabel() {
        return bombLabel;
    }

    public String getWord() {
        return word;
    }

    //맞았다면
    public boolean isCorrect() {
        if (getWord().equals(userInput.getText())) {
            correctFlag=true;
            return true;
        } else return false;
    }

    //폭탄이 떨어지는 것을 구현한 메서드
    class BombThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    bombLabel.setLocation(bombLabel.getX(), bombLabel.getY() + 10);
                    bombLabel.repaint();
                    //Y좌표가 600 이상(입력창보다 아래쯤)이면 폭탄 삭제 후 인터럽트
                    if(bombLabel.getY()>600){
                        gameGround.remove(bombLabel);
                        gameGround.repaint();
                        this.interrupt();
                    }
                    //정답이 맞았으면 폭탄이 얼음으로 터지는 이미지를 보여주고 폭탄 문자열 초기화
                    //3초간 얼음을 보여주고 새들 모두 정지
                    //그 후 이미지 제거
                    if(correctFlag){
                        gameGround.pauseAllBirds();
                        bombLabel.setIcon(bombShotImg);
                        bombLabel.setText("");
                        gameGround.repaint();
                        sleep(3000); // 3초 동안 모든 새들의 움직임을 멈춤
                        gameGround.resumeAllBirds();
                        gameGround.remove(bombLabel);
                        gameGround.repaint();
                    //3초 지난 뒤에 인터럽트를 통해 스레드 스케쥴링 멈춤
                        this.interrupt();
                    }
                    sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

}