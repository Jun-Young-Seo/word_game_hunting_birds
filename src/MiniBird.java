import javax.swing.*;
import java.awt.*;

public class MiniBird extends Bird {
    private JLabel miniBirdLabel; // MiniBird의 레이블
    private Thread miniBirdThread; // MiniBird의 스레드
    private boolean correctFlag = false; // 정답 여부를 판단하는 플래그

    @Override
    public Thread getThread() {
        return miniBirdThread; // MiniBird 스레드 반환
    }

    private int right = 1; // 현재 방향을 나타내는 변수, 오른쪽으로 향하고 있으면 1
    private int current = 1; // 날개 퍼덕임의 현재 상태

    // MiniBird의 날개 퍼덕임을 위한 이미지, 날개 위 아래 + 새의 머리 좌 우 향한 모습의 4개 이미지
    private ImageIcon miniBird1 = new ImageIcon("images/minibird1.png");
    private ImageIcon miniBird2 = new ImageIcon("images/minibird2.png");
    private ImageIcon miniBird3 = new ImageIcon("images/minibird3.png");
    private ImageIcon miniBird4 = new ImageIcon("images/minibird4.png");
    private ImageIcon shot = new ImageIcon("images/shot.png"); // 새가 맞았을 때의 이미지

    public void setCorrectFlag(boolean correctFlag) {
        this.correctFlag = correctFlag; // 정답 플래그 설정
    }

    // MiniBird 생성자, 단어 입력창, 라운드, GameGround에 대한 레퍼런스 전달
    public MiniBird(JTextField userInput, int round, GameGround gameGround) {
        super(userInput, round, gameGround);
        miniBirdLabel = new JLabel();
        miniBirdLabel.setIcon(miniBird1); // 초기 이미지 설정
        miniBirdLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트 위치 조정
        miniBirdLabel.setVerticalTextPosition(JLabel.CENTER);
        miniBirdLabel.setForeground(Color.RED); // 텍스트 색상
        miniBirdLabel.setFont(new Font("고딕체", Font.BOLD, 20)); // 텍스트 폰트
        miniBirdLabel.setText(this.word); // 생성 시 설정되는 단어
        miniBirdThread = new MiniBirdThread(); // MiniBird 스레드 생성 및 시작
        miniBirdThread.start();
    }

    @Override
    public ImageIcon getBirdImage() {
        return miniBird1; // 사냥하면 안되는 새 이미지 반환
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
        return miniBirdLabel; // 이 JLabel의 레퍼런스 반환
    }

    public void goRight() {
        miniBirdLabel.setLocation(miniBirdLabel.getX() + 10, miniBirdLabel.getY());
        if (miniBirdLabel.getX() > 1300) {
            right = 1; // 오른쪽으로 이동 중 방향 변경
        }
    }

    public void goLeft() {
        miniBirdLabel.setLocation(miniBirdLabel.getX() - 10, miniBirdLabel.getY());
        if (miniBirdLabel.getX() < 0) {
            right = 0; // 왼쪽으로 이동 중 방향 변경
        }
    }

    public void moveWings() {
        // 날개 퍼덕임 구현, 현재 상태와 방향에 따라 다른 이미지 설정
        if (current == 0 && right == 1) {
            miniBirdLabel.setIcon(miniBird1);
            current = 1;
        } else if (current == 1 && right == 1) {
            miniBirdLabel.setIcon(miniBird2);
            current = 0;
        } else if (current == 0 && right == 0) {
            miniBirdLabel.setIcon(miniBird3);
            current = 1;
        } else if (current == 1 && right == 0) {
            miniBirdLabel.setIcon(miniBird4);
            current = 0;
        }
    }

    class MiniBirdThread extends Thread {
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
                        miniBirdLabel.setIcon(shot);
                        miniBirdLabel.repaint();
                        miniBirdLabel.setText("");
                        sleep(1000);
                        gameGround.remove(miniBirdLabel);
                        gameGround.repaint();
                        this.interrupt(); // 스레드 종료
                    }

                    sleep(500 / round); // 날아가는 속도 조절
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
