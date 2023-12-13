import javax.swing.*;
import java.awt.*;

public class Sparrow extends Bird { // 참새, 10점
    private JLabel sparrowLabel; // Sparrow의 레이블
    private Thread sparrowThread; // Sparrow의 스레드
    private int right = 0; // 현재 방향을 나타내는 변수, 오른쪽으로 향하고 있으면 0
    private int current = 0; // 날개 퍼덕임의 현재 상태
    private boolean correctFlag = false; // 정답 여부를 판단하는 플래그

    @Override
    public Thread getThread() {
        return sparrowThread; // Sparrow 스레드 반환
    }

    // Sparrow의 날개 퍼덕임을 위한 이미지, 날개 위 아래 + 새의 머리 좌 우 향한 모습의 4개 이미지
    private ImageIcon sparrow1 = new ImageIcon("images/sparrow1.png");
    private ImageIcon sparrow2 = new ImageIcon("images/sparrow2.png");
    private ImageIcon sparrow3 = new ImageIcon("images/sparrow3.png");
    private ImageIcon sparrow4 = new ImageIcon("images/sparrow4.png");
    private ImageIcon shot = new ImageIcon("images/shot.png"); // 새가 맞았을 때의 이미지

    public Sparrow(JTextField userInput, int round, GameGround gameGround) {
        super(userInput, round, gameGround); // 상위 클래스 생성자 호출
        sparrowLabel = new JLabel();
        sparrowLabel.setIcon(sparrow1); // 초기 이미지 설정
        sparrowLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트 위치 조정
        sparrowLabel.setVerticalTextPosition(JLabel.CENTER);
        sparrowLabel.setForeground(Color.WHITE); // 텍스트 색상
        sparrowLabel.setFont(new Font("Monospaced", Font.BOLD, 20)); // 텍스트 폰트
        sparrowLabel.setText(this.word); // 생성 시 설정되는 단어
        this.sparrowThread = new SparrowThread(); // Sparrow 스레드 생성 및 시작
        sparrowThread.start();
    }

    public void setCorrectFlag(boolean correctFlag) {
        this.correctFlag = correctFlag; // 정답 플래그 설정
    }

    @Override
    public ImageIcon getBirdImage() {
        return sparrow1; // 사냥하면 안되는 새 이미지 반환
    }

    @Override
    public boolean isCorrect() {
        if (getWord().equals(userInput.getText())) {
            correctFlag = true; // 정답 플래그 설정
            setIsAlive(false); // 죽은 것으로 처리
            return true;
        } else return false;
    }

    @Override
    public JLabel getBirdLabel() {
        return sparrowLabel; // 이 JLabel의 레퍼런스 반환
    }

    public void goRight() {
        sparrowLabel.setLocation(sparrowLabel.getX() + 20, sparrowLabel.getY()); // 오른쪽으로 이동
        if (sparrowLabel.getX() > 1300) {
            right = 1; // 오른쪽으로 이동 중 방향 변경
        }
    }

    public void goLeft() {
        sparrowLabel.setLocation(sparrowLabel.getX() - 20, sparrowLabel.getY()); // 왼쪽으로 이동
        if (sparrowLabel.getX() < 0) {
            right = 0; // 왼쪽으로 이동 중 방향 변경
        }
    }

    public void moveWings() {
        // 날개 퍼덕임 구현, 현재 상태와 방향에 따라 다른 이미지 설정
        if (current == 0 && right == 1) {
            sparrowLabel.setIcon(sparrow1);
            current = 1;
        } else if (current == 1 && right == 1) {
            sparrowLabel.setIcon(sparrow2);
            current = 0;
        } else if (current == 0 && right == 0) {
            sparrowLabel.setIcon(sparrow3);
            current = 1;
        } else if (current == 1 && right == 0) {
            sparrowLabel.setIcon(sparrow4);
            current = 0;
        }
    }

    class SparrowThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    if (isMoving) {
                        moveWings(); // 날개 퍼덕임
                        if (right == 1) {
                            goLeft(); // 왼쪽 이동
                        } else {
                            goRight(); // 오른쪽 이동
                        }
                    }
                    if (correctFlag) {
                        // 맞았을 경우 처리, 이미지 변경 및 레이블 제거
                        sparrowLabel.setIcon(shot);
                        sparrowLabel.repaint();
                        sparrowLabel.setText("");
                        sleep(1000);
                        gameGround.remove(sparrowLabel);
                        gameGround.repaint();
                        this.interrupt(); // 스레드 종료
                    }

                    sleep(600 / round); // 날아가는 속도 조절
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
