import javax.swing.*;
import java.awt.*;

public class Eagle extends Bird {
    private JLabel eagleLabel;
    private Thread eagleThread;
    private boolean correctFlag = false;
    private int right = 1;
    //현재 방향을 나타내는 변수
    //오른쪽으로 향하고 있으면 1
    private int current = 1;

    @Override
    public Thread getThread() {
        return eagleThread;
    }
    //새들이 날개를 다르게 한 모양에 대한 이미지 4개
    //날개의 퍼덕임을 위해서 4개의 이미지(날개 위 아래 + 새의 머리 좌 우 향한 모습)
    private ImageIcon eagle1 = new ImageIcon("images/eagle1.png");
    private ImageIcon eagle2 = new ImageIcon("images/eagle2.png");
    private ImageIcon eagle3 = new ImageIcon("images/eagle3.png");
    private ImageIcon eagle4 = new ImageIcon("images/eagle4.png");
    private ImageIcon shot = new ImageIcon("images/shot.png");
    //맞았는지 여부를 판단할 플래그
    public void setCorrectFlag(boolean correctFlag) {
        this.correctFlag = correctFlag;
    }
    //Eagle 생성시 단어 입력창, 라운드, gameGround에 대한 레퍼런스 전달
    public Eagle(JTextField userInput, int round, GameGround gameGround) {
        super(userInput,round,gameGround);
        eagleLabel = new JLabel();
        eagleLabel.setIcon(eagle1);
        eagleLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트 위치 조정
        eagleLabel.setVerticalTextPosition(JLabel.CENTER);
        eagleLabel.setForeground(Color.RED); // 텍스트 색상
        eagleLabel.setFont(new Font("고딕체", Font.BOLD, 20)); // 텍스트 폰트
        //사냥이 완료되면 Eagle 객체 자체가 사라지게 설계됐으므로 단어는 생성 시 설정
        eagleLabel.setText(this.word);
        //gameGround에서 스레드 인터럽트를 위해 스레드 레퍼런스를 반환하기 위한 레퍼런스 변수
        this.eagleThread = new EagleThread();
        eagleThread.start();
    }
    //사냥하면 안되는 새 이미지에 사용되는 이미지 리턴
    @Override
    public ImageIcon getBirdImage(){
        return eagle1;
    }

    //맞았다면
    //Eagle의 getWord와 입력창의 텍스트가 동일하다면
    @Override
    public boolean isCorrect() {
        if (getWord().equals(userInput.getText())) {
            //정답 플래그 up
            correctFlag=true;
            //죽은 것으로 처리
            setIsAlive(false);
            return true;
        }
        else return false;
    }

    //이 JLabel의 레퍼런스 리턴
    @Override
    public JLabel getBirdLabel() {
        return eagleLabel;
    }

    //오른쪽으로 가게 하는 함수
    //X좌표가 1300이 넘으면 방향 설정 프래그 right 1로 설정(다시 왼쪽으로)
    public void goRight() {
        eagleLabel.setLocation(eagleLabel.getX() + 40, eagleLabel.getY());
        if (eagleLabel.getX() > 1300) {
            right = 1;
        }
    }
    //왼쪽으로 가게 하는 함수
    //X좌표가 0보다 작아지면 방향 설정 프래그 right 0으로 설정(다시 오른쪽으로)
    public void goLeft() {
        eagleLabel.setLocation(eagleLabel.getX() - 40, eagleLabel.getY());
        if (eagleLabel.getX() < 0) {
            right = 0;
        }
    }

    //날개 퍼덕임을 구현하는 함수
    //현재 상태에 따라 이미지를 다르게 해서 나는 것처럼 구현
    public void moveWings() {
        if (current == 0 && right == 1) {
            eagleLabel.setIcon(eagle1);
            current = 1;
        } else if (current == 1 && right == 1) {
            eagleLabel.setIcon(eagle2);
            current = 0;
        } else if (current == 0 && right == 0) {
            eagleLabel.setIcon(eagle3);
            current = 1;
        } else if (current == 1 && right == 0) {
            eagleLabel.setIcon(eagle4);
            current = 0;
        }
    }

    class EagleThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    //폭탄 정답을 맞추면 imMoving = false;
                    if(isMoving) {
                        moveWings();
                        //right Flag에 따라 방향 함수 다르게 호출
                        if (right == 1) {
                            goLeft();
                        } else {
                            goRight();
                        }
                    }
                    //맞았다면
                    if (correctFlag) {
                        //새가 총에 맞은 이미지로 레이블 변경
                        eagleLabel.setIcon(shot);
                        eagleLabel.repaint();
                        //새에 적혀있는 문자열 없애기
                        eagleLabel.setText("");
                        sleep(1000);
                        //새 이미지 제거
                        gameGround.remove(eagleLabel);
                        gameGround.repaint();
                        //인터럽트 처리를 통한 스레드 스케쥴링 멈춤
                        this.interrupt();
                    }

                    //sleep 매개변수 시간으로 날갯짓 시간 구현 + 초당 날아가는 거리 변경
                    //round는 GameGround에서 현재 라운드를 전달
                    //round가 올라갈수록 더 파닥거리고, 더 빨리 날아간다
                    sleep(500/round);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}